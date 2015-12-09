package com.instabot.service.impl;

import com.instabot.models.Order;
import com.instabot.models.Post;
import com.instabot.models.Response;
import com.instabot.utils.Constants;
import org.json.JSONObject;

import java.util.HashMap;

import static com.instabot.utils.RequestHelper.*;

public class ViaphoneService {

    public Response createPayment(Post post, Order order) {

        double amount = post.getPrice() * order.getQty();
        String details = "product=" + post.getProductName() + "_price=" + post.getPrice() + "_qty=" + order.getQty();

        HashMap<String, Object> params = new HashMap<>();
        params.put("customer", order.getUserName());
        params.put("merchant", post.getUserName());
        params.put("amount", amount);
        params.put("details", details);
        params.put("instagram", true);
        JSONObject object = makeRequestJson(Constants.Viaphone.CREATE_PAYMENT, params);
        System.out.println(object);
        return new Response(object);
    }

    public Response authorizePayment(String code, long paymentId, String merchLogin) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("payment", paymentId);
        params.put("merchant", merchLogin);
        params.put("instagram", true);
        JSONObject object = makeRequestJson(Constants.Viaphone.AUTHORIZE_PAYMENT, params);
        System.out.println(object);
        return new Response(object);
    }

    public Response lookupPayment(long paymentId, String merchLogin) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("payment", paymentId);
        params.put("merchant", merchLogin);
        params.put("calcDiscount", true);
        params.put("instagram", true);
        JSONObject object = makeRequestJson(Constants.Viaphone.LOOKUP_PAYMENT, params);
        System.out.println(object);
        return new Response(object);
    }

    public Response confirmPayment(long paymentId, String merchLogin) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("payment", paymentId);
        params.put("merchant", merchLogin);
        params.put("instagram", true);
        JSONObject object = makeRequestJson(Constants.Viaphone.CONFIRM_PAYMENT, params);
        System.out.println(object);
        return new Response(object);
    }
}
