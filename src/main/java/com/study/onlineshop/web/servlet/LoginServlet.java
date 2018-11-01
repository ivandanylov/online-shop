package com.study.onlineshop.web.servlet;

import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LoginServlet extends HttpServlet {
    private List<String> activeTokens;

    public LoginServlet(List<String> activeTokens) {
        this.activeTokens = activeTokens;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        HashMap<String, Object> parameters = new HashMap<>();

        String page = pageGenerator.getPage("login", parameters);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        System.out.println(login + " : " + password);

        // if user is valid
        String userToken = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("user-token", userToken);
        activeTokens.add(userToken);

        resp.addCookie(cookie);
        resp.sendRedirect("/");
    }
}
