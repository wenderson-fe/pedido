package br.com.food.pedidos.infra.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("pagamento-api")
public interface PagamentoClient {
    @RequestMapping(method = RequestMethod.POST, value = "/pagamento/cancelar-por-pedido/{pedidoId}")
    void cancelarPagamento(@PathVariable Long pedidoId);
}
