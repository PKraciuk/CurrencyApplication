package com.example.CurrencyApp.Exception.ApplicationExceptions;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }
}
