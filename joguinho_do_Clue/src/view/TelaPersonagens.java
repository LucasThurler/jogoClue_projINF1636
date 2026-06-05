package view;

import model.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class TelaPersonagens extends JFrame {

    private static final String[] NOMES = {
        "Srta. Scarlet",
        "Coronel Mustard",
        "Sra. White",
        "Reverendo Green",
        "Sra. Peacock",
        "Professor Plum"
    };

    private static final String[] ARQUIVOS = {
        "Scarlet", "Mustard", "White", "Green", "Peacock", "Plum"
    };

    private BufferedImage[] imgPersonagens = new BufferedImage[6];
    private JCheckBox[] checkBoxes = new JCheckBox[6];
    private boolean carregarSave;
    private File arquivoSave;

    public TelaPersonagens(boolean carregarSave) {
        this(carregarSave, null);
    }

    public TelaPersonagens(boolean carregarSave, File arquivoSave) {
        this.carregarSave  = carregarSave;
        this.arquivoSave   = arquivoSave;

        setTitle("Clue - Personagens");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        carregarImagens();
        configurarTela();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void carregarImagens() {
        for (int i = 0; i < 6; i++) {
            try {
                imgPersonagens[i] = ImageIO.read(
                    new File("src/Imagens/Suspeitos/" + ARQUIVOS[i] + ".jpg"));
            } catch (Exception e) {
                System.out.println("Erro ao carregar " + ARQUIVOS[i] + ": " + e.getMessage());
            }
        }
    }

    private void configurarTela() {
        setLayout(null);
        getContentPane().setPreferredSize(new Dimension(1400, 1050));
        getContentPane().setBackground(Color.DARK_GRAY);

        // Exibe os 6 personagens em duas linhas de 3
        int xInicio = 100, yInicio = 80;
        int largura = 180, altura = 280, espX = 220, espY = 340;

        for (int i = 0; i < 6; i++) {
            int col = i % 3;
            int row = i / 3;
            int x = xInicio + col * espX;
            int y = yInicio + row * espY;

            // Imagem do personagem via label + ImageIcon (janelas iniciais podem usar Swing)
            if (imgPersonagens[i] != null) {
                Image img = imgPersonagens[i].getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
                JLabel lblImg = new JLabel(new ImageIcon(img));
                lblImg.setBounds(x, y, largura, altura);
                add(lblImg);
            }

            // Checkbox para selecionar o personagem
            checkBoxes[i] = new JCheckBox(NOMES[i]);
            checkBoxes[i].setForeground(Color.WHITE);
            checkBoxes[i].setOpaque(false);
            checkBoxes[i].setFont(new Font("Arial", Font.BOLD, 13));
            checkBoxes[i].setBounds(x, y + altura + 5, largura, 25);
            add(checkBoxes[i]);
        }

        // Botão Jogar
        JButton btnJogar = new JButton("Jogar");
        btnJogar.setBounds(630, 950, 140, 45);
        btnJogar.setFont(new Font("Arial", Font.BOLD, 16));
        btnJogar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarJogo();
            }
        });
        add(btnJogar);
    }

    private void iniciarJogo() {
        List<Jogador> selecionados = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (checkBoxes[i].isSelected()) {
                selecionados.add(new Jogador(NOMES[i]));
            }
        }

        if (selecionados.size() < 3 || selecionados.size() > 6) {
            JOptionPane.showMessageDialog(this,
                "Selecione entre 3 e 6 personagens.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jogo jogo = new Jogo(selecionados);

        if (carregarSave && arquivoSave != null) {
            CarregarSave.carregar(jogo, arquivoSave);
        }

        dispose();
        new TelaTabuleiro(jogo);
    }
}