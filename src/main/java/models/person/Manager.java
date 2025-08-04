package models.person;

import models.rolePrivilege.Role;
import models.parent.User;

import java.time.LocalDateTime;

public class Manager extends User {
    public Manager(int id, String username, String password, Role role, String fullname, String email,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin, boolean isActive) {
        super(id, username, password, role, fullname, email, createdAt, updatedAt, lastLogin, isActive);
    }
}
