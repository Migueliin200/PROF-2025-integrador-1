package es.upm.grise.prof.curso2025.integrador1;

import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {

    String numeroCuenta;
    double saldoInicial;
    boolean admiteDescubierto;
    List<Operacion> operaciones;

    // Constructor
    public CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldoInicial = saldoInicial;
        this.admiteDescubierto = false; // no se indica que se pase por parámetro
        this.operaciones = new ArrayList<>(); // la lista se inicializa vacía
    }

    // Añade una operación a la lista operaciones
    public void addOperacion(Operacion operacion) throws OperacionNulaException, OperacionDuplicadaException {
        // 1) No puede ser null
        if (operacion == null) {
            throw new OperacionNulaException();
        }

        // 2) No puede haber dos operaciones con el mismo id
        for (Operacion op : operaciones) {
            if (op.getId() == operacion.getId()) {
                throw new OperacionDuplicadaException();
            }
        }

        // 3) Si pasa las comprobaciones, añadir
        operaciones.add(operacion);
    }

    // Calcula el saldo actual a partir del saldo inicial y las operaciones
    public double getSaldoActual() throws SaldoNegativoException {
        double saldo = saldoInicial;

        // sumar todos los importes
        for (Operacion op : operaciones) {
            saldo += op.getImporte();
        }

        // comprobar si puede o no ser negativo
        if (!admiteDescubierto && saldo < 0) {
            throw new SaldoNegativoException();
        }

        // redondear a dos decimales
        saldo = Math.round(saldo * 100.0) / 100.0;

        return saldo;
    }
}

