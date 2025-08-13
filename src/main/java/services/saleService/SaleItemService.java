package services.saleService;

import dao.interfaces.SaleItemsDAO;
import models.sale.SaleItems;

import java.math.BigDecimal;
import java.util.List;

public class SaleItemService {

    private final SaleItemsDAO saleItemsDAO;
    public SaleItemService(SaleItemsDAO saleItemsDAO) {
        this.saleItemsDAO = saleItemsDAO;
    }

    public void addSaleItem(SaleItems saleItems) throws Exception {

        if(saleItems == null){
            throw new Exception("Sale Items is Empty");
        }

        saleItems.setTotalPrice(saleItems.getTotalPrice().multiply(BigDecimal.valueOf(saleItems.getQuantity())));
        saleItemsDAO.addSaleItem(saleItems);
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
