package com.example.service;

import com.example.client.TapPayClient;
import com.example.dto.PaymentRequest;
import com.example.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final TapPayClient tapPayClient;

    public PaymentService(TapPayClient tapPayClient) {
        this.tapPayClient = tapPayClient;
    }

    public PaymentResponse pay(PaymentRequest request) {
        return tapPayClient.pay(
                request.prime(),
                request.amount(),
                "TapPay Sandbox 測試商品"
        );
    }
}
