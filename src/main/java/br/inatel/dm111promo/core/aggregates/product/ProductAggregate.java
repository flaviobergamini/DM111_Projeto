package br.inatel.dm111promo.core.aggregates.product;

public class ProductAggregate {
    private String productId;
    private String discount;

    public ProductAggregate(){}

    public ProductAggregate(String productId, String discount) {
        this.productId = productId;
        this.discount = discount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
