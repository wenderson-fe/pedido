package br.com.food.pedidos.dto.response;

import br.com.food.pedidos.model.Pedido;
import br.com.food.pedidos.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDto(
        Long id,
        LocalDateTime dataHora,
        Status status,
        List<ItemDoPedidoResponseDto> itens
) {
    public static PedidoResponseDto fromEntity(Pedido pedido) {
        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getDataHora(),
                pedido.getStatus(),
                ItemDoPedidoResponseDto.fromEntity(pedido.getItens())
        );
    }
}
