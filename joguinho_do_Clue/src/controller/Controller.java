package controller;

import model.*;
import java.util.List;
import java.util.Set;

public class Controller {
    private static Controller instance = null;
    private Jogo jogo;

    private Controller() {}

    public static Controller getInstance() {
        if (instance == null) instance = new Controller();
        return instance;
    }

    // Façade: registra um observador no Jogo
    public void registra(ObservadorIF o) {
        jogo.add(o);
    }

    public void inicializarPartida(List<Jogador> jogadores) {
        this.jogo = new Jogo(jogadores);
    }

    public Jogo getJogo() { return jogo; }

    public Jogador getJogadorAtual() { return jogo.getJogadorAtual(); }

    public void passarTurno() { jogo.proximoJogador(); }

    public void lancarDados(int d1, int d2) { jogo.setDados(d1, d2); }

    public Set<Integer> getCasasAlcancaveis(int[] dados) {
        return jogo.getMapeaCasas().mapearCasas(dados, jogo.getJogadorAtual().getPosicaoAtual());
    }

    public boolean moverJogador(int casaEscolhida, Set<Integer> casasAlcancaveis) {
        DeslocarPiao dp = new DeslocarPiao();
        boolean moveu = dp.deslocarPiao(jogo.getJogadorAtual(), casaEscolhida, casasAlcancaveis);
        if (moveu) jogo.notificarObservadores(); // so notifica, sem mudar dados
        return moveu;
    }

    public void usarPassagemSecreta() { jogo.usarPassagemSecreta(); }
    
    public boolean fazerAcusacao(String suspeito, String arma, String comodo) {
        return jogo.verificarAcusacao(suspeito, arma, comodo);
    }
    
    public void eliminarJogadorAtual() {
        jogo.eliminarJogadorAtual();
    }
    
    public String getSuspeitoEnvelope() {
        return jogo.getSuspeitoEnvelope();
    }

    public String getArmaEnvelope() {
        return jogo.getArmaEnvelope();
    }

    public String getComodoEnvelope() {
        return jogo.getComodoEnvelope();
    }
    
    public void carregarIndiceJogador(int indice) {
        jogo.carregarIndiceJogador(indice);
    }
}

