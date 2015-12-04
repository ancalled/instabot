package com.instabot.service;

import com.instabot.models.Comment;
import com.instabot.models.Post;

public interface MainService {

    Long createPost(Post post);

    Post getPost(String postId);

    Long createComment(Comment comment);

    Comment getComment(String commentId);
}
