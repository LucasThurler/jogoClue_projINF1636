package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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

    private boolean palpiteConfirmado = false;

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
                if (palpiteConfirmado) return; // bloqueia segundo clique
                String suspeito = (String) comboSuspeito.getSelectedItem();
                String arma     = (String) comboArma.getSelectedItem();
                String resposta = verificarSugestao(jogo, suspeito, arma, comodoAtual);
                lblResultado.setText(resposta);
                palpiteConfirmado = true;
                btnConfirmar.setEnabled(false); // desabilita após primeiro uso
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
        List<Jogador> jogadores = jogo.getJogadores();
        Jogador atual = jogo.getJogadorAtual();
        int indiceAtual = jogadores.indexOf(atual);
        int total = jogadores.size();

        for (int i = 1; i < total; i++) {
            Jogador j = jogadores.get((indiceAtual + i) % total);

            List<String> cartasRefutaveis = new ArrayList<>();
            for (Carta c : j.getMao()) {
                String nome = c.getNome();
                if (nome.equals(suspeito) || nome.equals(arma) || nome.equals(comodo)) {
                    cartasRefutaveis.add(nome);
                }
            }

            if (!cartasRefutaveis.isEmpty()) {
                String[] opcoes = cartasRefutaveis.toArray(new String[0]);

                // Dialogo sem opcao de cancelar — jogador e obrigado a mostrar uma carta
                JPanel painelEscolha = new JPanel(new BorderLayout(10, 10));
                painelEscolha.add(new JLabel(j.getNome() + " deve mostrar uma carta:"), BorderLayout.NORTH);

                JComboBox<String> comboEscolha = new JComboBox<>(opcoes);
                painelEscolha.add(comboEscolha, BorderLayout.CENTER);

                final String[] escolhida = {opcoes[0]};

                JDialog dialogo = new JDialog(this, "Refutação", true);
                dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                JButton btnMostrar = new JButton("Mostrar");
                btnMostrar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        escolhida[0] = (String) comboEscolha.getSelectedItem();
                        dialogo.dispose();
                    }
                });

                painelEscolha.add(btnMostrar, BorderLayout.SOUTH);
                dialogo.setContentPane(painelEscolha);
                dialogo.pack();
                dialogo.setLocationRelativeTo(this);
                dialogo.setVisible(true);

                return j.getNome() + " mostrou: " + escolhida[0];
            }
        }
        return "Ninguém refutou a sugestão.";
    }
}