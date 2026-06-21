package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaSugestao extends JDialog {

    private static final String[] SUSPEITOS = {
        "Coronel Mustard", "Srta. Scarlet", "Professor Plum",
        "Reverendo Green", "Sra. White", "Sra. Peacock"
    };

    private static final String[] ARMAS = {
        "Corda", "Cano de Chumbo", "Faca",
        "Chave Inglesa", "Castical", "Revolver"
    };

    public TelaSugestao(JFrame parent, Jogo jogo) {
        super(parent, "Fazer Sugestão", true);

        String comodoAtual = TabuleiroCasas.nomeComodo(
            jogo.getJogadorAtual().getPosicaoAtual());

        JPanel painel = new JPanel(null);
        painel.setPreferredSize(new Dimension(420, 340));
        painel.setBackground(Color.DARK_GRAY);

        JLabel lblComodo = new JLabel("Cômodo: " + comodoAtual);
        lblComodo.setForeground(Color.WHITE);
        lblComodo.setBounds(20, 20, 360, 25);
        painel.add(lblComodo);

        JLabel lblSuspeito = new JLabel("Suspeito:");
        lblSuspeito.setForeground(Color.WHITE);
        lblSuspeito.setBounds(20, 60, 100, 25);
        painel.add(lblSuspeito);

        JComboBox<String> comboSuspeito = new JComboBox<>(SUSPEITOS);
        comboSuspeito.setBounds(130, 60, 200, 25);
        painel.add(comboSuspeito);

        JLabel lblArma = new JLabel("Arma:");
        lblArma.setForeground(Color.WHITE);
        lblArma.setBounds(20, 100, 100, 25);
        painel.add(lblArma);

        JComboBox<String> comboArma = new JComboBox<>(ARMAS);
        comboArma.setBounds(130, 100, 200, 25);
        painel.add(comboArma);

        // Resultado da refutacao
        JLabel lblResultado = new JLabel("");
        lblResultado.setForeground(Color.YELLOW);
        lblResultado.setBounds(20, 200, 380, 25);
        painel.add(lblResultado);

        // Carta mostrada — visivel so para quem fez o palpite
        JLabel lblCarta = new JLabel("");
        lblCarta.setForeground(new Color(100, 220, 255));
        lblCarta.setFont(new Font("Arial", Font.BOLD, 13));
        lblCarta.setBounds(20, 230, 380, 25);
        painel.add(lblCarta);

        // Suspeito movido
        JLabel lblMoveu = new JLabel("");
        lblMoveu.setForeground(new Color(180, 255, 180));
        lblMoveu.setBounds(20, 260, 380, 25);
        painel.add(lblMoveu);

        JButton btnConfirmar = new JButton("Sugerir");
        btnConfirmar.setBounds(20, 155, 100, 30);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String suspeito = (String) comboSuspeito.getSelectedItem();
                String arma     = (String) comboArma.getSelectedItem();

                // Move o suspeito para o comodo atual (regra do jogo)
                jogo.moverSuspeitoParaComodo(suspeito,
                    jogo.getJogadorAtual().getPosicaoAtual());
                lblMoveu.setText(suspeito + " foi movido para " + comodoAtual + ".");

                // Verifica se algum adversario pode refutar
                Carta cartaMostrada = encontrarCartaRefutacao(
                    jogo, suspeito, arma, comodoAtual);

                if (cartaMostrada != null) {
                    // Mostra quem refutou mas so revela a carta para o jogador atual
                    String quemRefutou = encontrarQuemRefutou(
                        jogo, suspeito, arma, comodoAtual);
                    lblResultado.setText(quemRefutou + " mostrou uma carta!");
                    lblCarta.setText("Carta revelada para você: " + cartaMostrada.getNome());
                } else {
                    lblResultado.setText("Ninguém refutou a sugestão.");
                    lblCarta.setText("");
                }

                btnConfirmar.setEnabled(false);
            }
        });
        painel.add(btnConfirmar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBounds(140, 155, 100, 30);
        btnFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        painel.add(btnFechar);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // Retorna a primeira carta que refuta a sugestao (para mostrar ao jogador atual)
    private Carta encontrarCartaRefutacao(Jogo jogo, String suspeito,
                                           String arma, String comodo) {
        Jogador atual = jogo.getJogadorAtual();
        List<Jogador> jogadores = jogo.getJogadores();

        for (Jogador j : jogadores) {
            if (j == atual) continue;
            for (Carta c : j.getMao()) {
                String nome = c.getNome();
                if (nome.equals(suspeito) || nome.equals(arma) || nome.equals(comodo)) {
                    return c;
                }
            }
        }
        return null;
    }

    // Retorna o nome de quem refutou (para mostrar a todos)
    private String encontrarQuemRefutou(Jogo jogo, String suspeito,
                                         String arma, String comodo) {
        Jogador atual = jogo.getJogadorAtual();
        List<Jogador> jogadores = jogo.getJogadores();

        for (Jogador j : jogadores) {
            if (j == atual) continue;
            for (Carta c : j.getMao()) {
                String nome = c.getNome();
                if (nome.equals(suspeito) || nome.equals(arma) || nome.equals(comodo)) {
                    return j.getNome();
                }
            }
        }
        return "";
    }
}