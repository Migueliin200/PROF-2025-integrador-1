package es.upm.grise.prof.curso2025.integrador1;

public class SaldoNegativoException extends Exception {
    private static final long serialVersionUID = 1L;

    public SaldoNegativoException() {
        super("El saldo resultante no puede ser negativo.");
    }

    public SaldoNegativoException(String mensaje) {
        super(mensaje);
    }
}