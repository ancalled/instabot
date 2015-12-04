package com.instabot.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class Connection {

    private static final String driverClassName = "oracle.jdbc.OracleDriver";
    private static final String url = "jdbc:oracle:thin:@//localhost:1521/xe";
    private static final String username = "instabot";
    private static final String password = "root";

    public static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    public static void main(String[] args) throws Exception {
        DataSource dataSource = getDataSource();
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource);
    }
}
