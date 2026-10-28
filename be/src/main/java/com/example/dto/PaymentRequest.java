package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(
        @NotBlank(message = "prime 不可為空") String prime,
        int amount
) {}
