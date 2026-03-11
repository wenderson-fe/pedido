package br.com.food.pedidos.dto;

import jakarta.validation.constraints.NotNull;

public record StatusDto(
        @NotNull
        String status
) {
}
