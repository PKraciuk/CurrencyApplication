package com.example.CurrencyApp.Service;

import com.example.CurrencyApp.Exception.ApplicationExceptions.CurrencyInfoMissingException;
import com.example.CurrencyApp.Exception.ApplicationExceptions.ExternalApiException;
import com.example.CurrencyApp.Model.ExchangeRateTableSingle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class NbpApiCaller {

    private final String URL_RATE_DATE_FORMAT = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/%s?format=json";
    private final String URL_LAST_QUOTATIONS_FORMAT = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/%d?format=json";

    private final RestTemplate restTemplate;

    public NbpApiCaller() {
        restTemplate = new RestTemplate();
    }

    @Cacheable(value = "externalApiCalls", key = "{#currencyKey, #date}")
    public ExchangeRateTableSingle getCurrentAvgValue(String currencyKey, String date) {
        String path = String.format(URL_RATE_DATE_FORMAT, "A", currencyKey, date);
        ResponseEntity<ExchangeRateTableSingle> response = performGetRequest(path);
        assert response != null;
        return response.getBody();
    }

    @Cacheable(value = "externalApiCalls", key = "{#currencyKey, #quotationsNum}")
    public ExchangeRateTableSingle getAvgValues(String currencyKey, int quotationsNum) {
        String path = String.format(URL_LAST_QUOTATIONS_FORMAT, "A", currencyKey, quotationsNum);
        ResponseEntity<ExchangeRateTableSingle> response = performGetRequest(path);
        assert response != null;
        return response.getBody();
    }

    @Cacheable(value = "externalApiCalls", key = "{#currencyKey, #quotationsNum}")
    public ExchangeRateTableSingle getSellRates(String currencyKey, int quotationsNum) {
        String path = String.format(URL_LAST_QUOTATIONS_FORMAT, "C", currencyKey, quotationsNum);
        ResponseEntity<ExchangeRateTableSingle> response = performGetRequest(path);
        assert response != null;
        return response.getBody();

    }

    public ResponseEntity<ExchangeRateTableSingle> performGetRequest(String path) {
        try {
            log.debug("Performing GET request to endpoint - " + path);  //to verify caching is working correctly
            return restTemplate.getForEntity(path, ExchangeRateTableSingle.class);
        } catch (HttpClientErrorException ex) {
            handleRequestError(ex);
        }
        return null;
    }

    private void handleRequestError(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {
            case NOT_FOUND -> throw new CurrencyInfoMissingException();
            case INTERNAL_SERVER_ERROR -> throw new ExternalApiException("NBP API is currently unreachable");
            default -> throw new ExternalApiException("Provided parameters are incorrect or not available in NBP API");
        }
    }
}
