package services.privilegeRoleService;

import dao.implementations.RolePrivilegeImpl;
import dao.interfaces.RolePrivilegeDAO;
import models.rolePrivilege.Privilege;
import models.rolePrivilege.RolePrivilege;

import java.util.List;

public class RolePrivilegeService {

    public final RolePrivilegeDAO rolePrivilegeDAO;

    public RolePrivilegeService(RolePrivilegeDAO rolePrivilegeDAO) {
        this.rolePrivilegeDAO = rolePrivilegeDAO;
    }

    public int addPrivilegeToRole(int roleId, int privilegeId) throws Exception {
        if (roleId <= 0 || privilegeId <= 0) {
            throw new IllegalArgumentException("roleId and privilegeId must be positive integers.");
        }

        // check if privilege already exists for the role
        if (privilegeRoleExists(roleId, privilegeId)) {
            throw new IllegalStateException("Privilege already assigned to this role.");
        }

        return rolePrivilegeDAO.addPrivilegeToRole(roleId, privilegeId);
    }

    public int deletePrivilegeRole(int roleId, int privilegeId) throws Exception {
        if (roleId <= 0 || privilegeId <= 0) {
            throw new IllegalArgumentException("roleId and privilegeId must be positive integers.");
        }

        // Optional: Check if the mapping exists
        boolean exists = privilegeRoleExists(roleId, privilegeId);
        if (!exists) {
            throw new IllegalStateException("Privilege-role mapping does not exist.");
        }

        return rolePrivilegeDAO.removePrivilegeAndRole(roleId, privilegeId);
    }

    public List<RolePrivilege> getPrivilegesByRoleId(int roleId) throws Exception {

        List<RolePrivilege> result = rolePrivilegeDAO.getRolesWithPrivileges();
        if(result.isEmpty()){
            throw new Exception("No role privileges found");
        }
        else {
            return result;
        }
    }

    public List<RolePrivilege> getPrivilegeRole() throws Exception {

        List<RolePrivilege> result = rolePrivilegeDAO.getRolesWithPrivileges();
        if(result.isEmpty()){
            throw new Exception("No role privileges found");
        }
        else {
            return result;
        }
    }

    public boolean privilegeRoleExists(int roleId, int privilegeId) throws Exception {
        return rolePrivilegeDAO.exists(roleId,privilegeId);
    }

}
