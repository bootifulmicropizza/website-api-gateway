package com.bootifulmicropizza.gateway.website.rest.v1;

import com.bootifulmicropizza.gateway.website.facade.OrderFacade;
import com.bootifulmicropizza.gateway.website.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/orders", produces = "application/json")
public class OrderRestController {

    private OrderFacade orderFacade;

    public OrderRestController(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Order> getOrder(@PathVariable final Long id) {
        final Order order = orderFacade.getOrderByOrderId(id);

        if (order == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(order);
    }
}
