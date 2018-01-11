package com.bootifulmicropizza.gateway.website.service;

import com.bootifulmicropizza.gateway.website.model.Product;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("inventory-service")
public interface ProductService {

    @RequestMapping(method = RequestMethod.GET, value = "/products/search/by-product-id")
    Resource<Product> getProductByProductId(@RequestParam("productId") String productId);
}
