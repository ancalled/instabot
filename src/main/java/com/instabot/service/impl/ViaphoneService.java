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
        JSONObject object = makeRequestJson(Constants.Viaphone.CREATE_PAYMENT, req.toString());
        log.info("Result: " + object);
        return new Response(object, ref);
    }

    public Response authorizePayment(String code, long paymentId) {
        long ref = Utils.nextRef();

        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("code", code);
        req.put("paymentId", paymentId);

        log.info("Sending AUTHORIZE_PAYMENT request: \n\t" + req.toString());
        JSONObject object = makeRequestJson(Constants.Viaphone.AUTHORIZE_PAYMENT, req.toString());
        log.info("Result: " + object);
        return new Response(object, ref);
    }

    public Response lookupPayment(long paymentId, String merchant) {
        long ref = Utils.nextRef();
        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("paymentId", paymentId);
        req.put("merchant", merchant);
        req.put("calcDiscount", true);

        log.info("Sending LOOKUP_PAYMENT request: \n\t" + req.toString());
        JSONObject object = makeRequestJson(Constants.Viaphone.LOOKUP_PAYMENT, req.toString());
        log.info("Result: " + object);
        return new Response(object, ref);
    }

    public Response confirmPayment(long paymentId, String merchant) {
        long ref = Utils.nextRef();
        JSONObject req = new JSONObject();
        req.put("ref", ref);
        req.put("paymentId", paymentId);
        req.put("merchant", merchant);

        log.info("Sending CONFIRM_PAYMENT request: \n\t" + req.toString());
        JSONObject object = makeRequestJson(Constants.Viaphone.CONFIRM_PAYMENT, req.toString());
        log.info("Result: " + object);
        return new Response(object, ref);
    }
}
