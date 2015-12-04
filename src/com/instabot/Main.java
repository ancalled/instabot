package com.instabot;

import com.instabot.models.Comment;
import com.instabot.models.Media;
import com.instabot.models.Tag;
import com.instabot.models.User;

import java.util.List;

public class Main {

    private static final String SELL_TAG = "kase";
    private static final String BUY_TAG = "buy_viaphone";

    public static void main(String[] args) {

        MainService service = new MainService();
        User user = service.searchUsersByName("ikashkynbek").get(0);

        if (user != null) {
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

//            Tag tag = service.getTag(SELL_TAG);
//            List<Tag> tags = service.searchTags(SELL_TAG);
            List<Media> medias = service.getRecentTaggedMedia(SELL_TAG);
            for (Media m : medias) {
                System.out.println(m);
            }

//            List<Media> medias = service.getRecentUserMedias(user.getId());
//            for (Media m : medias) {
//                List<Comment> comments = service.getMediaComments(m.getId());
//                for (Comment comment : comments) {
//                    System.out.println(comment.getText() + " sender: " + comment.getSender().getFullName());
//                }
////                service.postComment(m.getId(), "filter name is " + m.getFilter());
//            }
        }
    }
}
