package com.instabot;

import com.instabot.models.Comment;
import com.instabot.models.Media;
import com.instabot.models.User;

import java.io.*;
import java.net.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        MainService service = new MainService();
        User user = service.searchUsersByName("payviaphone").get(0);

        if (user != null) {
//            User userById = service.getUserById(user.getId());
//            System.out.println("userById: " + userById.getId() + " name: " + userById.getFullName());
            List<Media> medias = service.getRecentUserMedias(user.getId());
            for (Media m: medias) {
                List<Comment> comments = service.getMediaComments(m.getId());
                for (Comment comment: comments) {
                    System.out.println(comment.getText() + " sender: " + comment.getSender().getFullName());
                }
                service.postComment(m.getId(), "filter name is " + m.getFilter());
            }
            System.out.println(medias);
        }
    }
}
