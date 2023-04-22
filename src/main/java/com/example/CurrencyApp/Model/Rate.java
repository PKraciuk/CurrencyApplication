package com.example.CurrencyApp.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Rate implements Serializable {
    @JsonProperty("no")
    private String no;
    @JsonProperty("effectiveDate")
    private LocalDate effectiveDate;
    @JsonProperty("mid")
    private Double mid;
    @JsonProperty("bid")
    private Double bid;
    @JsonProperty("ask")
    private Double ask;

}

