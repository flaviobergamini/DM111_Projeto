package br.inatel.dm111promo.infrastructure.consumer;

import java.util.List;

public record SuperMarketListMessage(
        String id,
        String name,
        String userId,
        List<String>products,
        long lastUpdatedOn
) {
}
