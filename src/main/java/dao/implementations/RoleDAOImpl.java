package dao.implementations;

import dao.interfaces.DepartmentDAO;
import dao.interfaces.RoleDAO;
import models.departmnet.Department;
import models.rolePrivilege.Privilege;
import models.rolePrivilege.Role;
import services.privilegeRoleService.PrivilegeService;
import utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public void addRole(String roleName) throws Exception {
        String sql = "INSERT INTO PahanaEdu.Role (name) VALUES (?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roleName);
            stmt.executeUpdate();
        }
    }

    @Override
    public Role getRoleById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Role> getAllRoles() throws Exception {

        String sql = "SELECT * FROM role";
        List<Role> roles = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                int roleId = rs.getInt("id");
                role.setId(roleId);
                role.setName(rs.getString("name"));
//                role.setPrivileges(getPrivilegesByRoleId(roleId));

                roles.add(role);
            }
        }

        return roles;
    }

    @Override
    public void updateRole(Role role) throws Exception {
        String sql = "UPDATE role SET name = ? WHERE id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
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
        String sql = "DELETE FROM role WHERE id = ?";
        int affectedRows = 0;

        try (Connection conn = DBConnectionUtil.getConnection();
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