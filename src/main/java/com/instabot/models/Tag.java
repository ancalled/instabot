package com.instabot.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tag {

    int mediaCount;
    String name;

    public Tag(JSONObject obj) throws JSONException {
        setName(obj.getString("name"));
        setMediaCount(obj.getInt("media_count"));
    }

    protected void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == this.getClass() && ((Tag) o).getName().equals(getName()));
    }
}
