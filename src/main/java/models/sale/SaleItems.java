package models.sale;

import java.math.BigDecimal;

public class SaleItems {
    private int id;
    private int saleId;
    private int productId;
    private int quantity;
    private double unitePrice;
    private BigDecimal totalPrice;

    public SaleItems() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(double unitePrice) {
        this.unitePrice = unitePrice;
    }

    public BigDecimal  getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal  totalPrice) {
        this.totalPrice = totalPrice;
    }
}
