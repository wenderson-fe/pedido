package br.com.food.pedidos.controller.openapi;

import br.com.food.pedidos.dto.PedidoCriacaoDto;
import br.com.food.pedidos.dto.PedidoDto;
import br.com.food.pedidos.dto.StatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public interface PedidoControllerOpenApi {

    @Operation(summary = "Lista todos os pedidos registrados")
    List<PedidoDto> listarTodos();

    @Operation(summary = "Busca um pedido pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<PedidoDto> listarPorId(@Parameter(description = "ID do pedido", example = "1") Long id);

    @Operation(summary = "Registra um novo pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", ref = "BadRequest")
    })

    ResponseEntity<PedidoDto> realizaPedido(PedidoCriacaoDto dto, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Atualiza o status de um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "BadRequest")
    })
    ResponseEntity<PedidoDto> atualizaStatus(@Parameter(description = "ID do pedido") Long id, StatusDto status);

    @Operation(summary = "Aprova o pagamento de um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento aprovado com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<Void> aprovaPagamento(@Parameter(description = "ID do pedido") Long id);

    @Operation(summary = "Endpoint de diagnóstico para verificar a porta da instância", hidden = true)
    String retornaPorta(String porta);

    @Operation(summary = "Exclui um pedido definitivamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso"),
            @ApiResponse(responseCode = "404", ref = "NotFound")
    })
    ResponseEntity<Void> deletarPedido(@Parameter(description = "ID do pedido") Long id);
}