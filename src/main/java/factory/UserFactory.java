package factory;

import models.parent.User;
import models.person.Admin;
import models.person.Manager;
import models.person.Staff;
import models.person.UserType;

import java.time.LocalDateTime;

public class UserFactory {

    public static User createUser(UserType userType, int id, String username, String password, int roleid,
                                  String full_name, String email, LocalDateTime createdAt,
                                  LocalDateTime updatedAt, LocalDateTime lastLogin, boolean active) {

        if (userType == null) {
            throw new IllegalArgumentException("Invalid user type: null");
        }
        User user;

        switch (userType) {
            case ADMIN:
                user = new Admin();
                break;
            case MANAGER:
                user = new Manager();
                break;
            case STAFF:
                user = new Staff();
                break;
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        // Set common fields
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole_id(roleid);
        user.setFull_name(full_name);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        user.setLastLogin(lastLogin);
        user.setActive(active);

        return user;
    }
}