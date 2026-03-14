package br.com.food.pedidos.dto.response;

import br.com.food.pedidos.model.ItemDoPedido;

import java.util.List;

public record ItemDoPedidoResponseDto(
        Long id,
        Integer quantidade,
        String descricao
) {
    public static ItemDoPedidoResponseDto fromEntity(ItemDoPedido item) {
        return new ItemDoPedidoResponseDto(
                item.getId(),
                item.getQuantidade(),
                item.getDescricao()
        );
    }

    public static List<ItemDoPedidoResponseDto> fromEntity(List<ItemDoPedido> itens) {
        if (itens == null) return List.of();

        return (
                itens.stream()
                        .map(ItemDoPedidoResponseDto::fromEntity)
                        .toList()
        );
    }
}
