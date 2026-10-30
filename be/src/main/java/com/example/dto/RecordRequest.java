package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordRequest(
        @NotBlank(message = "record_id 不可為空") String recordId
) {}