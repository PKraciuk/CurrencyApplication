package com.example.CurrencyApp.Exception.ApplicationExceptions;


public class InvalidCurrencyCodeException extends ApplicationValidationException {

    public InvalidCurrencyCodeException(String code) {
        super("Provided currency code: " + code + "  is not valid");
    }
}
