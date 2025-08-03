package models.person;

import models.rolePrivilege.Role;
import models.parent.User;

import java.util.Set;

public class Admin extends User {

    public Admin(int id, String username, String password, Set<Role> roles) {
        super(id, username, password, roles);
    }
}
