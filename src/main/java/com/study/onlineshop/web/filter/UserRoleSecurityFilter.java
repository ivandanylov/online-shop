package com.study.onlineshop.web.filter;

import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EnumSet;

public class UserRoleSecurityFilter implements Filter {
    private SecurityService securityService;

    public UserRoleSecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    // chain of responsibility
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        boolean isAuth = false;

        String token = securityService.getCookieToken(httpServletRequest);
        Session session = securityService.getSession(token);
        if (session != null) {
            if (EnumSet.of(UserRole.ADMIN, UserRole.USER).contains(session.getUser().getRole())) {
                if (session.getExpireDate().compareTo(LocalDateTime.now()) >= 0) {
                    isAuth = true;
                }
            }
        }

        if (isAuth) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}

