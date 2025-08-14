package dao.interfaces;

import models.rolePrivilege.Privilege;
import models.rolePrivilege.Role;
import models.rolePrivilege.RolePrivilege;

import java.util.List;

public interface RolePrivilegeDAO {
    int addPrivilegeToRole(int roleId, int privilegeId) throws Exception;

    int removePrivilegeAndRole(int roleId, int privilegeId) throws Exception;

    boolean exists(int roleId, int privilegeId) throws Exception;

    List<RolePrivilege> getRolesWithPrivileges() throws Exception;

    List<RolePrivilege> getPrivilegesByRoleId(int roleId) throws Exception;

}
