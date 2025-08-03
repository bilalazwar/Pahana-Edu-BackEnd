package models.transaction;

import models.customer.Customer;
import models.parent.Payment;

import java.time.LocalDateTime;
import java.util.List;

public class Transaction {

    private int transactionId;
    private Customer customer;
    private List<TransactionItem> items;
    private double totalAmount;
    private Payment payment;
    private LocalDateTime dateTime;

    public Transaction(int transactionId, Customer customer, List<TransactionItem> items, double totalAmount, Payment payment, LocalDateTime dateTime) {
        this.transactionId = transactionId;
        this.customer = customer;
        this.items = items;
        this.totalAmount = totalAmount;
        this.payment = payment;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
