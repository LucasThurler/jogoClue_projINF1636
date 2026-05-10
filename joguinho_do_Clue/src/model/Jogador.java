package model;

import java.util.ArrayList;
import java.util.List;

class Jogador {
    private String nome;
    private List<Carta> mao;
    private List<String> blocoDeNotas;
    private int posicaoAtual;

    Jogador(String nome) {
        this.nome = nome;
        this.mao = new ArrayList<>();
        this.blocoDeNotas = new ArrayList<>();
        this.posicaoAtual = -1;
    }

    void receberCarta(Carta carta) {
        mao.add(carta);
    }

    void anotarNoBloco(String anotacao) {
        blocoDeNotas.add(anotacao);
    }

    List<Carta> getMao() {
        return mao;
    }

    //Regra 6 - Bloco de notas para anotações
    List<String> getBlocoDeNotas() {
        return blocoDeNotas;
    }

    String getNome() {
        return nome;
    }
    
    int getPosicaoAtual() {
        return posicaoAtual;
    }

    void setPosicaoAtual(int novaPosicao) {
        this.posicaoAtual = novaPosicao;
    }

    @Override
    public String toString() {
        return nome + ": " + mao;
    }
}