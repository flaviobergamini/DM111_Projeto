package br.inatel.dm111promo.core.aggregates.promotion;


import br.inatel.dm111promo.core.aggregates.product.ProductAggregate;

import java.util.List;

public class PromotionAggregate {
    private String id;
    private String name;
    private String starting;
    private String expiration;
    private List<ProductAggregate> products;

    public PromotionAggregate(){}

    public PromotionAggregate(String id, String name, String starting, String expiration, List<ProductAggregate> products) {
        this.id = id;
        this.name = name;
        this.starting = starting;
        this.expiration = expiration;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public List<ProductAggregate> getProducts() {
        return products;
    }

    public void setProducts(List<ProductAggregate> products) {
        this.products = products;
    }
}
