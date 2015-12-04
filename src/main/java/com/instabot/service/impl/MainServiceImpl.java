package com.instabot.service.impl;

import com.instabot.models.Comment;
import com.instabot.models.Post;
import com.instabot.service.Connection;
import com.instabot.service.MainService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sun.misc.PostVMInitHook;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MainServiceImpl implements MainService {

    private static MainService instance;
    private JdbcTemplate db;

    public MainServiceImpl() {
        DataSource dataSource = Connection.getDataSource();
        this.db = new JdbcTemplate(dataSource);
    }

    public static MainService getInstance() {
        if (instance != null) return instance;
        return new MainServiceImpl();
    }

    @Override
    public Long createPost(Post post) {

        String insert = "INSERT INTO POSTS (POST_ID, LINK, TYPE, USER_ID, WHENCREATED, CAPTION_ID, CAPTION_TEXT, " +
                "CAPTION_WHENCREATED, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, post.getId());
            ps.setString(2, post.getLink());
            ps.setString(3, post.getType());
            ps.setString(4, post.getUser().getId());
            ps.setTimestamp(5, new Timestamp(post.getWhenCreated().getTime()));
            ps.setString(6, post.getCaptionId());
            ps.setString(7, post.getCaptionText());
            ps.setTimestamp(8, new Timestamp(post.getCaptionWhenCreated().getTime()));
            ps.setInt(9, Post.Status.ACTIVE.ordinal());
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
    public Long createComment(Comment comment) {

        String insert = "INSERT INTO COMMENTS (COMMENT_ID, POST_ID, TEXT, USER_ID, WHENCREATED) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, comment.getId());
            ps.setString(2, comment.getPostId());
            ps.setString(3, comment.getText());
            ps.setString(4, comment.getSender().getId());
            ps.setTimestamp(5, new Timestamp(comment.getWhenCreated().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Comment getComment(String commentId) {
        List<Comment> comments = db.query("SELECT * FROM COMMENTS WHERE COMMENT_ID=?", new CommentMapper(), commentId);
        return comments != null && !comments.isEmpty() ? comments.get(0) : null;
    }

    private static class PostMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            Post post = new Post();
            post.setId(resultSet.getString("POST_ID"));
            post.setLink(resultSet.getString("LINK"));
            post.setType(resultSet.getString("TYPE"));
            // TODO: 12/4/2015
//            post.setUser(resultSet.getString("USER_ID"));
            post.setWhenCreated(resultSet.getTimestamp("WHENCREATED"));
            post.setCaptionId(resultSet.getString("CAPTION_ID"));
            post.setCaptionText(resultSet.getString("CAPTION_TEXT"));
            post.setCaptionWhenCreated(resultSet.getTimestamp("CAPTION_WHENCREATED"));
            Integer status = resultSet.getInt("status");
            post.setStatus(Post.Status.values()[status]);
            return post;
        }
    }

    private static class CommentMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            Comment comment = new Comment();
            comment.setId(resultSet.getString("COMMENT_ID"));
            comment.setPostId(resultSet.getString("POST_ID"));
            comment.setText(resultSet.getString("TEXT"));
            // TODO: 12/4/2015
//            comment.setUser(resultSet.getString("USER_ID"));
            comment.setWhenCreated(resultSet.getTimestamp("WHENCREATED"));
            return comment;
        }
    }
}
