package br.com.food.pedidos.service;

import br.com.food.pedidos.dto.PedidoCriacaoDto;
import br.com.food.pedidos.dto.PedidoDto;
import br.com.food.pedidos.dto.StatusDto;
import br.com.food.pedidos.model.ItemDoPedido;
import br.com.food.pedidos.model.Pedido;
import br.com.food.pedidos.model.Status;
import br.com.food.pedidos.model.exception.PedidoException;
import br.com.food.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private final ModelMapper modelMapper;

    public List<PedidoDto> obterTodos() {
        return repository.findAll().stream()
                .map(PedidoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PedidoDto obterPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return PedidoDto.fromEntity(pedido);
    }

    @Transactional
    public PedidoDto criarPedido(PedidoCriacaoDto pedidoDto) {
        if (pedidoDto.itens() == null || pedidoDto.itens().isEmpty()) {
            throw new PedidoException("Não é possível criar um pedido sem itens!");
        }

        List<ItemDoPedido> itens = pedidoDto.itens().stream()
                .map(dto -> new ItemDoPedido(dto.quantidade(), dto.descricao()))
                .toList();

        Pedido pedido = new Pedido(Status.REALIZADO, itens);
        Pedido pedidoSalvo = repository.save(pedido);

        return PedidoDto.fromEntity(pedidoSalvo);
    }

    public PedidoDto atualizaStatus(Long id, StatusDto dto) {
        Pedido pedido = repository.porIdComItens(id);

        if (pedido == null) {
            throw new EntityNotFoundException();
        }

        pedido.setStatus(dto.status());
        repository.atualizaStatus(dto.status(), pedido);
        return PedidoDto.fromEntity(pedido);
    }

    public void aprovaPagamentoPedido(Long id) {
        Pedido pedido = repository.porIdComItens(id);

        if (pedido == null) {
            throw new EntityNotFoundException();
        }

        pedido.setStatus(Status.PAGO);
        repository.atualizaStatus(Status.PAGO, pedido);
    }

    @Transactional
    public void deletarPedido(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + id));

        repository.delete(pedido);
    }
}
