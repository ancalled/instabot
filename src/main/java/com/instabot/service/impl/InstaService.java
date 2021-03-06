package com.instabot.service.impl;

import com.instabot.models.*;
import com.instabot.utils.AccessTokenHelper;
import com.instabot.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.instabot.utils.RequestHelper.*;

public class InstaService {

    public List<User> searchUsersByName(String name) {
        String url = getUrl(Constants.Instagram.SEARCH_USER_BY_NAME, null) + "&q=" + name;
        JSONArray objects = makeRequestJson(url).getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            users.add(new User(objects.getJSONObject(i)));
        }
        return users;
    }

    public User getUserById(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String url = getUrl(Constants.Instagram.GET_DATA, map);
        JSONObject userObject = makeRequestJson(url);
        return userObject != null ? new User(userObject.getJSONObject("data")) : null;
    }

    public List<User> getFollows(String userId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String url = getUrl(Constants.Instagram.GET_FOLLOWS, map);
        JSONArray objects = makeRequestJson(url).getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            users.add(new User(objects.getJSONObject(i)));
        }
        return users;
    }

    public List<User> getFollowers(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String url = getUrl(Constants.Instagram.GET_FOLLOWERS, map);
        JSONArray objects = makeRequestJson(url).getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            users.add(new User(objects.getJSONObject(i)));
        }
        return users;
    }

    public List<Post> getRecentUserMedias(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String url = getUrl(Constants.Instagram.GET_RECENT_MEDIA, map);
        JSONArray objects = makeRequestJson(url).getJSONArray("data");

        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            posts.add(new Post(objects.getJSONObject(i)));
        }
        return posts;
    }

    public Post getMedia(String mediaId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("media_id", mediaId);
        String url = getUrl(Constants.Instagram.GET_MEDIA, map);
        JSONObject object = makeRequestJson(url);
        return new Post(object.getJSONObject("data"));
    }

    public List<Order> getOrdersByMediaId(String mediaId, CommentType commentType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("media_id", mediaId);
        String url = getUrl(Constants.Instagram.GET_MEDIA_COMMENTS, map);
        JSONArray commentObjects = makeRequestJson(url).getJSONArray("data");
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 0; i < commentObjects.length(); i++) {
            Order order = new Order(commentObjects.getJSONObject(i), mediaId);
            if (commentType.equals(CommentType.ORDER) && order.getQty() > 0) {
                orders.add(order);
            } else if (commentType.equals(CommentType.AUTHORIZATION) && order.getAuthCode() != null) {
                orders.add(order);
            } else if (commentType.equals(CommentType.CONFIRM) && order.getText().contains(Constants.CONFIRM_TAG)) {
                orders.add(order);
            }
        }
        return orders;
    }

    public Order postComment(String mediaId, String text) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("media_id", mediaId);
        String url = getUrl(Constants.Instagram.POST_MEDIA_COMMENT, map);

        HashMap<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("access_token", AccessTokenHelper.getAccessToken());
        JSONObject object = makeRequestJson(url, params);
        return new Order(object.getJSONObject("data"), mediaId);
    }

    public Tag getTag(String tagName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tag_name", tagName);
        String url = getUrl(Constants.Instagram.GET_TAG, map);
        JSONObject object = makeRequestJson(url);
        return new Tag(object.getJSONObject("data"));
    }

    public List<Post> getRecentTaggedMedia(String tagName) {
        tagName = tagName.replaceAll("^#*", "");
        HashMap<String, Object> map = new HashMap<>();
        map.put("tag_name", tagName);
        String url = getUrl(Constants.Instagram.GET_RECENT_TAGED_MEDIA, map);
        JSONArray objects = makeRequestJson(url).getJSONArray("data");

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < objects.length(); i++) {
            posts.add(new Post(objects.getJSONObject(i)));
        }
        return posts;
    }

    public List<Tag> searchTags(String tagName) {
        String url = getUrl(Constants.Instagram.SEARCH_TAGS, null) + "&q=" + tagName;
        JSONArray tagItems = makeRequestJson(url).getJSONArray("data");
        ArrayList<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagItems.length(); i++) {
            tags.add(new Tag(tagItems.getJSONObject(i)));
        }
        return tags;
    }
}
