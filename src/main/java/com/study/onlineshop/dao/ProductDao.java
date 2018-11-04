package com.study.onlineshop.dao;

import com.study.onlineshop.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    List<Product> getAll();
    Product addProduct(Product product);
    void editProduct(Product oldProduct, Product newProduct);
    void deleteProduct(Product product);
}
