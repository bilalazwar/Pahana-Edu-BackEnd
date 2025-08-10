package models.product;

import junit.framework.TestCase;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;

public class ProductTest extends TestCase {

    @Test
    public void testSettersAndGetters() {
        Product product = new Product();
        product.setId(1);
        product.setName("Book Title");
        product.setDescription("A great book");
        product.setType("Book");
        product.setAuthor("Author Name");
        product.setPublisher("Publisher Inc.");
        product.setIsbn("1234567890123");
        product.setLanguage("English");
        product.setBarcode(9876543210123L);
        product.setPrice(29.99);
        product.setDiscount(5.0);
        product.setQuantity(10);
        product.setActive(true);
        product.setDeleted(false);
        product.setCostPrice(15.50);
        product.setInternalNotes("Internal use only");
        product.setCreatedBy("admin");
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        // Now test all getters
        assertEquals(1, product.getId());
        assertEquals("Book Title", product.getName());
        assertEquals("A great book", product.getDescription());
        assertEquals("Book", product.getType());
        assertEquals("Author Name", product.getAuthor());
        assertEquals("Publisher Inc.", product.getPublisher());
        assertEquals("1234567890123", product.getIsbn());
        assertEquals("English", product.getLanguage());
        assertEquals(9876543210123L, (long) product.getBarcode());
        assertEquals(29.99, product.getPrice(), 0.001);
        assertEquals(5.0, product.getDiscount(), 0.001);
        assertEquals(10, product.getQuantity());
        assertTrue(product.isActive());
        assertFalse(product.isDeleted());
        assertEquals(15.50, product.getCostPrice(), 0.001);
        assertEquals("Internal use only", product.getInternalNotes());
        assertEquals("admin", product.getCreatedBy());
        assertEquals(now, product.getCreatedAt());
        assertEquals(now, product.getUpdatedAt());
    }

    @Test
    public void testDefaultConstructor() {    // checking whether it allows us to create products without arguments
        Product product = new Product();
        assertNotNull(product);               // making sure the object is not null
    }

    @Test
    public void testSetAndGetName() {
        Product product = new Product();
        product.setName("Notebook");
        assertEquals("Notebook", product.getName());
    }

    @Test
    public void testSetAndGetPrice() {
        Product product = new Product();
        product.setPrice(99.99);
        assertEquals(99.99, product.getPrice());
    }

    @Test
    public void testCostPrice() {
        Product product = new Product();
        product.setCostPrice(45.50);
        assertEquals(45.50, product.getCostPrice());
    }

    @Test
    public void testActiveAndDeletedFlags() {
        Product product = new Product();
        assertFalse(product.isActive());
        assertFalse(product.isDeleted());

        product.setActive(true);
        product.setDeleted(true);

        assertTrue(product.isActive());
        assertTrue(product.isDeleted());
    }

    @Test
    public void testCreatedBy() {
        Product product = new Product();
        product.setCreatedBy("admin");
        assertEquals("admin", product.getCreatedBy());
    }

    @Test
    public void testNegativePrice() {
        Product product = new Product();

        // Verify that an exception is thrown when setting a negative price
        assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(-99.99);
        });
    }

    @Test
    public void testNullFields() {
        Product product = new Product();

        product.setName(null);
        product.setAuthor(null);
        product.setLanguage(null);
        product.setInternalNotes(null);

        assertNull(product.getName());
        assertNull(product.getAuthor());
        assertNull(product.getLanguage());
        assertNull(product.getInternalNotes());
    }

    @Test
    public void testExtremeDates() {
        Product product = new Product();

        LocalDateTime futureDate = LocalDateTime.of(3000, 1, 1, 0, 0);
        LocalDateTime pastDate = LocalDateTime.of(1900, 1, 1, 0, 0);

        // Test: Trying to set createdAt to a future date should throw an exception
        assertThrows(IllegalArgumentException.class, () -> {
            product.setCreatedAt(futureDate);
        });

        // Test: Trying to set updatedAt to a date before createdAt should throw an exception
        product.setCreatedAt(LocalDateTime.now()); // Set a valid createdAt
        assertThrows(IllegalArgumentException.class, () -> {
            product.setUpdatedAt(pastDate);
        });

        // Test: Setting valid dates (createdAt is set before updatedAt)
        LocalDateTime validCreatedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime validUpdatedAt = LocalDateTime.now();
        product.setCreatedAt(validCreatedAt);
        product.setUpdatedAt(validUpdatedAt);

        assertEquals(validCreatedAt, product.getCreatedAt());
        assertEquals(validUpdatedAt, product.getUpdatedAt());
    }

    @Test
    public void testZeroPriceAndDiscount() {
        Product product = new Product();
        product.setPrice(0.00);
        product.setDiscount(0.00);
        assertEquals(0.00, product.getPrice());
        assertEquals(0.00, product.getDiscount());
    }




}