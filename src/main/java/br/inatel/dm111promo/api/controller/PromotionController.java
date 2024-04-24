package br.inatel.dm111promo.api.controller;

import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;
import br.inatel.dm111promo.core.models.ApiExceptionModel;
import br.inatel.dm111promo.core.models.PromotionRequestModel;
import br.inatel.dm111promo.core.models.PromotionResponseModel;
import br.inatel.dm111promo.core.services.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dm111")
public class PromotionController {
 //   private static final Logger _logger = LoggerFactory.getLogger(PromotionController.class);

    private final PromotionService _promotionService;
    public PromotionController(PromotionService promotionService)
    {
        this._promotionService = promotionService;
    }

    @GetMapping("/promotion/{id}")
    public ResponseEntity<PromotionResponseModel> getPromotionById (@PathVariable("id") String id) throws ApiExceptionModel {
        var promotion = this._promotionService.getPromotionById(id);

        return ResponseEntity.ok(promotionResponseConverter(promotion));
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<PromotionResponseModel>> getPromotions() throws ApiExceptionModel {
        var promotions = this._promotionService.searchPromotions();

        List<PromotionResponseModel> response = new ArrayList<>();

        for(var promotion : promotions){
            response.add(promotionResponseConverter(promotion));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/promotions")
    public ResponseEntity<PromotionResponseModel> postPromotion(@RequestBody PromotionRequestModel request) throws ParseException {
        var promotion = this._promotionService.createPromotion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionResponseConverter(promotion));
    }

    @PutMapping("/promotions/{id}")
    public ResponseEntity<PromotionResponseModel> putPromotion(
            @PathVariable("id") String id,
            @RequestBody PromotionRequestModel request) throws ApiExceptionModel, ParseException {
        var promotion = this._promotionService.updatePromotion(id, request);
        return ResponseEntity.ok(promotionResponseConverter(promotion));
    }

    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable("id")String id) throws ApiExceptionModel {
        this._promotionService.removePromotion(id);
        return ResponseEntity.noContent().build();
    }

    private PromotionResponseModel promotionResponseConverter(PromotionAggregate promotion){
        var simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return new PromotionResponseModel(
                promotion.getId(),
                promotion.getName(),
                simpleDateFormat.format(promotion.getStarting()),
                simpleDateFormat.format(promotion.getExpiration()),
                promotion.getProducts()
        );
    }
}
