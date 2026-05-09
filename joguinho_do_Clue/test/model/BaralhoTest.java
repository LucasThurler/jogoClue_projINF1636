package model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class BaralhoTest {

    private Baralho baralho;

    @Before
    public void setUp() {
        baralho = new Baralho();
    }

    @Test
    public void testBaralhoInicializaComCartasCorretas() {
        assertEquals(6, baralho.getCartas(TipoCarta.SUSPEITO).size());
        assertEquals(6, baralho.getCartas(TipoCarta.ARMA).size());
        assertEquals(9, baralho.getCartas(TipoCarta.COMODO).size());
    }

    @Test
    public void testEnvelopeTemUmaCartaDeCadaTipo() {
        baralho.embaralhar();
        baralho.preencherEnvelope();

        assertNotNull(baralho.getEnvelopeConfidencial().get(TipoCarta.SUSPEITO));
        assertNotNull(baralho.getEnvelopeConfidencial().get(TipoCarta.ARMA));
        assertNotNull(baralho.getEnvelopeConfidencial().get(TipoCarta.COMODO));
    }

    @Test
    public void testEnvelopeRemoveCartaDoBaralho() {
        baralho.embaralhar();
        baralho.preencherEnvelope();

        assertEquals(5, baralho.getCartas(TipoCarta.SUSPEITO).size());
        assertEquals(5, baralho.getCartas(TipoCarta.ARMA).size());
        assertEquals(8, baralho.getCartas(TipoCarta.COMODO).size());
    }

    @Test
    public void testDistribuicaoSemCartasRepetidas() {
        baralho.embaralhar();
        baralho.preencherEnvelope();

        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Jogador 1"));
        jogadores.add(new Jogador("Jogador 2"));
        jogadores.add(new Jogador("Jogador 3"));

        baralho.distribuirCartas(jogadores);

        // Verifica que nenhuma carta aparece em duas mãos diferentes
        List<Carta> todasCartas = new ArrayList<>();
        for (Jogador j : jogadores) {
            for (Carta c : j.getMao()) {
                assertFalse(todasCartas.contains(c));
                todasCartas.add(c);
            }
        }
    }

    @Test
    public void testDistribuicaoUsaTodasAsCartas() {
        baralho.embaralhar();
        baralho.preencherEnvelope();

        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Jogador 1"));
        jogadores.add(new Jogador("Jogador 2"));
        jogadores.add(new Jogador("Jogador 3"));

        baralho.distribuirCartas(jogadores);

        int totalCartas = 0;
        for (Jogador j : jogadores) {
            totalCartas += j.getMao().size();
        }

        // 21 cartas totais - 3 do envelope = 18
        assertEquals(18, totalCartas);
    }
}