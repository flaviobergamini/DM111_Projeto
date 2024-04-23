package br.inatel.dm111promo.api.controller;

import br.inatel.dm111promo.core.aggregates.promotion.PromotionAggregate;
import br.inatel.dm111promo.core.models.ApiExceptionModel;
import br.inatel.dm111promo.core.models.PromotionRequestModel;
import br.inatel.dm111promo.core.services.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PromotionAggregate> getPromotionById (@PathVariable("id") String id) throws ApiExceptionModel {
        var promotion = this._promotionService.getPromotionById(id);

        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<PromotionAggregate>> getPromotions() throws ApiExceptionModel {

        var promotions = this._promotionService.searchPromotions();
        return ResponseEntity.ok(promotions);
    }

    @PostMapping("/promotions")
    public ResponseEntity<PromotionAggregate> postPromotion(@RequestBody PromotionRequestModel request) {
        var promotion = this._promotionService.createPromotion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(promotion);
    }

    @PutMapping("/promotions/{id}")
    public ResponseEntity<PromotionAggregate> putPromotion(
            @PathVariable("id") String id,
            @RequestBody PromotionRequestModel request) throws ApiExceptionModel {
        var promotion = this._promotionService.updatePromotion(id, request);
        return ResponseEntity.ok(promotion);
    }

    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable("id")String id) throws ApiExceptionModel {
        this._promotionService.removePromotion(id);
        return ResponseEntity.noContent().build();
    }
}
