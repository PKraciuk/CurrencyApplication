package com.example.CurrencyApp.Exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ApplicationError {
    private final String message;
    private final Instant timestamp;

    public ApplicationError(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }
}
