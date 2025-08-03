package dao.interfaces;

import models.rolePrivilege.Role;

import java.util.List;

public interface RoleDAO {

    void addRole(Role role) throws Exception;
    Role getRoleById(int id) throws Exception;
    List<Role> getAllRoles() throws Exception;
    void updateRole(Role role) throws Exception;
    void deleteRole(int id) throws Exception;

    // Many-to-many handling
    void addPrivilegeToRole(int roleId, int privilegeId) throws Exception;
    void removePrivilegeFromRole(int roleId, int privilegeId) throws Exception;
    List<Role> getRolesWithPrivileges() throws Exception;
}
