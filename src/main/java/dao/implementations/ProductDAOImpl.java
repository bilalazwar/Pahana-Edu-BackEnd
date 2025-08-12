package dao.implementations;

import dao.interfaces.ProductDAO;
import models.product.Product;
import utils.DBConnectionUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ProductDAOImpl implements ProductDAO {

    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getInstance().getConnection();
    }

    @Override
    public void addProduct(Product product) throws Exception {

        String sql = "INSERT INTO products (name, description, type, author, publisher, isbn, language, " +
                "price, discount, barcode, quantity, active, deleted, cost_price, internal_notes, created_by, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getType());
            stmt.setString(4, product.getAuthor());
            stmt.setString(5, product.getPublisher());
            stmt.setString(6, product.getIsbn());
            stmt.setString(7, product.getLanguage());
            stmt.setDouble(8, product.getPrice());
            stmt.setDouble(9, product.getDiscount());
            stmt.setLong(10, product.getBarcode());           // ✅ Barcode added here
            stmt.setInt(11, product.getQuantity());
            stmt.setBoolean(12, product.isActive());
            stmt.setBoolean(13, product.isDeleted());
            stmt.setDouble(14, product.getCostPrice());
            stmt.setString(15, product.getInternalNotes());
            stmt.setString(16, product.getCreatedBy());
            stmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(18, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
        }
    }



    @Override
    public Product getProductById(int id) throws Exception {
        String sql = "SELECT * FROM products WHERE id = ? AND deleted = false";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
//                Product product = extractProductFromResultSet(rs);
                return extractProductFromResultSet(rs);
            }
            else return null;
        }
    }

    @Override
    public Product getProductByBarcode(Long barcode) throws Exception {
        String sql = "SELECT * FROM products WHERE barcode = ? AND deleted = false";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, barcode);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
            else return null;
        }
    }


    @Override
    public List<Product> getAllProducts() throws Exception {
        String sql = "SELECT * FROM products WHERE deleted = false";

        List<Product> products = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }

        return products;
    }

    @Override
    public void updateProduct(Product product, int id) throws Exception {

        String sql = "UPDATE products SET name = ?, description = ?, type = ?, author = ?, publisher = ?, isbn = ?, " +
                "language = ?, barcode = ?, price = ?, discount = ?, quantity = ?, active = ?, deleted = ?, " +
                "cost_price = ?, internal_notes = ?, updated_at = ?,  cost_price = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getType());
            stmt.setString(4, product.getAuthor());
            stmt.setString(5, product.getPublisher());
            stmt.setString(6, product.getIsbn());
            stmt.setString(7, product.getLanguage());
            stmt.setLong(8, product.getBarcode());  // Assuming barcode is a string
            stmt.setDouble(9, product.getPrice());
            stmt.setDouble(10, product.getDiscount());  // Corrected index
            stmt.setInt(11, product.getQuantity());
            stmt.setBoolean(12, product.isActive());
            stmt.setBoolean(13, product.isDeleted());
            stmt.setDouble(14, product.getCostPrice());
            stmt.setString(15, product.getInternalNotes());
            stmt.setTimestamp(16, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(17, id);  // Corrected index for id
            stmt.setInt(18, id);

            stmt.executeUpdate();
        }
    }


    @Override
    public void deleteProduct(int id) throws Exception {
        String sql = "UPDATE products SET deleted = true, updated_at = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public int getProductQuantity(int id) throws Exception {
        String sql = "SELECT quantity FROM products WHERE id = ? AND deleted = false";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
                else return 0;
            }
        }
    }

    @Override
    public int updateProductQuantity(int id, int quantity) throws Exception {
        String sql = "UPDATE products SET quantity = ?, updated_at = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, id);

            return stmt.executeUpdate(); // returns affected row count
        }
    }

    @Override
    public boolean productExists(int id) throws Exception {
        String sql = "SELECT 1 FROM products WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a row exists, user exists
            }
        }
    }

    // Helper method to map result set to product object
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();

        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setType(rs.getString("type"));
        product.setAuthor(rs.getString("author"));
        product.setPublisher(rs.getString("publisher"));
        product.setIsbn(rs.getString("isbn"));
        product.setLanguage(rs.getString("language"));
        product.setBarcode(rs.getLong("barcode"));

        product.setPrice(rs.getDouble("price"));
        product.setDiscount(rs.getDouble("discount"));
        product.setQuantity(rs.getInt("quantity"));

        product.setActive(rs.getBoolean("active"));
        product.setDeleted(rs.getBoolean("deleted"));
        product.setCostPrice(rs.getDouble("cost_price"));
        product.setInternalNotes(rs.getString("internal_notes"));
        product.setCreatedBy(rs.getString("created_by"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) product.setCreatedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) product.setUpdatedAt(updatedAt.toLocalDateTime());

        return product;
    }

}
//LocalDateTime.now()