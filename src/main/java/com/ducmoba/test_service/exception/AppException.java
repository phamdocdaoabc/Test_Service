package com.ducmoba.test_service.exception;

public class AppException extends RuntimeException{
    private  ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMesssge());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
