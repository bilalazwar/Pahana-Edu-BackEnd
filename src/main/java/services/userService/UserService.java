package services.userService;

import dao.implementations.UserImpl;
import dao.interfaces.UserDAO;
import models.parent.User;
import utils.PasswordUtil;

import java.util.regex.Pattern;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserImpl(); // You can inject this via constructor if needed
    }

    // Register a new user
    public void registerUser(User user) throws Exception {
        validateUser(user, true); // true = check password too

        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        userDAO.addUser(user);
    }

    // Authenticate user login
    public boolean login(String username, String rawPassword) throws Exception {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");
        if (rawPassword == null || rawPassword.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        return userDAO.verifyUserPassword(username, rawPassword);
    }

    // Update password (requires user ID and new password)
    public void updateUserPassword(int userId, String newPassword) throws Exception {
        validatePassword(newPassword);
        userDAO.updatePassword(userId, newPassword);
    }

    // Fetch user by ID
    public User getUser(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid user ID");
        return userDAO.getUserById(id);
    }

    // Delete user
    public void deleteUser(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid user ID");
        userDAO.deleteUser(id);
    }

    // Update full user info (must pass full user object)
    public void updateUser(User user) throws Exception {
        validateUser(user, false); // false = skip password validation
        userDAO.updateUser(user);
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
