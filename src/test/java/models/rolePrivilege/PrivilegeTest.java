package models.rolePrivilege;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class PrivilegeTest extends TestCase {

    @Test
    public void testDefaultConstructor() {
        Privilege privilege = new Privilege();
        assertNotNull(privilege);
    }

    @Test
    public void testSetAndGetName() {
        Privilege privilege = new Privilege();
        privilege.setName("VIEW_REPORT");

        assertEquals("VIEW_REPORT", privilege.getName());
    }

    @Test
    public void testSetAndGetId() {
        // Even though ID is usually auto-generated, test the getter/setter
        Privilege privilege = new Privilege();
        privilege.setId(101);

        assertEquals(101, privilege.getId());
    }

    public void testNameCannotBeEmptyString() {
        Privilege privilege = new Privilege();

        assertThrows(IllegalArgumentException.class, () -> {
            privilege.setName("");
        });
    }

}