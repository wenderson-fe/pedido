package br.com.food.pedidos.controller.openapi;

import br.com.food.pedidos.dto.request.PedidoRequestDto;
import br.com.food.pedidos.dto.response.PedidoResponseDto;
import br.com.food.pedidos.dto.request.StatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public interface PedidoControllerOpenApi {

    @Operation(summary = "Lista todos os pedidos registrados")
    List<PedidoResponseDto> listarTodos();

    @Operation(summary = "Busca um pedido pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<PedidoResponseDto> listarPorId(@Parameter(description = "ID do pedido", example = "1") Long id);

    @Operation(summary = "Registra um novo pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", ref = "BadRequest")
    })

    ResponseEntity<PedidoResponseDto> realizaPedido(PedidoRequestDto dto, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Atualiza o status de um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "BadRequest")
    })
    ResponseEntity<PedidoResponseDto> atualizaStatus(@Parameter(description = "ID do pedido") Long id, StatusDto status);

    @Operation(summary = "Aprova o pagamento de um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento aprovado com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<Void> aprovaPagamento(@Parameter(description = "ID do pedido") Long id);

    @Operation(summary = "Endpoint de diagnóstico para verificar a porta da instância", hidden = true)
    String retornaPorta(String porta);

    @Operation(summary = "Cancela um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "422", ref = "UnprocessableEntity")
    })
    ResponseEntity<Void> cancelarPedido(@Parameter(description = "ID do pedido") Long id);
}