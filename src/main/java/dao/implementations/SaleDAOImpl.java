package dao.implementations;

import dao.interfaces.SaleDAO;
import models.sale.Sale;
import utils.DBConnectionUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAOImpl implements SaleDAO {


    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getInstance().getConnection();
    }

    // this below has to return the sale ID, only then I will be able to add
    @Override
    public int addSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (customer_id, user_id, sale_date, payment_type, total_amount) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, sale.getCustomerId());
            stmt.setInt(2, sale.getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, "cash");
            stmt.setBigDecimal(5, sale.getTotalAmount());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    sale.setId(generatedId); // Optional: still set it on the object
                    return generatedId;      // 👈 return the generated ID
                } else {
                    throw new SQLException("Failed to retrieve generated sale ID.");
                }
            }
        }
    }


    @Override
    public Sale getSaleById(int id) throws SQLException {
        String sql = "SELECT * FROM sales WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractSale(rs);
            }
        }
        return null;
    }

    @Override
    public List<Sale> getAllSales() throws SQLException {
        String sql = "SELECT * FROM sales";
        List<Sale> sales = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sales.add(extractSale(rs));
            }
        }
        return sales;
    }

    @Override
    public List<Sale> getSalesByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM sales WHERE customer_id = ?";
        List<Sale> sales = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sales.add(extractSale(rs));
            }
        }
        return sales;
    }

    @Override
    public List<Sale> getSalesByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM sales WHERE user_id = ?";
        List<Sale> sales = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sales.add(extractSale(rs));
            }
        }
        return sales;
    }

    @Override
    public List<Sale> getSalesBetweenDates(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = "SELECT * FROM sales WHERE sale_date BETWEEN ? AND ?";
        List<Sale> sales = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(start));
            stmt.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sales.add(extractSale(rs));
            }
        }
        return sales;
    }

    @Override
    public void updateSale(Sale sale) throws SQLException {
        String sql = "UPDATE sales SET customer_id = ?, user_id = ?, sale_date = ?, total_amount = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, sale.getCustomerId());
            stmt.setInt(2, sale.getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(sale.getDateTime()));
            stmt.setBigDecimal(4, sale.getTotalAmount());
            stmt.setInt(5, sale.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteSale(int id) throws SQLException {
        String sql = "DELETE FROM sales WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public double getTotalSalesAmount() throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM sales";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

             ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    @Override
    public double getTotalSalesByUserId(int userId) throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM sales WHERE user_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    @Override
    public double getTotalSalesByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM sales WHERE customer_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    @Override
    public int getSaleCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sales";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }


    @Override
    public int getSaleCountByUserId(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sales WHERE user_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private double getTotalAmount() throws SQLException {

        String sql = "SELECT SUM(total_amount) AS total_sales FROM sales";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1); // Or rs.getDouble("total_sales");
            } else {
                return 0.0; // No rows returned
            }
        }
    }

    // Helper method to convert ResultSet to Sale object
    private Sale extractSale(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setCustomerId(rs.getInt("customer_id"));
        sale.setUserId(rs.getInt("user_id"));
        sale.setDateTime(rs.getTimestamp("sale_date").toLocalDateTime());
        sale.setTotalAmount(rs.getBigDecimal("total_amount"));
        return sale;
    }
}
