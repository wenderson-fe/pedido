package br.com.food.pedidos.model;

public enum Status {
    REALIZADO("Realizado"),
    CANCELADO("Cancelado"),
    PAGO("Pago"),
    NAO_AUTORIZADO("NaoAutorizado"),
    CONFIRMADO("Confirmado"),
    PRONTO("Pronto"),
    SAIU_PARA_ENTREGA("SaiuParaEntrega"),
    ENTREGUE("Entregue");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public static Status fromString(String text) {
        for (Status status : Status.values()) {
            if (status.descricao.equalsIgnoreCase(text)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Status desconhecido: " + text);
    }
}
