package com.instabot.models;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private static Logger log = Logger.getLogger(User.class);
    private String id;
    private String userName;
    private String fullName;
    private String profilePictureURI;
    private String bio;
    private String website;
    private int mediaCount = -1;
    private int followerCount = -1;
    private int followingCount = -1;

    public User(JSONObject obj) {
        try {
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
        } catch (Exception e) {
            log.error("Cannot parse JSONObject: " + obj);
            log.error(e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePictureURI() {
        return profilePictureURI;
    }

    private void setProfilePictureURI(String profilePictureURI) {
        this.profilePictureURI = profilePictureURI;
    }

    public String getBio() throws Exception {
        return bio;
    }

    private void setBio(String bio) throws JSONException {
        this.bio = bio;
    }

    public String getWebsite() throws Exception {
        return website;
    }

    private void setWebsite(String website) {
        this.website = website;
    }

    public int getMediaCount() throws Exception {
        return mediaCount;
    }

    private void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public int getFollowerCount() throws Exception {
        return followerCount;
    }

    private void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() throws Exception {
        return followingCount;
    }

    private void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((User) o).getId() == getId());
    }
}
