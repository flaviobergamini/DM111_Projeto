package br.inatel.dm111promo.core.services;

import br.inatel.dm111promo.core.models.ApiExceptionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiExceptionModel.class)
    protected ResponseEntity<AppError> handleEntity(ApiExceptionModel exception, WebRequest request) {
        return new ResponseEntity<>(buildError(exception), exception.getStatus());
    }

    private AppError buildError(ApiExceptionModel exception) {
        return new AppError(exception.getErrorCode(), exception.getMessage());
    }
}
