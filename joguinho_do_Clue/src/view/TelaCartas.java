package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCartas extends JDialog {

    public TelaCartas(JFrame parent, Jogador jogador) {
        super(parent, "Cartas de " + jogador.getNome(), true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.DARK_GRAY);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Suas cartas:");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));

        List<Carta> mao = jogador.getMao();
        if (mao.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma carta.");
            vazio.setForeground(Color.GRAY);
            painel.add(vazio);
        } else {
            for (Carta c : mao) {
                JLabel lblCarta = new JLabel("• " + c.toString());
                lblCarta.setForeground(Color.WHITE);
                lblCarta.setFont(new Font("Arial", Font.PLAIN, 14));
                painel.add(lblCarta);
            }
        }

        painel.add(Box.createVerticalStrut(20));
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        painel.add(btnFechar);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(parent);
    }
}