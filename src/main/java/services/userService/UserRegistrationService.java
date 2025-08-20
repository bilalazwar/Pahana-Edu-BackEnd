package services.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.UserDAO;
import dtos.UserRegistrationDto;
import factory.UserFactory;
import jakarta.servlet.http.HttpServletRequest;
import models.parent.User;
import models.person.UserType;
import utils.PasswordUtil;

import java.time.LocalDateTime;

public class UserRegistrationService {

    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public UserRegistrationService(UserDAO userDAO, ObjectMapper objectMapper) {
        this.userDAO = userDAO;
        this.objectMapper = objectMapper;
    }
    public void registerUser(HttpServletRequest request) throws Exception {
        UserRegistrationDto dto = objectMapper.readValue(request.getReader(), UserRegistrationDto.class);
        UserType userType = resolveUserType(dto.getRole_id());

        User user = UserFactory.createUser(userType,
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole_id(),
                dto.getFull_name(),
                dto.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                true);

        validateUser(user, false);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDAO.addUser(user);
    }
    private UserType resolveUserType(int roleId) {
//        System.out.println("Role id = " + roleId);
        UserType userType;
        switch (roleId) {
            case 1:
                userType = UserType.ADMIN;
                break;
            case 2:
                userType = UserType.MANAGER;
                break;
            case 3:
                userType = UserType.STAFF;
                break;
            default:
                throw new IllegalArgumentException("Invalid role_id:");
        };
        return userType;
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
