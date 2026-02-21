package br.com.food.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemDoPedidoDto(
        Long id,

        @NotNull
        @Positive
        Integer quantidade,
        String descricao

) {

}
