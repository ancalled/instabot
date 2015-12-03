package com.instabot.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    protected String id;
    protected String userName;
    protected String fullName;
    protected String profilePictureURI;
    protected String bio;
    protected String website;
    protected int mediaCount = -1;
    protected int followerCount = -1;
    protected int followingCount = -1;

    public User(JSONObject obj) throws JSONException {

        setId(obj.getString("id"));
        setUserName(obj.getString("username"));
        setFullName(obj.getString("full_name"));
        setProfilePictureURI(obj.getString("profile_picture"));

        setWebsite(obj.optString("website"));
        setBio(obj.optString("bio"));

        if (obj.has("counts")) {
            JSONObject counts = obj.getJSONObject("counts");
            setFollowerCount(counts.getInt("followed_by"));
            setFollowingCount(counts.getInt("follows"));
            setMediaCount(counts.getInt("media"));
        } else {
            setFollowerCount(-1);
            setFollowingCount(-1);
            setMediaCount(-1);
        }
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    protected void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePictureURI() {
        return profilePictureURI;
    }

    protected void setProfilePictureURI(String profilePictureURI) {
        this.profilePictureURI = profilePictureURI;
    }

    public String getBio() throws Exception {
        return bio;
    }

    protected void setBio(String bio) throws JSONException {
        this.bio = bio;
    }

    public String getWebsite() throws Exception {
        return website;
    }

    protected void setWebsite(String website) {
        this.website = website;
    }

    public int getMediaCount() throws Exception {
        return mediaCount;
    }

    protected void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public int getFollowerCount() throws Exception {
        return followerCount;
    }

    protected void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() throws Exception {
        return followingCount;
    }

    protected void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((User) o).getId() == getId());
    }
}
