package dao.implementations;

import dao.interfaces.RolePrivilegeDAO;
import models.rolePrivilege.Privilege;
import models.rolePrivilege.RolePrivilege;
import utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolePrivilegeImpl implements RolePrivilegeDAO {

    @Override
    public int addPrivilegeToRole(int roleId, int privilegeId) throws Exception {
        String sql = "INSERT INTO role_privileges (role_id, privilege_id) VALUES (?, ?)";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            stmt.setInt(2, privilegeId);
            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
            return 0;
        }

    }

    @Override
    public int removePrivilegeAndRole(int roleId, int privilegeId) throws Exception {
        String sql = "DELETE FROM role_privileges WHERE role_id = ? AND privilege_id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            stmt.setInt(2, privilegeId);
            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
            return 0;
        }
    }

    @Override
    public boolean exists(int roleId, int privilegeId) throws Exception {
        String sql = "SELECT 1 FROM role_privileges WHERE role_id = ? AND privilege_id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            stmt.setInt(2, privilegeId);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // returns true if a row exists
        }
        catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
            return false;
        }
    }

    @Override
    public List<RolePrivilege> getRolesWithPrivileges() throws Exception {
        String sql = "SELECT * FROM role_privileges";
        List<RolePrivilege> rolePrivileges = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RolePrivilege rp = new RolePrivilege();
                rp.setRoleId(rs.getInt("role_id"));
                rp.setPrivilegeId(rs.getInt("privilege_id"));
                rolePrivileges.add(rp);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Optional: Replace with logger
            throw new Exception("Error fetching role_privilege data", e);
        }

        return rolePrivileges;
    }

    @Override
    public List<RolePrivilege> getPrivilegesByRoleId(int roleId) throws Exception {

        String sql = "SELECT * FROM role_privileges WHERE role_id = ?";

        List<RolePrivilege> rolePrivileges  = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                RolePrivilege rp = new RolePrivilege();
                rp.setRoleId(rs.getInt("role_id"));
                rp.setPrivilegeId(rs.getInt("privilege_id"));
                rolePrivileges.add(rp);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // replace this with proper logging
            throw new Exception("Error retrieving privileges for role ID: " + roleId, e);
        }

        return rolePrivileges;
    }

}
