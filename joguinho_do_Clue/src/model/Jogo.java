// INF1636
// Lucas Thurler
// Pedro Augusto
// Douglas Gomes

package model;

import java.util.ArrayList;
import java.util.List;

public class Jogo {
    private List<Jogador> jogadores;
    private int indiceJogadorAtual;
    private Baralho baralho;
    private MapeaCasas mapeaCasas;

    public Jogo(List<Jogador> jogadores) {
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
        // Conecta o grafo real ao MapeaCasas
        mapeaCasas = new MapeaCasas(TabuleiroCasas.construirGrafo());
        
        // Define posição inicial de cada jogador
        for (Jogador j : jogadores) {
            switch (j.getNome()) {
                case "Coronel Mustard": j.setPosicaoAtual(TabuleiroCasas.INICIO_CORONEL_MUSTARD); break;
                case "Srta. Scarlet":   j.setPosicaoAtual(TabuleiroCasas.INICIO_SRTA_SCARLET);    break;
                case "Professor Plum":  j.setPosicaoAtual(TabuleiroCasas.INICIO_PROF_PLUM);       break;
                case "Reverendo Green": j.setPosicaoAtual(TabuleiroCasas.INICIO_REV_GREEN);       break;
                case "Sra. White":      j.setPosicaoAtual(TabuleiroCasas.INICIO_SRA_WHITE);       break;
                case "Sra. Peacock":    j.setPosicaoAtual(TabuleiroCasas.INICIO_SRA_PEACOCK);     break;
            }
        }
    }
    
    public void usarPassagemSecreta() {
        int posAtual = getJogadorAtual().getPosicaoAtual();
        int destino  = TabuleiroCasas.passagemSecreta(posAtual);
        if (destino != -1) {
            getJogadorAtual().setPosicaoAtual(destino);
        }
    }
    
    public MapeaCasas getMapeaCasas() {
        return mapeaCasas;
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

    public Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    public void proximoJogador() {
        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public Baralho getBaralho() {
        return baralho;
    }

    // APENAS PARA DEBUG -- RETIRAR DEPOIS
    public static void main(String[] args) {
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Coronel Mustard"));
        jogadores.add(new Jogador("Srta. Scarlet"));
        jogadores.add(new Jogador("Professor Plum"));

        Jogo jogo = new Jogo(jogadores);

        // Testa posições iniciais
        System.out.println("=== Posicoes iniciais ===");
        for (Jogador j : jogo.getJogadores()) {
            System.out.println(j.getNome() + ": casa " + j.getPosicaoAtual());
        }

        // Testa MapeaCasas: Srta. Scarlet começa em 681, dado=3
        Jogador scarlet = jogo.getJogadorAtual();
        System.out.println("\n=== Teste MapeaCasas ===");
        System.out.println("Jogador atual: " + scarlet.getNome());
        System.out.println("Posicao atual: " + scarlet.getPosicaoAtual());
        java.util.Set<Integer> alcancaveis = jogo.getMapeaCasas()
            .mapearCasas(new int[]{3}, scarlet.getPosicaoAtual());
        System.out.println("Casas alcancaveis com dado=3: " + alcancaveis);
        System.out.println("Quantidade: " + alcancaveis.size());
    }
}