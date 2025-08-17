package services.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.RolePrivilegeDAO;
import dao.interfaces.UserDAO;
import dtos.UserDto;
import dtos.UserRegistrationDto;
import factory.UserFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.UserMapper;
import models.parent.User;
import models.person.UserType;
import utils.PasswordUtil;

import java.time.LocalDateTime;
import java.util.List;

public class UserManagementService {

    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public UserManagementService(UserDAO userDAO, ObjectMapper objectMapper) {
        this.userDAO = userDAO;
        this.objectMapper = objectMapper;
    }

    public User getUserById(int id) throws Exception {
        if (id <= 0 || !userDAO.userExist(id)) throw new IllegalArgumentException("Invalid user ID");
        return userDAO.getUserById(id);
    }

    public List<UserDto> getAllUsers() throws Exception {
        List<User> users = userDAO.getAllUsers();
        return UserMapper.toDTO(users);
    }

    public void deleteUser(int id) throws Exception {
        if (id <= 0 || !userDAO.userExist(id)) throw new IllegalArgumentException("Invalid user ID");
        userDAO.deleteUser(id);
    }

//    public void updateUser(User user, String id) throws Exception {
//        if (user == null) throw new IllegalArgumentException("User cannot be null");
//        user.setId(Integer.parseInt(id));
//        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
//        userDAO.updateUser(user);
//    }

    public void updateUser(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {

        User user = returUser(request);
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        validateUser(user, true); // false = skip password validation
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setId(Integer.parseInt(id));
        userDAO.updateUser(user);
    }

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

    public boolean userExists(int id) throws Exception {
        return userDAO.userExist(id);
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
    private void validateUser(User user, boolean checkPassword) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new IllegalArgumentException("Username is required");
        if (checkPassword) validatePassword(user.getPassword());
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
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        int at = email.indexOf('@');
        int dot = email.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < email.length() - 1 && email.indexOf(' ') == -1;
    }
}
