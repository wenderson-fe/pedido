package br.com.food.pedidos.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "item_do_pedido")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemDoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    private String descricao;

    @ManyToOne(optional = false)
    private Pedido pedido;

    public ItemDoPedido(Integer quantidade, String descricao) {
        this.quantidade = quantidade;
        this.descricao = descricao;
    }
}
