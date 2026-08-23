package com.magrega.demo.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        @NotBlank String name,
        String imageUrl
) {}