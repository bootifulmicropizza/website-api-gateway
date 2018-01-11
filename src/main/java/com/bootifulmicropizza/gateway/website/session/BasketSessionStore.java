package com.bootifulmicropizza.gateway.website.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BasketSessionStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> basketItems = new HashMap<>();

    public Map<String, Integer> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(final Map<String, Integer> basketItems) {
        this.basketItems = basketItems;
    }

    public void addItem(final String productId, final Integer quantity) {
        basketItems.put(productId, quantity);
    }

    public void removeItem(final String productId) {
        basketItems.remove(productId);
    }
}
