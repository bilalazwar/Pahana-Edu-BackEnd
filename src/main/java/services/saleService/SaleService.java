package services.saleService;

import dao.implementations.SaleItemsDAOImpl;
import dao.interfaces.SaleDAO;
import dao.interfaces.SaleItemsDAO;
import models.sale.Sale;
import models.sale.SaleItems;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SaleService {

    private final SaleDAO saleDAO;

    public SaleService(SaleDAO saleDAO, SaleItemsDAO saleItemsDAO) {
        this.saleDAO = saleDAO;
    }

    private final SaleItemsDAO saleItemsDAO = new SaleItemsDAOImpl();
    private final SaleItemService saleItemService = new SaleItemService(saleItemsDAO);

    public void createSale1(Sale sale) throws Exception {
        if (sale == null) {
            throw new Exception("Sale is null.");
        }
        if(sale.getCustomerId() == 0){
            throw new Exception("Customer Id is null.");
        }

        try {

            List<SaleItems> saleItemsList = sale.getItems();
            for(SaleItems saleItem1 : saleItemsList){

//                BigDecimal unitPrice = BigDecimal.valueOf(saleItem1.getUnitePrice());
//                BigDecimal quantity = BigDecimal.valueOf(saleItem1.getQuantity());
//                BigDecimal totalPrice = unitPrice.multiply(quantity);


                Double unitPrice, totalPrice = 0.00;
                int quantity = 0;
                unitPrice = saleItem1.getUnitePrice();
                quantity = saleItem1.getQuantity();
                totalPrice = unitPrice * quantity;

                BigDecimal price = new BigDecimal(totalPrice);


                saleItem1.setTotalPrice(price);

            }

            BigDecimal totalAmount1 = BigDecimal.ZERO;
            for(SaleItems saleItems : saleItemsList){
                totalAmount1 = totalAmount1.add(saleItems.getTotalPrice());
                System.out.println("Individual Sale Items Price == "+ totalAmount1);
            }

            sale.setTotalAmount(totalAmount1);
            int generatedId = saleDAO.addSale(sale);

            if(generatedId != -1){

                for(SaleItems saleItems : saleItemsList){
                    saleItems.setSaleId(generatedId);
//                    saleItemsDAO.addSaleItem(saleItems);
                    saleItemService.addSaleItem(saleItems);
                    System.out.println(saleItems.getProductName());
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); // Can replace with Logger
            throw new Exception("Failed to create sale: " + e.getMessage());
        }
    }

    public void createSale(Sale sale) throws Exception {

        List<SaleItems> saleItemsList = sale.getItems();

        BigDecimal totalSaleAmount = BigDecimal.ZERO;

        for (SaleItems saleItem : saleItemsList) {
            BigDecimal unitPrice = BigDecimal.valueOf(saleItem.getUnitePrice());
            BigDecimal quantity = BigDecimal.valueOf(saleItem.getQuantity());
            BigDecimal itemTotal = unitPrice.multiply(quantity);

            saleItem.setTotalPrice(itemTotal); //
            totalSaleAmount = totalSaleAmount.add(itemTotal);
        }

        sale.setTotalAmount(totalSaleAmount);

        int generatedId = saleDAO.addSale(sale);

        if(generatedId != -1){

            for(SaleItems saleItems : saleItemsList){
                saleItems.setSaleId(generatedId);
//                    saleItemsDAO.addSaleItem(saleItems);
                saleItemService.addSaleItem(saleItems);
                System.out.println(saleItems.getProductName());
            }
        }

    }


    public Sale getSaleById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid sale ID.");
        }

        try {
            List<SaleItems> saleItems = saleItemsDAO.getSaleItemsBySaleId(id);
            Sale sale = saleDAO.getSaleById(id);
            sale.setItems(saleItems);
            return sale;
        }
        catch (SQLException e) {
            throw new Exception("Failed to fetch sale by ID: " + e.getMessage());
        }
    }

    public List<Sale> getAllSales() throws Exception {
        try {
            List<Sale> sales = saleDAO.getAllSales();

            for (Sale sale : sales) {
                sale.setItems(saleItemService.getSaleItemsBySaleId(sale.getId()));
            }

            return sales;
        } catch (SQLException e) {
            throw new Exception("Failed to fetch sales: " + e.getMessage());
        }
    }

    public List<Sale> getSalesByCustomerId(int customerId) throws Exception {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        try {
            return saleDAO.getSalesByCustomerId(customerId);
        } catch (SQLException e) {
            throw new Exception("Failed to fetch sales by customer ID: " + e.getMessage());
        }
    }

    public List<Sale> getSalesByUserId(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        try {
            return saleDAO.getSalesByUserId(userId);
        } catch (SQLException e) {
            throw new Exception("Failed to fetch sales by user ID: " + e.getMessage());
        }
    }

    public List<Sale> getSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid date range.");
        }

        try {
            return saleDAO.getSalesBetweenDates(startDate, endDate);
        }
        catch (SQLException e) {
            throw new Exception("Failed to fetch sales between dates: " + e.getMessage());
        }
    }

    public void updateSale(Sale sale) throws Exception {
        if (sale == null || sale.getId() <= 0) {
            throw new IllegalArgumentException("Invalid sale data.");
        }

        try {
            saleDAO.updateSale(sale);
        }
        catch (SQLException e) {
            throw new Exception("Failed to update sale: " + e.getMessage());
        }
    }

    public void deleteSale(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid sale ID.");
        }

        try {
            saleDAO.deleteSale(id);
        }
        catch (SQLException e) {
            throw new Exception("Failed to delete sale: " + e.getMessage());
        }
    }

    public double getTotalSalesAmount() throws Exception {
        try {
            return saleDAO.getTotalSalesAmount();
        } catch (SQLException e) {
            throw new Exception("Failed to calculate total sales: " + e.getMessage());
        }
    }

    public double getTotalSalesByUserId(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        try {
            return saleDAO.getTotalSalesByUserId(userId);
        } catch (SQLException e) {
            throw new Exception("Failed to calculate total sales by user: " + e.getMessage());
        }
    }

    public double getTotalSalesByCustomerId(int customerId) throws Exception {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        try {
            return saleDAO.getTotalSalesByCustomerId(customerId);
        } catch (SQLException e) {
            throw new Exception("Failed to calculate total sales by customer: " + e.getMessage());
        }
    }


    public int getSaleCount() throws Exception {

        try {
            return saleDAO.getSaleCount();
        }
        catch (SQLException e) {
            throw new Exception("Failed to fetch sale Count " + e.getMessage());
        }
    }
}
