package model;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MapeaCasasTest {

    // Teste original: grafo simples, 2 passos
    @Test
    public void testMapearCasasSumOfDados() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(1, Arrays.asList(2, 3));
        adj.put(2, Arrays.asList(1, 4));
        adj.put(3, Arrays.asList(1));
        adj.put(4, Arrays.asList(2));

        MapeaCasas m = new MapeaCasas(adj);
        Set<Integer> res = m.mapearCasas(new int[]{2}, 1);
        assertTrue(res.contains(2));
        assertTrue(res.contains(3));
        assertTrue(res.contains(4));
        assertEquals(3, res.size());
    }

    // Casa sem vizinhos: resultado deve ser vazio
    @Test
    public void testCasaSemVizinhos() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(1, Collections.emptyList());

        MapeaCasas m = new MapeaCasas(adj);
        Set<Integer> res = m.mapearCasas(new int[]{3}, 1);
        assertTrue(res.isEmpty());
    }

    // 1 passo só: apenas vizinhos diretos
    @Test
    public void testUmPasso() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(1, Arrays.asList(2, 3));
        adj.put(2, Arrays.asList(1));
        adj.put(3, Arrays.asList(1));

        MapeaCasas m = new MapeaCasas(adj);
        Set<Integer> res = m.mapearCasas(new int[]{1}, 1);
        assertTrue(res.contains(2));
        assertTrue(res.contains(3));
        assertEquals(2, res.size());
    }

    // Múltiplos dados: soma os passos
    @Test
    public void testMultiplosDados() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(1, Arrays.asList(2));
        adj.put(2, Arrays.asList(1, 3));
        adj.put(3, Arrays.asList(2, 4));
        adj.put(4, Arrays.asList(3));

        MapeaCasas m = new MapeaCasas(adj);
        // dados {1,2} = 3 passos a partir de 1 → chega até 4
        Set<Integer> res = m.mapearCasas(new int[]{1, 2}, 1);
        assertTrue(res.contains(4));
    }
}