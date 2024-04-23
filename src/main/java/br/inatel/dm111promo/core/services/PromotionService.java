package br.inatel.dm111promo.core.services;

import br.inatel.dm111promo.core.enums.AppErrorCode;
import br.inatel.dm111promo.core.interfaces.IPromotionRepository;
import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;
import br.inatel.dm111promo.core.models.ApiExceptionModel;
import br.inatel.dm111promo.core.models.PromotionRequestModel;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class PromotionService {

    private final IPromotionRepository _promotionRepository;

    public PromotionService(IPromotionRepository promotionRepository)
    {
        this._promotionRepository = promotionRepository;
    }

    public List<PromotionAggregate> searchPromotions() throws ApiExceptionModel{
        try {
            return this._promotionRepository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiExceptionModel(AppErrorCode.PROMOTIONS_QUERY_ERROR);
        }
    }

    public PromotionAggregate getPromotionById(String id) throws ApiExceptionModel {
        return retrievePromotion(id);
    }

    public PromotionAggregate createPromotion(PromotionRequestModel request){
        var id = UUID.randomUUID().toString();
        var formatter = new SimpleDateFormat("dd/MM/yyyy");

        var promotion = new PromotionAggregate(
                id,
                request.name(),
                request.starting(),
                request.expiration(),
                request.products()
        );

        this._promotionRepository.save(promotion);

        return promotion;
    }

    public PromotionAggregate updatePromotion(String id, PromotionRequestModel request) throws ApiExceptionModel {
        var promotion = retrievePromotion(id);

        var formatter = new SimpleDateFormat("dd/MM/yyyy");

        promotion.setName(request.name());
        promotion.setStarting(request.starting());
        promotion.setExpiration(request.expiration());
        promotion.setProducts(request.products());

        this._promotionRepository.update(promotion);

        return promotion;
    }

    public void removePromotion(String id) throws ApiExceptionModel {
        try {
            _promotionRepository.delete(id);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiExceptionModel(AppErrorCode.PROMOTIONS_QUERY_ERROR);
        }
    }


    private PromotionAggregate retrievePromotion(String id) throws ApiExceptionModel {
        try {
            return this._promotionRepository.findById(id)
                    .orElseThrow(() -> new ApiExceptionModel(AppErrorCode.PROMOTION_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiExceptionModel(AppErrorCode.PROMOTIONS_QUERY_ERROR);
        }
    }
}
