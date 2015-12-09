package com.instabot;

import com.instabot.models.*;
import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.DbServiceImpl;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;

import java.util.List;

import static com.instabot.utils.Constants.*;

public class Main {

    private static Logger log = Logger.getLogger(Main.class);

    private static InstaService instaService = new InstaService();
    private static ViaphoneService viaphoneService = new ViaphoneService();

    private DbService dbService;

    public static void main(String[] args) {
        Main main = new Main();
        main.dbService = DbServiceImpl.getInstance();
//        main.scanMediaToSell();
//        main.scanOrdersComments();
//        main.scanAuthorizeComments();
        main.scanConfirmComments();
    }

    private void scanMediaToSell() {
        List<Post> posts = instaService.getRecentTaggedMedia(SELL_TAG);
        for (Post p : posts) {
            Post oldPost = dbService.getPost(p.getPostId());
            if (oldPost == null) {
                Long id = dbService.createPost(p);
                if (id != null) log.info("Post with id: " + id + " inserted successfully to db!");
            }
        }
    }

    private void scanOrdersComments() {
        List<Post> activePosts = dbService.getActivePosts();
        for (Post p : activePosts) {
            if (p.getLeavesQty() > 0) {
                List<Order> orders = instaService.getOrdersByMediaId(p.getPostId(), CommentType.ORDER);
                for (Order order : orders) {
                    if (order.getUserName() != null) {
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
                                    String message = "@" + order.getUserName() + " your order created successfully! " +
                                            "Please comment here your authorization code like #code_viaphone****";
                                    instaService.postComment(p.getPostId(), message);
                                }
                            } else if (response.getStatus().equals(Response.Status.CUSTOMER_NOT_FOUND)) {
                                String message = "@" + order.getUserName() + " your don't registered on viaphone! " +
                                        "Please register payviaphone.com";
                                instaService.postComment(p.getPostId(), message);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanAuthorizeComments() {
        List<Post> activePosts = dbService.getActivePosts();
        for (Post p : activePosts) {
            List<Order> orders = instaService.getOrdersByMediaId(p.getPostId(), CommentType.AUTHORIZATION);
            for (Order order : orders) {
                List<Order> userOrders = dbService.getOrdersByPostIdUserStatus(p.getPostId(), order.getUserName(), PaymentStatus.CREATED);
                for (Order userOrder : userOrders) {
                    Response response = viaphoneService.authorizePayment(order.getAuthCode(),
                            userOrder.getPaymentId(), p.getUserName());
                    if (response.getStatus().equals(Response.Status.OK)) {
                        dbService.updateOrderStatus(userOrder.getId(), response.getPaymentStatus());
                        Response lookupRes = viaphoneService.lookupPayment(userOrder.getPaymentId(), p.getUserName());
                        if (lookupRes.getStatus().equals(Response.Status.OK)) {
                            dbService.updateOrder(userOrder.getId(), lookupRes.getDiscountPrice());
                            String message = "@" + userOrder.getUserName() + " your order authorized successfully " +
                                    "with discount amount: " + lookupRes.getDiscountPrice() +
                                    " Please comment here tag #confirm_viaphone tag to confirm payment!";
                            instaService.postComment(p.getPostId(), message);
                        }
                    }
                }
            }
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

//        Tag tag = instaService.getTag(SELL_TAG);
//        List<Tag> tags = instaService.searchTags(SELL_TAG);


//        User user = instaService.searchUsersByName("testviaphone").get(0);

//        if (user != null) {
//            User userById = instaService.getUserById(user.getId());
//            System.out.println("userById: " + userById.getId() + " name: " + userById.getFullName());


//            List<User> userFollows = instaService.getFollows(user.getId());
//            int count = 1;
//            for (User u : userFollows) {
//                System.out.println("userFollow " + count + ": " + u.getFullName());
//            }
//
//            List<User> userFollowers = instaService.getFollowers(user.getId());
//            count = 1;
//            for (User u : userFollowers) {
//                System.out.println("userFollow " + count + ": " + u.getFullName());
//            }

//            List<Media> medias = instaService.getRecentUserMedias(user.getId());
//            for (Media m : medias) {
//                List<Comment> comments = instaService.getMediaComments(m.getId());
//                for (Comment comment : comments) {
//                    System.out.println(comment.getText() + " sender: " + comment.getSender().getFullName());
//                }
////                instaService.postComment(m.getId(), "filter name is " + m.getFilter());
//            }
//        }
}
