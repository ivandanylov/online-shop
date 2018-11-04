package com.study.onlineshop.dao;

import com.study.onlineshop.entity.User;

public interface UserDao {
    User getUser(String login, String password);

    String getSole(String login);
}
