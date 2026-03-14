package br.com.food.pedidos.controller;

import br.com.food.pedidos.controller.openapi.PedidoControllerOpenApi;
import br.com.food.pedidos.dto.request.PedidoRequestDto;
import br.com.food.pedidos.dto.response.PedidoResponseDto;
import br.com.food.pedidos.dto.request.StatusDto;
import br.com.food.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController implements PedidoControllerOpenApi{

    @Autowired
    private PedidoService service;

    @GetMapping()
    public List<PedidoResponseDto> listarTodos() {
        return service.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> listarPorId(@PathVariable Long id) {
        PedidoResponseDto dto = service.obterPorId(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    public ResponseEntity<PedidoResponseDto> realizaPedido(@RequestBody @Valid PedidoRequestDto dto, UriComponentsBuilder uriBuilder) {
        PedidoResponseDto pedidoRealizado = service.criarPedido(dto);

        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.id()).toUri();

        return ResponseEntity.created(endereco).body(pedidoRealizado);

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDto> atualizaStatus(@PathVariable Long id, @RequestBody @Valid StatusDto status) {
        return ResponseEntity.ok(service.atualizaStatus(id, status));
    }

    @PostMapping("/{id}/pago")
    public ResponseEntity<Void> aprovaPagamento(@PathVariable Long id) {
        service.aprovaPagamentoPedido(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/porta")
    public String retornaPorta(@Value("${local.server.port}") String porta) {
        return String.format("Requisição respondida pela instância executando na porta %s", porta);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        service.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }

}
