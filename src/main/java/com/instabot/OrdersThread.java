package com.instabot;

import com.instabot.models.*;
import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;

import java.util.List;

import static com.instabot.utils.Constants.*;

public class OrdersThread implements Runnable {

    private static Logger log = Logger.getLogger(OrdersThread.class);

    private InstaService instaService;
    private ViaphoneService viaphoneService;
    private DbService dbService;

    public OrdersThread(InstaService instaService, ViaphoneService viaphoneService, DbService dbService) {
        this.instaService = instaService;
        this.viaphoneService = viaphoneService;
        this.dbService = dbService;
    }

    @Override
    public void run() {
        try {
            log.info("Starting scan orders");
            scanOrders();
            log.info("Finished scaning orders");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void scanOrders() {
        List<Post> activePosts = dbService.getActivePosts();
        activePosts.stream().filter(p -> p.getLeavesQty() > 0).forEach(p -> {

            List<Order> orders = instaService.getOrdersByMediaId(p.getPostId(), CommentType.ORDER);
            orders.stream().filter(order -> order.getUserName() != null).forEach(order -> {

                Order oldOrder = dbService.getOrderByCommentId(order.getCommentId());
                if (oldOrder == null) {
                    log.info("User " + order.getUserName() + " with id: " + order.getUserId()
                            + " want to buy product, media id: " + p.getPostId() + " qty:" + order.getQty()
                            + " comment id:" + order.getCommentId());

                    Response response = viaphoneService.createPayment(p, order);
                    if (response.getStatus().equals(Response.Status.OK)) {
                        order.setPaymentId(response.getPaymentId());
                        order.setDiscountPrice(response.getDiscountPrice());
                        order.setStatus(response.getPaymentStatus());
                        Long id = dbService.createOrder(order);
                        if (id != null) {
                            log.info("Order with id: " + id + " inserted successfully to db!");
                            instaService.postComment(p.getPostId(), String.format(MSG_ORDER_CREATED, order.getUserName()));
                        }
                    } else if (response.getStatus().equals(Response.Status.CUSTOMER_NOT_FOUND)) {
                        instaService.postComment(p.getPostId(), String.format(MSG_NOT_REGISTERED, order.getUserName()));
                    }
                }
            });
        });
    }
}