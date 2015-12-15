package com.instabot.models;

import com.instabot.utils.Utils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.instabot.utils.Constants.AUTH_CODE_TAG;
import static com.instabot.utils.Constants.BUY_TAG;

public class Order {

    private static Logger log = Logger.getLogger(Order.class);

    private long id;
    private String commentId;
    private String postId;
    private int qty;
    private String text;
    private String userId;
    private String userName;
    private long paymentId;
    private PaymentStatus status;
    private double discountPrice;
    private String authCode;
    private Date whenCreated;

    public Order() {
    }

    public Order(JSONObject obj, String postId) {
        try {
            setCommentId(obj.getString("id"));
            setText(obj.getString("text"));

            User user = new User(obj.getJSONObject("from"));
            setUserId(user.getId());
            setUserName(user.getUserName());

            setWhenCreated(Utils.timestampToDate(obj.getString("created_time")));
            setPostId(postId);

            if (getText().contains(BUY_TAG)) {
                String qtyStr = getText().replace(BUY_TAG, "");
                int qty = !qtyStr.equals("") ? Integer.parseInt(qtyStr) : 1;
                setQty(qty);
            }

            if (getText().contains(AUTH_CODE_TAG)) {
                String authCode = getText().replace(AUTH_CODE_TAG, "");
                setAuthCode(authCode);
            }
        } catch (Exception e) {
            log.error("Cannot parse JSONObject: " + obj);
            log.error(e.getMessage());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((Order) o).getCommentId().equals(getCommentId()));
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "\n\tid: " + id + "\n\tcommentId: " + commentId + "\n\tpostId: " + postId + "\n\tqty: " + qty
                + "\n\ttext: " + text + "\n\tuserId: " + userId + "\n\tuserName: " + userName
                + "\n\tpaymentId: " + paymentId + "\n\tPaymentStatus: " + status + "\n\tdiscountPrice: " + discountPrice;
    }
}