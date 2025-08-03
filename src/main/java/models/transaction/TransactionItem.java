package models.transaction;

import models.product.Product;

public class TransactionItem {

    private int transactionItemId;
    private Product product;
    private int quantity;
    private double price;

    public TransactionItem(int transactionItemId, Product product, int quantity, double price) {
        this.transactionItemId = transactionItemId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public int getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(int transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
