package com.example.CurrencyApp.Controller;

import com.example.CurrencyApp.Dto.CurrencyExtremaResponseDto;
import com.example.CurrencyApp.Service.CurrencyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/current-rate/{currencyCode}/{date}")
    public Double getCurrentCurrencyRate(@PathVariable("currencyCode") String currencyCode,
                                         @PathVariable("date") String date) {
        return currencyService.getCurrentRateByCodeAndDate(currencyCode, date);
    }

    @GetMapping("/extrema/{currencyCode}/{n}")
    public CurrencyExtremaResponseDto getCurrencyExtrema(@PathVariable("currencyCode") String currencyCode,
                                                         @PathVariable("n") int n) {
        return currencyService.getCurrencyExtrema(currencyCode, n);
    }

    @GetMapping("/major-difference/{currencyCode}/{n}")
    public Double getMajorDifference(@PathVariable("currencyCode") String currencyCode,
                                     @PathVariable("n") int n) {
        return currencyService.getMajorDifference(currencyCode, n);
    }
}

