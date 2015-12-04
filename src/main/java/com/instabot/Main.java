package com.instabot;

import com.instabot.models.Comment;
import com.instabot.models.Post;
import com.instabot.models.User;
import com.instabot.service.MainService;
import com.instabot.service.impl.MainServiceImpl;
import org.apache.log4j.Logger;

import java.util.List;

public class Main {

    private static Logger log = Logger.getLogger(Main.class);
    private static final String SELL_TAG = "sell_viaphone";
    private static final String BUY_TAG = "#buy_viaphone";
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
            Post oldPost = mainService.getPost(p.getId());
            if (oldPost == null) {
                Long id = mainService.createPost(p);
                if (id != null) log.info("Post with id: " + id + " inserted successfully to db!");
            }

            if (p.getCommentCount() > 0) {
                List<Comment> comments = instaService.getMediaComments(p.getId());
                for (Comment comment : comments) {
                    if (comment.getText().contains(BUY_TAG)) {
                        User sender = comment.getSender();
                        if (sender != null) {

                            Comment oldComment = mainService.getComment(comment.getId());
                            if (oldComment == null) {
                                Long id = mainService.createComment(comment);
                                if (id != null) log.info("Comment with id: " + id + " inserted successfully to db!");
                            }

                            String qtyStr = comment.getText().replace(BUY_TAG, "");
                            int qty = !qtyStr.equals("") ? Integer.parseInt(qtyStr) : 1;

                            log.info("User " + sender.getUserName() + " with id: " + sender.getId()
                                    + " want to buy product, media id: " + p.getId() + " qty:" + qty
                                    + " comment id:" + comment.getId());
                        }
                    }
                    log.info(comment.getText() + " sender: " + comment.getSender().getUserName());
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
