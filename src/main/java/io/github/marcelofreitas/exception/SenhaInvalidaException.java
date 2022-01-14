package io.github.marcelofreitas.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha invalida.");
    }
}
