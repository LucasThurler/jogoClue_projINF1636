package model;

import java.util.*;

public class MapeaCasas {
    private final Map<Integer, List<Integer>> adj;

    public MapeaCasas(Map<Integer, List<Integer>> adj) {
        this.adj = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> e : adj.entrySet()) {
            this.adj.put(e.getKey(), new ArrayList<>(e.getValue()));
        }
    }

    public Set<Integer> mapearCasas(int[] dados, int casaInicial) {
        int totalPassos = 0;
        for (int d : dados) totalPassos += d;

        Set<Integer> alcancaveis = new HashSet<>();
        Queue<Node> fila = new ArrayDeque<>();
        fila.add(new Node(casaInicial, 0));

        Set<String> visitados = new HashSet<>();
        visitados.add(casaInicial + ":" + 0);

        while (!fila.isEmpty()) {
            Node atual = fila.poll();

            if (atual.passos == totalPassos) {
                alcancaveis.add(atual.pos);
                continue;
            }

            // Se chegou num comodo antes de usar todos os passos, para aqui
            if (TabuleiroCasas.isComodo(atual.pos) && atual.passos > 0) {
                alcancaveis.add(atual.pos);
                continue;
            }

            List<Integer> vizinhos = adj.getOrDefault(atual.pos, Collections.emptyList());
            for (int vizinho : vizinhos) {
                int novosPassos = atual.passos + 1;
                String chave = vizinho + ":" + novosPassos;
                if (!visitados.contains(chave)) {
                    visitados.add(chave);
                    fila.add(new Node(vizinho, novosPassos));
                }
            }
        }
        
        alcancaveis.remove(casaInicial);
        return alcancaveis;
    }

    private static class Node {
        private final int pos;
        private final int passos;

        private Node(int pos, int passos) {
            this.pos = pos;
            this.passos = passos;
        }
    }

    // APENAS PARA DEBUG
    public static void main(String[] args) {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(1, Arrays.asList(2, 3));
        adj.put(2, Arrays.asList(1, 4));
        adj.put(3, Arrays.asList(1));
        adj.put(4, Arrays.asList(2));

        MapeaCasas m = new MapeaCasas(adj);
        int[] dados = {2};
        Set<Integer> reach = m.mapearCasas(dados, 1);
        System.out.println("Dados: " + Arrays.toString(dados) + " -> alcançáveis a partir de 1: " + reach);
    }
}