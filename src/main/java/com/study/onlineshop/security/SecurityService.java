package com.study.onlineshop.security;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.service.impl.DefaultUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityService {
    private List<Session> sessionList = new ArrayList<>();
    private DefaultUserService userService;

    private String getPassword(String sole, String password) {
        if (sole == null || sole.isEmpty()) {
            return null;
        }

        return sha1(sole.concat(password));
    }

    public SecurityService(DefaultUserService userService) {
        this.userService = userService;
    }

    private String sha1(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        byte[] result = messageDigest.digest(input.getBytes());
        StringBuilder stringBuilder = new StringBuilder();

        for (byte aResult : result) {
            stringBuilder.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }

    public Session login(String name, String password) {
        String sole = userService.getSole(name);
        if (sole == null || sole.isEmpty()) {
            return null;
        }

        User user = userService.getUser(name, getPassword(sole, password));

        if (user != null) {
            Session session = new Session();
            session.setUser(user);
            session.setToken(UUID.randomUUID().toString());

            session.setExpireDate(LocalDateTime.now().plusHours(5));
            sessionList.add(session);

            return session;
        }

        return null;
    }

    public void logout(String token) {
        // iterate over session, find and remove session
        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                sessionList.remove(session);
                return;
            }
        }
    }

    public Session getSession(String token) {
        // iterate, check if not expired  and return
        if (token == null || token.isEmpty()) {
            return null;
        }

        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                return session;
            }
        }

        return null;
    }

    public String getCookieToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public Cookie setCookieToken(Session session) {
        if (session == null) {
            return null;
        }

        Cookie cookie = new Cookie("user-token", session.getToken());
        cookie.setMaxAge(60 * 60 * 5);

        return cookie;
    }
}
