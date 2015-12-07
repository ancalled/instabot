package com.instabot.utils;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class AccessTokenHelper {

    private static String clientId = "952e8af86e46429ea78e8437262f21ea";
    private static String clientSecret = "51506bbbfe7742abb13fcc52428f8a8f";
    private static String redirectUri = "https://localhost";
    private static String code = "07e9fcad711a46d59e47a423e5e624e9";
    private static String accessToken = "2294796387.952e8af.21c101af07924391a3ecfe8eba7e1294";
    private static String scope = "basic+public_content+follower_list+likes+comments+relationships";

    public static void main(String[] args) throws Exception {
//        getCode();
        receiveAccessToken();
    }


    public static void getCode() {
        String url = "https://instagram.com/oauth/authorize/?client_id=" + clientId
                + "&redirect_uri=" + redirectUri + "&response_type=code&scope=" + scope;

        try {
            URLConnection con = new URL(url).openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            URL redirectUrl = con.getURL();
            if (redirectUrl != null) {
                //todo open browser and get code
                System.out.println(redirectUrl);
//                redirect(redirectUrl.toString());
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void redirect(String url) {
        try {

            HttpURLConnection conn = (HttpURLConnection) (new URL(url).openConnection());
            conn.setInstanceFollowRedirects(false);
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
            conn.connect();

            boolean redirect = false;

            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            if (redirect) {

                String newUrl = conn.getHeaderField("Location");
                String cookies = conn.getHeaderField("Set-Cookie");
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");
                System.out.println("Redirect to URL : " + newUrl);

            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder html = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine).append("\n");
            }
            in.close();
            System.out.println("URL Content... \n" + html.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void receiveAccessToken() {

        String urlParameters = "client_id=" + clientId + "&client_secret=" + clientSecret
                + "&grant_type=authorization_code" + "&redirect_uri=" + redirectUri + "&code=" + code;
        try {

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            URL url = new URL("https://api.instagram.com/oauth/access_token/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            for (int c = in.read(); c != -1; c = in.read()) {
                System.out.print((char) c);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAccessToken() {
        return accessToken;
    }
}
