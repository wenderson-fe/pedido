package br.com.food.pedidos.infra.exception;

import br.com.food.pedidos.model.exception.PedidoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarErroRecursoNaoEncontrado(EntityNotFoundException e) {
        String mensagem = (e.getMessage() == null || e.getMessage().isBlank())
                ? "Recurso não encontrado" : e.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.detalhesDoErro(404, LocalDateTime.now(), mensagem));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> tratarErroDeArgumento(IllegalArgumentException e) {
        String mensagem = (e.getMessage() == null || e.getMessage().isBlank())
                ? "Recurso não encontrado" : e.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.detalhesDoErro(400, LocalDateTime.now(), mensagem));
    }

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<ErrorResponse> tratarRegraDeNogocio(PedidoException e) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponse.detalhesDoErro(422, LocalDateTime.now(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarErroEntradaInvalida(MethodArgumentNotValidException e) {
        List<ErrorValidationDetails> validationDetails = e.getFieldErrors().stream()
                .map(error -> ErrorValidationDetails.validationDetails(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.detalhesDoErro(400, LocalDateTime.now(), "Dados inválidos", validationDetails));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> tratarErroLeituraJson(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.detalhesDoErro(400, LocalDateTime.now(), "Erro na leitura do corpo da requisição (JSON malformado)"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> tratarErroServidor(Exception e) {
        // Mostra o erro real no console
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.detalhesDoErro(500, LocalDateTime.now(), "Ocorreu um erro interno no servidor. Tente novamente mais tarde."));
    }
}
