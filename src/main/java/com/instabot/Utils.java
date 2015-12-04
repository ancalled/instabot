package com.instabot;

import java.util.Date;

public class Utils {

    public static Date timestampToDate(String timestamp) {
        return new Date((Long.parseLong(timestamp) * 1000));
    }
}
