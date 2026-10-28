package com.example.dto;

public record PaymentResponse(
        Integer status,
        String message,
        String recTradeId,
        boolean success
) {}
