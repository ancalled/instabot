package com.instabot;

import com.instabot.models.Order;
import com.instabot.models.Post;
import com.instabot.models.Payment;
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
        main.scanMediaToSell();
    }

    private void scanMediaToSell() {
        List<Post> posts = instaService.getRecentTaggedMedia(SELL_TAG);
        for (Post p : posts) {
            Post oldPost = dbService.getPost(p.getPostId());
            if (oldPost == null) {
                Long id = dbService.createPost(p);
                if (id != null) log.info("Post with id: " + id + " inserted successfully to db!");
            }

            if (p.getCommentCount() > 0 && p.getLeavesQty() > 0) {
                List<Order> orders = instaService.getOrdersByMediaId(p.getPostId());
                for (Order order : orders) {
                    if (order.getUserName() != null) {
                        Order oldOrder = dbService.getOrderByCommentId(order.getCommentId());
                        if (oldOrder == null) {
                            log.info("User " + order.getUserName() + " with id: " + order.getUserId()
                                    + " want to buy product, media id: " + p.getPostId() + " qty:" + order.getQty()
                                    + " comment id:" + order.getCommentId());

                            Long id = dbService.createOrder(order);
                            if (id != null) {
                                log.info("Order with id: " + id + " inserted successfully to db!");
                                Payment payment = viaphoneService.createPayment(p, order);
                                if (payment.getStatus().equals(Payment.Status.OK)
                                        && payment.getPaymentStatus().equals(Payment.PaymentStatus.CREATED)) {

                                    dbService.createPayment(payment);
                                    String message = "@" + order.getUserName() + " your order created successfully! " +
                                            "Please comment here your authorization code like #code_viaphone=****";
                                    instaService.postComment(p.getPostId(), message);
                                }
                            }
                        }
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
