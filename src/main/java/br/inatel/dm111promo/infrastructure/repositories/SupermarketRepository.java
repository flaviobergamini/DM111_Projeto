package br.inatel.dm111promo.infrastructure.repositories;

import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;
import br.inatel.dm111promo.core.aggregates.supermarket.SupermarketAggregate;
import br.inatel.dm111promo.core.interfaces.ISupermarketRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class SupermarketRepository implements ISupermarketRepository
{
    private static final String COLLECTION_NAME = "supermarket_list";

    private final Firestore _firestore;

    public SupermarketRepository(Firestore firestore) {
        this._firestore = firestore;
    }

    @Override
    public List<SupermarketAggregate> findByUserId(String userId) throws ExecutionException, InterruptedException {
        return this._firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(spl -> spl.toObject(SupermarketAggregate.class))
                .filter(spl -> spl.getUserId().equals(userId))
                .toList();
    }
}
