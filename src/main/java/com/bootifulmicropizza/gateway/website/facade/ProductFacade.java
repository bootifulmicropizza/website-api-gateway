package com.bootifulmicropizza.gateway.website.facade;

import com.bootifulmicropizza.gateway.website.model.Product;
import com.bootifulmicropizza.gateway.website.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFacade.class);

    private static final String CACHE_NAME = "products";

    private ProductService productService;

    public ProductFacade(final ProductService productService) {
        this.productService = productService;
    }

    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    @Scheduled(fixedDelay = 86400000) // 24 hours
    public void evictCache() {
        LOGGER.info("Evicting cache " + CACHE_NAME);
    }

    @Cacheable(cacheNames = CACHE_NAME)
    public Product getProductByProductId(final String productId) {
        Resource<Product> productResource = productService.getProductByProductId(productId);

        if (productResource == null) {
            return null;
        }

        return productResource.getContent();
    }
}
