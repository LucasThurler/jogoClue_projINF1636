package view;

import model.Jogo;
import model.Jogador;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(new Jogador("Srta. Scarlet"));
        jogadores.add(new Jogador("Coronel Mustard"));
        jogadores.add(new Jogador("Professor Plum"));

        Jogo jogo = new Jogo(jogadores);

        new TelaTabuleiro(jogo);
    }
}