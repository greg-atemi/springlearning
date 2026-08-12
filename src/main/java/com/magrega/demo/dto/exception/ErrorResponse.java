package com.magrega.demo.dto.exception;

public record ErrorResponse(
        String code,
        String message
) {
}