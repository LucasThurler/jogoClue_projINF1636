package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class DadoTest {

    @Test
    public void testLancarRetornaValorNoIntervalo() {
        Dado d = new Dado(6);
        int valor = d.lancar();
        assertTrue(valor >= 1 && valor <= 6);
    }

    @Test
    public void testGetValorAtualIgualAoLancado() {
        Dado d = new Dado(6);
        int lancado = d.lancar();
        assertEquals(lancado, d.getValorAtual());
    }

    @Test
    public void testLancarMultiplasVezes() {
        Dado d = new Dado(6);
        for (int i = 0; i < 100; i++) {
            int valor = d.lancar();
            assertTrue(valor >= 1 && valor <= 6);
        }
    }
}