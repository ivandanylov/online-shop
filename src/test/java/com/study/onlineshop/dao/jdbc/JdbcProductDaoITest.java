package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.entity.Product;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JdbcProductDaoITest {
    @Test
    public void testGetAll() throws Exception {
        DataSource dataSourceFactory = DataSourceFactory.getPostgresDataSource();
        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSourceFactory);
        List<Product> products = jdbcProductDao.getAll();

        for (Product product : products) {
            assertNotNull(product.getName());
            assertNotNull(product.getCreationDate());
        }
    }

}