package com.example.service;

import com.example.client.TapPayClient;
import com.example.dto.PaymentRequest;
import com.example.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static final int PRODUCT_AMOUNT = 100;
    private static final String PRODUCT_NAME = "TapPay Sandbox 測試商品";

    private final TapPayClient tapPayClient;

    public PaymentService(TapPayClient tapPayClient) {
        this.tapPayClient = tapPayClient;
    }

    public PaymentResponse pay(PaymentRequest request) {
        return tapPayClient.pay(
                request.prime(),
                PRODUCT_AMOUNT,
                PRODUCT_NAME
        );
    }
}
