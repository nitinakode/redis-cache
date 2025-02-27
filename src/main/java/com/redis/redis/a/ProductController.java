package com.redis.redis.a;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;  // Service to interact with DB



    @GetMapping("/redis/all-products")
    public Set<String> getAllProductsInRedis() {
        return productService.getAllRedisKeys();
    }
    @PostMapping("/save")
    public ResponseEntity<?>save(@RequestBody Product product)
    {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }

    @DeleteMapping("/redis/clear-all-cache")
    public String clearAllCache() {
        productService.clearCache();
        return "All cache cleared!";
    }
}
