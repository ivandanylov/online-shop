package com.study.onlineshop.dao.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceFactory {
    public static DataSource getPostgresDataSource() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";

        Properties appProps = new Properties();

        try (InputStream inputStream = new FileInputStream(appConfigPath)) {
            appProps.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(appProps.getProperty("jdbc.url"));
        ds.setUsername(appProps.getProperty("jdbc.user"));
        ds.setPassword(appProps.getProperty("jdbc.password"));
        ds.setMaxIdle(Integer.valueOf(appProps.getProperty("jdbc.MinIdle")));
        ds.setMaxIdle(Integer.valueOf(appProps.getProperty("jdbc.MaxIdle")));
        ds.setMaxOpenPreparedStatements(Integer.valueOf(appProps.getProperty("jdbc.MaxOpenPreparedStatements")));

        return ds;
    }
}
