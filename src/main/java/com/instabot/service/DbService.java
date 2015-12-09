package com.instabot.service;

import com.instabot.models.Order;
import com.instabot.models.PaymentStatus;
import com.instabot.models.Response;
import com.instabot.models.Post;

import java.util.List;

public interface DbService {

    Long createPost(Post post);

    Post getPost(String postId);

    List<Post> getActivePosts();

    Long createOrder(Order order);

    Order getOrderByCommentId(String commentId);

    List<Order> getOrdersByPostIdUserStatus(String postId, String userName, PaymentStatus status);

    void updateOrderStatus(long id, PaymentStatus status);

    void updateOrder(long id, double discountPrice);
}
