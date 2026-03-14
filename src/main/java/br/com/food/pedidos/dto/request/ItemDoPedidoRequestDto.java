package br.com.food.pedidos.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemDoPedidoRequestDto(
        @NotNull
        @Positive(message = "A quantidade deve ser um número positivo")
        Integer quantidade,
        String descricao
) {
}
