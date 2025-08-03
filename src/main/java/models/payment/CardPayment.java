package models.payment;

import models.parent.Payment;

public class CardPayment extends Payment {

    private String cardNumber;
    private String cardHolderName;
    private String cardType;

    public CardPayment(int paymentId, double amount, String cardNumber, String cardHolderName, String cardType) {
        super(paymentId, amount);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardType = cardType;
    }

    @Override
    public String getPaymentType() {
        return "Card";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}
