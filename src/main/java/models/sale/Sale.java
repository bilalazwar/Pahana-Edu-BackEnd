package models.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Sale {

    private int id;
    private int customerId;
    private int userId;
    private LocalDateTime dateTime;
    private BigDecimal totalAmount;
    private List<SaleItems> items;
    // need to add total quantity also later

    public Sale(){

    }

    public int getId() {
        return id;
    }

    public List<SaleItems> getItems() {
        return items;
    }

    public void setItems(List<SaleItems> items) {
        this.items = items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateTime() {
//        return dateTime;
        return dateTime != null ? dateTime.toString() : null;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal  totalAmount) {
        this.totalAmount = totalAmount;
    }
}
