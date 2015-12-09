package com.instabot.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment {

    private long id;
    private long paymentId;
    private String customer;
    private String merchant;
    private PaymentStatus paymentStatus;
    private double discountPrice;
    private Status status;

    public Payment(JSONObject obj) throws JSONException {
        setPaymentId(obj.getLong("paymentId"));
        setCustomer(obj.getString("customer"));
        setMerchant(obj.getString("merchant"));
        setDiscountPrice(obj.getDouble("discountPrice"));
        setStatus(Status.valueOf(obj.getString("status")));

        if (!obj.isNull("paymentStatus")) {
            setPaymentStatus(PaymentStatus.valueOf(obj.getString("paymentStatus")));
        }
    }

    public Payment() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        ERROR(1, "Неизветсная ошибка"),
        OK(0, "Ok"),
        CUSTOMER_NOT_FOUND(2, "Клиент не найден"),
        MERCHANT_NOT_FOUND(3, "Организация не найдена"),
        MERCHANT_TOKEN_NOT_FOUND(4, "Токен не найден"),
        MERCHANT_TOKEN_NOT_MATCH(5, "Токены не корректный"),
        NOT_CORRECT_SECRET_CODE(6, "Не корректный секретный код"),
        NOT_CORRECT_PRICE(7, "Цена указана не верно"),
        NOT_CORRECT_NUMBER(8, "Не корректное число"),
        CANT_SEND_SECRET_CODE(9, "Не могу отослать секретный код"),
        TRANSACTION_ERROR(10, "Ошибка транзакции"),
        PAYMENT_NOT_FOUND(11, "Платеж не найден");

        private int code;
        private String comment;

        Status(int code, String comment) {
            this.code = code;
            this.comment = comment;
        }

        public int getCode() {
            return code;
        }

        public String getComment() {
            return comment;
        }

    }

    public enum PaymentStatus {
        CREATED,
        AUTHORIZED,
        FUNDED,
        INTRANSIT,
        SETTLED,
        REFUNDED,
        PARTIALLY_REFUNDED,
        CANCELED;
    }

    @Override
    public String toString() {
        return "paymentId: " + paymentId + " customer: " + customer + " merchant: " + merchant
                + " paymentStatus:" + paymentStatus + " status: " + status;
    }
}
