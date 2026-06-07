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

        String comodoAtual = TabuleiroCasas.nomeComodo(jogo.getJogadorAtual().getPosicaoAtual());

        JPanel painel = new JPanel(null);
        painel.setPreferredSize(new Dimension(400, 300));
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

        JLabel lblResultado = new JLabel("");
        lblResultado.setForeground(Color.YELLOW);
        lblResultado.setBounds(20, 180, 360, 25);
        painel.add(lblResultado);

        JButton btnConfirmar = new JButton("Sugerir");
        btnConfirmar.setBounds(20, 140, 100, 30);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String suspeito = (String) comboSuspeito.getSelectedItem();
                String arma     = (String) comboArma.getSelectedItem();
                String resposta = verificarSugestao(jogo, suspeito, arma, comodoAtual);
                lblResultado.setText(resposta);
            }
        });
        painel.add(btnConfirmar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBounds(140, 140, 100, 30);
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

    private String verificarSugestao(Jogo jogo, String suspeito, String arma, String comodo) {
        Jogador atual = jogo.getJogadorAtual();
        List<Jogador> jogadores = jogo.getJogadores();

        for (Jogador j : jogadores) {
            if (j == atual) continue;
            for (Carta c : j.getMao()) {
                String nome = c.getNome();
                if (nome.equals(suspeito)) {
                    return j.getNome() + " mostrou uma carta de suspeito: " + suspeito;
                }
                else if (nome.equals(arma)) {
                    return j.getNome() + " mostrou uma carta de arma: " + arma;
                }
                else if (nome.equals(comodo)) {
                    return j.getNome() + " mostrou uma carta de comodo: " + comodo;
                }

            }
        }
        return "Ninguém refutou a sugestão.";
    }
}