package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.dao.jdbc.mapper.UserRowMapper;
import com.study.onlineshop.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    private static final String GET_USER_SQL = "SELECT id, login, password, sole, role FROM users WHERE login = ? AND password = ?;";
    private static final String GET_SOLE_SQL = "SELECT sole FROM users WHERE login = ?;";
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private PreparedStatement getUserPreparedStatement(Connection connection, String login, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);

        return preparedStatement;
    }

    private PreparedStatement getSolePreparedStatement(Connection connection, String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_SOLE_SQL);
        preparedStatement.setString(1, login);

        return preparedStatement;
    }

    @Override
    public User getUser(String login, String password) {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = getUserPreparedStatement(connection, login, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            ) {
            while (resultSet.next()) {
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                return user;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSole(String login) {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = getSolePreparedStatement(connection, login);
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()) {
                return resultSet.getString("sole");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
