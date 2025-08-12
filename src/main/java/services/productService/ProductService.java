package services.productService;

import dao.interfaces.ProductDAO;
import dtos.ProductDto;
import mapper.ProductMapper;
import models.product.Product;

import java.util.List;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void addProduct(Product product) throws Exception {

        if(product == null){
            throw new Exception("Product is null");
        }
        productDAO.addProduct(product);
    }

    public ProductDto getProductById(int id) throws Exception {

        if (id <= 0 || !productExists(id)) {
            throw new IllegalArgumentException("Invalid Product Id.");
        }
        if (productDAO.getProductById(id) == null) {
            throw new Exception("Product not found.");
        }
        return ProductMapper.toDto(productDAO.getProductById(id));
    }

    public ProductDto getProductByBarcode(Long barcode) throws Exception {

        if (productDAO.getProductByBarcode(barcode) == null) {
            throw new Exception("Product not found.");
        }
        return ProductMapper.toDto(productDAO.getProductByBarcode(barcode));
    }

    public List<ProductDto> getAllProducts() throws Exception {
        if (productDAO.getAllProducts() == null) {
            throw new Exception("No products found.");
        }
        return ProductMapper.toDTO(productDAO.getAllProducts());
    }

    public void updateProduct(Product product, int id) throws Exception {

        if (product == null || id <= 0 || !productExists(id)) {
            throw new IllegalArgumentException("Product is null");
        }
        productDAO.updateProduct(product, id);
    }

    public void deleteProduct(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid role ID.");
        }
        else productDAO.deleteProduct(id);
    }

    public void updateProductQuantity(int id, int quantity) throws Exception {
        if (id <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid Product Id and quantity.");
        }
        if (!productExists(id)) {
            throw new IllegalArgumentException("Product Doesnt Exist");
        }
        productDAO.updateProductQuantity(id, quantity);
    }

    public int getProductQuantity(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Product ID.");
        }
        else return productDAO.getProductQuantity(id);
    }

    public boolean productExists(int id) throws Exception {
        return productDAO.productExists(id);
    }

}
