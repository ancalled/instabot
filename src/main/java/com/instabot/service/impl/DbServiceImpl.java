package com.instabot.service.impl;

import com.instabot.models.Order;
import com.instabot.models.PaymentStatus;
import com.instabot.models.Post;
import com.instabot.service.DbService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

public class DbServiceImpl implements DbService {

    private static final String driverClassName = "oracle.jdbc.OracleDriver";

    private JdbcTemplate db;

    public DbServiceImpl(Properties prop) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(prop.getProperty("db.url"));
        dataSource.setUsername(prop.getProperty("db.username"));
        dataSource.setPassword(prop.getProperty("db.password"));

        this.db = new JdbcTemplate(dataSource);
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
    public List<Post> getActivePosts() {
        return db.query("SELECT * FROM POSTS WHERE STATUS=?", new PostMapper(), Post.Status.ACTIVE.ordinal());
    }

    @Override
    public Post getPost(String postId) {
        List<Post> posts = db.query("SELECT * FROM POSTS WHERE POST_ID=?", new PostMapper(), postId);
        return posts != null && !posts.isEmpty() ? posts.get(0) : null;
    }

    @Override
    public void updatePost(long id, int leavesQty) {
        Post.Status status = leavesQty > 0 ? Post.Status.ACTIVE : Post.Status.ARCHIVE;
        db.update("UPDATE POSTS SET LEAVES_QTY=?, STATUS=? WHERE ID=?", leavesQty, status.ordinal(), id);
    }

    @Override
    public Long createOrder(Order order) {
        String insert = "INSERT INTO ORDERS (COMMENT_ID, POST_ID, QTY, TEXT, USER_ID, USER_NAME, PAYMENT_ID, " +
                "DISCOUNT_PRICE, STATUS, WHENCREATED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, order.getCommentId());
            ps.setString(2, order.getPostId());
            ps.setInt(3, order.getQty());
            ps.setString(4, order.getText());
            ps.setString(5, order.getUserId());
            ps.setString(6, order.getUserName());
            ps.setLong(7, order.getPaymentId());
            ps.setDouble(8, order.getDiscountPrice());
            ps.setInt(9, order.getStatus().ordinal());
            ps.setTimestamp(10, new Timestamp(order.getWhenCreated().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Order getOrderByCommentId(String commentId) {
        List<Order> orders = db.query("SELECT * FROM ORDERS WHERE COMMENT_ID=?", new OrderMapper(), commentId);
        return orders != null && !orders.isEmpty() ? orders.get(0) : null;
    }

    @Override
    public List<Order> getOrdersByPostIdUserStatus(String postId, String userName, PaymentStatus status) {
        return db.query("SELECT * FROM ORDERS WHERE POST_ID=? and USER_NAME=? and ORDERS.STATUS=?",
                new OrderMapper(), postId, userName, status.ordinal());
    }

    @Override
    public void updateOrderStatus(long id, PaymentStatus status) {
        db.update("UPDATE ORDERS SET STATUS=? WHERE ID=?", status.ordinal(), id);
    }

    @Override
    public void updateOrder(long id, double discountPrice) {
        db.update("UPDATE ORDERS SET DISCOUNT_PRICE=? WHERE ID=?", discountPrice, id);
    }

    private static class PostMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
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

    private static class OrderMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int i) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("ID"));
            order.setCommentId(rs.getString("COMMENT_ID"));
            order.setPostId(rs.getString("POST_ID"));
            order.setQty(rs.getInt("QTY"));
            order.setText(rs.getString("TEXT"));
            order.setUserId(rs.getString("USER_ID"));
            order.setUserName(rs.getString("USER_NAME"));
            order.setPaymentId(rs.getLong("PAYMENT_ID"));
            order.setDiscountPrice(rs.getDouble("DISCOUNT_PRICE"));
            Integer status = rs.getInt("STATUS");
            order.setStatus(PaymentStatus.values()[status]);
            order.setWhenCreated(rs.getTimestamp("WHENCREATED"));
            return order;
        }
    }
}
