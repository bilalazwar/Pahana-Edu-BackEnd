package dao.interfaces;

import models.sale.SaleItems;

import java.sql.SQLException;
import java.util.List;

public interface SaleItemsDAO {
    void addSaleItem(SaleItems saleItem) throws SQLException;
    SaleItems getSaleItemById(int id) throws SQLException;
    List<SaleItems> getSaleItemsBySaleId(int saleId) throws SQLException;
    boolean saleExist(int saleId) throws SQLException;
    List<SaleItems> getAllSaleItems() throws SQLException;
    void updateSaleItem(SaleItems saleItem) throws SQLException;
    void deleteSaleItem(int id) throws SQLException;
}

//        | id | sale_id  | product_id  | quantity | unit_price  | total_price  |
//        | -- | -------- | ----------- | -------- | ----------- | ------------ |
//        | 1  | 1        | 1           | 1        | 2500.0      | 2500.0       |
//        | 2  | 1        | 3           | 1        | 3200.0      | 3200.0       |
//        | 3  | 2        | 2           | 1        | 3000.0      | 3000.0       |

