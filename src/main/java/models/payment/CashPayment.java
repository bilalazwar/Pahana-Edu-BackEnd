package models.payment;

import models.parent.Payment;

public class CashPayment extends Payment {
    public CashPayment(int paymentId, double amount) {
        super(paymentId, amount);
    }

    @Override
    public String getPaymentType() {
        return "";
    }
}
