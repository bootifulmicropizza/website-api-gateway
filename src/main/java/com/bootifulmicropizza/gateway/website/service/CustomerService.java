package com.bootifulmicropizza.gateway.website.service;

import com.bootifulmicropizza.gateway.website.model.Customer;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("account-service/customers")
public interface CustomerService {

    @RequestMapping(method = RequestMethod.POST, value = "/")
    Resource<Customer> createCustomer(@RequestBody Customer customer);

    @RequestMapping(method = RequestMethod.PUT, value = "/")
    Resource<Customer> updateCustomer(@RequestBody Customer customer);
}
