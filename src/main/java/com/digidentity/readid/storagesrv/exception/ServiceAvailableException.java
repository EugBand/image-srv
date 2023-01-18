package com.digidentity.readid.storagesrv.exception;

public class ServiceAvailableException extends RuntimeException{
    int statusCode;

    public ServiceAvailableException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
