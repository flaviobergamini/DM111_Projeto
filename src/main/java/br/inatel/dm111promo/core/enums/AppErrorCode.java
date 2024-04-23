package br.inatel.dm111promo.core.enums;

import org.springframework.http.HttpStatus;

public enum AppErrorCode {
    PROMOTION_NOT_FOUND("entity.promotion.not-found", "The Promotion does not exist.", HttpStatus.NOT_FOUND),
    PROMOTIONS_NOT_FOUND("entity.promotions.not-found", "The Promotion(s) do(es) not exist.", HttpStatus.NOT_FOUND),
    PROMOTIONS_QUERY_ERROR("query.promotion.error", "The promotion query is not working. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),
    SUPERMARKET_LIST_OPERATION_NOT_ALLOWED("entity.supermarket-list.operation-not-allowed", "The operation is not allowed.", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS("auth.user.invalid-credentials", "The provided credentials are invalid.", HttpStatus.UNAUTHORIZED),
    PROMOTIONS_OPERATION_NOT_ALLOWED("entity.products.operation-not-allowed", "The operation is not allowed.", HttpStatus.FORBIDDEN),
    USERS_QUERY_ERROR("query.user.error", "The user query is not working. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_ENCRYPTION_ERROR("encrypt.user.error", "The user encryption did not work!", HttpStatus.INTERNAL_SERVER_ERROR);

    private String code;
    private String message;
    private HttpStatus status;

    AppErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
