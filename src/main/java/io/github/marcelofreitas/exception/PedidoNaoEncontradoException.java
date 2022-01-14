package io.github.marcelofreitas.exception;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException() {
        super("Pedido nao encontrado.");
    }
}
