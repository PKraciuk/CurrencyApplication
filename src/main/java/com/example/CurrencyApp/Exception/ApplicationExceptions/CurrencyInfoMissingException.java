package com.example.CurrencyApp.Exception.ApplicationExceptions;

public class CurrencyInfoMissingException extends ExternalApiException {

    public CurrencyInfoMissingException() {
        super("Requested resource doesn't exist or is not available");
    }
}
