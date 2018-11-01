package com.study.onlineshop;

import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.service.impl.DefaultProductService;
import com.study.onlineshop.web.servlet.LoginServlet;
import com.study.onlineshop.web.servlet.ProductsApiServlet;
import com.study.onlineshop.web.servlet.ProductsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws Exception {
        // configure daos
        JdbcProductDao jdbcProductDao = new JdbcProductDao();

        // configure services
        DefaultProductService defaultProductService = new DefaultProductService(jdbcProductDao);

        // store
        List<String> activeTokens = new ArrayList<>();

        // servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(defaultProductService);
        productsServlet.setActiveTokens(activeTokens);
        ProductsApiServlet productsApiServlet = new ProductsApiServlet(defaultProductService);


        // config web server
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/");

        servletContextHandler.addServlet(new ServletHolder(productsApiServlet), "/api/v1/products");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(activeTokens)), "/login");

        Server server = new Server(8083);
        server.setHandler(servletContextHandler);
        server.start();
    }
}
