package br.com.food.pedidos.service;

import br.com.food.pedidos.dto.request.PedidoRequestDto;
import br.com.food.pedidos.dto.response.PedidoResponseDto;
import br.com.food.pedidos.dto.request.StatusDto;
import br.com.food.pedidos.infra.http.PagamentoClient;
import br.com.food.pedidos.model.ItemDoPedido;
import br.com.food.pedidos.model.Pedido;
import br.com.food.pedidos.model.Status;
import br.com.food.pedidos.model.exception.PedidoException;
import br.com.food.pedidos.repository.PedidoRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository repository;
    private final PagamentoClient pagamentoClient;

    public List<PedidoResponseDto> obterTodos() {
        return repository.findAll().stream()
                .map(PedidoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PedidoResponseDto obterPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return PedidoResponseDto.fromEntity(pedido);
    }

    @Transactional
    public PedidoResponseDto criarPedido(PedidoRequestDto pedidoDto) {
        if (pedidoDto.itens() == null || pedidoDto.itens().isEmpty()) {
            throw new PedidoException("Não é possível criar um pedido sem itens!");
        }

        List<ItemDoPedido> itens = pedidoDto.itens().stream()
                .map(dto -> new ItemDoPedido(dto.quantidade(), dto.descricao()))
                .toList();

        Pedido pedido = new Pedido(Status.REALIZADO, itens);
        Pedido pedidoSalvo = repository.save(pedido);

        return PedidoResponseDto.fromEntity(pedidoSalvo);
    }

    @Transactional
    public PedidoResponseDto atualizaStatus(Long id, StatusDto dto) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + id));

        pedido.setStatus(Status.fromString(dto.status()));
        return PedidoResponseDto.fromEntity(pedido);
    }

    @Transactional
    public void aprovaPagamentoPedido(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + id));

        pedido.setStatus(Status.PAGO);
    }

    @Transactional
    @CircuitBreaker(name = "cancelaPagamento", fallbackMethod = "cancelamentoPendente")
    public void cancelarPedido(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + id));

        if(pedido.getStatus().equals(Status.CANCELADO)) {
            throw new PedidoException("Pedido ja cancelado");
        }

        try {
            pagamentoClient.cancelarPagamento(id);
        }catch (FeignException.NotFound e) {
            //Não foi encontrado um pagamento para esse pedido.
            //Contínua a execução mesmo com o "erro".
            //Registra o aviso
            log.warn("Falha ao cancelar pagamento para o pedido {}. motivo: {}", id, e.getMessage());
        }

        pedido.setStatus(Status.CANCELADO);
    }

    // fallback de "cancelarPagamentoNoMicroservico"
    @Transactional
    public void cancelamentoPendente(Long id, Exception e) {
        log.error("Acionando fallback para pedido {}. Motivo: {}", id, e.getMessage());

        if (e.getClass().equals(PedidoException.class)) {
            throw new PedidoException(e.getMessage());
        }

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + id));

        pedido.setStatus(Status.CANCELAMENTO_PENDENTE);
    }
}
