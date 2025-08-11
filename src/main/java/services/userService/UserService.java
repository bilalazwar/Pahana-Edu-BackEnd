package services.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.implementations.RolePrivilegeImpl;
import dao.implementations.UserDAOImpl;
import dao.interfaces.RolePrivilegeDAO;
import dao.interfaces.UserDAO;
import dtos.UserDto;
import dtos.UserRegistrationDto;
import factory.UserFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mapper.UserMapper;
import models.parent.User;
import models.person.UserType;
import models.rolePrivilege.Privilege;
import utils.PasswordUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {

    private final UserDAO userDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RolePrivilegeDAO rolePrivilegeDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl(); // You can inject this via constructor if needed
        this.rolePrivilegeDAO = new RolePrivilegeImpl();
    }

    public User returUser(HttpServletRequest request) throws Exception {

        UserRegistrationDto userRegistrationDto = objectMapper.readValue(request.getReader(), UserRegistrationDto.class);
        User user;
        UserType userType;

        switch (userRegistrationDto.getRole_id()) {
            case 1:  // Admin
                userType = UserType.ADMIN;
                break;
            case 2:  // Manager
                userType = UserType.MANAGER;
                break;
            case 3:  // Staff
                userType = UserType.STAFF;
                break;
            default:
                throw new IllegalArgumentException("Invalid role_id:");
        }

        user = UserFactory.createUser(userType,userRegistrationDto.getId(),
                userRegistrationDto.getUsername(),
                userRegistrationDto.getPassword(),
                userRegistrationDto.getRole_id(),
                userRegistrationDto.getFull_name(),
                userRegistrationDto.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                true
        );
        System.out.println(user);

        return user;
    }

    public void registerUser(HttpServletRequest request) throws Exception {

        UserRegistrationDto userRegistrationDto = objectMapper.readValue(request.getReader(), UserRegistrationDto.class);
        User user;
        UserType userType;

        switch (userRegistrationDto.getRole_id()) {
            case 1:  // Admin
                userType = UserType.MANAGER;
                break;
            case 2:  // Manager
                userType = UserType.MANAGER;
                break;
            case 3:  // Staff
                userType = UserType.MANAGER;
                break;
            default:
                throw new IllegalArgumentException("Invalid role_id:");
        }

        user = UserFactory.createUser(userType,userRegistrationDto.getId(),
                userRegistrationDto.getUsername(),
                userRegistrationDto.getPassword(),
                userRegistrationDto.getRole_id(),
                userRegistrationDto.getFull_name(),
                userRegistrationDto.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                true
        );

        validateUser(user, false);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDAO.addUser(user);

        System.out.println(user);
    }

    // Authenticate user login
    public boolean login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = returUser(request);
        System.out.println(user);

        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        boolean result = userDAO.verifyUserPassword(user.getUsername(), user.getPassword());

        if (result) {
            // ✅ Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("username", user.getUsername());
            session.setMaxInactiveInterval(15 * 60); // 15 minutes
            session.setAttribute("user_id", user.getId());
            session.setAttribute("role_id", user.getRole_id());

            List<Privilege> rolePrivileges = rolePrivilegeDAO.getPrivilegesByRoleId(user.getRole_id());
            session.setAttribute("rolePrivileges", rolePrivileges);

            return true;
        }
        else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    // Update password (requires user ID and new password)
    public void updateUserPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = returUser(request);

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (user.getId() <= 0) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDAO.updatePassword(user.getId(), user.getPassword());
    }

    // Fetch user by ID
    public User getUserById(int id) throws Exception {

        if (id <= 0 || !userExists(id)) throw new IllegalArgumentException("Invalid user ID");
        return userDAO.getUserById(id);
    }

    public List<UserDto> getAllUsers() throws Exception {

        List<User> user = userDAO.getAllUsers();
        return UserMapper.toDTO(user);
    }

    // Delete user
    public void deleteUser(int id) throws Exception {
        if (id <= 0 || !userExists(id)) throw new IllegalArgumentException("Invalid user ID");

        userDAO.deleteUser(id);
    }

    // Update full user info (must pass full user object)
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = returUser(request);

        validateUser(user, false); // false = skip password validation
        userDAO.updateUser(user);
    }

    public boolean userExists(int id) throws Exception {
        return userDAO.userExist(id);
    }


    private void validateUser(User user, boolean checkPassword) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new IllegalArgumentException("Username is required");

        if (checkPassword) {
            validatePassword(user.getPassword());
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail()))
            throw new IllegalArgumentException("Invalid email format");

        if (user.getFull_name() == null || user.getFull_name().isEmpty())
            throw new IllegalArgumentException("Full name is required");

        if (user.getRole_id() < 1 || user.getRole_id() > 3)
            throw new IllegalArgumentException("Invalid role ID (must be 1, 2, or 3)");
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        // Optionally check complexity: upper, lower, digit, special char, etc.
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }
}
