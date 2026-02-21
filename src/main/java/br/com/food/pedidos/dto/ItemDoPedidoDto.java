package br.com.food.pedidos.dto;

import br.com.food.pedidos.model.ItemDoPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ItemDoPedidoDto(
        Long id,

        @NotNull
        @Positive
        Integer quantidade,
        String descricao
) {
    public static ItemDoPedidoDto fromEntity(ItemDoPedido item) {
        return new ItemDoPedidoDto(
                item.getId(),
                item.getQuantidade(),
                item.getDescricao()
        );
    }

    public static List<ItemDoPedidoDto> fromEntity(List<ItemDoPedido> itens) {
        if (itens == null) return List.of();

        return (
                itens.stream()
                        .map(ItemDoPedidoDto::fromEntity)
                        .toList()
        );
    }
}
