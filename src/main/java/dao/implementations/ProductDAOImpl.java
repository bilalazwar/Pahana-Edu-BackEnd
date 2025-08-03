package dao.implementations;

import dao.interfaces.DepartmentDAO;
import dao.interfaces.ProductDAO;
import models.departmnet.Department;
import models.product.Product;

import java.util.List;

public class ProductDAOImpl implements ProductDAO {


    @Override
    public void addProduct(Product product) throws Exception {

    }

    @Override
    public Product getProductById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        return List.of();
    }

    @Override
    public void updateProduct(Product product) throws Exception {

    }

    @Override
    public void deleteProduct(int id) throws Exception {

    }

    @Override
    public int getProductQuantity(int id) throws Exception {
        return 0;
    }

    @Override
    public int updateProductQuantity(int id, int quantity) throws Exception {
        return 0;
    }

}
