package dao.interfaces;

import models.rolePrivilege.Role;

import java.util.List;

public interface RoleDAO {

    void addRole(String roleName) throws Exception;
    Role getRoleById(int id) throws Exception;
    List<Role> getAllRoles() throws Exception;
    void updateRole(Role role) throws Exception;
    String deleteRoleById(int id) throws Exception;

}
