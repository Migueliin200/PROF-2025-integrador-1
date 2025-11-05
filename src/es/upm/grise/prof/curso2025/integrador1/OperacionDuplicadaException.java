package es.upm.grise.prof.curso2025.integrador1;

public class OperacionDuplicadaException extends Exception {
    private static final long serialVersionUID = 1L;

    public OperacionDuplicadaException() {
        super("No se puede añadir una operación duplicada (mismo ID).");
    }

    public OperacionDuplicadaException(String mensaje) {
        super(mensaje);
    }
}