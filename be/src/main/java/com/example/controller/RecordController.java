package com.example.controller;

import com.example.dto.RecordRequest;
import com.example.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "http://localhost:5173")
public class RecordController {

    private final PaymentService paymentService;

    public RecordController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/query")
    public Map<String, Object> query(@Valid @RequestBody RecordRequest request) {
        return paymentService.query(request);
    }

    @PostMapping("/refund")
    public Map<String, Object> refund(@Valid @RequestBody RecordRequest request) {
        return paymentService.refund(request);
    }
}