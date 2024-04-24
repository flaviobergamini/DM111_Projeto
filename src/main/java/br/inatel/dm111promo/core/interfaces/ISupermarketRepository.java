package br.inatel.dm111promo.core.interfaces;

import br.inatel.dm111promo.core.aggregates.supermarket.SupermarketAggregate;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ISupermarketRepository {
    List<SupermarketAggregate> findByUserId(String userId)  throws ExecutionException, InterruptedException;
}
