package view;

import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaAcusacao extends JDialog {

    private static final String[] SUSPEITOS = {
        "Coronel Mustard", "Srta. Scarlet", "Professor Plum",
        "Reverendo Green", "Sra. White", "Sra. Peacock"
    };

    private static final String[] ARMAS = {
        "Corda", "Cano de Chumbo", "Faca",
        "Chave Inglesa", "Castical", "Revolver"
    };

    private static final String[] COMODOS = {
        "Cozinha", "Sala de Música", "Jardim de Inverno", "Salão de Jogos",
        "Sala de Jantar", "Biblioteca", "Entrada", "Sala de Estar", "Escritório"
    };

    public TelaAcusacao(JFrame parent) {
        super(parent, "Acusação Final", true);

        JPanel painel = new JPanel(null);
        painel.setPreferredSize(new Dimension(420, 290));
        painel.setBackground(Color.DARK_GRAY);

        JLabel lblAviso = new JLabel("Atenção: Se a acusação estiver errada, você perde!");
        lblAviso.setForeground(Color.RED);
        lblAviso.setBounds(20, 10, 380, 25);
        painel.add(lblAviso);

        JLabel lblSuspeito = new JLabel("Suspeito:");
        lblSuspeito.setForeground(Color.WHITE);
        lblSuspeito.setBounds(20, 50, 100, 25);
        painel.add(lblSuspeito);

        JComboBox<String> comboSuspeito = new JComboBox<>(SUSPEITOS);
        comboSuspeito.setBounds(130, 50, 200, 25);
        painel.add(comboSuspeito);

        JLabel lblArma = new JLabel("Arma:");
        lblArma.setForeground(Color.WHITE);
        lblArma.setBounds(20, 90, 100, 25);
        painel.add(lblArma);

        JComboBox<String> comboArma = new JComboBox<>(ARMAS);
        comboArma.setBounds(130, 90, 200, 25);
        painel.add(comboArma);

        JLabel lblComodo = new JLabel("Cômodo:");
        lblComodo.setForeground(Color.WHITE);
        lblComodo.setBounds(20, 130, 100, 25);
        painel.add(lblComodo);

        JComboBox<String> comboComodo = new JComboBox<>(COMODOS);
        comboComodo.setBounds(130, 130, 200, 25);
        painel.add(comboComodo);

        JLabel lblResultado = new JLabel("");
        lblResultado.setForeground(Color.YELLOW);
        lblResultado.setFont(new Font("Arial", Font.BOLD, 13));
        lblResultado.setBounds(20, 225, 380, 25);
        painel.add(lblResultado);

        JButton btnConfirmar = new JButton("Acusar");
        btnConfirmar.setBounds(60, 180, 120, 30);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String suspeito = (String) comboSuspeito.getSelectedItem();
                String arma     = (String) comboArma.getSelectedItem();
                String comodo   = (String) comboComodo.getSelectedItem();

                Controller ctrl = Controller.getInstance();
                boolean venceu  = ctrl.fazerAcusacao(suspeito, arma, comodo);

                if (venceu) {
                    lblResultado.setForeground(Color.GREEN);
                    lblResultado.setText("Parabéns! Você venceu o jogo!");
                    btnConfirmar.setEnabled(false);
                    // Fecha apos breve pausa para o jogador ler o resultado
                    Timer timer = new Timer(2000, new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            dispose();
                            System.exit(0);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    lblResultado.setForeground(Color.RED);
                    lblResultado.setText("Acusação errada! Você está eliminado.");
                    btnConfirmar.setEnabled(false);
                    // Elimina o jogador via Controller (nao direto no model)
                    ctrl.eliminarJogadorAtual();
                    // Verifica se todos os jogadores foram eliminados
                    if (ctrl.getJogo().getJogadores().isEmpty()) {
                        Timer timer = new Timer(1500, new ActionListener() {
                            public void actionPerformed(ActionEvent ev) {
                                dispose();
                                String solucao = "Suspeito: "  + ctrl.getSuspeitoEnvelope()
                                              + "\nArma: "     + ctrl.getArmaEnvelope()
                                              + "\nCômodo: "   + ctrl.getComodoEnvelope();
                                int resposta = JOptionPane.showOptionDialog(
                                    parent,
                                    "Todos os jogadores foram eliminados!\n\nA solução era:\n" + solucao
                                    + "\n\nDeseja iniciar uma nova partida?",
                                    "Fim de Jogo",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    new String[]{"Nova Partida", "Sair"},
                                    "Nova Partida"
                                );
                                if (resposta == JOptionPane.YES_OPTION) {
                                    parent.dispose();
                                    new TelaPersonagens(false);
                                } else {
                                    System.exit(0);
                                }
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            }
        });
        painel.add(btnConfirmar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBounds(210, 180, 120, 30);
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
}