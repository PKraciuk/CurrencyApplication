package com.example.CurrencyApp.Exception.ApplicationExceptions;

public class InvalidQuotationAmountException extends ApplicationValidationException {
    public InvalidQuotationAmountException(int n) {
        super("Number of quotations must be in range <1,255>");
    }
}
