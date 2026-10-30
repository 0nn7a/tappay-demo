package com.example.controller;

import com.example.client.TapPayClient;
import com.example.dto.RecordRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "http://localhost:5173")
public class RecordController {

    private final TapPayClient tapPayClient;

    public RecordController(TapPayClient tapPayClient) {
        this.tapPayClient = tapPayClient;
    }

    @PostMapping("/query")
    public Map<String, Object> query(@Valid @RequestBody RecordRequest request) {
        return tapPayClient.queryRecord(request.recordId());
    }

    @PostMapping("/refund")
    public Map<String, Object> refund(@Valid @RequestBody RecordRequest request) {
        return tapPayClient.refund(request.recordId());
    }
}