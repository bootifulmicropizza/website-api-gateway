package com.bootifulmicropizza.gateway.website.rest.v1;

import com.bootifulmicropizza.gateway.website.facade.ProductFacade;
import com.bootifulmicropizza.gateway.website.model.Basket;
import com.bootifulmicropizza.gateway.website.model.Product;
import com.bootifulmicropizza.gateway.website.rest.v1.request.UpdateBasketRequest;
import com.bootifulmicropizza.gateway.website.session.BasketSessionStore;
import com.bootifulmicropizza.gateway.website.util.SessionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/v1/basket", produces = "application/json")
public class BasketRestController {

    private ProductFacade productFacade;

    public BasketRestController(final ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Basket> loadBasket(final HttpSession session) {
        final Basket basket = new Basket();

        final BasketSessionStore basketSessionStore = SessionUtils.getBasket(session);
        basketSessionStore.getBasketItems().entrySet().forEach(entry -> {
            final String productId = entry.getKey();
            final Integer quantity = entry.getValue();

            final Product product = productFacade.getProductByProductId(productId);
            basket.addItem(product, quantity);
        });

        return ResponseEntity.ok(basket);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResponseEntity<Void> clearBasket(final HttpSession session) {
        SessionUtils.updateBasket(new BasketSessionStore(), session);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateBasket(@RequestBody final UpdateBasketRequest updateBasketRequest,
                                                   final HttpSession session) {

        final BasketSessionStore basket = SessionUtils.getBasket(session);

        basket.addItem(updateBasketRequest.getProductId(), updateBasketRequest.getQuantity());

        SessionUtils.updateBasket(basket, session);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{productId}/", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeFromBasket(@PathVariable final String productId,
                                                 final HttpSession session) {

        final BasketSessionStore basket = SessionUtils.getBasket(session);

        basket.removeItem(productId);

        SessionUtils.updateBasket(basket, session);

        return ResponseEntity.ok().build();
    }
}
