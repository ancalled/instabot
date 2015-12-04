package com.instabot.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.instabot.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Post {

    private String id;
    private String type;
    private String link;
    private User user;
    private Date whenCreated;
    private String captionId;
    private String captionText;
    private Date captionWhenCreated;
    private int likeCount;
    private int commentCount;
    private List<String> tags;
    private List<Comment> comments;
    private List<User> likers;
    private Status status;

    public Post() {}

    public Post(JSONObject obj) throws JSONException {

        setId(obj.getString("id"));
        setType(obj.getString("type"));
        setLink(obj.optString("link"));
        setUser(new User(obj.getJSONObject("user")));
        setWhenCreated(Utils.timestampToDate(obj.getString("created_time")));

        if (!obj.isNull("caption")) {
            JSONObject captionObj = obj.getJSONObject("caption");
            setCaptionId(captionObj.getString("id"));
            setCaptionText(captionObj.getString("text"));
            setCaptionWhenCreated(Utils.timestampToDate(captionObj.getString("created_time")));
        }

        JSONObject likes = obj.getJSONObject("likes");
        setLikeCount(likes.getInt("count"));

        JSONObject comments = obj.getJSONObject("comments");
        setCommentCount(comments.getInt("count"));

        JSONArray tagStrings = obj.getJSONArray("tags");
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < tagStrings.length(); i++) {
            tags.add(tagStrings.getString(i));
        }
        setTags(tags);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getLikers() {
        return likers;
    }

    public void setLikers(List<User> likers) {
        this.likers = likers;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((Post) o).getId().equals(getId()));
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ACTIVE, ARCHIVE
    }
}
