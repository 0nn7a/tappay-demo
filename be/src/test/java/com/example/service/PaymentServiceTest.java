package com.example.service;

import com.example.client.TapPayClient;
import com.example.dto.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    TapPayClient tapPayClient;
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(tapPayClient);
    }

    @Test
    void pay_usesFixedAmountAndProductName() {
        // Arrange：告訴假物件「被這樣呼叫時，回傳這個結果」
        when(tapPayClient
                .payByPrime(eq("prime123"), eq(100), eq("TapPay Sandbox 測試商品"), anyMap()))
                .thenReturn(Map.<String, Object>of("status", 0, "rec_trade_id", "REC123")
        );

        // Act
        Map<String, Object> result = paymentService.pay(new PaymentRequest("prime123"));

        // Assert
        assertThat(result).containsEntry("status", 0).containsEntry("rec_trade_id", "REC123");
        verify(tapPayClient).payByPrime(eq("prime123"), eq(100), eq("TapPay Sandbox 測試商品"), anyMap());
    }
}
