package com.darkj24.ioc.exceptions;

public class NeedsDependencyException extends RuntimeException {
    public NeedsDependencyException(String message) {
        super(message);
    }

    public NeedsDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}