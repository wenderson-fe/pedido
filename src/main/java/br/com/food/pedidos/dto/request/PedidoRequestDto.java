package br.com.food.pedidos.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PedidoRequestDto(
        @NotEmpty(message = "O pedido deve ter pelo menos um item.")
        List<@Valid ItemDoPedidoRequestDto> itens
) {
}
