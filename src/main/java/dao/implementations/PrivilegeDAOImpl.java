package dao.implementations;

import dao.interfaces.DepartmentDAO;
import dao.interfaces.PrivilegeDAO;
import dtos.PrivilegeDTO;
import mapper.PrivilegeMapper;
import models.departmnet.Department;
import models.rolePrivilege.Privilege;
import utils.DBConnectionUtil;

import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeDAOImpl implements PrivilegeDAO {

    @Override
    public void addPrivilege(Privilege privilege) throws Exception {
        String sql = "INSERT INTO pahanaedu.privilege (name) VALUES (?)";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
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

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
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

        String sql = "SELECT * FROM PahanaEdu.Privilege";
        List<Privilege> dbPrivileges = new ArrayList<>();

        try {
            DBConnectionUtil.getInstance();
            try (Connection conn = DBConnectionUtil.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) { //stmt: The PreparedStatement object that will hold the SQL query and allow you to set values and execute it.

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Privilege privilege = new Privilege();
                    privilege.setId(id);
                    privilege.setName(name);
                    dbPrivileges.add(privilege);
                    System.out.println(privilege);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace(); // You can later replace this with proper logging
        }
        return dbPrivileges;

    }

    @Override
    public int updatePrivilege(PrivilegeDTO privilegeDTO) throws Exception {

        String sql = "UPDATE pahanaedu.privilege SET name = ? WHERE id = ?";

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

        String sql = "DELETE FROM pahanaedu.privilege WHERE id = ?";

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
    public void deletePrivilege(int id) throws Exception {

    }
}
