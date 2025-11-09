package com.tenpo.testtenpo.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryResponseDto {
    private Long id;
    private LocalDateTime timestamp;
    private String endpoint;
    private String request;
    private String response;
    private String error;
}