package com.instabot.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.instabot.utils.Utils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.instabot.utils.Constants.*;

public class Post {

    private static Logger log = Logger.getLogger(Post.class);

    private long id;
    private String postId;
    private String link;
    private String userId;
    private String userName;
    private Date whenCreated;
    private String captionId;
    private String captionText;
    private Date captionWhenCreated;
    private String productName;
    private double price;
    private int qty;
    private int leavesQty;
    private int commentCount;
    private List<String> tags;
    private List<Order> orders;
    private Status status;

    public Post() {
    }

    public Post(JSONObject obj) {

        try {


            setPostId(obj.getString("id"));
            setLink(obj.optString("link"));

            User user = new User(obj.getJSONObject("user"));
            setUserId(user.getId());
            setUserName(user.getUserName());

            setWhenCreated(Utils.timestampToDate(obj.getString("created_time")));
            setQty(1);
            setLeavesQty(1);

            if (!obj.isNull("caption")) {
                JSONObject captionObj = obj.getJSONObject("caption");
                setCaptionId(captionObj.getString("id"));
                setCaptionText(captionObj.getString("text"));
                setCaptionWhenCreated(Utils.timestampToDate(captionObj.getString("created_time")));
            }

            JSONObject comments = obj.getJSONObject("comments");
            setCommentCount(comments.getInt("count"));

            JSONArray tagStrings = obj.getJSONArray("tags");
            ArrayList<String> tags = new ArrayList<>();
            for (int i = 0; i < tagStrings.length(); i++) {
                String tagName = tagStrings.getString(i).toLowerCase();

                if (tagName.contains(NAME_TAG)) {
                    String productName = tagName.replace(NAME_TAG, "").replaceAll("_", " ");
                    setProductName(productName);
                }

                if (tagName.contains(PRICE_TAG)) {
                    String priceStr = tagName.replace(PRICE_TAG, "");
                    double price = !priceStr.equals("") ? Double.parseDouble(priceStr) : 0;
                    setPrice(price);
                }

                if (tagName.contains(QTY_TAG)) {
                    String qtyStr = tagName.replace(QTY_TAG, "");
                    int qty = !qtyStr.equals("") ? Integer.parseInt(qtyStr) : 1;
                    setQty(qty);
                    setLeavesQty(qty);
                }

                tags.add(tagName);
            }
            setTags(tags);
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getCaptionId() {
        return captionId;
    }

    public void setCaptionId(String captionId) {
        this.captionId = captionId;
    }

    public String getCaptionText() {
        return captionText;
    }

    public void setCaptionText(String captionText) {
        this.captionText = captionText;
    }

    public Date getCaptionWhenCreated() {
        return captionWhenCreated;
    }

    public void setCaptionWhenCreated(Date captionWhenCreated) {
        this.captionWhenCreated = captionWhenCreated;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(int leavesQty) {
        this.leavesQty = leavesQty;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((Post) o).getPostId().equals(getPostId()));
    }

    @Override
    public String toString() {
        return "\n\tid: " + id + " \n\tpostId: " + postId + " \n\tlink: " + link + " \n\tuserId: " + userId
                + " \n\tuserName: " + userName + " \n\tcaptionText: " + captionText + " \n\tproductName: " + productName
                + " \n\tprice: " + price + " \n\tqty: " + qty + " \n\tleavesQty: " + leavesQty;
    }

    public enum Status {
        ACTIVE, ARCHIVE
    }
}
