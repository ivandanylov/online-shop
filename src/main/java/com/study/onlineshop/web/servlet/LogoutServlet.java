package com.study.onlineshop.web.servlet;

import com.study.onlineshop.security.SecurityService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private SecurityService securityService;

    public LogoutServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = securityService.getCookieToken(req);
        securityService.logout(token);

        resp.sendRedirect("/login");
    }

}
