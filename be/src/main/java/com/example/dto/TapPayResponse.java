package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TapPayResponse(
        Integer status,
        String msg,

        @JsonProperty("rec_trade_id")
        String recTradeId
) {}
