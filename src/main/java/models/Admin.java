package models;

import java.util.EnumSet;

public class Admin extends User {
    public Admin(Long id, String username, String email, String password){
        super(id,username,email,password );
    }
}
