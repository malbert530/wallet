package com.example.wallet.handler;

import com.example.wallet.apiError.ApiError;
import com.example.wallet.exception.LowBalanceException;
import com.example.wallet.exception.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleWalletNotFound(WalletNotFoundException e) {
        return ApiError.builder().error(e.getMessage()).errorCode(HttpStatus.NOT_FOUND.value()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ApiError handleLowBalanceException(LowBalanceException e) {
        return ApiError.builder().error(e.getMessage()).errorCode(HttpStatus.PAYMENT_REQUIRED.value()).build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(Exception e) {
        return ApiError.builder().error(e.getMessage()).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUuidFormatException(HttpMessageNotReadableException e) {
        return ApiError.builder().error(e.getMessage()).errorCode(HttpStatus.BAD_REQUEST.value()).build();
    }

}
