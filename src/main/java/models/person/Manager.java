package models.person;

import models.rolePrivilege.Role;
import models.parent.User;

import java.time.LocalDateTime;

public class Manager extends User {
    public Manager(int id, String username, String password, int role_id, String full_name, String email,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin, boolean isActive) {
        super(id, username, password, role_id, full_name, email, createdAt, updatedAt, lastLogin, isActive);
    }
}
