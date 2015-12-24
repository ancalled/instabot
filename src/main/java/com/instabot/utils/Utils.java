package com.instabot.utils;

import java.util.Date;

public class Utils {

    private static long ref = 1;

    public static synchronized long nextRef() {
        return ref++;
    }

    public static Date timestampToDate(String timestamp) {
        return new Date((Long.parseLong(timestamp) * 1000));
    }
}
