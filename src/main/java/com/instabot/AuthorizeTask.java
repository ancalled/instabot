package com.instabot;

import com.instabot.models.*;
import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;

import java.util.List;

import static com.instabot.utils.Constants.*;

public class AuthorizeTask implements Runnable {

    private static Logger log = Logger.getLogger(MediaTask.class);

    private InstaService instaService;
    private ViaphoneService viaphoneService;
    private DbService dbService;

    public AuthorizeTask(InstaService instaService, ViaphoneService viaphoneService, DbService dbService) {
        this.instaService = instaService;
        this.viaphoneService = viaphoneService;
        this.dbService = dbService;
    }

    @Override
    public void run() {
        try {
            log.info("Starting scan authorize orders");
            scanAuthorizeComments();
            log.info("Finished scaning authorize orders");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void scanAuthorizeComments() {
        List<Post> activePosts = dbService.getActivePosts();
        for (Post p : activePosts) {
            List<Order> orders = instaService.getOrdersByMediaId(p.getPostId(), CommentType.AUTHORIZATION);
            log.info("Got" + orders.size() + " orders to authorize for post " + p.getPostId());
            for (Order order : orders) {
                List<Order> userOrders = dbService.getOrdersByPostIdUserStatus(p.getPostId(), order.getUserName(), PaymentStatus.CREATED);
                for (Order userOrder : userOrders) {
                    Response response = viaphoneService.authorizePayment(order.getAuthCode(),
                            userOrder.getPaymentId(), p.getUserName());
                    if (response.getStatus().equals(Response.Status.OK)) {
                        dbService.updateOrderStatus(userOrder.getId(), response.getPaymentStatus());
                        Response lookupRes = viaphoneService.lookupPayment(userOrder.getPaymentId(), p.getUserName());
                        if (lookupRes.getStatus().equals(Response.Status.OK)) {
                            double discount = lookupRes.getDiscountPrice();
                            dbService.updateOrder(userOrder.getId(), discount);
                            instaService.postComment(p.getPostId(), String.format(MSG_ORDER_AUTH, userOrder.getUserName(), discount));
                        }
                    }
                }
            }
        }
    }
}