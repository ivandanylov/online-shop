package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {
    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(UserRole.valueOf(resultSet.getString("role")));
        user.setSole(resultSet.getString("sole"));

        return user;
    }
}
