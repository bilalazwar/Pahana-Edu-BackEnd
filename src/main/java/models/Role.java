package models;

import java.util.Set;

public class Role {
    private Long id;
    private String roleName;
    private Set<Privilege> privileges;

    public Role() {}
    public Role(Long id, String roleName, Set<Privilege> privileges) {
        this.id = id;
        this.roleName = roleName;
        this.privileges = privileges;
    }
    public void setId(Long id) { this.id = id; }
    public void setName(String roleName) { this.roleName = roleName; }
    public void setPrivileges(Set<Privilege> privileges) { this.privileges = privileges; }


    // Getters
    public Long getId() { return id; }
    public String getRoleName() { return roleName; }
    public Set<Privilege> getPrivileges() { return privileges; }
}
