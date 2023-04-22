package com.example.CurrencyApp.Exception.ApplicationExceptions;

import javax.xml.bind.ValidationException;

public class InvalidCurrencyCodeException extends ApplicationValidationException {

    public InvalidCurrencyCodeException(String code) {
        super("Provided currency code: " + code + "  is not valid");
    }
}
