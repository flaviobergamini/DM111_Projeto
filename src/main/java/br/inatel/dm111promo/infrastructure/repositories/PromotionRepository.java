package br.inatel.dm111promo.infrastructure.repositories;

import br.inatel.dm111promo.core.interfaces.IPromotionRepository;
import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class PromotionRepository implements IPromotionRepository
{
    private static final String COLLECTION_NAME = "promotions";

    private final Firestore _firestore;

    public PromotionRepository(Firestore firestore) {
        this._firestore = firestore;
    }

    @Override
    public void save(PromotionAggregate promotion) {
        this._firestore.collection(COLLECTION_NAME)
                .document(promotion.getId())
                .set(promotion);
    }

    @Override
    public List<PromotionAggregate> findAll() throws ExecutionException, InterruptedException {
        return this._firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(promotion -> promotion.toObject(PromotionAggregate.class))
                .toList();
    }

    @Override
    public Optional<PromotionAggregate> findById(String id) throws ExecutionException, InterruptedException {
        var promotion = this._firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(PromotionAggregate.class);
        return Optional.ofNullable(promotion);
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        this._firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    @Override
    public void update(PromotionAggregate promotion) {
        save(promotion);
    }
}
