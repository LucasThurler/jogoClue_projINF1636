package view;

import model.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TelaCartas extends JDialog {

    private static final Map<String, String> MAPA_IMAGENS = new HashMap<>();
    static {
        // Suspeitos
        MAPA_IMAGENS.put("Coronel Mustard", "src/Imagens/Suspeitos/Mustard.jpg");
        MAPA_IMAGENS.put("Srta. Scarlet",   "src/Imagens/Suspeitos/Scarlet.jpg");
        MAPA_IMAGENS.put("Professor Plum",  "src/Imagens/Suspeitos/Plum.jpg");
        MAPA_IMAGENS.put("Reverendo Green", "src/Imagens/Suspeitos/Green.jpg");
        MAPA_IMAGENS.put("Sra. White",      "src/Imagens/Suspeitos/White.jpg");
        MAPA_IMAGENS.put("Sra. Peacock",    "src/Imagens/Suspeitos/Peacock.jpg");
        // Armas
        MAPA_IMAGENS.put("Corda",           "src/Imagens/Armas/Corda.jpg");
        MAPA_IMAGENS.put("Cano de Chumbo",  "src/Imagens/Armas/Cano.jpg");
        MAPA_IMAGENS.put("Faca",            "src/Imagens/Armas/Faca.jpg");
        MAPA_IMAGENS.put("Chave Inglesa",   "src/Imagens/Armas/ChaveInglesa.jpg");
        MAPA_IMAGENS.put("Castical",        "src/Imagens/Armas/Castical.jpg");
        MAPA_IMAGENS.put("Revolver",        "src/Imagens/Armas/Revolver.jpg");
        // Comodos
        MAPA_IMAGENS.put("Cozinha",           "src/Imagens/Comodos/Cozinha.jpg");
        MAPA_IMAGENS.put("Sala de Música",    "src/Imagens/Comodos/SalaDeMusica.jpg");
        MAPA_IMAGENS.put("Jardim de Inverno", "src/Imagens/Comodos/JardimInverno.jpg");
        MAPA_IMAGENS.put("Salão de Jogos",    "src/Imagens/Comodos/SalaoDeJogos.jpg");
        MAPA_IMAGENS.put("Sala de Jantar",    "src/Imagens/Comodos/SalaDeJantar.jpg");
        MAPA_IMAGENS.put("Biblioteca",        "src/Imagens/Comodos/Biblioteca.jpg");
        MAPA_IMAGENS.put("Entrada",           "src/Imagens/Comodos/Entrada.jpg");
        MAPA_IMAGENS.put("Sala de Estar",     "src/Imagens/Comodos/SalaDeEstar.jpg");
        MAPA_IMAGENS.put("Escritório",        "src/Imagens/Comodos/Escritorio.jpg");
    }

    public TelaCartas(JFrame parent, Jogador jogador) {
        super(parent, "Cartas de " + jogador.getNome(), true);

        List<Carta> mao = jogador.getMao();

        PainelCartas painel = new PainelCartas(mao);
        setContentPane(painel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    class PainelCartas extends JPanel {

        private List<Carta> mao;
        private List<BufferedImage> imagens;

        private static final int CARD_W  = 120;
        private static final int CARD_H  = 180;
        private static final int PADDING = 20;

        PainelCartas(List<Carta> mao) {
            this.mao     = mao;
            this.imagens = new java.util.ArrayList<>();

            for (Carta c : mao) {
                String caminho = MAPA_IMAGENS.get(c.getNome());
                BufferedImage img = null;
                if (caminho != null) {
                    try {
                        img = ImageIO.read(new File(caminho));
                    } catch (Exception e) {
                        System.out.println("Erro ao carregar imagem: " + caminho);
                    }
                }
                imagens.add(img);
            }

            int cols    = Math.min(mao.size(), 4);
            int rows    = (int) Math.ceil(mao.size() / 4.0);
            int largura = cols * (CARD_W + PADDING) + PADDING;
            int altura  = rows * (CARD_H + PADDING + 20) + PADDING + 50;
            setPreferredSize(new Dimension(Math.max(largura, 300), Math.max(altura, 300)));
            setBackground(Color.DARK_GRAY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Titulo
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Suas cartas:", PADDING, 30);

            if (mao.isEmpty()) {
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.setColor(Color.GRAY);
                g2.drawString("Nenhuma carta.", PADDING, 60);
                return;
            }

            for (int i = 0; i < mao.size(); i++) {
                int col = i % 4;
                int row = i / 4;
                int x   = PADDING + col * (CARD_W + PADDING);
                int y   = 50 + row * (CARD_H + PADDING + 20);

                BufferedImage img = imagens.get(i);
                if (img != null) {
                    // Imagem da carta via drawImage — obrigatorio pelo professor
                    g2.drawImage(img, x, y, CARD_W, CARD_H, null);
                } else {
                    // Fallback se imagem nao carregar
                    g2.setColor(Color.GRAY);
                    g2.fillRoundRect(x, y, CARD_W, CARD_H, 10, 10);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.PLAIN, 11));
                    g2.drawString(mao.get(i).getNome(), x + 5, y + CARD_H / 2);
                }

                // Nome da carta abaixo da imagem
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                g2.drawString(mao.get(i).getNome(), x, y + CARD_H + 15);
            }
        }
    }
}