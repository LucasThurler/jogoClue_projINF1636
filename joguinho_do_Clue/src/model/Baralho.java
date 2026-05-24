package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Baralho {
    private HashMap<TipoCarta, List<Carta>> cartas;
    private HashMap<TipoCarta, Carta> envelopeConfidencial;

    Baralho() {
        cartas = new HashMap<>();
        envelopeConfidencial = new HashMap<>();

        cartas.put(TipoCarta.SUSPEITO, new ArrayList<>());
        cartas.put(TipoCarta.COMODO, new ArrayList<>());
        cartas.put(TipoCarta.ARMA, new ArrayList<>());

        inicializarCartas();
    }

    private void inicializarCartas() {

    // 6 suspeitos
    adicionarCarta("Coronel Mustard", TipoCarta.SUSPEITO);
    adicionarCarta("Srta. Scarlet", TipoCarta.SUSPEITO);
    adicionarCarta("Professor Plum", TipoCarta.SUSPEITO);
    adicionarCarta("Reverendo Green", TipoCarta.SUSPEITO);
    adicionarCarta("Sra. White", TipoCarta.SUSPEITO);
    adicionarCarta("Sra. Peacock", TipoCarta.SUSPEITO);

    // 6 armas
    adicionarCarta("Corda", TipoCarta.ARMA);
    adicionarCarta("Cano de Chumbo", TipoCarta.ARMA);
    adicionarCarta("Faca", TipoCarta.ARMA);
    adicionarCarta("Chave Inglesa", TipoCarta.ARMA);
    adicionarCarta("Castical", TipoCarta.ARMA);
    adicionarCarta("Revolver", TipoCarta.ARMA);

    // 9 cômodos
    adicionarCarta("Comodo 1", TipoCarta.COMODO);
    adicionarCarta("Comodo 2", TipoCarta.COMODO);
    adicionarCarta("Comodo 3", TipoCarta.COMODO);
    adicionarCarta("Comodo 4", TipoCarta.COMODO);
    adicionarCarta("Comodo 5", TipoCarta.COMODO);
    adicionarCarta("Comodo 6", TipoCarta.COMODO);
    adicionarCarta("Comodo 7", TipoCarta.COMODO);
    adicionarCarta("Comodo 8", TipoCarta.COMODO);
    adicionarCarta("Comodo 9", TipoCarta.COMODO);
}

    private void adicionarCarta(String nome, TipoCarta tipo) {
        cartas.get(tipo).add(new Carta(nome, tipo));
    }

    void embaralhar() {
        for (List<Carta> lista : cartas.values()) {
            Collections.shuffle(lista);
        }
    }

    void preencherEnvelope() {
        for (TipoCarta tipo : TipoCarta.values()) {
            Carta carta = cartas.get(tipo).remove(0);
            envelopeConfidencial.put(tipo, carta);
        }
    }

    void distribuirCartas(List<Jogador> jogadores) {
        List<Carta> todasCartas = new ArrayList<>();
        for (List<Carta> lista : cartas.values()) {
            todasCartas.addAll(lista);
        }

        int i = 0;
        for (Carta carta : todasCartas) {
            jogadores.get(i % jogadores.size()).receberCarta(carta);
            i++;
        }
    }

    HashMap<TipoCarta, Carta> getEnvelopeConfidencial() {
        return envelopeConfidencial;
    }

    List<Carta> getCartas(TipoCarta tipo) {
        return cartas.get(tipo);
    }

    // APENAS PARA DEBUG -- RETIRAR DEPOIS
    public static void main(String[] args) {
        Baralho b = new Baralho();

        System.out.println("=== Antes de embaralhar ===");
        System.out.println("Suspeitos: " + b.getCartas(TipoCarta.SUSPEITO));
        System.out.println("Comodos: " + b.getCartas(TipoCarta.COMODO));
        System.out.println("Armas: " + b.getCartas(TipoCarta.ARMA));

        b.embaralhar();

        System.out.println("\n=== Depois de embaralhar ===");
        System.out.println("Suspeitos: " + b.getCartas(TipoCarta.SUSPEITO));
        System.out.println("Comodos: " + b.getCartas(TipoCarta.COMODO));
        System.out.println("Armas: " + b.getCartas(TipoCarta.ARMA));

        b.preencherEnvelope();

        System.out.println("\n=== Envelope Confidencial ===");
        System.out.println(b.getEnvelopeConfidencial());

        System.out.println("\n=== Cartas restantes apos envelope ===");
        System.out.println("Suspeitos: " + b.getCartas(TipoCarta.SUSPEITO));
        System.out.println("Comodos: " + b.getCartas(TipoCarta.COMODO));
        System.out.println("Armas: " + b.getCartas(TipoCarta.ARMA));

        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Jogador 1"));
        jogadores.add(new Jogador("Jogador 2"));
        jogadores.add(new Jogador("Jogador 3"));
        jogadores.add(new Jogador("Jogador 4"));
        jogadores.add(new Jogador("Jogador 5"));

        b.distribuirCartas(jogadores);

        System.out.println("\n=== Maos dos jogadores ===");
        for (Jogador j : jogadores) {
            System.out.println(j);
        }
    }
}