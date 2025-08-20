package dao.implementations;

import dao.interfaces.PrivilegeDAO;
import dtos.PrivilegeDTO;
import mapper.PrivilegeMapper;
import models.rolePrivilege.Privilege;
import models.rolePrivilege.Role;
import utils.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeDAOImpl implements PrivilegeDAO {

    @Override
    public void addPrivilege(Privilege privilege) throws Exception {
        String sql = "INSERT INTO PrivilegeS (name) VALUES (?)";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //stmt: The PreparedStatement object that will hold the SQL query and allow you to set values and execute it.

            stmt.setString(1, privilege.getName());
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
        }
    }

    @Override
    public List<Privilege> getAllPrivileges() throws Exception {

        String sql = "SELECT * FROM Privileges";
        List<Privilege> dbPrivileges = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                Privilege privilege = new Privilege();
                privilege.setId(id);
                privilege.setName(name);
                dbPrivileges.add(privilege);

                System.out.println(privilege); // Optional debug logging
            }

        } catch (SQLException e) {
            e.printStackTrace(); // For debugging, consider replacing with a logger
            throw new Exception("Error fetching privileges from DB", e); // Rethrow to notify caller
        }

        return dbPrivileges;
    }


    @Override
    public int updatePrivilege(PrivilegeDTO privilegeDTO) throws Exception {

        String sql = "UPDATE PrivilegeS SET name = ? WHERE id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Privilege privilege = PrivilegeMapper.toModel(privilegeDTO);

            stmt.setString(1, privilege.getName());
            stmt.setInt(2, privilege.getId());

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated;

        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
            return 0;
        }
    }

    @Override
    public int deletePrivilegeByID(int id) throws Exception {

        String sql = "DELETE FROM Privileges WHERE id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated;

        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
            return 0;
        }
    }

    @Override
    public Privilege getPrivilegeById(int id) throws Exception {
        String sql = "SELECT id, name FROM Privileges WHERE id = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            System.out.println("Id is ===== " + id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Privilege privilege = new Privilege();
                    privilege.setId(rs.getInt("id"));
                    privilege.setName(rs.getString("name"));
                    // Add more setters if your table has more columns
                    return privilege;
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error retrieving privilege with ID: " + id, e);
        }

        return null; // No privilege found for given ID
    }
}