package model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class JogoTest {

    private List<Jogador> jogadores;

    @Before
    public void setUp() {
        jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Coronel Mustard"));
        jogadores.add(new Jogador("Srta. Scarlet"));
        jogadores.add(new Jogador("Professor Plum"));
    }

    @Test
    public void testSrtaScarletComecaOJogo() {
        Jogo jogo = new Jogo(jogadores);
        assertEquals("Srta. Scarlet", jogo.getJogadorAtual().getNome());
    }

    @Test
    public void testProximoJogador() {
        Jogo jogo = new Jogo(jogadores);
        jogo.proximoJogador();
        assertEquals("Professor Plum", jogo.getJogadorAtual().getNome());
    }

    @Test
    public void testOrdemCircular() {
        Jogo jogo = new Jogo(jogadores);
        jogo.proximoJogador();
        jogo.proximoJogador();
        jogo.proximoJogador();
        // Volta para Srta. Scarlet
        assertEquals("Srta. Scarlet", jogo.getJogadorAtual().getNome());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMenosDe3JogadoresLancaExcecao() {
        List<Jogador> poucos = new ArrayList<>();
        poucos.add(new Jogador("Jogador 1"));
        poucos.add(new Jogador("Jogador 2"));
        new Jogo(poucos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaisDe6JogadoresLancaExcecao() {
        List<Jogador> muitos = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            muitos.add(new Jogador("Jogador " + i));
        }
        new Jogo(muitos);
    }

    @Test
    public void testEnvelopeNaoEstaVazio() {
        Jogo jogo = new Jogo(jogadores);
        assertNotNull(jogo.getBaralho().getEnvelopeConfidencial().get(TipoCarta.SUSPEITO));
        assertNotNull(jogo.getBaralho().getEnvelopeConfidencial().get(TipoCarta.ARMA));
        assertNotNull(jogo.getBaralho().getEnvelopeConfidencial().get(TipoCarta.COMODO));
    }
}