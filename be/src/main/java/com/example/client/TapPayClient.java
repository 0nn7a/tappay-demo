package com.example.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
            @Value("${tappay.base-url}") String baseUrl,
            @Value("${tappay.partner-key}") String partnerKey,
            @Value("${tappay.merchant-id}") String merchantId) {
        this.partnerKey = partnerKey;
        this.merchantId = merchantId;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", partnerKey)
                .build();
    }

    public Map<String, Object> payByPrime(String prime,
                                      int amount,
                                      String details,
                                      Map<String, String> cardholder) {
        return post("/tpc/payment/pay-by-prime", Map.of(
                "prime", prime,
                "partner_key", partnerKey,
                "merchant_id", merchantId,
                "amount", amount,
                "currency", "TWD",
                "details", details,
                "cardholder", cardholder
        ));
    }

    public Map<String, Object> queryRecord(String recTradeId) {
        return post("/tpc/transaction/query", Map.of(
                "partner_key", partnerKey,
                "records_per_page", 1,
                "page", 0,
                "filters", Map.of("rec_trade_id", recTradeId)
        ));
    }

    public Map<String, Object> refund(String recTradeId) {
        return post("/tpc/transaction/refund", Map.of(
                "partner_key", partnerKey,
                "rec_trade_id", recTradeId
        ));
    }

    private Map<String, Object> post(String path, Map<String, Object> payload) {
        Map<String, Object> res = restClient.post()
                .uri(path)
                .body(payload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        if (res == null) throw new IllegalStateException("TapPay 無回傳結果");
        return res;
    }
}
