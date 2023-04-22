package com.example.CurrencyApp.Service;

import com.example.CurrencyApp.Dto.CurrencyExtremaResponseDto;
import com.example.CurrencyApp.Exception.ApplicationExceptions.InvalidCurrencyCodeException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.InvalidDataFormatException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.InvalidQuotationAmountException;
import com.example.CurrencyApp.Model.ExchangeRateTableSingle;
import com.example.CurrencyApp.Model.Rate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;

@Service
public class CurrencyService {
    public NbpApiCaller nbpApiCaller;

    public CurrencyService(NbpApiCaller nbpApiCaller) {
        this.nbpApiCaller = nbpApiCaller;
    }

    public Double getCurrentRateByCodeAndDate(String code, String date) {
        validateCurrencyCode(code);
        validateDateFormat(date);
        ExchangeRateTableSingle callResponse = nbpApiCaller.getCurrentAvgValue(code, date);
        return callResponse.getRates().get(0).getMid();
    }

    public CurrencyExtremaResponseDto getCurrencyExtrema(String code, int n) {
        validateCurrencyCode(code);
        validateQuotationsNumber(n);
        CurrencyExtremaResponseDto response = new CurrencyExtremaResponseDto();
        ExchangeRateTableSingle callResponse = nbpApiCaller.getAvgValues(code, n);
        DoubleSummaryStatistics stats = callResponse.getRates().stream()
                .mapToDouble(Rate::getMid)
                .summaryStatistics();
        response.setMax(stats.getMax());
        response.setMin(stats.getMin());
        return response;
    }

    public Double getMajorDifference(String code, int n) {
        validateCurrencyCode(code);
        validateQuotationsNumber(n);
        ExchangeRateTableSingle callResponse = nbpApiCaller.getSellRates(code, n);
        return callResponse.getRates().stream()
                .mapToDouble(Rate -> Math.abs(Rate.getAsk() - Rate.getBid()))
                .map(d -> Math.round(d * 10000.0) / 10000.0)
                .max()
                .orElseThrow(RuntimeException::new);
    }

    private void validateCurrencyCode(String code) {
        if (!code.matches("^[a-zA-Z]{3}$")) {
            throw new InvalidCurrencyCodeException(code);
        }
    }

    private void validateDateFormat(String date) {
        try {
            LocalDate.parse(date);
        } catch (Exception ex) {
            throw new InvalidDataFormatException(date);
        }
    }

    private void validateQuotationsNumber(int n) {
        if (n < 1 || n > 255) {
            throw new InvalidQuotationAmountException(n);
        }
    }
}
