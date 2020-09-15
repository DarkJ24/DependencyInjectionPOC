package com.darkj24.ioc.exceptions;

public class BeanInstantiationException extends ProviderInstantiationException {
    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}