package com.study.onlineshop.web.servlet;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProductsServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        List<Product> products = productService.getAll();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);

        String token = securityService.getCookieToken(req);
        parameters.put("role", securityService.getSession(token).getUser().getRole().toString());

        String page = pageGenerator.getPage("products", parameters);
        resp.getWriter().write(page);
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
