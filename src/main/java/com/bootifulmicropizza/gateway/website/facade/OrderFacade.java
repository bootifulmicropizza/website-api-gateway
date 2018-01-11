package com.bootifulmicropizza.gateway.website.facade;

import com.bootifulmicropizza.gateway.website.model.Order;
import com.bootifulmicropizza.gateway.website.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFacade.class);

    private static final String CACHE_NAME = "orders";

    private OrderService orderService;

    public OrderFacade(final OrderService orderService) {
        this.orderService = orderService;
    }

    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    @Scheduled(fixedDelay = 86400000) // 24 hours
    public void evictCache() {
        LOGGER.info("Evicting cache " + CACHE_NAME);
    }

    @Cacheable(cacheNames = CACHE_NAME)
    public Order getOrderByOrderId(final Long id) {
        Resource<Order> orderResource = orderService.getOrderByOrderId(id);

        if (orderResource == null) {
            return null;
        }

        return orderResource.getContent();
    }
}
