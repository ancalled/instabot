package com.instabot.service.impl;

import com.instabot.models.Order;
import com.instabot.models.Post;
import com.instabot.models.Response;
import com.instabot.utils.Constants;
import com.instabot.utils.Utils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.instabot.utils.RequestHelper.*;

public class ViaphoneService {

    private static Logger log = Logger.getLogger(ViaphoneService.class);

    private String clientId;
    private String clientSecret;
    private String accessToken;

    public ViaphoneService(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        getAccessToken();
    }

    public Response createPayment(Post post, Order order) {
        double amount = post.getPrice() * order.getQty();
        long ref = Utils.nextRef();

        JSONObject product = new JSONObject();
        product.put("name", post.getProductName());
        product.put("price", post.getPrice());
        product.put("qty", order.getQty());
        JSONArray details = new JSONArray();
        details.put(product);

        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("customer", order.getUserId());
        req.put("merchant", post.getUserId());
        req.put("amount", amount);
        req.put("details", details);

        log.info("Sending CREATE_PAYMENT request: \n\t" + req.toString());
        return sendRequest(Constants.Viaphone.CREATE_PAYMENT, req);
    }

    public Response authorizePayment(String code, long paymentId) {
        long ref = Utils.nextRef();

        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("code", code);
        req.put("paymentId", paymentId);

        log.info("Sending AUTHORIZE_PAYMENT request: \n\t" + req.toString());
        return sendRequest(Constants.Viaphone.AUTHORIZE_PAYMENT, req);
    }

    public Response lookupPayment(long paymentId, String merchant) {
        long ref = Utils.nextRef();
        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("paymentId", paymentId);
        req.put("merchant", merchant);
        req.put("calcDiscount", true);

        log.info("Sending LOOKUP_PAYMENT request: \n\t" + req.toString());
        return sendRequest(Constants.Viaphone.LOOKUP_PAYMENT, req);
    }

    public Response confirmPayment(long paymentId, String merchant) {
        long ref = Utils.nextRef();
        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("paymentId", paymentId);
        req.put("merchant", merchant);
        log.info("Sending CONFIRM_PAYMENT request: \n\t" + req.toString());
        return sendRequest(Constants.Viaphone.CONFIRM_PAYMENT, req);
    }

    private Response sendRequest(String url, JSONObject content) {
        JSONObject obj = makeRequestJson(url, accessToken, content.toString());
        if (!obj.isNull("error") && obj.getString("error").equals("invalid_token")) {
            getAccessToken();
            obj = makeRequestJson(url, accessToken, content.toString());
        }
        log.info("Result: " + obj);
        return new Response(obj);
    }

    private void getAccessToken() {
        String url = Constants.Viaphone.ACCESS_TOKEN + "?grant_type=password&client_id=" + clientId
                + "&client_secret=" + clientSecret;
        JSONObject obj = makeRequestJson(url);
        if (!obj.isNull("access_token")) {
            accessToken = obj.getString("access_token");
        }
    }
}
