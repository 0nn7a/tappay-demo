package com.example.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


public class TapPayClientTest {

    private static final String BASE_URL = "https://sandbox.tappaysdk.com";

    private MockRestServiceServer server;
    private TapPayClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build(); // 一定要在建立 client 之前
        client = new TapPayClient(builder, BASE_URL, "test-partner-key", "test-merchant-id");
    }

    @AfterEach
    void tearDown() {
        server.verify();   // 確認「預期的請求」真的有發生
    }

    @Test
    void refund_sendsExpectedRequest_andReturnsTapPayJson() {
        server.expect(requestTo(BASE_URL + "/tpc/transaction/refund"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-api-key", "test-partner-key"))
                .andExpect(jsonPath("$.partner_key").value("test-partner-key"))
                .andExpect(jsonPath("$.rec_trade_id").value("REC123"))
                .andRespond(withSuccess("""
                        {"status":0,"msg":"Success","refund_id":"RF1","refund_amount":100}
                        """, MediaType.APPLICATION_JSON));

        Map<String, Object> result = client.refund("REC123");

        assertThat(result)
                .containsEntry("status", 0)
                .containsEntry("refund_id", "RF1");
    }

    @Test
    void queryRecord_filtersByRecTradeId() {
        server.expect(requestTo(BASE_URL + "/tpc/transaction/query"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.records_per_page").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.filters.rec_trade_id").value("REC123"))
                .andRespond(withSuccess("""
                        {"status":0,"msg":"Success","number_of_transactions":1,"trade_records":[]}
                        """, MediaType.APPLICATION_JSON));

        Map<String, Object> result = client.queryRecord("REC123");

        assertThat(result).containsEntry("number_of_transactions", 1);
    }

    @Test
    void payByPrime_sendsMerchantIdAndAmount() {
        server.expect(requestTo(BASE_URL + "/tpc/payment/pay-by-prime"))
                .andExpect(jsonPath("$.prime").value("prime123"))
                .andExpect(jsonPath("$.merchant_id").value("test-merchant-id"))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.currency").value("TWD"))
                .andRespond(withSuccess("""
                        {"status":0,"msg":"Success","rec_trade_id":"REC123"}
                        """, MediaType.APPLICATION_JSON));

        Map<String, Object> result = client.payByPrime("prime123", 100, "item", Map.of("name", "n"));

        assertThat(result).containsEntry("rec_trade_id", "REC123");
    }

    @Test
    void refund_whenTapPayReturns500_throwsException() {
        server.expect(requestTo(BASE_URL + "/tpc/transaction/refund"))
                .andRespond(withServerError());

        assertThatThrownBy(() -> client.refund("REC123"))
                .isInstanceOf(HttpServerErrorException.class);
    }
}
