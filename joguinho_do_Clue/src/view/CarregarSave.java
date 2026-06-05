package view;

import model.*;
import java.io.*;
import java.util.*;

public class CarregarSave {

    // Salva o estado atual do jogo em arquivo .txt
    public static void salvar(Jogo jogo, File arquivo) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(arquivo));

            // Salva indice do jogador atual
            List<Jogador> jogadores = jogo.getJogadores();
            pw.println(jogadores.size());

            for (Jogador j : jogadores) {
                pw.println(j.getNome());
                pw.println(j.getPosicaoAtual());
            }

            pw.close();
            System.out.println("Jogo salvo em: " + arquivo.getPath());
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    // Carrega o estado salvo no arquivo e aplica ao jogo
    public static void carregar(Jogo jogo, File arquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));

            int numJogadores = Integer.parseInt(br.readLine().trim());

            for (int i = 0; i < numJogadores; i++) {
                String nome     = br.readLine().trim();
                int posicao     = Integer.parseInt(br.readLine().trim());

                for (Jogador j : jogo.getJogadores()) {
                    if (j.getNome().equals(nome)) {
                        j.setPosicaoAtual(posicao);
                        break;
                    }
                }
            }

            br.close();
            System.out.println("Jogo carregado de: " + arquivo.getPath());
        } catch (Exception e) {
            System.out.println("Erro ao carregar save: " + e.getMessage());
        }
    }
}