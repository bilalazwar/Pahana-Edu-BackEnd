package dao.interfaces;

import models.sale.Sale;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleDAO {

    // Create
    int addSale(Sale sale) throws SQLException;

    // Read
    Sale getSaleById(int id) throws SQLException;
    List<Sale> getAllSales() throws SQLException;
    List<Sale> getSalesByCustomerId(int customerId) throws SQLException;
    List<Sale> getSalesByUserId(int userId) throws SQLException;
    List<Sale> getSalesBetweenDates(LocalDateTime start, LocalDateTime end) throws SQLException;
    void updateSale(Sale sale) throws SQLException;
    void deleteSale(int id) throws SQLException;

    // Aggregate / Extra
    double getTotalSalesAmount() throws SQLException;
    double getTotalSalesByUserId(int userId) throws SQLException;
    double getTotalSalesByCustomerId(int customerId) throws SQLException;
    int getSaleCount() throws SQLException;
    int getSaleCountByUserId(int userId) throws SQLException;

}

//        | id | customer_id | sale_date           | total _amount |
//        | -- | ------------| ------------------- | ------------- |
//        | 1  | 1           | 2025-08-01 10:00:00 | 5500.0        |
//        | 2  | 2           | 2025-08-02 14:30:00 | 3000.0        |
//        | 3  | 3           | 2025-08-03 09:15:00 | 5700.0        |