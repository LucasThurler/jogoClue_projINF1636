package view;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TelaInicio extends JFrame {

    private BufferedImage imgFundo;

    public TelaInicio() {
        setTitle("Clue - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        carregarImagem();

        PainelInicio painel = new PainelInicio();
        add(painel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void carregarImagem() {
        try {
            imgFundo = ImageIO.read(new File("src/Imagens/Telas/TelaInicio.jpg"));
        } catch (Exception e) {
            System.out.println("Erro ao carregar tela de inicio: " + e.getMessage());
        }
    }

    class PainelInicio extends JPanel {

        PainelInicio() {
            setLayout(null);
            setPreferredSize(new Dimension(1400, 1050));

            JButton btnNovoJogo = new JButton("Novo Jogo");
            btnNovoJogo.setBounds(580, 450, 150, 40);
            btnNovoJogo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new TelaPersonagens(false);
                }
            });
            add(btnNovoJogo);

            JButton btnContinuar = new JButton("Continuar");
            btnContinuar.setBounds(580, 510, 150, 40);
            btnContinuar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    int resultado = fc.showOpenDialog(null);
                    if (resultado == JFileChooser.APPROVE_OPTION) {
                        File arquivo = fc.getSelectedFile();
                        dispose();
                        new TelaPersonagens(true, arquivo);
                    }
                }
            });
            add(btnContinuar);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            if (imgFundo != null) {
                g2.drawImage(imgFundo, 0, 0, getWidth(), getHeight(), null);
            } else {
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}