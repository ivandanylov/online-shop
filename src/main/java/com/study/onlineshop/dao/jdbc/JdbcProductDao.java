package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.dao.jdbc.mapper.ProductRowMapper;
import com.study.onlineshop.entity.Product;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcProductDao implements ProductDao {

    private static final String GET_ALL_SQL = "SELECT id, name, creation_date, price FROM product;";
    private static final String ADD_SQL = "INSERT INTO product (name, creation_date, price) VALUES (?, ?, ?) RETURNING id;";
    private static final String EDIT_SQL = "UPDATE product SET name = ?, price = ? WHERE id = ?;";
    private static final String DELETE_SQL = "DELETE FROM product WHERE id = ?;";

    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();

    private DataSource dataSource;

    private PreparedStatement addProductPreparedStatement(Connection connection, Product product) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(product.getCreationDate()));
        preparedStatement.setDouble(3, product.getPrice());

        return preparedStatement;
    }

    public JdbcProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    @Override
    public Product addProduct(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = addProductPreparedStatement(connection, product);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                product.setId(resultSet.getInt("id"));

                return product;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editProduct(Product oldProduct, Product newProduct) {

    }

    @Override
    public void deleteProduct(Product product) {

    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
