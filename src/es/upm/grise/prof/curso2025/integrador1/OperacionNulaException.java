package es.upm.grise.prof.curso2025.integrador1;

public class OperacionNulaException extends Exception {
    private static final long serialVersionUID = 1L;

    public OperacionNulaException() {
        super("La operación no puede ser nula.");
    }

    public OperacionNulaException(String mensaje) {
        super(mensaje);
    }
}