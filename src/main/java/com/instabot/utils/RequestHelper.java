package com.instabot.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHelper {

    private static Logger log = Logger.getLogger(RequestHelper.class);

    public static String getUrl(String uri, HashMap<String, Object> map) {
        try {
            if (map != null) {
                for (Map.Entry<String, Object> arg : map.entrySet()) {
                    String key = URLEncoder.encode(arg.getKey(), "UTF-8");
                    String value = URLEncoder.encode(arg.getValue().toString(), "UTF-8");
                    uri = uri.replaceAll("\\{" + key + "\\}", value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        uri += "?access_token=" + AccessTokenHelper.getAccessToken();
        return uri;
    }

    public static String makeRequest(String url) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            int resCode = response.getStatusLine().getStatusCode();

            StringBuilder result = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            if (resCode == 200) {
                return result.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    public static String makePostRequest(String url, Map<String, Object> params) {
        StringBuilder result = new StringBuilder();

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
            for (Map.Entry<String, Object> arg : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(arg.getKey(), arg.getValue().toString()));
            }

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return result.toString();
    }

    public static JSONObject makeRequestJson(String url) {
        return toJson(makeRequest(url));
    }

    public static JSONObject makeRequestJson(String url, Map<String, Object> params) {
        return toJson(makePostRequest(url, params));
    }

    public static JSONObject makeRequestJson(String url, String accessToken, String content) {
        String result = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + accessToken);
            HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return toJson(result);
    }

    private static JSONObject toJson(String jsonString) {
        JSONObject res = null;
        try {
            if (jsonString != null) {
                res = new JSONObject(new JSONTokener(jsonString));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.error(jsonString);
            log.error(e.getMessage());
        }
        return res;
    }
}
