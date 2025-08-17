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

public class UserDAOImpl implements UserDAO {

    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getInstance().getConnection();
    }

    @Override
    public void addUser(User user) throws Exception {

        String sql = "INSERT INTO users (username, password, role_id, full_name, email, created_at, updated_at, last_login, active) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getRole_id());
            stmt.setString(4, user.getFull_name());
            stmt.setString(5, user.getEmail());
            stmt.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setTimestamp(8, user.getLastLogin() != null ? Timestamp.valueOf(user.getLastLogin()) : null);
            stmt.setBoolean(9, true);
            stmt.executeUpdate();
        }
    }

    @Override
    public User getUserById(int id) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ?";
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
        String sql = "SELECT * FROM users";

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
        String sql = "UPDATE users SET username = ?, password = ?, role_id = ?, full_name = ?, email = ?, " +
                "updated_at = ?, last_login = ?, active = ? WHERE id = ?";

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
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean userExist(int id) throws Exception {
        String sql = "SELECT 1 FROM users WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a row exists, user exists
            }
        }
    }

    @Override
    public int verifyUserPassword(String username, String enteredPassword, int roleId) throws Exception {

        String sql = "SELECT id, password FROM users WHERE username = ? AND role_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, roleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                int id = rs.getInt("id");
                System.out.println("ID============"+id);

                // Hash the entered password and compare with stored hash
                String hashedInput = PasswordUtil.hashPassword(enteredPassword);
                System.out.println("Hash Password " +hashedInput);
                System.out.println("Entered Password " +storedHash);

                if(storedHash.equals(hashedInput)) {  // If the hashes match, the password is correct

                    return id;

                }
            }
        }
        return 0;  // User not found
    }

    @Override
    public void updatePassword(int userId, String newPassword) throws Exception {
        String sql = "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";

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
        int role_id = rs.getInt("role_id");
        String full_name = rs.getString("full_name");
        String email = rs.getString("email");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        Timestamp lastLoginTS = rs.getTimestamp("last_login");
        LocalDateTime lastLogin = (lastLoginTS != null) ? lastLoginTS.toLocalDateTime() : null;
        boolean active = rs.getBoolean("active");

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

        return UserFactory.createUser(userType, id, username, password, role_id, full_name, email, createdAt, updatedAt, lastLogin, active);
    }
}
