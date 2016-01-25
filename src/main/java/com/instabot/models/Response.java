package com.instabot.models;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Response {

    private static Logger log = Logger.getLogger(Response.class);

    private long ref;
    private long paymentId;
    private PaymentStatus paymentStatus;
    private double discountPrice;
    private Status status;
    private String confirmType;

    public Response(JSONObject obj, long ref) {
        try {
            if (ref != obj.getLong("ref")) {
                log.error("Current ref" + ref + " doesn't match for response" + obj.toString());
                return;
            }

            if (!obj.isNull("paymentId")) {
                setPaymentId(obj.getLong("paymentId"));
            }

            if (!obj.isNull("paymentStatus")) {
                setPaymentStatus(PaymentStatus.valueOf(obj.getString("paymentStatus")));
            }

            if (!obj.isNull("discountPrice")) {
                setDiscountPrice(obj.getDouble("discountPrice"));
            }

            setStatus(Status.valueOf(obj.getString("status")));

            if (!obj.isNull("confirmType")) {
                setConfirmType(obj.getString("confirmType"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
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

    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
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
    }

    @Override
    public String toString() {
        return "ref: " + ref + " paymentId:" + paymentId + " paymentStatus: " + paymentStatus
                + " discountPrice: " + discountPrice + " status: " + status + " confirmType: " + confirmType;
    }
}
