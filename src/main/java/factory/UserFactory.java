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
                                  LocalDateTime updatedAt, LocalDateTime lastLogin, boolean isActive) {
        switch (userType) {
            case ADMIN:
                return new Admin(id, username, password, roleid, full_name, email, createdAt, updatedAt, lastLogin, isActive);
            case MANAGER:
                return new Manager(id, username, password, roleid, full_name, email, createdAt, updatedAt, lastLogin, isActive);
            case STAFF:
                return new Staff(id, username, password, roleid, full_name, email, createdAt, updatedAt, lastLogin, isActive);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}