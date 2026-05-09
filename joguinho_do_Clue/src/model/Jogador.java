package model;

import java.util.ArrayList;
import java.util.List;

class Jogador {
    private String nome;
    private List<Carta> mao;
    private List<String> blocoDeNotas;

    Jogador(String nome) {
        this.nome = nome;
        this.mao = new ArrayList<>();
        this.blocoDeNotas = new ArrayList<>();
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

    List<String> getBlocoDeNotas() {
        return blocoDeNotas;
    }

    String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome + ": " + mao;
    }
}