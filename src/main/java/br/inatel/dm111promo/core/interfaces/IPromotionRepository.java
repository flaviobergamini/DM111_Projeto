package br.inatel.dm111promo.core.interfaces;

import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface IPromotionRepository
{
    void save(PromotionAggregate promotion);

    List<PromotionAggregate> findAll() throws ExecutionException, InterruptedException;

    Optional<PromotionAggregate> findById(String id) throws ExecutionException, InterruptedException;

    void delete(String id) throws ExecutionException, InterruptedException;

    void update(PromotionAggregate promotion);
}
