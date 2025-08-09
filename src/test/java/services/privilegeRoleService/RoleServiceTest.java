package services.privilegeRoleService;

import dao.interfaces.RoleDAO;
import junit.framework.TestCase;
import models.rolePrivilege.Role;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RoleServiceTest extends TestCase {

    private RoleService roleService;
    private MockRoleDAOImpl mockRoleDAOImpl;

    @Before // means this method runs before each test.
    public void setUp() {
        mockRoleDAOImpl = new MockRoleDAOImpl();
        roleService = new RoleService(mockRoleDAOImpl);
    }

    @Test
    public void testAddRole_ValidName() throws Exception {
        roleService.addRole("Admin");
        assertTrue(mockRoleDAOImpl.addedRoles.contains("Admin"));
    }

    @Test
    public void testAddRole_InvalidName() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roleService.addRole("");
        });
        assertEquals("Role name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testGetAllRoles() throws Exception {
        List<Role> roles = roleService.getAllRoles();
        assertEquals(2, roles.size());
        assertEquals("Admin", roles.get(0).getName());
    }








    // ===== Mock DAO Implementation for Testing =====
    static class MockRoleDAOImpl implements RoleDAO {

        List<String> addedRoles = new ArrayList<>();
        List<Role> updatedRoles = new ArrayList<>();

        @Override
        public void addRole(String roleName) {
            addedRoles.add(roleName);
        }

        @Override
        public Role getRoleById(int id) throws Exception {
            return null;
        }

        @Override
        public List<Role> getAllRoles() {
            List<Role> roles = new ArrayList<>();
            Role r1 = new Role();
            r1.setId(1);
            r1.setName("Admin");

            Role r2 = new Role();
            r2.setId(2);
            r2.setName("Manager");

            roles.add(r1);
            roles.add(r2);
            return roles;
        }

        @Override
        public void updateRole(Role role) {
            updatedRoles.add(role);
        }

        @Override
        public String deleteRoleById(int id) {
            return "Role with ID " + id + " deleted";
        }
    }
}
