package br.inatel.dm111promo.core.models;

import br.inatel.dm111promo.core.aggregates.product.ProductAggregate;

import java.util.List;

public record PromotionRequestModel(String name, String starting, String expiration, List<ProductAggregate> products)
{
}
