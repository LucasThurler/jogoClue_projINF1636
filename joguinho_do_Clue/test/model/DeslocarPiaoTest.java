package model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DeslocarPiaoTest {

    // Deslocamento válido: jogador se move para casa alcançável
    @Test
    public void testDeslocarParaCasaValida() {
        Jogador jogador = new Jogador("Srta. Scarlet");
        jogador.setPosicaoAtual(1);
        DeslocarPiao dp = new DeslocarPiao();
        Set<Integer> casas = new HashSet<>(Arrays.asList(2, 3, 4));

        boolean resultado = dp.deslocarPiao(jogador, 3, casas);

        assertTrue(resultado);
        assertEquals(3, jogador.getPosicaoAtual());
    }

    // Deslocamento inválido: casa não está entre as alcançáveis
    @Test
    public void testDeslocarParaCasaInvalida() {
        Jogador jogador = new Jogador("Srta. Scarlet");
        jogador.setPosicaoAtual(1);
        DeslocarPiao dp = new DeslocarPiao();
        Set<Integer> casas = new HashSet<>(Arrays.asList(2, 3, 4));

        boolean resultado = dp.deslocarPiao(jogador, 99, casas);

        assertFalse(resultado);
        assertEquals(1, jogador.getPosicaoAtual());
    }

    // Conjunto vazio: nenhum movimento possível
    @Test
    public void testCasasAlcancaveisVazio() {
        Jogador jogador = new Jogador("Srta. Scarlet");
        jogador.setPosicaoAtual(1);
        DeslocarPiao dp = new DeslocarPiao();
        Set<Integer> casas = new HashSet<>();

        boolean resultado = dp.deslocarPiao(jogador, 2, casas);

        assertFalse(resultado);
        assertEquals(1, jogador.getPosicaoAtual());
    }

    // Integração com MapeaCasas: fluxo completo de uma jogada
    @Test
    public void testIntegracaoComMapeaCasas() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(1, Arrays.asList(2, 3));
        adj.put(2, Arrays.asList(1, 4));
        adj.put(3, Arrays.asList(1));
        adj.put(4, Arrays.asList(2));

        MapeaCasas m = new MapeaCasas(adj);
        Set<Integer> casas = m.mapearCasas(new int[]{2}, 1);

        Jogador jogador = new Jogador("Srta. Scarlet");
        jogador.setPosicaoAtual(1);
        DeslocarPiao dp = new DeslocarPiao();

        boolean resultado = dp.deslocarPiao(jogador, 4, casas);

        assertTrue(resultado);
        assertEquals(4, jogador.getPosicaoAtual());
    }
}