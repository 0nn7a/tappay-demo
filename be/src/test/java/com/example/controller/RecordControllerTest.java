package com.example.controller;

import com.example.client.TapPayClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecordController.class)
class RecordControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TapPayClient tapPayClient;

    @Test
    void query_returnsTapPayJsonAsIs() throws Exception {
        when(tapPayClient.queryRecord("REC123"))
                .thenReturn(Map.of("status", 0, "msg", "Success"));

        mockMvc.perform(post("/api/records/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recordId\":\"REC123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    void refund_returnsTapPayJsonAsIs() throws Exception {
        when(tapPayClient.refund("REC123"))
                .thenReturn(Map.of("status", 0, "refund_id", "RF1"));

        mockMvc.perform(post("/api/records/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recordId\":\"REC123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refund_id").value("RF1"));
    }

    @Test
    void query_blankRecordId_returns400_andDoesNotCallTapPay() throws Exception {
        mockMvc.perform(post("/api/records/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recordId\":\"   \"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tapPayClient);   // 驗證失敗時，不該打到 TapPay
    }
}