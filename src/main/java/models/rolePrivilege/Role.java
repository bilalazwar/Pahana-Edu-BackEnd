package models.rolePrivilege;

import java.util.Set;

public class Role {
    private int id;
    private String name;

    public Role() {
    }
    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
