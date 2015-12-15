package com.instabot.models;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Tag {

    private static Logger log = Logger.getLogger(Tag.class);
    int mediaCount;
    String name;

    public Tag(JSONObject obj) {
        try {
            setName(obj.getString("name"));
            setMediaCount(obj.getInt("media_count"));
        } catch (Exception e) {
            log.error("Cannot parse JSONObject: " + obj);
            log.error(e.getMessage());
        }
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
