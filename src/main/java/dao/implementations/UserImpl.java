package dao.implementations;

import dao.interfaces.UserDAO;
import factory.UserFactory;
import models.parent.User;
import models.person.UserType;
import utils.DBConnectionUtil;
import utils.PasswordUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserImpl implements UserDAO {

    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getInstance().getConnection();
    }

    @Override
    public void addUser(User user) throws Exception {

        String sql = "INSERT INTO user (id, username, password, role_id, full_name, email, created_at, updated_at, last_login, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getRole_id());
            stmt.setString(5, user.getFull_name());
            stmt.setString(6, user.getEmail());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setTimestamp(9, user.getLastLogin() != null ? Timestamp.valueOf(user.getLastLogin()) : null);
            stmt.setBoolean(10, user.isActive());
            stmt.executeUpdate();
        }
    }

    @Override
    public User getUserById(int id) throws Exception {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            } else {
                return null;
            }
        }
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        }

        return users;
    }

    @Override
    public void updateUser(User user) throws Exception {
        String sql = "UPDATE user SET username = ?, password = ?, role_id = ?, full_name = ?, email = ?, " +
                "updated_at = ?, last_login = ?, is_active = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getRole_id());
            stmt.setString(4, user.getFull_name());
            stmt.setString(5, user.getEmail());
            stmt.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setTimestamp(7, user.getLastLogin() != null ? Timestamp.valueOf(user.getLastLogin()) : null);
            stmt.setBoolean(8, user.isActive());
            stmt.setInt(9, user.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws Exception {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean verifyUserPassword(String username, String enteredPassword) throws Exception {

        String sql = "SELECT password FROM user WHERE username = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");

                // Hash the entered password and compare with stored hash
                String hashedInput = PasswordUtil.hashPassword(enteredPassword);

                return storedHash.equals(hashedInput);  // If the hashes match, the password is correct
            }
        }
        return false;  // User not found
    }

    @Override
    public void updatePassword(int userId, String newPassword) throws Exception {
        String sql = "UPDATE user SET password = ?, updated_at = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            // Hash the new password
            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            stmt.setString(1, hashedPassword);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // update the timestamp
            stmt.setInt(3, userId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No user found with ID: " + userId);
            }
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        int role_id = rs.getInt("role");
        String full_name = rs.getString("full_name");
        String email = rs.getString("email");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        Timestamp lastLoginTS = rs.getTimestamp("last_login");
        LocalDateTime lastLogin = (lastLoginTS != null) ? lastLoginTS.toLocalDateTime() : null;
        boolean isActive = rs.getBoolean("is_active");

        UserType userType;
        switch (role_id) {
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
                throw new SQLException("Unknown role type: " + role_id);
        }

        return UserFactory.createUser(userType, id, username, password, role_id, full_name, email, createdAt, updatedAt, lastLogin, isActive);
    }
}
