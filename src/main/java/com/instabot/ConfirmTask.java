package com.instabot;

import com.instabot.models.*;
import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;

import java.util.List;

public class ConfirmTask implements Runnable {

    private static Logger log = Logger.getLogger(MediaTask.class);

    private InstaService instaService;
    private ViaphoneService viaphoneService;
    private DbService dbService;

    public ConfirmTask(InstaService instaService, ViaphoneService viaphoneService, DbService dbService) {
        this.instaService = instaService;
        this.viaphoneService = viaphoneService;
        this.dbService = dbService;
    }

    @Override
    public void run() {
        try {
            log.info("Starting scan confirm orders");
            scanConfirmComments();
            log.info("Finished scaning confirm orders");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void scanConfirmComments() {
        List<Post> activePosts = dbService.getActivePosts();
        for (Post p : activePosts) {
            List<Order> orders = instaService.getOrdersByMediaId(p.getPostId(), CommentType.CONFIRM);
            for (Order order : orders) {
                List<Order> userOrders = dbService.getOrdersByPostIdUserStatus(p.getPostId(), order.getUserName(), PaymentStatus.AUTHORIZED);
                for (Order userOrder : userOrders) {
                    Response response = viaphoneService.confirmPayment(userOrder.getPaymentId(), p.getUserId());
                    if (response.getStatus().equals(Response.Status.OK)) {
                        dbService.updateOrderStatus(userOrder.getId(), response.getPaymentStatus());
                        int leavesQty = p.getLeavesQty() - userOrder.getQty();
                        dbService.updatePost(p.getId(), leavesQty);
                    }
                }
            }
        }
    }
}