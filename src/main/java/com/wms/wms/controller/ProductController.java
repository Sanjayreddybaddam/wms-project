package com.wms.wms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wms.wms.entity.Product;
import com.wms.wms.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @PostMapping
    public Product create(@RequestBody Product p) {
        logger.info("Creating product: {} with SKU: {}", p.getName(), p.getSku());

        Product saved = service.create(p);

        logger.info("Product created with ID: {}", saved.getId());
        return saved;
    }

    @GetMapping
    public List<Product> getAll() {
        logger.info("Fetching all products");

        List<Product> list = service.getAll();

        logger.info("Total products fetched: {}", list.size());
        return list;
    }
}