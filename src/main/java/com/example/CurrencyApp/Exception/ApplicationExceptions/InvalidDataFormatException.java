package com.example.CurrencyApp.Exception.ApplicationExceptions;

public class InvalidDataFormatException extends ApplicationValidationException {

    public InvalidDataFormatException(String data) {
        super(data + " is not correct data format. Required format is YYYY-MM-DD");
    }
}
