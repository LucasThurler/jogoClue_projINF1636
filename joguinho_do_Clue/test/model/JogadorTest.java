package model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class JogadorTest {

    private Jogador jogador;

    @Before
    public void setUp() {
        jogador = new Jogador("Srta. Scarlet");
    }

    @Test
    public void testJogadorIniciaComMaoVazia() {
        assertTrue(jogador.getMao().isEmpty());
    }

    @Test
    public void testReceberCarta() {
        Carta carta = new Carta("Faca", TipoCarta.ARMA);
        jogador.receberCarta(carta);
        assertEquals(1, jogador.getMao().size());
    }

    @Test
    public void testAnotarNoBloco() {
        jogador.anotarNoBloco("Faca eliminada");
        assertEquals(1, jogador.getBlocoDeNotas().size());
        assertEquals("Faca eliminada", jogador.getBlocoDeNotas().get(0));
    }

    @Test
    public void testGetNome() {
        assertEquals("Srta. Scarlet", jogador.getNome());
    }
}