package com.instabot.service;

import com.instabot.models.Order;
import com.instabot.models.Payment;
import com.instabot.models.Post;

public interface DbService {

    Long createPost(Post post);

    Post getPost(String postId);

    Long createOrder(Order order);

    Order getOrderByCommentId(String commentId);

    Long createPayment(Payment payment);
}
