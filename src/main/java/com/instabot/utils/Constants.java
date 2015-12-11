package com.instabot.utils;

public class Constants {

    public static final String SELL_TAG = "sell_viaphone";
    public static final String BUY_TAG = "#buy_viaphone";
    public static final String AUTH_CODE_TAG = "#code_viaphone";
    public static final String CONFIRM_TAG = "#confirm_viaphone";
    public static final String NAME_TAG = "name_";
    public static final String PRICE_TAG = "price_";
    public static final String QTY_TAG = "qty_";

    public static final String MSG_ORDER_CREATED = "@%s your order created successfully! Please comment here your authorization code like #code_viaphone****";
    public static final String MSG_NOT_REGISTERED = "@%s your don't registered on viaphone! Please register on payviaphone.com";
    public static final String MSG_ORDER_AUTH = "@%s your order authorized successfully with discount amount: "
            + "%.2f Please comment here tag #confirm_viaphone tag to confirm payment!";


    public class Instagram {

        public static final String API_ROOT = "https://api.instagram.com/v1";

        public static final String GET_DATA = API_ROOT + "/users/{user_id}";
        public static final String GET_FEED = API_ROOT + "/users/self/feed";
        public static final String GET_RECENT_MEDIA = API_ROOT + "/users/{user_id}/media/recent";
        public static final String GET_LIKED_MEDIA = API_ROOT + "/users/self/media/liked";
        public static final String SEARCH_USER_BY_NAME = API_ROOT + "/users/search";

        public static final String GET_FOLLOWS = API_ROOT + "/users/{user_id}/follows";
        public static final String GET_FOLLOWERS = API_ROOT + "/users/{user_id}/followed-by";
        public static final String GET_FOLLOW_REQUESTS = API_ROOT + "/users/self/requested-by";
        public static final String GET_RELATIONSHIP_STATUS = API_ROOT + "/users/{user_id}/relationship";
        public static final String MUTATE_RELATIONSHIP = API_ROOT + "/users/{user_id}/relationship";

        public static final String GET_MEDIA = API_ROOT + "/media/{media_id}";
        public static final String SEARCH_MEDIA = API_ROOT + "/media/search";
        public static final String GET_POPULAR_MEDIA = API_ROOT + "/media/popular";

        public static final String GET_MEDIA_COMMENTS = API_ROOT + "/media/{media_id}/comments";
        public static final String POST_MEDIA_COMMENT = API_ROOT + "/media/{media_id}/comments";
        public static final String DELETE_MEDIA_COMMENT = API_ROOT + "/media/{media_id}/comments/{comment_id}";

        public static final String GET_LIKERS = API_ROOT + "/media/{media_id}/likes";
        public static final String SET_LIKE = API_ROOT + "/media/{media_id}/likes";
        public static final String REMOVE_LIKE = API_ROOT + "/media/{media_id}/likes";

        public static final String GET_TAG = API_ROOT + "/tags/{tag_name}";
        public static final String GET_RECENT_TAGED_MEDIA = API_ROOT + "/tags/{tag_name}/media/recent";
        public static final String SEARCH_TAGS = API_ROOT + "/tags/search";

        public static final String GET_LOCATION = API_ROOT + "/locations/{location_id}";
        public static final String GET_MEDIA_FROM_LOCATION = API_ROOT + "/locations/{location_id}/media/recent";
        public static final String SEARCH_LOCATIONS = API_ROOT + "/locations/search";
    }

    public static class Viaphone {

        public static String API_ROOT = "http://payviaphone.com/api";
        public static final String CREATE_PAYMENT = API_ROOT + "/create-payment";
        public static final String AUTHORIZE_PAYMENT = API_ROOT + "/authorize-payment";
        public static final String LOOKUP_PAYMENT = API_ROOT + "/lookup-payment";
        public static final String CONFIRM_PAYMENT = API_ROOT + "/confirm-payment";

        public static void setApiRoot (String apiRoot) {
            API_ROOT = apiRoot;
        }
    }
}
