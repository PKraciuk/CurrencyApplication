package com.example.CurrencyApp.Exception;

import com.example.CurrencyApp.Exception.ApplicationExceptions.ApplicationValidationException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.CurrencyInfoMissingException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(ApplicationValidationException.class)
    public ResponseEntity<Object> handleApplicationValidationException(ApplicationValidationException e) {
        return new ResponseEntity<>(
                logInfoAndPrepareApplicationError(e),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CurrencyInfoMissingException.class)
    public ResponseEntity<Object> handleCurrencyInfoMissionException(CurrencyInfoMissingException e) {
        return new ResponseEntity<>(
                logInfoAndPrepareApplicationError(e),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<Object> handleExternalApiException(ExternalApiException e) {
        return new ResponseEntity<>(
                logInfoAndPrepareApplicationError(e),
                HttpStatus.BAD_REQUEST
        );
    }


    private ApplicationError logInfoAndPrepareApplicationError(Exception e) {
        log.info("Exception occurred - " + e.getClass());
        return new ApplicationError(e.getMessage());
    }

    private ApplicationError logErrorAndPrepareApplicationError(Exception e) {
        log.error("Unknown Exception Occurred - " + e.getMessage());
        return new ApplicationError("Unknown Exception Occurred");
    }


}
