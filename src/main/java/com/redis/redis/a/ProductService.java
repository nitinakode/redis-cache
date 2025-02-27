package com.redis.redis.a;


import com.redis.redis.a.Product;
import com.redis.redis.a.ProductRepository;
import com.redis.redis.a.RedisService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_BUCKET = "product_bucket";


    @PostConstruct
    public void insertIntoCache()
    {
        List<Product>productList=   productRepository.findAll();
        List<String>productNamesInDb=productList.stream().map(Product::getName).toList();
        List<String> productNamesInRedis=redisService.getValue(PRODUCT_BUCKET).keySet().stream().toList();
        List<String> deletedProducts=productNamesInRedis.stream().filter(productName->!productNamesInDb.contains(productName)).toList();
        for(String productName:deletedProducts)
        {
            redisService.deleteCache(PRODUCT_BUCKET,productName);
        }
        for(Product product:productList)
        {
            redisService.putValue(PRODUCT_BUCKET,product.getName(),product);
        }
    }


    public Product createProduct(Product product) {
        redisService.putValue(PRODUCT_BUCKET,product.getName(),product);
        return productRepository.save(product);
    }



    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        redisService.deleteCache(PRODUCT_BUCKET,product.getName());
    }

    public void clearCache() {
       List<String> productNames= redisService.getValue(PRODUCT_BUCKET).keySet().stream().toList();
       for(String productName:productNames)
       {
           redisService.deleteCache(PRODUCT_BUCKET,productName);
       }

    }

    public Set<String> getAllRedisKeys() {
        return  redisTemplate.keys(PRODUCT_BUCKET);
    }
}
