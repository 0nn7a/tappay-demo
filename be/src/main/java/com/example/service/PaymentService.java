package com.example.service;

import com.example.client.TapPayClient;
import com.example.dto.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    private static final int PRODUCT_AMOUNT = 100;
    private static final String PRODUCT_NAME = "TapPay Sandbox 測試商品";
    private static final Map<String, String> SANDBOX_CARDHOLDER = Map.of(
            "phone_number", "0912345678",
            "name", "Sandbox Tester",
            "email", "sandbox@example.com"
    );

    private final TapPayClient tapPayClient;

    public PaymentService(TapPayClient tapPayClient) {
        this.tapPayClient = tapPayClient;
    }

    public Map<String, Object> pay(PaymentRequest request) {
        return tapPayClient.payByPrime(
                request.prime(),
                PRODUCT_AMOUNT,
                PRODUCT_NAME,
                SANDBOX_CARDHOLDER
        );
    }
}
