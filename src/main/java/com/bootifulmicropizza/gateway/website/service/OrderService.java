package com.bootifulmicropizza.gateway.website.service;

import com.bootifulmicropizza.gateway.website.model.Order;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("order-service")
public interface OrderService {

    @RequestMapping(method = RequestMethod.GET, value = "/orders/{id}/")
    Resource<Order> getOrderByOrderId(@PathVariable("id") Long orderId);
}
