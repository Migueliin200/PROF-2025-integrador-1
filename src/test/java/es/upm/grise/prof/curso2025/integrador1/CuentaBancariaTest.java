package es.upm.grise.prof.curso2025.integrador1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CuentaBancariaTest {

    CuentaBancaria cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new CuentaBancaria("ES00-TEST", 100.00);
    }

    @Test
    @DisplayName("El constructor deja la lista de operaciones vacía")
    void constructorInicializaListaVacia() {
        assertNotNull(cuenta.operaciones, "La lista de operaciones debe inicializarse");
        assertTrue(cuenta.operaciones.isEmpty(), "La lista debe empezar vacía");
    }

    @Test
    @DisplayName("addOperacion(null) lanza OperacionNulaException")
    void addOperacionNullLanza() {
        assertThrows(OperacionNulaException.class, () -> cuenta.addOperacion(null));
    }

    @Test
    @DisplayName("No se permite añadir dos operaciones con el mismo ID")
    void operacionesDuplicadasLanzanExcepcion() throws Exception {
        Operacion op1 = mock(Operacion.class);
        when(op1.getId()).thenReturn(1L);
        when(op1.getImporte()).thenReturn(10.0);

        Operacion op2 = mock(Operacion.class);
        when(op2.getId()).thenReturn(1L); // mismo id
        when(op2.getImporte()).thenReturn(5.0);

        cuenta.addOperacion(op1);
        assertThrows(OperacionDuplicadaException.class, () -> cuenta.addOperacion(op2));
    }

    @Test
    @DisplayName("addOperacion válida se añade a la lista")
    void addOperacionValidaSeAñade() throws Exception {
        Operacion op = mock(Operacion.class);
        when(op.getId()).thenReturn(2L);
        when(op.getImporte()).thenReturn(5.5);

        cuenta.addOperacion(op);

        assertEquals(1, cuenta.operaciones.size());
        assertSame(op, cuenta.operaciones.get(0));
    }

    @Test
    @DisplayName("getSaldoActual suma importes y redondea a 2 decimales")
    void getSaldoActualSumaYRedondea() throws Exception {
        // saldoInicial = 100.00
        Operacion opA = mock(Operacion.class);
        when(opA.getId()).thenReturn(10L);
        when(opA.getImporte()).thenReturn(0.105); // 3 decimales

        Operacion opB = mock(Operacion.class);
        when(opB.getId()).thenReturn(11L);
        when(opB.getImporte()).thenReturn(0.104); // 3 decimales

        cuenta.addOperacion(opA);
        cuenta.addOperacion(opB);

        // 100 + 0.105 + 0.104 = 100.209 → redondeado = 100.21
        double saldo = cuenta.getSaldoActual();
        assertEquals(100.21, saldo, 0.0000001);
    }

    @Test
    @DisplayName("Si no admite descubierto y el saldo queda negativo, lanza SaldoNegativoException")
    void saldoNegativoNoPermitidoLanza() throws Exception {
        Operacion gasto = mock(Operacion.class);
        when(gasto.getId()).thenReturn(20L);
        when(gasto.getImporte()).thenReturn(-150.0); // 100 + (-150) = -50

        cuenta.addOperacion(gasto);

        assertThrows(SaldoNegativoException.class, () -> cuenta.getSaldoActual());
    }

    @Test
    @DisplayName("Si admite descubierto, el saldo negativo es válido")
    void saldoNegativoPermitidoSiAdmiteDescubierto() throws Exception {
        // Está en el mismo paquete, así que podemos configurar el flag directo
        cuenta.admiteDescubierto = true;

        Operacion gasto = mock(Operacion.class);
        when(gasto.getId()).thenReturn(21L);
        when(gasto.getImporte()).thenReturn(-150.0); // 100 + (-150) = -50 → permitido

        cuenta.addOperacion(gasto);

        double saldo = cuenta.getSaldoActual();
        assertEquals(-50.00, saldo, 0.0000001);
    }

    @Test
    @DisplayName("Sin operaciones, el saldo actual es el saldo inicial redondeado")
    void sinOperacionesSaldoEsInicial() throws Exception {
        double saldo = cuenta.getSaldoActual();
        assertEquals(100.00, saldo, 0.0000001);
    }
}
