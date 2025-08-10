package factory;

import models.parent.User;
import models.person.Admin;
import models.person.Manager;
import models.person.Staff;
import models.person.UserType;
import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.*;

public class UserFactoryTest {

    @Test
    public void testCreateAdminUser() {
        LocalDateTime now = now();

        User user = UserFactory.createUser(
                UserType.ADMIN, 1, "adminUser", "adminPass", 10,
                "Admin Name", "admin@example.com", now, now, now, true
        );

        assertTrue(user instanceof Admin);
        assertEquals(1, user.getId());
        assertEquals("adminUser", user.getUsername());
        assertEquals("adminPass", user.getPassword());
        assertEquals(10, user.getRole_id());
        assertEquals("Admin Name", user.getFull_name());
        assertEquals("admin@example.com", user.getEmail());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertEquals(now, user.getLastLogin());
        assertTrue(user.isActive());
    }

    @Test
    public void testCreateManagerUser() {
        LocalDateTime now = now();

        User user = UserFactory.createUser(
                UserType.MANAGER, 2, "managerUser", "managerPass", 20,
                "Manager Name", "manager@example.com", now, now, now, false
        );

        assertTrue(user instanceof Manager);
    }

    @Test
    public void testCreateStaffUser() {
        LocalDateTime now = now();

        User user = UserFactory.createUser(
                UserType.STAFF, 3, "staffUser", "staffPass", 30,
                "Staff Name", "staff@example.com", now, now, now, true
        );

        assertTrue(user instanceof Staff);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUserTypeThrowsException() {
        // This will throw a NullPointerException before hitting the switch
        // So you must fix the factory method to explicitly check for null
        UserFactory.createUser(null, 0, "", "", 0, "", "", null, null, null, false);
    }

    @Test
    public void testCreateUserWithEmptyUsername() {
        LocalDateTime now = now();
        User user = UserFactory.createUser(
                UserType.ADMIN, 4, "", "somePass", 1,
                "Name", "email@example.com", now, now, now, true
        );
        assertEquals("", user.getUsername());
    }

    @Test
    public void testMultipleUserTypes() {
        User admin = UserFactory.createUser(UserType.ADMIN, 1, "a", "p", 1, "A", "a@x.com", now(), now(), now(), true);
        User manager = UserFactory.createUser(UserType.MANAGER, 2, "m", "p", 2, "M", "m@x.com", now(), now(), now(), true);
        User staff = UserFactory.createUser(UserType.STAFF, 3, "s", "p", 3, "S", "s@x.com", now(), now(), now(), true);

        assertTrue(admin instanceof Admin);
        assertTrue(manager instanceof Manager);
        assertTrue(staff instanceof Staff);
    }
}