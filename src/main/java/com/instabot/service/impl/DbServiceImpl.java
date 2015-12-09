package com.instabot.service.impl;

import com.instabot.models.Order;
import com.instabot.models.Payment;
import com.instabot.models.Post;
import com.instabot.service.DbService;
import com.instabot.utils.DbHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class DbServiceImpl implements DbService {

    private static DbService instance;
    private JdbcTemplate db;

    public DbServiceImpl() {
        DataSource dataSource = DbHelper.getDataSource();
        this.db = new JdbcTemplate(dataSource);
    }

    public static DbService getInstance() {
        if (instance != null) return instance;
        return new DbServiceImpl();
    }

    @Override
    public Long createPost(Post post) {

        String insert = "INSERT INTO POSTS (POST_ID, LINK, USER_ID, WHENCREATED, CAPTION_ID, CAPTION_TEXT, " +
                "CAPTION_WHENCREATED, PRODUCT_NAME, PRICE, QTY, LEAVES_QTY, STATUS, USER_NAME) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, post.getPostId());
            ps.setString(2, post.getLink());
            ps.setString(3, post.getUserId());
            ps.setTimestamp(4, new Timestamp(post.getWhenCreated().getTime()));
            ps.setString(5, post.getCaptionId());
            ps.setString(6, post.getCaptionText());
            ps.setTimestamp(7, new Timestamp(post.getCaptionWhenCreated().getTime()));
            ps.setString(8, post.getProductName());
            ps.setDouble(9, post.getPrice());
            ps.setInt(10, post.getQty());
            ps.setInt(11, post.getLeavesQty());
            ps.setInt(12, Post.Status.ACTIVE.ordinal());
            ps.setString(13, post.getUserName());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Post getPost(String postId) {
        List<Post> posts = db.query("SELECT * FROM POSTS WHERE POST_ID=?", new PostMapper(), postId);
        return posts != null && !posts.isEmpty() ? posts.get(0) : null;
    }

    @Override
    public Long createOrder(Order order) {
        String insert = "INSERT INTO ORDERS (COMMENT_ID, POST_ID, QTY, TEXT, USER_ID, USER_NAME, WHENCREATED)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, order.getCommentId());
            ps.setString(2, order.getPostId());
            ps.setInt(3, order.getQty());
            ps.setString(4, order.getText());
            ps.setString(5, order.getUserId());
            ps.setString(6, order.getUserName());
            ps.setTimestamp(7, new Timestamp(order.getWhenCreated().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Order getOrderByCommentId(String commentId) {
        List<Order> orders = db.query("SELECT * FROM ORDERS WHERE COMMENT_ID=?", new CommentMapper(), commentId);
        return orders != null && !orders.isEmpty() ? orders.get(0) : null;
    }


    @Override
    public Long createPayment(Payment payment) {
        String insert = "INSERT INTO PAYMENTS (PAYMENT_ID, CUSTOMER, MERCHANT, DISCOUNT_PRICE, PAYMENT_STATUS, STATUS, WHENCREATED)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setLong(1, payment.getPaymentId());
            ps.setString(2, payment.getCustomer());
            ps.setString(3, payment.getMerchant());
            ps.setDouble(4, payment.getDiscountPrice());
            ps.setInt(5, payment.getPaymentStatus().ordinal());
            ps.setInt(6, payment.getStatus().ordinal());
            ps.setTimestamp(7, new Timestamp(new Date().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private static class PostMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            Post post = new Post();

            post.setId(rs.getLong("ID"));
            post.setPostId(rs.getString("POST_ID"));
            post.setLink(rs.getString("LINK"));
            post.setUserId(rs.getString("USER_ID"));
            post.setUserName(rs.getString("USER_NAME"));
            post.setWhenCreated(rs.getTimestamp("WHENCREATED"));
            post.setCaptionId(rs.getString("CAPTION_ID"));
            post.setCaptionText(rs.getString("CAPTION_TEXT"));
            post.setCaptionWhenCreated(rs.getTimestamp("CAPTION_WHENCREATED"));
            post.setProductName(rs.getString("PRODUCT_NAME"));
            post.setPrice(rs.getDouble("PRICE"));
            post.setQty(rs.getInt("QTY"));
            post.setLeavesQty(rs.getInt("LEAVES_QTY"));
            Integer status = rs.getInt("status");
            post.setStatus(Post.Status.values()[status]);
            return post;
        }
    }

    private static class CommentMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("ID"));
            order.setCommentId(rs.getString("COMMENT_ID"));
            order.setPostId(rs.getString("POST_ID"));
            order.setQty(rs.getInt("QTY"));
            order.setText(rs.getString("TEXT"));
            order.setUserId(rs.getString("USER_ID"));
            order.setUserName(rs.getString("USER_NAME"));
            order.setWhenCreated(rs.getTimestamp("WHENCREATED"));
            return order;
        }
    }

    private static class PaymentMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            Payment payment = new Payment();
            payment.setId(rs.getLong("ID"));
            payment.setPaymentId(rs.getLong("payment_id"));
            payment.setCustomer(rs.getString("customer"));
            payment.setMerchant(rs.getString("merchant"));
            payment.setDiscountPrice(rs.getDouble("discount_price"));
            Integer status = rs.getInt("status");
            payment.setStatus(Payment.Status.values()[status]);
            Integer paymentStatus = rs.getInt("payment_status");
            payment.setPaymentStatus(Payment.PaymentStatus.values()[paymentStatus]);
            return payment;
        }
    }
}
