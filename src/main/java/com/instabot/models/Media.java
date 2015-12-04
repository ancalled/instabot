package com.instabot.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Media {

    public static enum Filters {
        TOASTER, HUDSON, SIERRA, INKWELL, NORMAL, AMARO, RISE, VALENCIA, NONE
    }

    protected String type;
    protected String filter;
    protected String link;
    protected List<String> tags;
    protected Image lowResolutionImage;
    protected Image thumbnailImage;
    protected Image standardResolutionImage;
    protected List<Comment> comments;
    protected List<User> likers;
    protected User user;
    protected String createdTimestamp;
    protected String id;
    protected Caption caption;
    protected Boolean userHasLikedMedia;
    protected List<UserPhotoTag> usersInPhoto;
    protected int likeCount;
    protected int commentCount;

    public Media(JSONObject obj) throws JSONException {
        if (!obj.isNull("caption"))
            setCaption(this.new Caption(obj.getJSONObject("caption")));
        setCreatedTimestamp(obj.getString("created_time"));
        setFilter(obj.optString("filter"));
        setLink(obj.optString("link"));
        setId(obj.getString("id"));
        setType(obj.getString("type"));
        setUser(new User(obj.getJSONObject("user")));
        setUserHasLikedMedia(obj.getBoolean("user_has_liked"));

        JSONObject images = obj.getJSONObject("images");
        setLowResolutionImage(this.new Image(images.getJSONObject("low_resolution")));
        setThumbnailImage(this.new Image(images.getJSONObject("thumbnail")));
        setStandardResolutionImage(this.new Image(images.getJSONObject("standard_resolution")));

        JSONArray tagStrings = obj.getJSONArray("tags");
        ArrayList<String> tags = new ArrayList<String>();
        for (int i = 0; i < tagStrings.length(); i++) {
            tags.add(tagStrings.getString(i));
        }
        setTags(tags);


        JSONArray jsonUserPhotoTags = obj.optJSONArray("users_in_photo");
        ArrayList<UserPhotoTag> userPhotoTags = new ArrayList<UserPhotoTag>();
        if (jsonUserPhotoTags != null) {
            for (int i = 0; i < jsonUserPhotoTags.length(); i++) {
                userPhotoTags.add(
                        new UserPhotoTag(jsonUserPhotoTags.getJSONObject(i))
                );
            }
        }
        setUsersInPhoto(userPhotoTags);

        JSONObject likes = obj.getJSONObject("likes");
        setLikeCount(likes.getInt("count"));

        JSONObject comments = obj.getJSONObject("comments");
        setCommentCount(comments.getInt("count"));
    }


    public static Media fromJSON(JSONObject obj) throws JSONException {
        if (!obj.getString("type").equals("video")) {
            return new ImageMedia(obj);
        }
        return null;
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public String getFilter() {
        return filter;
    }


    protected void setFilter(String filter) {
        this.filter = filter;
    }

    public int getLikeCount() {
        return likeCount;
    }

    private void setLikeCount(int count) {
        likeCount = count;
    }

    public int getCommentCount() {
        return commentCount;
    }


    private void setCommentCount(int count) {
        commentCount = count;
    }

    public Caption getCaption() {
        return caption;
    }

    public String getLink() {
        return link;
    }


    protected void setLink(String link) {
        this.link = link;
    }

    public List<Comment> getComments() {
        return comments;
    }

    protected void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    public Boolean userHasLikedMedia() {
        return userHasLikedMedia;
    }

    protected void setUserHasLikedMedia(Boolean userHasLikedMedia) {
        this.userHasLikedMedia = userHasLikedMedia;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }


    protected void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getId() {
        return id;
    }


    protected void setId(String id) {
        this.id = id;
    }

    public Image getLowResolutionImage() {
        return lowResolutionImage;
    }


    protected void setLowResolutionImage(Image lowResolutionImage) {
        this.lowResolutionImage = lowResolutionImage;
    }

    public Image getThumbnailImage() {
        return thumbnailImage;
    }


    protected void setThumbnailImage(Image thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public Image getStandardResolutionImage() {
        return standardResolutionImage;
    }


    protected void setStandardResolutionImage(Image standardResolutionImage) {
        this.standardResolutionImage = standardResolutionImage;
    }

    protected void setCaption(Caption caption) {
        this.caption = caption;
    }

    public List<User> getLikers() {
        return likers;
    }

    protected void setLikers(List<User> likers) {
        this.likers = likers;
    }

    public List<String> getTags() {
        return tags;
    }

    protected void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<UserPhotoTag> getUsersInPhoto() {
        return this.usersInPhoto;
    }

    protected void setUsersInPhoto(List<UserPhotoTag> users) {
        this.usersInPhoto = users;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        return ((Media) o).getId().equals(getId());
    }

    public class Image {

        String uri;
        int width;
        int heigth;

        public Image(JSONObject obj) throws JSONException {
            this.setUri(obj.getString("url"));
            this.setWidth(obj.getInt("width"));
            this.setHeigth(obj.getInt("height"));
        }

        public String getUri() {
            return uri;
        }

        protected void setUri(String uri) {
            this.uri = uri;
        }

        public int getWidth() {
            return width;
        }

        protected void setWidth(int width) {
            this.width = width;
        }

        public int getHeigth() {
            return heigth;
        }

        protected void setHeigth(int heigth) {
            this.heigth = heigth;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o.getClass() != this.getClass()) return false;
            return ((Media.Image) o).getUri().equals(getUri());
        }
    }

    public class Caption {

        String text;

        String createdTimestamp;

        User from;

        String id;

        public Caption(JSONObject captionObject) throws JSONException {
            this.setId(captionObject.getString("id"));
            this.setFrom(new User(captionObject.getJSONObject("from")));
            this.setText(captionObject.getString("text"));
            this.setCreatedTimestamp(captionObject.getString("created_time"));
        }

        public String getText() {
            return text;
        }

        protected void setText(String text) {
            this.text = text;
        }

        public String getCreatedTimestamp() {
            return createdTimestamp;
        }

        protected void setCreatedTimestamp(String createdTimestamp) {
            this.createdTimestamp = createdTimestamp;
        }

        public User getFrom() {
            return from;
        }

        protected void setFrom(User from) {
            this.from = from;
        }

        public String getId() {
            return id;
        }

        protected void setId(String id) {
            this.id = id;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o.getClass() != this.getClass()) return false;
            return ((Media.Caption) o).getId().equals(getId());
        }
    }

    public class UserPhotoTag {
        double x;
        double y;
        User user;

        public UserPhotoTag(JSONObject obj) throws JSONException {
            JSONObject position = obj.getJSONObject("position");
            this.setX(position.getDouble("x"));
            this.setY(position.getDouble("y"));
            this.setUser(new User(obj.getJSONObject("user")));
        }

        public double getX() {
            return x;
        }

        private void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        private void setY(double y) {
            this.y = y;
        }

        public User getUser() {
            return user;
        }

        private void setUser(User user) {
            this.user = user;
        }
    }
}
