package com.example.client;

import com.example.dto.PaymentResponse;
import com.example.dto.TapPayResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class TapPayClient {
    private final String partnerKey;
    private final String merchantId;
    private final RestClient restClient;

    public TapPayClient(
            @Value("${tappay.api-url}") String apiUrl,
            @Value("${tappay.partner-key}") String partnerKey,
            @Value("${tappay.merchant-id}") String merchantId) {
        this.partnerKey = partnerKey;
        this.merchantId = merchantId;
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", partnerKey)
                .build();
    }

    public PaymentResponse pay(String prime, int amount, String details) {
        Map<String, Object> payload = Map.of(
                "prime", prime,
                "partner_key", partnerKey,
                "merchant_id", merchantId,
                "amount", amount,
                "currency", "TWD",
                "details", details,
                "cardholder", Map.of(
                        "phone_number", "0912345678",
                        "name", "Sandbox Tester",
                        "email", "sandbox@example.com"
                )
        );

        TapPayResponse response = restClient.post()
                .body(payload)
                .retrieve()
                .body(TapPayResponse.class);

        if (response == null) throw new IllegalStateException("TapPay 無回傳結果");

        Integer status = response.status();
        String message = response.msg() != null ? response.msg() : "無交易訊息";
        String recTradeId = response.recTradeId();
        boolean success = Integer.valueOf(0).equals(status);

        return new PaymentResponse(status, message, recTradeId, success);
    }
}
