package com.bootifulmicropizza.gateway.website.model;

import java.io.Serializable;
import java.util.*;

public class Basket implements Serializable {

    private Set<BasketItem> items = new HashSet<>();

    public void addItem(final Product product, final Integer quantity) {
        items.add(new BasketItem(product, quantity));
    }

    public Set<BasketItem> getItems() {
        return items;
    }

    public void setItems(Set<BasketItem> items) {
        this.items = items;
    }
}
