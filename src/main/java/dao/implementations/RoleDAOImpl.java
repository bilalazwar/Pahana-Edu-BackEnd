package dao.implementations;

import dao.interfaces.RoleDAO;
import models.rolePrivilege.Role;
import utils.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public void addRole(String roleName) throws Exception {
        String sql = "INSERT INTO Roles (name) VALUES (?)";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roleName);
            stmt.executeUpdate();
        }
    }

    @Override
    public Role getRoleById(int id) throws Exception {
        String sql = "SELECT id, name FROM Roles WHERE id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setName(rs.getString("name"));
                    return role;
                }
            }
        }

        return null; // Role not found
    }

    @Override
    public List<Role> getAllRoles() throws Exception {

        String sql = "SELECT * FROM Roles";
        List<Role> roles = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"), rs.getString("name")));
            }
        }

        return roles;
    }

    @Override
    public void updateRole(Role role) throws Exception {
        String sql = "UPDATE Roles SET name = ? WHERE id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.getName());
            stmt.setInt(2, role.getId());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to update role name: " + e.getMessage(), e);
        }
    }

    @Override
    public String deleteRoleById(int id) throws Exception {
        String sql = "DELETE FROM Roles WHERE id = ?";
        int affectedRows = 0;

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            affectedRows = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to delete role: " + e.getMessage(), e);
        }

        if (affectedRows == 1) {
            return "Deleted role with ID: " + id;
        } else {
            return "No role found with ID: " + id;
        }
    }
}