package view;

import model.*;
import java.io.*;
import java.util.*;
import controller.Controller;

public class CarregarSave {

    // Salva o estado atual do jogo em arquivo .txt
	public static void salvar(Jogo jogo, File arquivo) {
	    try {
	        PrintWriter pw = new PrintWriter(new FileWriter(arquivo));
	        List<Jogador> jogadores = jogo.getJogadores();

	        // Indice do jogador atual
	        pw.println(jogadores.indexOf(jogo.getJogadorAtual()));
	        pw.println(jogadores.size());

	        for (Jogador j : jogadores) {
	            pw.println(j.getNome());
	            pw.println(j.getPosicaoAtual());
	            // Cartas
	            List<String[]> mao = j.getMaoParaSalvar();
	            pw.println(mao.size());
	            for (String[] c : mao) {
	                pw.println(c[0]); // nome
	                pw.println(c[1]); // tipo
	            }
	            // Bloco de notas
	            List<String> bloco = j.getBlocoDeNotas();
	            pw.println(bloco.size());
	            for (String linha : bloco) {
	                pw.println(linha);
	            }
	        }

	        // Envelope confidencial
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

    // Carrega o estado salvo no arquivo e aplica ao jogo
	public static void carregar(Jogo jogo, File arquivo) {
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(arquivo));

	        // Indice do jogador atual
	        int indiceAtual = Integer.parseInt(br.readLine().trim());

	        int numJogadores = Integer.parseInt(br.readLine().trim());

	        Map<String, Integer> posicoes = new HashMap<>();
	        Map<String, List<String[]>> cartas = new HashMap<>();
	        Map<String, List<String>> blocos = new HashMap<>();

	        for (int i = 0; i < numJogadores; i++) {
	            String nome    = br.readLine().trim();
	            int posicao    = Integer.parseInt(br.readLine().trim());
	            posicoes.put(nome, posicao);

	            // Cartas
	            int numCartas = Integer.parseInt(br.readLine().trim());
	            List<String[]> mao = new ArrayList<>();
	            for (int k = 0; k < numCartas; k++) {
	                String nomeCarta = br.readLine().trim();
	                String tipoCarta = br.readLine().trim();
	                mao.add(new String[]{nomeCarta, tipoCarta});
	            }
	            cartas.put(nome, mao);

	            // Bloco de notas
	            int numLinhas = Integer.parseInt(br.readLine().trim());
	            List<String> bloco = new ArrayList<>();
	            for (int k = 0; k < numLinhas; k++) {
	                bloco.add(br.readLine().trim());
	            }
	            blocos.put(nome, bloco);
	        }

	        // Envelope confidencial — apenas lemos, não precisamos restaurar
	        // pois o Jogo já gerou um envelope ao ser criado
	        br.readLine(); // suspeito
	        br.readLine(); // arma
	        br.readLine(); // comodo

	        br.close();

	        // Aplica posicoes
	        jogo.carregarPosicoes(posicoes);
	        jogo.carregarCartas(cartas);
	        
	        // Aplica bloco de notas
	        for (Map.Entry<String, List<String>> entry : blocos.entrySet()) {
	            jogo.atualizarBlocoDeNotas(entry.getKey(), entry.getValue());
	        }

	        // Aplica indice do jogador atual
	        jogo.carregarIndiceJogador(indiceAtual);

	        System.out.println("Jogo carregado de: " + arquivo.getPath());
	    } catch (Exception e) {
	        System.out.println("Erro ao carregar save: " + e.getMessage());
	    }
	}
}