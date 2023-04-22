package com.example.CurrencyApp.Service;

import com.example.CurrencyApp.Dto.CurrencyExtremaResponseDto;
import com.example.CurrencyApp.Exception.ApplicationExceptions.InvalidCurrencyCodeException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.InvalidDataFormatException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.InvalidQuotationAmountException;
import com.example.CurrencyApp.Model.ExchangeRateTableSingle;
import com.example.CurrencyApp.Model.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CurrencyServiceTest {

    @Mock
    private NbpApiCaller nbpApiCaller;

    @Spy
    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentRateByCodeAndDate_validInput_success() {
        String code = "USD";
        String date = "2023-04-22";
        ExchangeRateTableSingle expectedResponse = new ExchangeRateTableSingle();
        Rate rate = new Rate();
        rate.setMid(1.23);
        expectedResponse.setRates(Arrays.asList(rate));
        when(nbpApiCaller.getCurrentAvgValue(any(String.class), any(String.class))).thenReturn(expectedResponse);
        Double actualResponse = currencyService.getCurrentRateByCodeAndDate(code, date);
        assertEquals(1.23, actualResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB1","123","ABCD","AB"})
    void getCurrentRateByCodeAndDate_invalidCurrencyCode_exceptionThrown(String code) {
        String date = "2023-04-22";
        assertThrows(InvalidCurrencyCodeException.class, () -> currencyService.getCurrentRateByCodeAndDate(code, date));
    }

    @ParameterizedTest
    @ValueSource(strings = {"22-04-2023", "2023/04/22", "22 Apr 2023", "2022-01-01T10:00:00"})
    void getCurrentRateByCodeAndDate_invalidDateFormat_exceptionThrown(String date) {
        String code = "USD";
        assertThrows(InvalidDataFormatException.class, () -> currencyService.getCurrentRateByCodeAndDate(code, date));
    }

    @Test
    void getCurrencyExtrema_validInput_success() {
        String code = "USD";
        int n = 10;
        ExchangeRateTableSingle expectedResponse = new ExchangeRateTableSingle();
        Rate rate1 = new Rate();
        rate1.setMid(1.2);
        Rate rate2 = new Rate();
        rate2.setMid(1.3);
        expectedResponse.setRates(Arrays.asList(rate1, rate2));
        when(nbpApiCaller.getAvgValues(any(String.class), any(Integer.class))).thenReturn(expectedResponse);
        CurrencyExtremaResponseDto actualResponse = currencyService.getCurrencyExtrema(code, n);
        assertEquals(1.2, actualResponse.getMin());
        assertEquals(1.3, actualResponse.getMax());
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB1","123","ABCD","AB"})
    void getCurrencyExtrema_invalidCurrencyCode_exceptionThrown(String code) {
        int n = 10;
        assertThrows(InvalidCurrencyCodeException.class, () -> currencyService.getCurrencyExtrema(code, n));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,0,256})
    void getCurrencyExtrema_invalidQuotationsNumber_exceptionThrown(int quotation) {
        String code = "USD";
        assertThrows(InvalidQuotationAmountException.class, () -> currencyService.getCurrencyExtrema(code, quotation));
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB1","123","ABCD","AB"})
    void testGetMajorDifferenceWithInvalidCurrencyCode(String code) {
        // Arrange
        int n = 7;
        CurrencyService currencyService = new CurrencyService(nbpApiCaller);

        // Act
        // Assert
        assertThrows(InvalidCurrencyCodeException.class, () -> currencyService.getMajorDifference(code, n));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,0,256})
    void testGetMajorDifferenceWithInvalidQuotationsNumber(int quotation) {
        // Arrange
        String code = "USD";
        CurrencyService currencyService = new CurrencyService(nbpApiCaller);

        // Act
        // Assert
        assertThrows(InvalidQuotationAmountException.class, () -> currencyService.getMajorDifference(code, quotation));
    }

    @Test
    void testGetMajorDifferenceWithValidInputs() {
        // Arrange
        String code = "EUR";
        int n = 7;
        ExchangeRateTableSingle exchangeRateTableSingle = new ExchangeRateTableSingle();
        exchangeRateTableSingle.setRates(Arrays.asList(
                createRate(1.0, 1.1),
                createRate(2.0, 2.2),
                createRate(3.0, 3.3),
                createRate(4.0, 4.4),
                createRate(5.0, 5.5),
                createRate(6.0, 6.6),
                createRate(7.0, 7.7)
        ));
        when(nbpApiCaller.getSellRates(code, n)).thenReturn(exchangeRateTableSingle);
        CurrencyService currencyService = new CurrencyService(nbpApiCaller);

        // Act
        Double result = currencyService.getMajorDifference(code, n);

        // Assert
        assertEquals(0.7, result);
    }

    private Rate createRate(double ask, double bid) {
        Rate tmp = new Rate();
        tmp.setAsk(ask);
        tmp.setBid(bid);
        return tmp;

    }

}