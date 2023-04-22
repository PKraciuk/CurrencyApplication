package com.example.CurrencyApp.Exception.ApplicationExceptions;

public class ApplicationValidationException extends RuntimeException {
    public ApplicationValidationException(String message) {
        super(message);
    }
}
