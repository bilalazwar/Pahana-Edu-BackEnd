package dao.implementations;

import dao.interfaces.SaleItemsDAO;
import models.sale.SaleItems;
import utils.DBConnectionUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleItemsDAOImpl implements SaleItemsDAO {

    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getInstance().getConnection();
    }

    @Override
    public void addSaleItem(SaleItems saleItem) throws SQLException {
        String sql = "INSERT INTO sale_items (sale_id, product_id, product_name, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // only then the generated key will be returned.

            BigDecimal unitPrice = BigDecimal.valueOf(saleItem.getUnitePrice());
            BigDecimal quantity = BigDecimal.valueOf(saleItem.getQuantity());
            BigDecimal itemTotal = unitPrice.multiply(quantity);


            stmt.setInt(1, saleItem.getSaleId());
            stmt.setInt(2, saleItem.getProductId());
            stmt.setString(3, saleItem.getProductName());
            stmt.setInt(4, saleItem.getQuantity());
            stmt.setDouble(5, saleItem.getUnitePrice());
            stmt.setBigDecimal(6, itemTotal);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys(); // This line retrieves any auto-generated keys that were created as a result of the insert operation.
            if (rs.next()) {                        // rs.next() moves the cursor to the first row of the ResultSet. If there is a generated key, it returns true.
                saleItem.setId(rs.getInt(1));  // rs.getInt(1) gets the value of the first column in the ResultSet — which is usually the auto-generated ID.
                                                            // saleItem.setId(...) sets this ID back to your SaleItems object so that it now has the database-generated ID stored in it.
            }
        }
    }

    @Override
    public SaleItems getSaleItemById(int id) throws SQLException {
        String sql = "SELECT * FROM sale_items WHERE sale_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractSaleItem(rs);
            }
        }
        return null;
    }

    @Override
    public List<SaleItems> getSaleItemsBySaleId(int saleId) throws SQLException {
        String sql = "SELECT * FROM sale_items WHERE sale_id = ?";
        List<SaleItems> items = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(extractSaleItem(rs));
            }
        }
        return items;
    }

    @Override
    public List<SaleItems> getAllSaleItems() throws SQLException {
        String sql = "SELECT * FROM sale_items";
        List<SaleItems> items = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(extractSaleItem(rs));
            }
        }
        return items;
    }

    @Override
    public void updateSaleItem(SaleItems saleItem) throws SQLException {
        String sql = "UPDATE sale_items SET sale_id = ?, product_id = ?, product_name = ?, quantity = ?, unit_price = ?, total_price = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, saleItem.getSaleId());
            stmt.setInt(2, saleItem.getProductId());
            stmt.setString(3, saleItem.getProductName());
            stmt.setInt(4, saleItem.getQuantity());
            stmt.setDouble(5, saleItem.getUnitePrice());
            stmt.setBigDecimal(6, saleItem.getTotalPrice());
            stmt.setInt(7, saleItem.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteSaleItem(int id) throws SQLException {
        String sql = "DELETE FROM sale_items WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean saleExist(int saleId) throws SQLException {
        String sql = "SELECT 1 FROM sales WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // returns true if any record exists
            }
        }
    }

    // Helper method
    private SaleItems extractSaleItem(ResultSet rs) throws SQLException {
        SaleItems item = new SaleItems();
        item.setId(rs.getInt("id"));
        item.setSaleId(rs.getInt("sale_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitePrice(rs.getDouble("unit_price"));
        item.setTotalPrice(rs.getBigDecimal("total_price"));
        return item;
    }
}
