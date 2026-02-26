package br.com.food.pedidos.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PedidoCriacaoDto(
        @NotEmpty(message = "O pedido deve ter pelo menos um item.")
        List<ItemDoPedidoDto> itens
) {
}
