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
        painel.setPreferredSize(new Dimension(400, 250));
        painel.setBackground(Color.DARK_GRAY);

        JLabel lblAviso = new JLabel("Atenção: Se a acusação estiver errada, você perde!");
        lblAviso.setForeground(Color.RED);
        lblAviso.setBounds(20, 10, 360, 25);
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

        JButton btnConfirmar = new JButton("Acusar");
        btnConfirmar.setBounds(60, 180, 120, 30);
        // Utilizando classe anônima em vez de Lambda
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String suspeito = (String) comboSuspeito.getSelectedItem();
                String arma = (String) comboArma.getSelectedItem();
                String comodo = (String) comboComodo.getSelectedItem();

                Controller ctrl = Controller.getInstance();
                boolean venceu = ctrl.fazerAcusacao(suspeito, arma, comodo);

                if (venceu) {
                    JOptionPane.showMessageDialog(TelaAcusacao.this,
                        "Parabéns! Você desvendou o mistério e venceu o jogo!",
                        "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(TelaAcusacao.this,
                        "Acusação incorreta! Você perdeu o jogo.",
                        "Fim de Jogo", JOptionPane.ERROR_MESSAGE);
                }
                
                System.exit(0); // Encerra o jogo em ambos os casos na acusação final
            }
        });
        painel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 180, 120, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        painel.add(btnCancelar);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}