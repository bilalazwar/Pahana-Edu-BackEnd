package dao.interfaces;

import models.product.Product;

import java.util.List;

public interface ProductDAO {

    void addProduct(Product product) throws Exception;
    Product getProductById(int id) throws Exception;
    List<Product> getAllProducts() throws Exception;
    void updateProduct(Product product) throws Exception;
    void deleteProduct(int id) throws Exception;
    int getProductQuantity(int id) throws Exception;
    int updateProductQuantity(int id, int quantity) throws Exception;
}
