package com.study.onlineshop.service.impl;

import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.entity.User;
import com.study.onlineshop.service.UserService;

public class DefaultUserService implements UserService {
    private UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUser(String login, String password) {
        return userDao.getUser(login, password);
    }

    @Override
    public String getSole(String login) {
        return userDao.getSole(login);
    }
}
