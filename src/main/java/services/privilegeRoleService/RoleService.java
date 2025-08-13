package services.privilegeRoleService;

import dao.interfaces.RoleDAO;
import models.rolePrivilege.Role;

import java.util.List;

public class RoleService {

    private final RoleDAO roleDAO;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void addRole(String roleName) throws Exception {
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty.");
        }
        roleDAO.addRole(roleName);
    }
    public List<Role> getAllRoles() throws Exception {
        return roleDAO.getAllRoles();
    }

    public void updateRole(Role role) throws Exception {
        if (role.getName() == null || role.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid role data for update.");
        }
        roleDAO.updateRole(role);
    }

    public String deleteRoleById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid role ID.");
        }
        return roleDAO.deleteRoleById(id);
    }

    public Role getRoleById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid role ID.");
        }
        return roleDAO.getRoleById(id);
    }
}
