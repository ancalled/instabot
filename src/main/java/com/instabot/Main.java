package com.instabot;

import com.instabot.models.Order;
import com.instabot.models.Post;
import com.instabot.models.User;
import com.instabot.service.MainService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.MainServiceImpl;
import org.apache.log4j.Logger;

import java.util.List;

import static com.instabot.utils.Constants.*;

public class Main {

    private static Logger log = Logger.getLogger(Main.class);

    private static InstaService instaService = new InstaService();

    private MainService mainService;

    public static void main(String[] args) {
        Main main = new Main();
        main.mainService = MainServiceImpl.getInstance();
        main.scanMediaToSell();
    }

    private void scanMediaToSell() {
        List<Post> posts = instaService.getRecentTaggedMedia(SELL_TAG);
        for (Post p : posts) {
            Post oldPost = mainService.getPost(p.getPostId());
            if (oldPost == null) {
                Long id = mainService.createPost(p);
                if (id != null) log.info("Post with id: " + id + " inserted successfully to db!");
            }

            if (p.getCommentCount() > 0) {
                List<Order> orders = instaService.getOrdersByMediaId(p.getPostId());
                for (Order order : orders) {
                    User sender = order.getUser();
                    if (sender != null) {
                        Order oldOrder = mainService.getOrderByCommentId(order.getCommentId());
                        if (oldOrder == null) {
                            log.info("User " + sender.getUserName() + " with id: " + sender.getId()
                                    + " want to buy product, media id: " + p.getPostId() + " qty:" + order.getQty()
                                    + " comment id:" + order.getCommentId());

                            Long id = mainService.createOrder(order);
                            if (id != null) log.info("Order with id: " + id + " inserted successfully to db!");
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
