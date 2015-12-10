package com.instabot;

import com.instabot.models.*;
import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;

import java.util.List;

public class ConfirmThread implements Runnable {

    private static Logger log = Logger.getLogger(MediaThread.class);

    private InstaService instaService;
    private ViaphoneService viaphoneService;
    private DbService dbService;

    public ConfirmThread(InstaService instaService, ViaphoneService viaphoneService, DbService dbService) {
        this.instaService = instaService;
        this.viaphoneService = viaphoneService;
        this.dbService = dbService;
    }

    @Override
    public void run() {
        try {
            log.info("Starting scan authorize orders");
            scanConfirmComments();
            log.info("Finished scaning authorize orders");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void scanConfirmComments() {
        List<Post> activePosts = dbService.getActivePosts();
        for (Post p : activePosts) {
            List<Order> orders = instaService.getOrdersByMediaId(p.getPostId(), CommentType.CONFIRM);
            for (Order order : orders) {
                List<Order> userOrders = dbService.getOrdersByPostIdUserStatus(p.getPostId(), order.getUserName(), PaymentStatus.AUTHORIZED);
                for (Order userOrder : userOrders) {
                    Response response = viaphoneService.confirmPayment(userOrder.getPaymentId(), p.getUserName());
                    if (response.getStatus().equals(Response.Status.OK)) {
                        dbService.updateOrderStatus(userOrder.getId(), response.getPaymentStatus());
                    }
                }
            }
        }
    }
}