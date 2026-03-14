package br.com.food.pedidos.dto.request;

import jakarta.validation.constraints.NotNull;

public record StatusDto(
        @NotNull
        String status
) {
}
