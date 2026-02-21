package br.com.food.pedidos.dto;

import br.com.food.pedidos.model.Pedido;
import br.com.food.pedidos.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDto(
        Long id,
        LocalDateTime dataHora,
        Status status,
        List<ItemDoPedidoDto> itens
) {
    public static PedidoDto fromEntity(Pedido pedido) {
        return new PedidoDto(
                pedido.getId(),
                pedido.getDataHora(),
                pedido.getStatus(),
                ItemDoPedidoDto.fromEntity(pedido.getItens())
        );
    }
}
