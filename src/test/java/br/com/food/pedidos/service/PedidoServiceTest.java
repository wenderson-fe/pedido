package br.com.food.pedidos.service;

import br.com.food.pedidos.dto.request.ItemDoPedidoRequestDto;
import br.com.food.pedidos.dto.request.PedidoRequestDto;
import br.com.food.pedidos.dto.request.StatusDto;
import br.com.food.pedidos.dto.response.ItemDoPedidoResponseDto;
import br.com.food.pedidos.dto.response.PedidoResponseDto;
import br.com.food.pedidos.infra.http.PagamentoClient;
import br.com.food.pedidos.model.ItemDoPedido;
import br.com.food.pedidos.model.Pedido;
import br.com.food.pedidos.model.Status;
import br.com.food.pedidos.model.exception.PedidoException;
import br.com.food.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    @Mock
    private PedidoRepository repository;

    @Mock
    private PagamentoClient client;

    @InjectMocks
    private PedidoService service;

    @Captor
    private ArgumentCaptor<Pedido> pedidoCaptor;

    @Test
    @DisplayName("Deve disparar uma exception quando itens de pedido for vazio")
    void deveDispararExceptionSemItens() {
        PedidoRequestDto pedidoVazio = new PedidoRequestDto(List.of());

        // Valida exception e mensagem ao criar pedido sem itens.
        PedidoException ex = assertThrows(
                PedidoException.class,
                () -> service.criarPedido(pedidoVazio));
        assertEquals(
                "Não é possível criar um pedido sem itens!",
                ex.getMessage());
    }

    @Test
    @DisplayName("Deve salvar o pedido e retornar o DTO corretamente quando os dados forem válidos")
    void deveCriarPedidoComSucesso() {
        PedidoRequestDto pedidoRequest = criaPedidoRequest(
                List.of(new ItemDoPedidoRequestDto(10, "item"))
        );
        Pedido pedidoSalvo = criaPedido();
        when(repository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        PedidoResponseDto pedidoResponse = service.criarPedido(pedidoRequest);

        // Verifica se repository esta sendo chamado 1 vez com o parâmetro correto.
        verify(repository, times(1)).save(any(Pedido.class));

        // Verifica se as informações do Pedido retornado são iguais às do Pedido salvo.
        assertAll("Validação dos campos do PedidoResponse",
                () -> assertEquals(pedidoSalvo.getId(), pedidoResponse.id()),
                () -> assertEquals(pedidoSalvo.getStatus(), pedidoResponse.status()),
                () -> assertEquals(pedidoSalvo.getDataHora(), pedidoResponse.dataHora()),
                () -> assertEquals(1, pedidoResponse.itens().size())
        );
    }

    @Test
    @DisplayName("O status deve sempre ser igual a REALIZADO para um novo pedido")
    void deveAdicionarStatusRealizado() {
        PedidoRequestDto pedidoComItens = criaPedidoRequest(
                List.of(new ItemDoPedidoRequestDto(10, "item"))
        );
        // Pedido usado no stub do save. Evita exception na conversão para PedidoResponseDto.
        Pedido pedido = criaPedido();

        when(repository.save(any(Pedido.class))).thenReturn(pedido);

        service.criarPedido(pedidoComItens);

        // Verifica se repository foi chamado 1 vez e captura o Pedido passado.
        Mockito.verify(repository, times(1)).save(pedidoCaptor.capture());
        Pedido pedidoEnviadoParaSalvar = pedidoCaptor.getValue();

        // Garante que o pedido enviado ao repository esteja com status REALIZADO.
        assertEquals(Status.REALIZADO, pedidoEnviadoParaSalvar.getStatus());
    }

    @Test
    @DisplayName("Deve disparar exception quando pedido não existir")
    void deveDispararExceptionIdInvalido() {
        Long id = 1L;
        StatusDto novoStatus = new StatusDto("Cancelado");
        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.atualizaStatus(id, novoStatus));

        assertEquals(
                "Pedido não encontrado com id: " + id,
                ex.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar o status do pedido quando informações estiverem corretas")
    void deveAtualizarStatus() {
        StatusDto novoStatus = new StatusDto("Cancelado");
        Pedido pedido = criaPedido();
        when(repository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        PedidoResponseDto response = service.atualizaStatus(pedido.getId(), novoStatus);

        assertEquals(Status.CANCELADO, response.status());
    }

    PedidoRequestDto criaPedidoRequest(List<ItemDoPedidoRequestDto> itens) {
        return new PedidoRequestDto(itens);
    }

    // Retorna pedido com valores informados.
    Pedido criaPedido(Long id, LocalDateTime time, Status status, List<ItemDoPedido> itens) {
        return new Pedido(id, time, status, itens);
    }

    //Retorna pedido com valores padrão.
    //Método utilizado quando valores de pedido não são importantes.
    Pedido criaPedido() {
        return new Pedido(
                1L,
                LocalDateTime.now(),
                Status.REALIZADO,
                List.of(new ItemDoPedido(10, "item"))
        );
    }

}