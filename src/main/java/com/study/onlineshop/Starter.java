package com.study.onlineshop;

import com.study.onlineshop.dao.jdbc.DataSourceFactory;
import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.dao.jdbc.JdbcUserDao;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.service.impl.DefaultProductService;
import com.study.onlineshop.service.impl.DefaultUserService;
import com.study.onlineshop.web.filter.UserRoleSecurityFilter;
import com.study.onlineshop.web.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.util.EnumSet;

public class Starter {
    public static void main(String[] args) throws Exception {
        // connection pool creating
        DataSource dataSourceFactory = DataSourceFactory.getPostgresDataSource();

        // configure daos
        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSourceFactory);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSourceFactory);

        // configure services
        DefaultProductService defaultProductService = new DefaultProductService(jdbcProductDao);
        DefaultUserService defaultUserService = new DefaultUserService(jdbcUserDao);

        //security
        SecurityService securityService = new SecurityService(defaultUserService);

        // servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(defaultProductService);
        productsServlet.setSecurityService(securityService);

        ProductsApiServlet productsApiServlet = new ProductsApiServlet(defaultProductService);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(defaultProductService);

        LoginServlet loginServlet = new LoginServlet(securityService);
        LogoutServlet logoutServlet = new LogoutServlet(securityService);

        // config web server
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "");
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/product/add");
        servletContextHandler.addServlet(new ServletHolder(productsApiServlet), "/api/v1/products");

        servletContextHandler.addServlet(new ServletHolder(loginServlet), "/login");
        servletContextHandler.addServlet(new ServletHolder(logoutServlet), "/logout");

        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "",
                EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/products",
                EnumSet.of(DispatcherType.REQUEST));

        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/product/add",
                EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(8083);
        server.setHandler(servletContextHandler);
        server.start();
    }
}
