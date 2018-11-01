package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.dao.jdbc.mapper.ProductRowMapper;
import com.study.onlineshop.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao {

    private static final String GET_ALL_SQL = "SELECT id, name, creation_date, price FROM product;";
    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();

    @Override
    public List<Product> getAll() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_SQL)) {


            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = PRODUCT_ROW_MAPPER.mapRow(resultSet);
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://ec2-46-137-75-170.eu-west-1.compute.amazonaws.com/dfrg5dsc9v79ue";
        String name = "ufimjmvmkfcrku";
        String password = "669a090426397d9c30cc4c8c8b33c37a0dc980415e6c14ad2da799352380d680";

        return DriverManager.getConnection(url, name, password);
    }
}
