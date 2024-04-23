package br.inatel.dm111promo.core.models;

import br.inatel.dm111promo.core.enums.AppErrorCode;
import org.springframework.http.HttpStatus;

public class ApiExceptionModel extends Exception{

    private String errorCode;
    private String message;
    private HttpStatus status;

    public ApiExceptionModel(AppErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
