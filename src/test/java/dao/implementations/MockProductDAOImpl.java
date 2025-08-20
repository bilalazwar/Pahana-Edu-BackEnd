package dao.implementations;

import dao.interfaces.ProductDAO;
import models.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockProductDAOImpl implements ProductDAO {

    private final Map<Integer, Product> productsDatabase = new HashMap<>();
    private int idCounter = 1;  // to simulate auto-generated IDs for products

    @Override
    public void addProduct(Product product) {
        product.setId(idCounter++);
        productsDatabase.put(product.getId(), product);
    }

    @Override
    public Product getProductById(int id) {
        return productsDatabase.get(id);
    }

    @Override
    public Product getProductByBarcode(Long barcode) {
        return productsDatabase.values().stream()
                .filter(product -> product.getBarcode().equals(barcode))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productsDatabase.values());
    }

    @Override
    public void updateProduct(Product product, int id) {
        if (productsDatabase.containsKey(id)) {
            product.setId(id);  // Ensure the ID stays the same
            productsDatabase.put(id, product);
        }
    }

    @Override
    public void deleteProduct(int id) {
        productsDatabase.remove(id);
    }

    @Override
    public int getProductQuantity(int id) {
        Product product = productsDatabase.get(id);
        return (product != null) ? product.getQuantity() : 0;
    }

    @Override
    public int updateProductQuantity(int id, int quantity) {
        Product product = productsDatabase.get(id);
        if (product != null) {
            product.setQuantity(quantity);
            return 1; // Assuming one row is affected
        }
        return 0;
    }

    @Override
    public boolean productExists(int id) {
        return productsDatabase.containsKey(id);
    }


}
