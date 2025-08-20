package utils;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class PasswordUtilTest extends TestCase {

    @Test
    public void testHashPassword() {
        String password = "mySecretPassword";

        // Expected hash value for "mySecretPassword" (SHA-256) -
        // You can use an online tool to generate this or run the method once
        String expectedHash = "2250e74c6f823de9d70c2222802cd059dc970f56ed8d41d5d22d1a6d4a2ab66f";

        // Get the hash for the password
        String actualHash = PasswordUtil.hashPassword(password);
//        System.out.println("Hash Password: " + actualHash);

        // Assert the result matches the expected hash
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashPasswordConsistency() {
        String password = "testPassword";

        // Hash the password multiple times to ensure the result is always the same
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);

        assertEquals(hash1, hash2); // The hash should be the same every time
    }

    @Test
    public void testHashPasswordNotNull() {
        String password = "somePassword";

        String hash = PasswordUtil.hashPassword(password);

        assertNotNull(hash); // The hash should not be null
    }

    @Test
    public void testHashPasswordLength() {
        String password = "checkLength";

        String hash = PasswordUtil.hashPassword(password);

        assertEquals(64, hash.length()); // SHA-256 produces a 64-character hash
    }

    @Test
    public void testHashPasswordWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.hashPassword(null);
        });
    }

    @Test
    public void testCaseSensitivity() {
        String lower = "password";
        String upper = "PASSWORD";
        assertNotEquals(PasswordUtil.hashPassword(lower), PasswordUtil.hashPassword(upper));
    }

    @Test
    public void testWhitespaceSensitiveHashing() {
        String password1 = "password";
        String password2 = " password ";
        assertNotEquals(PasswordUtil.hashPassword(password1), PasswordUtil.hashPassword(password2));
    }


}