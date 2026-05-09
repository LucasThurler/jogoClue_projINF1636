package model;

import java.util.ArrayList;
import java.util.List;

class Jogo {
    private List<Jogador> jogadores;
    private int indiceJogadorAtual;
    private Baralho baralho;

    Jogo(List<Jogador> jogadores) {
        if (jogadores.size() < 3 || jogadores.size() > 6) {
            throw new IllegalArgumentException("O jogo deve ter entre 3 e 6 jogadores.");
        }

        this.jogadores = jogadores;
        this.baralho = new Baralho();

        prepararJogo();
    }

    private void prepararJogo() {
        // Regra 4 e 5
        baralho.embaralhar();
        baralho.preencherEnvelope();
        baralho.distribuirCartas(jogadores);

        // Regra 7 - Srta. Scarlet sempre começa
        indiceJogadorAtual = encontrarSrtaScarlet();
    }

    private int encontrarSrtaScarlet() {
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).getNome().equals("Srta. Scarlet")) {
                return i;
            }
        }
        // Se Srta. Scarlet não estiver entre os jogadores, começa pelo primeiro
        return 0;
    }

    Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    void proximoJogador() {
        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
    }

    List<Jogador> getJogadores() {
        return jogadores;
    }

    Baralho getBaralho() {
        return baralho;
    }

    // APENAS PARA DEBUG -- RETIRAR DEPOIS
    public static void main(String[] args) {
    List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Coronel Mustard"));
        jogadores.add(new Jogador("Srta. Scarlet"));
        jogadores.add(new Jogador("Professor Plum"));
        jogadores.add(new Jogador("Reverendo Green"));
        jogadores.add(new Jogador("Sra. White"));
        jogadores.add(new Jogador("Sra. Peacock"));
        

        Jogo jogo = new Jogo(jogadores);

        System.out.println("=== Envelope Confidencial ===");
        System.out.println(jogo.getBaralho().getEnvelopeConfidencial());

        System.out.println("\n=== Maos dos jogadores ===");
        for (Jogador j : jogo.getJogadores()) {
            System.out.println(j);
        }

        System.out.println("\n=== Ordem dos turnos ===");
        for (int i = 0; i < jogadores.size(); i++) {
            System.out.println("Turno " + (i + 1) + ": " + jogo.getJogadorAtual().getNome());
            jogo.proximoJogador();
        }
    }
}