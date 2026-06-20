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
        painel.setPreferredSize(new Dimension(420, 320));
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
        lblResultado.setBounds(20, 200, 380, 25);
        painel.add(lblResultado);

        JLabel lblMoveu = new JLabel("");
        lblMoveu.setForeground(new Color(100, 200, 255));
        lblMoveu.setBounds(20, 230, 380, 25);
        painel.add(lblMoveu);

        JButton btnConfirmar = new JButton("Sugerir");
        btnConfirmar.setBounds(20, 155, 100, 30);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String suspeito = (String) comboSuspeito.getSelectedItem();
                String arma     = (String) comboArma.getSelectedItem();

                // Ponto 4: mover o suspeito para o comodo atual
                jogo.moverSuspeitoParaComodo(suspeito,
                    jogo.getJogadorAtual().getPosicaoAtual());
                lblMoveu.setText(suspeito + " foi movido para " + comodoAtual + ".");

                // Verificar se algum adversario tem carta da sugestao
                String resposta = verificarSugestao(jogo, suspeito, arma, comodoAtual);
                lblResultado.setText(resposta);
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

    private String verificarSugestao(Jogo jogo, String suspeito,
                                      String arma, String comodo) {
        Jogador atual    = jogo.getJogadorAtual();
        List<Jogador> jogadores = jogo.getJogadores();

        for (Jogador j : jogadores) {
            if (j == atual) continue;
            for (Carta c : j.getMao()) {
                String nome = c.getNome();
                if (nome.equals(suspeito) || nome.equals(arma) || nome.equals(comodo)) {
                    return j.getNome() + " mostrou uma carta!";
                }
            }
        }
        return "Ninguém refutou a sugestão.";
    }
}