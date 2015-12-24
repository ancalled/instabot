package com.instabot.service.impl;

import com.instabot.models.Order;
import com.instabot.models.Post;
import com.instabot.models.Response;
import com.instabot.utils.Constants;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;

import static com.instabot.utils.RequestHelper.*;

public class ViaphoneService {

    private static Logger log = Logger.getLogger(ViaphoneService.class);

    public Response createPayment(Post post, Order order) {
        double amount = post.getPrice() * order.getQty();
        String details = "product=" + post.getProductName() + "_price=" + post.getPrice() + "_qty=" + order.getQty();

        HashMap<String, Object> params = new HashMap<>();
        params.put("customer", order.getUserId());
        params.put("merchant", post.getUserName());
        params.put("amount", amount);
        params.put("details", details);
        params.put("instagram", true);

        log.info("Sending CREATE_PAYMENT request: \n\tcustomer:" + order.getUserId()
                + "\n\tmerchant:" + post.getUserName() + "\n\tamount:" + amount + "\n\tdetails:" + details);
        JSONObject object = makeRequestJson(Constants.Viaphone.CREATE_PAYMENT, params);
        log.info("Result: " + object);
        return new Response(object);
    }

    public Response authorizePayment(String code, long paymentId, String merchLogin) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("payment", paymentId);
        params.put("merchant", merchLogin);
        params.put("instagram", true);
        log.info("Sending AUTHORIZE_PAYMENT request: \n\tcode:" + code + "\n\tpayment:" + paymentId + "\n\tmerchant:" + merchLogin);
        JSONObject object = makeRequestJson(Constants.Viaphone.AUTHORIZE_PAYMENT, params);
        log.info("Result: " + object);
        return new Response(object);
    }

    public Response lookupPayment(long paymentId, String merchLogin) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("payment", paymentId);
        params.put("merchant", merchLogin);
        params.put("calcDiscount", true);
        params.put("instagram", true);
        log.info("Sending LOOKUP_PAYMENT request: \n\tpayment:" + paymentId + "\n\tmerchant:" + merchLogin);
        JSONObject object = makeRequestJson(Constants.Viaphone.LOOKUP_PAYMENT, params);
        log.info("Result: " + object);
        return new Response(object);
    }

    public Response confirmPayment(long paymentId, String merchLogin) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("payment", paymentId);
        params.put("merchant", merchLogin);
        params.put("instagram", true);
        log.info("Sending CONFIRM_PAYMENT request: \n\tpayment:" + paymentId + "\n\tmerchant:" + merchLogin);
        JSONObject object = makeRequestJson(Constants.Viaphone.CONFIRM_PAYMENT, params);
        log.info("Result: " + object);
        return new Response(object);
    }
}
