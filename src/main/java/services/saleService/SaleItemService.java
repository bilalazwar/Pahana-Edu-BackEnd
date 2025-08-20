package services.saleService;

import dao.implementations.ProductDAOImpl;
import dao.interfaces.ProductDAO;
import dao.interfaces.SaleItemsDAO;
import models.sale.SaleItems;

import java.math.BigDecimal;
import java.util.List;

public class SaleItemService {

    private final SaleItemsDAO saleItemsDAO;
    ProductDAO  productDAO;

    public SaleItemService(SaleItemsDAO saleItemsDAO, ProductDAO  productDAO) {
        this.saleItemsDAO = saleItemsDAO;
        this.productDAO =  productDAO;
    }

    public void addSaleItem(SaleItems saleItems) throws Exception {

        if(saleItems == null){
            throw new Exception("Sale Items is Empty");
        }

        saleItems.setTotalPrice(saleItems.getTotalPrice().multiply(BigDecimal.valueOf(saleItems.getQuantity())));
        saleItemsDAO.addSaleItem(saleItems);

        // need to update the quantity.
        int currentQty = productDAO.getProductQuantity(saleItems.getProductId());
//        System.out.println("Current Quantity: " + currentQty);
        int updatedQty = currentQty - saleItems.getQuantity();
        saleItems.setQuantity(updatedQty);
        productDAO.updateProductQuantity(saleItems.getProductId(), updatedQty);

    }

    public SaleItems getSaleItemById(int id) throws Exception {
        return null;
    }

    public List<SaleItems> getSaleItemsBySaleId(int saleId) throws Exception {

        if (saleId <= 0 || !saleExist(saleId)) {
            throw new IllegalArgumentException("Invalid Sale Id.");
        }
        return saleItemsDAO.getSaleItemsBySaleId(saleId);
    }

    private boolean saleExist(int saleId) throws Exception {
        return saleItemsDAO.saleExist(saleId);
    }
}
