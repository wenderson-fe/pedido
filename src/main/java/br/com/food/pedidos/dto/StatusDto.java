package br.com.food.pedidos.dto;

import br.com.food.pedidos.model.Status;
import jakarta.validation.constraints.NotNull;

public record StatusDto(
        @NotNull
        Status status
) {
}
