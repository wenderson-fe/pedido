package br.com.food.pedidos.dto;

import br.com.food.pedidos.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDto(
        Long id,
        LocalDateTime dataHora,
        Status status,
        List<ItemDoPedidoDto> itens
) {
}
