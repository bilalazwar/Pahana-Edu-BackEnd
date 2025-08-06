package models.rolePrivilege;

import java.util.Set;

public class Role {
    private int id;
    private String name;
    private Set<Privilege> privileges;

    public Role() {
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Privilege> getPrivileges() { return privileges; }
    public void setPrivileges(Set<Privilege> privileges) { this.privileges = privileges; }
}
