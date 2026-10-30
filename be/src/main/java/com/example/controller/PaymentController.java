package com.example.controller;

import com.example.dto.PaymentRequest;
import com.example.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Map<String, Object> pay(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }
}
