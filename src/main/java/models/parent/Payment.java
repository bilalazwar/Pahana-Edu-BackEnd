package models.parent;

public abstract class Payment {
    private int paymentId;
    private double amount;

    public Payment(int paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
    }

    public abstract String getPaymentType();

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
