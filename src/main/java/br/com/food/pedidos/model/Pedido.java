package br.com.food.pedidos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dataHora;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido", orphanRemoval = true)
    private List<ItemDoPedido> itens = new ArrayList<>();

    public Pedido(Status status, List<ItemDoPedido> itens) {
        this.dataHora = LocalDateTime.now();
        this.status = status;
        adicionarItens(itens);
    }

    public void adicionarItens(List<ItemDoPedido> itens) {
        itens.forEach(item -> {
            item.setPedido(this);
            this.itens.add(item);
        });
    }
}
