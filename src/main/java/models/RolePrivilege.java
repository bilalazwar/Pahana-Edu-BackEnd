package models;

public class RolePrivilege {
    private Long roleId;
    private Long privilegeId;

    public RolePrivilege(Long roleId, Long privilegeId) {
        this.roleId = roleId;
        this.privilegeId = privilegeId;
    }

    // Getters and Setters
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }
}