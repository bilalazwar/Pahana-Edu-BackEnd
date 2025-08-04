package dao.implementations;

import dao.interfaces.DepartmentDAO;
import dao.interfaces.PrivilegeDAO;
import models.departmnet.Department;
import models.rolePrivilege.Privilege;
import utils.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeDAOImpl implements PrivilegeDAO {

    @Override
    public void addPrivilege(Privilege privilege) throws Exception {
        String sql = "INSERT INTO pahanaedu.privilege (name) VALUES (?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //stmt: The PreparedStatement object that will hold the SQL query and allow you to set values and execute it.

            stmt.setString(1, privilege.getName());
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
        }
    }

    @Override
    public Privilege getPrivilegeById(int id) throws Exception {

        String sql = "SELECT * FROM pahanaedu.privilege where id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //stmt: The PreparedStatement object that will hold the SQL query and allow you to set values and execute it.

            stmt.setInt(1, id);
            // use this when parameter expects an int for string have to use setString


        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
        }

        return null;
    }

    @Override
    public List<Privilege> getAllPrivileges() throws Exception {

        String sql = "SELECT * FROM pahanaedu.privilege";
        List<Privilege> dbPrivileges = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //stmt: The PreparedStatement object that will hold the SQL query and allow you to set values and execute it.

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Privilege privilege = new Privilege();
                dbPrivileges.add(privilege);
            }


        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
        }
        return dbPrivileges;
    }

    @Override
    public void updatePrivilege(Privilege privilege) throws Exception {

    }

    @Override
    public void deletePrivilege(int id) throws Exception {

    }
}
