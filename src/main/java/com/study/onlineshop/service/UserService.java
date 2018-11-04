package com.study.onlineshop.service;

import com.study.onlineshop.entity.User;

public interface UserService {
    User getUser(String login, String password);
    String getSole(String login);
}
