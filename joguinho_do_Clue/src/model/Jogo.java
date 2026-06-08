package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Jogo implements ObservadoIF {
    private List<Jogador> jogadores;
    private int indiceJogadorAtual;
    private Baralho baralho;
    private MapeaCasas mapeaCasas;
    private List<ObservadorIF> observadores = new ArrayList<>();

    private int valorDado1 = 1;
    private int valorDado2 = 1;

    public Jogo(List<Jogador> jogadores) {
        if (jogadores.size() < 3 || jogadores.size() > 6)
            throw new IllegalArgumentException("O jogo deve ter entre 3 e 6 jogadores.");
        this.jogadores = jogadores;
        this.baralho = new Baralho();
        prepararJogo();
    }

    private void prepararJogo() {
        baralho.embaralhar();
        baralho.preencherEnvelope();
        baralho.distribuirCartas(jogadores);
        indiceJogadorAtual = encontrarSrtaScarlet();
        mapeaCasas = new MapeaCasas(TabuleiroCasas.construirGrafo());
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

    // --- ObservadoIF ---
    @Override
    public void add(ObservadorIF o) {
        if (!observadores.contains(o)) observadores.add(o);
    }

    @Override
    public void remove(ObservadorIF o) {
        observadores.remove(o);
    }

    // Indices: 1=dado1, 2=dado2, 3=posicao jogador atual
    @Override
    public int get(int i) {
        switch (i) {
            case 1: return valorDado1;
            case 2: return valorDado2;
            case 3: return getJogadorAtual().getPosicaoAtual();
            default: return -1;
        }
    }

    private void atualiza() {
        for (ObservadorIF ob : observadores) ob.notify(this);
    }

    // --- Acoes do jogo ---
    public void setDados(int d1, int d2) {
        this.valorDado1 = d1;
        this.valorDado2 = d2;
        atualiza();
    }

    public void usarPassagemSecreta() {
        int posAtual = getJogadorAtual().getPosicaoAtual();
        int destino  = TabuleiroCasas.passagemSecreta(posAtual);
        if (destino != -1) {
            getJogadorAtual().setPosicaoAtual(destino);
            atualiza();
        }
    }

    public void proximoJogador() {
        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
        atualiza();
    }

    public void atualizarBlocoDeNotas(String nomeJogador, List<String> novasAnotacoes) {
        for (Jogador j : jogadores) {
            if (j.getNome().equals(nomeJogador)) {
                j.getBlocoDeNotas().clear();
                for (String linha : novasAnotacoes) {
                    j.anotarNoBloco(linha);
                }
                atualiza();
                break;
            }
        }
    }

    public void carregarPosicoes(Map<String, Integer> posicoes) {
        for (Jogador j : jogadores) {
            if (posicoes.containsKey(j.getNome())) {
                j.setPosicaoAtual(posicoes.get(j.getNome()));
            }
        }
        atualiza();
    }

    private int encontrarSrtaScarlet() {
        for (int i = 0; i < jogadores.size(); i++)
            if (jogadores.get(i).getNome().equals("Srta. Scarlet")) return i;
        return 0;
    }

    public Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public Baralho getBaralho() {
        return baralho;
    }

    public MapeaCasas getMapeaCasas() {
        return mapeaCasas;
    }

    // APENAS PARA DEBUG -- RETIRAR DEPOIS
    public static void main(String[] args) {
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Coronel Mustard"));
        jogadores.add(new Jogador("Srta. Scarlet"));
        jogadores.add(new Jogador("Professor Plum"));

        Jogo jogo = new Jogo(jogadores);

        System.out.println("=== Posicoes iniciais ===");
        for (Jogador j : jogo.getJogadores()) {
            System.out.println(j.getNome() + ": casa " + j.getPosicaoAtual());
        }

        Jogador scarlet = jogo.getJogadorAtual();
        System.out.println("\n=== Teste MapeaCasas ===");
        System.out.println("Jogador atual: " + scarlet.getNome());
        System.out.println("Posicao atual: " + scarlet.getPosicaoAtual());
        java.util.Set<Integer> alcancaveis = jogo.getMapeaCasas()
            .mapearCasas(new int[]{3}, scarlet.getPosicaoAtual());
        System.out.println("Casas alcancaveis com dado=3: " + alcancaveis);
        System.out.println("Quantidade: " + alcancaveis.size());
    }
    
    public boolean verificarAcusacao(String suspeito, String arma, String comodo) {
        // Assume-se que 'Jogo' possui um atributo 'baralho' instanciado
        java.util.Map<model.TipoCarta, model.Carta> envelope = baralho.getEnvelopeConfidencial();

        boolean acertouSuspeito = envelope.get(model.TipoCarta.SUSPEITO).getNome().equals(suspeito);
        boolean acertouArma = envelope.get(model.TipoCarta.ARMA).getNome().equals(arma);
        boolean acertouComodo = envelope.get(model.TipoCarta.COMODO).getNome().equals(comodo);

        return acertouSuspeito && acertouArma && acertouComodo;
    }
    
}