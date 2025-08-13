package models.sale;

import java.math.BigDecimal;

public class SaleItems {
    private int id;
    private int saleId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitePrice;
    private BigDecimal totalPrice;

    public SaleItems() {
        totalPrice = new BigDecimal(0);
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

        if(quantity > 1000) {
            throw new IllegalArgumentException("Quantity cannot be greater than 1000");
        }
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(double unitePrice) {

        if(unitePrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitePrice = unitePrice;
    }

    public BigDecimal  getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (totalPrice != null && totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        this.totalPrice = totalPrice;
    }
}
