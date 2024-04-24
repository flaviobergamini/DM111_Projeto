package br.inatel.dm111promo.core.models;

import br.inatel.dm111promo.core.aggregates.product.ProductAggregate;

import java.util.List;
public record PromotionByUserModel(
        String Id,
        String name,
        String starting,
        String expiration,
        List<ProductAggregate> productsForYou,
        List<ProductAggregate> products)
{
}