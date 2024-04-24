package br.inatel.dm111promo.core.services;

import br.inatel.dm111promo.core.aggregates.product.ProductAggregate;
import br.inatel.dm111promo.core.enums.AppErrorCode;
import br.inatel.dm111promo.core.interfaces.IPromotionRepository;
import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;
import br.inatel.dm111promo.core.interfaces.ISupermarketRepository;
import br.inatel.dm111promo.core.models.ApiExceptionModel;
import br.inatel.dm111promo.core.models.PromotionByUserModel;
import br.inatel.dm111promo.core.models.PromotionRequestModel;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class PromotionService {

    private final int MINIMUM_FREQUENCY = 2;
    private final IPromotionRepository _promotionRepository;
    private final ISupermarketRepository _supermarketRepository;

    public PromotionService(IPromotionRepository promotionRepository, ISupermarketRepository supermarketRepository)
    {
        this._promotionRepository = promotionRepository;
        this._supermarketRepository = supermarketRepository;
    }

    public List<PromotionAggregate> searchPromotions() throws ApiExceptionModel{
        try {
            var promotions = this._promotionRepository.findAll();
            var actualDate = new Date();
            List<PromotionAggregate> response = new ArrayList<>();

            for(var promotion : promotions){
                if(actualDate.compareTo(promotion.getStarting()) >= 0 && actualDate.compareTo(promotion.getExpiration()) <= 0)
                    response.add(promotion);
            }

            return response;
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiExceptionModel(AppErrorCode.PROMOTIONS_QUERY_ERROR);
        }
    }

    public PromotionAggregate getPromotionById(String id) throws ApiExceptionModel, ExecutionException, InterruptedException {
        var promotion = this._promotionRepository.findById(id);
        var actualDate = new Date();

        if(!promotion.isEmpty() && actualDate.compareTo(promotion.get().getStarting()) >= 0 && actualDate.compareTo(promotion.get().getExpiration()) <= 0)
            return promotion.get();
        else
            throw new ApiExceptionModel(AppErrorCode.PROMOTION_NOT_FOUND);
    }

    public PromotionAggregate createPromotion(PromotionRequestModel request) throws ParseException {
        var id = UUID.randomUUID().toString();
        var simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        var starting = simpleDateFormat.parse(request.starting());
        var expiration = simpleDateFormat.parse(request.expiration());

        var promotion = new PromotionAggregate(
                id,
                request.name(),
                starting,
                expiration,
                request.products()
        );

        this._promotionRepository.save(promotion);

        return promotion;
    }

    public PromotionAggregate updatePromotion(String id, PromotionRequestModel request) throws ApiExceptionModel, ParseException {
        var promotion = retrievePromotion(id);

        var simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        var starting = simpleDateFormat.parse(request.starting());
        var expiration = simpleDateFormat.parse(request.expiration());

        promotion.setName(request.name());
        promotion.setStarting(starting);
        promotion.setExpiration(expiration);
        promotion.setProducts(request.products());

        this._promotionRepository.update(promotion);

        return promotion;
    }

    public PromotionByUserModel getPromotionsForUser(String userId) throws ExecutionException, InterruptedException {
        var promotions = this._promotionRepository.findAll();
        var actualDate = new Date();
        List<PromotionAggregate> validPromotions = new ArrayList<>();

        for(var promotion : promotions){
            if(actualDate.compareTo(promotion.getStarting()) >= 0 && actualDate.compareTo(promotion.getExpiration()) <= 0)
                validPromotions.add(promotion);
        }

        var supermarketlist = this._supermarketRepository.findByUserId(userId);

        List<ProductAggregate> productsPromotion = new ArrayList<>();
        List<String> productsSupermarket = new ArrayList<>();

        for(var promotion: validPromotions){
            productsPromotion.addAll(promotion.getProducts());
        }

        for(var supermarket: supermarketlist){
            productsSupermarket.addAll(supermarket.getProducts());
        }

        List<ProductAggregate> productsForYou = new ArrayList<>();

        var simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for(var product: productsPromotion){
            for(var productSupermarket : productsSupermarket){
                if(productSupermarket.contains(product.getProductId())){
                    productsForYou.add(product);
                }
            }
        }

        var response = new PromotionByUserModel(
                validPromotions.get(0).getId(),
                validPromotions.get(0).getName(),
                simpleDateFormat.format(validPromotions.get(0).getStarting()),
                simpleDateFormat.format(validPromotions.get(0).getExpiration()),
                productsForYou,
                productsPromotion
        );

        return response;

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
