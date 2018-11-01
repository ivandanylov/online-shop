package com.study.onlineshop.web.servlet;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProductsServlet extends HttpServlet {
    private ProductService productService;
    private List<String> activeTokens;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        boolean isAuth = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    if (activeTokens.contains(cookie.getValue())) {
                        isAuth = true;
                    }
                    break;
                }
            }
        }
        isAuth = true;
        if (isAuth) {
            PageGenerator pageGenerator = PageGenerator.instance();
            List<Product> products = productService.getAll();

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("products", products);

            String page = pageGenerator.getPage("products", parameters);
            resp.getWriter().write(page);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setActiveTokens(List<String> activeTokens) {
        this.activeTokens = activeTokens;
    }
}
