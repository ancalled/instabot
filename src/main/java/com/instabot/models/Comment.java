package com.instabot.models;

import com.instabot.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Comment {

    private String id;
    private String postId;
    private String text;
    private User sender;
    private Date whenCreated;

    public Comment(){}

    public Comment(JSONObject obj, String postId) throws JSONException {
        setId(obj.getString("id"));
        setText(obj.getString("text"));
        setSender((new User(obj.getJSONObject("from"))));
        setWhenCreated(Utils.timestampToDate(obj.getString("created_time")));
        setPostId(postId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((Comment) o).getId().equals(getId()));
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}