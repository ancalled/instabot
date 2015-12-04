package com.instabot;

import com.instabot.models.Comment;
import com.instabot.models.Media;
import com.instabot.models.Tag;
import com.instabot.models.User;
import org.apache.log4j.Logger;

import java.util.List;

public class Main {

    private static Logger log = Logger.getLogger(Main.class);
    private static final String SELL_TAG = "sell_viaphone";
    private static final String BUY_TAG = "#buy_viaphone";
    private static MainService service = new MainService();

    public static void main(String[] args) {

        scanMediaToSell();
//        Tag tag = service.getTag(SELL_TAG);
//        List<Tag> tags = service.searchTags(SELL_TAG);


//        User user = service.searchUsersByName("testviaphone").get(0);

//        if (user != null) {
//            User userById = service.getUserById(user.getId());
//            System.out.println("userById: " + userById.getId() + " name: " + userById.getFullName());


//            List<User> userFollows = service.getFollows(user.getId());
//            int count = 1;
//            for (User u : userFollows) {
//                System.out.println("userFollow " + count + ": " + u.getFullName());
//            }
//
//            List<User> userFollowers = service.getFollowers(user.getId());
//            count = 1;
//            for (User u : userFollowers) {
//                System.out.println("userFollow " + count + ": " + u.getFullName());
//            }

//            List<Media> medias = service.getRecentUserMedias(user.getId());
//            for (Media m : medias) {
//                List<Comment> comments = service.getMediaComments(m.getId());
//                for (Comment comment : comments) {
//                    System.out.println(comment.getText() + " sender: " + comment.getSender().getFullName());
//                }
////                service.postComment(m.getId(), "filter name is " + m.getFilter());
//            }
//        }
    }

    private static void scanMediaToSell() {
        List<Media> medias = service.getRecentTaggedMedia(SELL_TAG);
        for (Media m : medias) {
            if (m.getCommentCount() > 0) {
                List<Comment> comments = service.getMediaComments(m.getId());
                for (Comment comment : comments) {
                    if (comment.getText().contains(BUY_TAG)) {
                        User sender = comment.getSender();
                        if (sender != null) {
                            String qtyStr = comment.getText().replace(BUY_TAG, "");
                            int qty = !qtyStr.equals("") ? Integer.parseInt(qtyStr) : 1;

                            log.info("User " + sender.getUserName() + " with id: " + sender.getId()
                                    + " want to buy product, media id: " + m.getId() + " qty:" + qty
                                    + " comment id:" + comment.getId());
                        }
                    }
                    log.info(comment.getText() + " sender: " + comment.getSender().getUserName());
                }
            }
        }
    }
}
