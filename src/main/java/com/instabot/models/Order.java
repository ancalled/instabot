package com.instabot.models;

import com.instabot.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.instabot.utils.Constants.BUY_TAG;

public class Order {

    private long id;
    private String commentId;
    private String postId;
    private int qty;
    private String text;
    private User user;
    private Date whenCreated;

    public Order() {
    }

    public Order(JSONObject obj, String postId) throws JSONException {
        setCommentId(obj.getString("id"));
        setText(obj.getString("text"));
        setUser((new User(obj.getJSONObject("from"))));
        setWhenCreated(Utils.timestampToDate(obj.getString("created_time")));
        setPostId(postId);

        if (getText().contains(BUY_TAG)) {
            String qtyStr = getText().replace(BUY_TAG, "");
            int qty = !qtyStr.equals("") ? Integer.parseInt(qtyStr) : 1;
            setQty(qty);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((Order) o).getCommentId().equals(getCommentId()));
    }

}