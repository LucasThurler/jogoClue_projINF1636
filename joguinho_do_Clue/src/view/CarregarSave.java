package view;

import model.*;
import java.io.*;
import java.util.*;
import controller.Controller;

public class CarregarSave {

    public static void salvar(Jogo jogo, File arquivo) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(arquivo));
            List<Jogador> jogadores = jogo.getJogadores();

            pw.println(jogadores.indexOf(jogo.getJogadorAtual()));
            pw.println(jogadores.size());

            for (Jogador j : jogadores) {
                pw.println(j.getNome());
                pw.println(j.getPosicaoAtual());
                List<String[]> mao = j.getMaoParaSalvar();
                pw.println(mao.size());
                for (String[] c : mao) {
                    pw.println(c[0]);
                    pw.println(c[1]);
                }
                List<String> bloco = j.getBlocoDeNotas();
                pw.println(bloco.size());
                for (String linha : bloco) {
                    pw.println(linha);
                }
            }

            // Ponto 3: salva envelope corretamente
            Controller ctrl = Controller.getInstance();
            pw.println(ctrl.getSuspeitoEnvelope());
            pw.println(ctrl.getArmaEnvelope());
            pw.println(ctrl.getComodoEnvelope());

            pw.close();
            System.out.println("Jogo salvo em: " + arquivo.getPath());
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static void carregar(Jogo jogo, File arquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));

            int indiceAtual  = Integer.parseInt(br.readLine().trim());
            int numJogadores = Integer.parseInt(br.readLine().trim());

            Map<String, Integer>        posicoes = new HashMap<>();
            Map<String, List<String[]>> cartas   = new HashMap<>();
            Map<String, List<String>>   blocos   = new HashMap<>();

            for (int i = 0; i < numJogadores; i++) {
                String nome   = br.readLine().trim();
                int posicao   = Integer.parseInt(br.readLine().trim());
                posicoes.put(nome, posicao);

                int numCartas = Integer.parseInt(br.readLine().trim());
                List<String[]> mao = new ArrayList<>();
                for (int k = 0; k < numCartas; k++) {
                    String nomeCarta = br.readLine().trim();
                    String tipoCarta = br.readLine().trim();
                    mao.add(new String[]{nomeCarta, tipoCarta});
                }
                cartas.put(nome, mao);

                int numLinhas = Integer.parseInt(br.readLine().trim());
                List<String> bloco = new ArrayList<>();
                for (int k = 0; k < numLinhas; k++) {
                    bloco.add(br.readLine().trim());
                }
                blocos.put(nome, bloco);
            }

            // Ponto 3: restaura envelope salvo
            String suspeitoEnv = br.readLine().trim();
            String armaEnv     = br.readLine().trim();
            String comodoEnv   = br.readLine().trim();

            br.close();

            jogo.carregarPosicoes(posicoes);
            jogo.carregarCartas(cartas);
            jogo.carregarEnvelope(suspeitoEnv, armaEnv, comodoEnv);

            for (Map.Entry<String, List<String>> entry : blocos.entrySet()) {
                jogo.atualizarBlocoDeNotas(entry.getKey(), entry.getValue());
            }

            jogo.carregarIndiceJogador(indiceAtual);

            System.out.println("Jogo carregado de: " + arquivo.getPath());
        } catch (Exception e) {
            System.out.println("Erro ao carregar save: " + e.getMessage());
        }
    }
}