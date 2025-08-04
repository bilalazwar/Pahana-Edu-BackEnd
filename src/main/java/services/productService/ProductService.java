package services.productService;

import dao.interfaces.ProductDAO;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }


}
