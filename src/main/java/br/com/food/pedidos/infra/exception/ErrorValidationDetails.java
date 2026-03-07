package br.com.food.pedidos.infra.exception;

public record ErrorValidationDetails(String field, String message)
{
    public static ErrorValidationDetails validationDetails(String field, String message) {
        return new ErrorValidationDetails(
                field,
                message
        );
    }
}
