package es.upm.grise.prof.curso2025.integrador1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CuentaBancariaMockitoTest {

    // Constantes para evitar Magic Numbers
    private static final String NUM_CUENTA = "ES00-TEST";
    private static final double SALDO_INICIAL = 100.00;

    private static final long OP1_ID = 1L;
    private static final long OP2_ID = 2L;
    private static final long OP_DUP_ID = 99L;

    private static final double IMPORTE_INGRESO = 25.30;
    private static final double IMPORTE_GASTO   = -5.10;
    private static final double IMPORTE_REDONDEO_A = 0.105; // 3 decimales
    private static final double IMPORTE_REDONDEO_B = 0.104; // 3 decimales
    private static final double IMPORTE_GASTO_GRANDE = -150.0;

    private CuentaBancaria cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new CuentaBancaria(NUM_CUENTA, SALDO_INICIAL);
    }

    @Test
    @DisplayName("Constructor: la lista de operaciones se inicializa vacía")
    void constructorInicializaListaVacia() {
        assertNotNull(cuenta.operaciones, "La lista de operaciones debe inicializarse");
        assertTrue(cuenta.operaciones.isEmpty(), "La lista debe empezar vacía");
    }

    @Test
    @DisplayName("addOperacion(null) → OperacionNulaException")
    void addOperacionNullLanza() {
        assertThrows(OperacionNulaException.class, () -> cuenta.addOperacion(null));
    }

    @Test
    @DisplayName("addOperacion: no permite dos operaciones con el mismo ID")
    void operacionesDuplicadasLanzanExcepcion() throws Exception {
        Operacion op1 = mock(Operacion.class);
        when(op1.getId()).thenReturn(OP_DUP_ID);
        when(op1.getImporte()).thenReturn(IMPORTE_INGRESO);

        Operacion op2 = mock(Operacion.class);
        when(op2.getId()).thenReturn(OP_DUP_ID); // mismo id
        when(op2.getImporte()).thenReturn(IMPORTE_GASTO);

        cuenta.addOperacion(op1);
        assertThrows(OperacionDuplicadaException.class, () -> cuenta.addOperacion(op2));
    }

    @Test
    @DisplayName("addOperacion válida se añade a la lista")
    void addOperacionValidaSeAñade() throws Exception {
        Operacion op = mock(Operacion.class);
        when(op.getId()).thenReturn(OP1_ID);
        when(op.getImporte()).thenReturn(IMPORTE_INGRESO);

        cuenta.addOperacion(op);

        assertEquals(1, cuenta.operaciones.size());
        assertSame(op, cuenta.operaciones.get(0));
    }

    @Test
    @DisplayName("getSaldoActual: suma importes y redondea a 2 decimales")
    void getSaldoActualSumaYRedondea() throws Exception {
        Operacion opA = mock(Operacion.class);
        when(opA.getId()).thenReturn(OP1_ID);
        when(opA.getImporte()).thenReturn(IMPORTE_REDONDEO_A);

        Operacion opB = mock(Operacion.class);
        when(opB.getId()).thenReturn(OP2_ID);
        when(opB.getImporte()).thenReturn(IMPORTE_REDONDEO_B);

        cuenta.addOperacion(opA);
        cuenta.addOperacion(opB);

        double esperado = Math.round((SALDO_INICIAL + IMPORTE_REDONDEO_A + IMPORTE_REDONDEO_B) * 100.0) / 100.0;
        assertEquals(esperado, cuenta.getSaldoActual(), 1e-9);
    }

    @Test
    @DisplayName("getSaldoActual: si no admite descubierto y queda negativo → SaldoNegativoException")
    void saldoNegativoNoPermitidoLanza() throws Exception {
        Operacion gasto = mock(Operacion.class);
        when(gasto.getId()).thenReturn(OP1_ID);
        when(gasto.getImporte()).thenReturn(IMPORTE_GASTO_GRANDE); // 100 + (-150) = -50

        cuenta.addOperacion(gasto);

        assertThrows(SaldoNegativoException.class, () -> cuenta.getSaldoActual());
    }

    @Test
    @DisplayName("getSaldoActual: si admite descubierto, permite saldo negativo")
    void saldoNegativoPermitidoSiAdmiteDescubierto() throws Exception {
        cuenta.admiteDescubierto = true;

        Operacion gasto = mock(Operacion.class);
        when(gasto.getId()).thenReturn(OP1_ID);
        when(gasto.getImporte()).thenReturn(IMPORTE_GASTO_GRANDE);

        cuenta.addOperacion(gasto);

        double esperado = Math.round((SALDO_INICIAL + IMPORTE_GASTO_GRANDE) * 100.0) / 100.0;
        assertEquals(esperado, cuenta.getSaldoActual(), 1e-9);
    }

    @Test
    @DisplayName("Sin operaciones, el saldo actual es el saldo inicial (redondeado)")
    void sinOperacionesSaldoEsInicial() throws Exception {
        double esperado = Math.round(SALDO_INICIAL * 100.0) / 100.0;
        assertEquals(esperado, cuenta.getSaldoActual(), 1e-9);
    }
}
