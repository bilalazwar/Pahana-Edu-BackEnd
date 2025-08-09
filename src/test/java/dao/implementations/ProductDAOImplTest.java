package dao.implementations;

import dao.interfaces.ProductDAO;
import junit.framework.TestCase;
import models.product.Product;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class ProductDAOImplTest extends TestCase {

    private ProductDAO productDAO;

    @Before
    public void setUp() {
        productDAO = new MockProductDAOImpl();  // Use the mock DAO implementation
    }

    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setName("Product 1");
        product.setPrice(100.0);
        product.setQuantity(10);

        productDAO.addProduct(product);

        assertNotNull(product.getId());  // ID should be set after adding
        Product retrievedProduct = productDAO.getProductById(product.getId());
        assertNotNull(retrievedProduct);
        assertEquals("Product 1", retrievedProduct.getName());
        assertEquals(100.0, retrievedProduct.getPrice());
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setName("Product 2");
        product.setPrice(200.0);
        product.setQuantity(20);
        productDAO.addProduct(product);

        Product retrievedProduct = productDAO.getProductById(product.getId());
        assertNotNull(retrievedProduct);
        assertEquals(product.getId(), retrievedProduct.getId());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Product 3");
        product.setPrice(150.0);
        product.setQuantity(15);
        productDAO.addProduct(product);

        product.setName("Updated Product");
        product.setPrice(180.0);
        productDAO.updateProduct(product, product.getId());

        Product updatedProduct = productDAO.getProductById(product.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(180.0, updatedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Product 4");
        product.setPrice(250.0);
        product.setQuantity(25);
        productDAO.addProduct(product);

        productDAO.deleteProduct(product.getId());
        Product deletedProduct = productDAO.getProductById(product.getId());
        assertNull(deletedProduct);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setName("Product 5");
        product1.setPrice(300.0);
        product1.setQuantity(30);
        productDAO.addProduct(product1);

        Product product2 = new Product();
        product2.setName("Product 6");
        product2.setPrice(400.0);
        product2.setQuantity(40);
        productDAO.addProduct(product2);

        List<Product> products = productDAO.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    public void testProductExists() throws Exception {
        Product product = new Product();
        product.setName("Product 7");
        product.setPrice(500.0);
        product.setQuantity(50);
        productDAO.addProduct(product);

        assertTrue(productDAO.productExists(product.getId()));
        assertFalse(productDAO.productExists(999));  // Non-existent ID
    }

}