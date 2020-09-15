package com.darkj24.ioc.exceptions;

public class ProviderInstantiationException extends RuntimeException {

    public ProviderInstantiationException(String message) {
        super(message);
    }

    public ProviderInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
