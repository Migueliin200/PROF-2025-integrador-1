package es.upm.grise.prof.curso2025.integrador1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class CuentaBancariaSaldoTest {

    // Constantes (evitan Magic Number)
    private static final String NUM_CUENTA = "ES00-TEST";
    private static final double SALDO_INICIAL = 100.00;

    private static final long OP_INGRESO_ID = 10L;
    private static final long OP_GASTO_ID   = 11L;

    private static final double IMPORTE_INGRESO = 20.30;
    private static final double IMPORTE_GASTO   = -5.10;

    @Test
    void getSaldoActual_devuelve_saldo_correcto() throws Exception {
        // Arrange
        CuentaBancaria cuenta = new CuentaBancaria(NUM_CUENTA, SALDO_INICIAL);

        Operacion ingreso = mock(Operacion.class);
        when(ingreso.getId()).thenReturn(OP_INGRESO_ID);
        when(ingreso.getImporte()).thenReturn(IMPORTE_INGRESO);

        Operacion gasto = mock(Operacion.class);
        when(gasto.getId()).thenReturn(OP_GASTO_ID);
        when(gasto.getImporte()).thenReturn(IMPORTE_GASTO);

        cuenta.addOperacion(ingreso);
        cuenta.addOperacion(gasto);

        // Expected sin números mágicos: saldoInicial + sum(importes), redondeado a 2 decimales
        double esperado = Math.round(
            (SALDO_INICIAL + IMPORTE_INGRESO + IMPORTE_GASTO) * 100.0
        ) / 100.0;

        // Act
        double real = cuenta.getSaldoActual();

        // Assert
        assertEquals(esperado, real, 1e-9);
    }
}
