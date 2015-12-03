package com.instabot;

import com.instabot.models.Comment;
import com.instabot.models.Media;
import com.instabot.models.Tag;
import com.instabot.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.instabot.RequestHelper.*;

public class MainService {

    public List<User> searchUsersByName(String name) {
        String uri = getUrl(Endpoints.Users.SEARCH_USER_BY_NAME, null) + "&q=" + name;
        JSONArray objects = makeRequestJson(uri).getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            users.add(new User(objects.getJSONObject(i)));
        }
        return users;
    }

    public User getUserById(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String uri = getUrl(Endpoints.Users.GET_DATA, map);
        JSONObject userObject = makeRequestJson(uri);
        return userObject != null ? new User(userObject.getJSONObject("data")) : null;
    }


    public List<User> getFollows(String userId) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", userId);
        String uri = getUrl(Endpoints.Relationships.GET_FOLLOWS, map);
        JSONArray objects = makeRequestJson(uri).getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            users.add(new User(objects.getJSONObject(i)));
        }
        return users;
    }

    public List<User> getFollowers(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String uri = getUrl(Endpoints.Relationships.GET_FOLLOWERS, map);
        JSONArray objects = makeRequestJson(uri).getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            users.add(new User(objects.getJSONObject(i)));
        }
        return users;
    }

    public List<Media> getRecentUserMedias(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        String uri = getUrl(Endpoints.Users.GET_RECENT_MEDIA, map);
        JSONArray objects = makeRequestJson(uri).getJSONArray("data");

        List<Media> medias = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            medias.add(new Media(objects.getJSONObject(i)));
        }
        return medias;
    }

    public Media getMedia(String mediaId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("media_id", mediaId);
        String uri = getUrl(Endpoints.Media.GET_MEDIA, map);
        JSONObject object = makeRequestJson(uri);
        return Media.fromJSON(object.getJSONObject("data"));
    }

    public List<Comment> getMediaComments(String mediaId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("media_id", mediaId);
        String url = getUrl(Endpoints.Comments.GET_MEDIA_COMMENTS, map);
        JSONArray commentObjects = makeRequestJson(url).getJSONArray("data");
        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 0; i < commentObjects.length(); i++) {
            comments.add(new Comment(commentObjects.getJSONObject(i)));
        }
        return comments;
    }

    public Comment postComment(String mediaId, String text) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("media_id", mediaId);
        String url = getUrl(Endpoints.Comments.POST_MEDIA_COMMENT, map);

        HashMap<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("access_token", AccessTokenHelper.getAccessToken());
        JSONObject object = makeRequestJson(url, params);
        return new Comment(object.getJSONObject("data"));
    }

    public Tag getTag(String tagName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tag_name", tagName);
        String url = getUrl(Endpoints.Tags.GET_TAG, map);
        JSONObject object = makeRequestJson(url);
        return new Tag(object.getJSONObject("data"));
    }

    public List<Media> getRecentMediaForTag(String tagName) {
        tagName = tagName.replaceAll("^#*", "");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tag_name", tagName);
        String uri = getUrl(Endpoints.Tags.GET_RECENT_TAGED_MEDIA, map);
        JSONArray objects = makeRequestJson(uri).getJSONArray("data");

        List<Media> medias = new ArrayList<>();

        for (int i = 0; i < objects.length(); i++) {
            medias.add(new Media(objects.getJSONObject(i)));
        }
        return medias;
    }
}
