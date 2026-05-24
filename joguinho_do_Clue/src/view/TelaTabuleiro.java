package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;

public class TelaTabuleiro extends JFrame {

    private Jogo jogo;
    private PainelTabuleiro painelTabuleiro;

    public TelaTabuleiro(Jogo jogo) {
        this.jogo = jogo;
        setTitle("Clue - Jogo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        painelTabuleiro = new PainelTabuleiro(jogo);
        add(painelTabuleiro);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Painel principal onde tudo é desenhado via Graphics2D
    class PainelTabuleiro extends JPanel implements MouseListener {

        private Jogo jogo;
        private BufferedImage imgTabuleiro;
        private Map<Integer, BufferedImage> imgDados;
        private int[] valoresDados;
        private Set<Integer> casasAlcancaveis;
        private boolean dadosLancados;

        // Cores dos personagens
        private static final Map<String, Color> CORES_PERSONAGENS = new HashMap<>();
        static {
            CORES_PERSONAGENS.put("Coronel Mustard", new Color(255, 215, 0));   // Amarelo
            CORES_PERSONAGENS.put("Srta. Scarlet",   new Color(220, 20,  60));  // Vermelho
            CORES_PERSONAGENS.put("Professor Plum",  new Color(128, 0,  128));  // Roxo
            CORES_PERSONAGENS.put("Reverendo Green", new Color(34,  139, 34));  // Verde
            CORES_PERSONAGENS.put("Sra. White",      new Color(240, 240, 240)); // Branco
            CORES_PERSONAGENS.put("Sra. Peacock",    new Color(0,   0,  205)); // Azul
        }

        // Componentes Swing permitidos
        private JButton btnJogarDados;
        private JComboBox<Integer> combo1, combo2;
        private JLabel lblJogadorAtual;
        private JLabel lblPassos;

        PainelTabuleiro(Jogo jogo) {
            this.jogo = jogo;
            this.valoresDados = new int[]{1, 1};
            this.casasAlcancaveis = new HashSet<>();
            this.dadosLancados = false;

            carregarImagens();
            configurarLayout();
            addMouseListener(this);
        }

        private void carregarImagens() {
            try {
                imgTabuleiro = ImageIO.read(new File("src/Imagens/Tabuleiros/Tabuleiro-Clue-A.jpg"));
            } catch (Exception e) {
                System.out.println("Erro ao carregar tabuleiro: " + e.getMessage());
            }

            imgDados = new HashMap<>();
            for (int i = 1; i <= 6; i++) {
                try {
                    imgDados.put(i, ImageIO.read(new File("src/Imagens/Tabuleiros/dado" + i + ".jpg")));
                } catch (Exception e) {
                    System.out.println("Erro ao carregar dado " + i + ": " + e.getMessage());
                }
            }
        }

        private void configurarLayout() {
            setLayout(null);
            setPreferredSize(new Dimension(1400, 1050));

            // Label jogador atual
            lblJogadorAtual = new JLabel("Vez de: " + jogo.getJogadorAtual().getNome());
            lblJogadorAtual.setFont(new Font("Arial", Font.BOLD, 14));
            lblJogadorAtual.setBounds(750, 60, 300, 25);
            add(lblJogadorAtual);

            // Label passos
            lblPassos = new JLabel("Passos: -");
            lblPassos.setFont(new Font("Arial", Font.PLAIN, 14));
            lblPassos.setBounds(750, 90, 200, 25);
            add(lblPassos);

            // Botão jogar dados
            btnJogarDados = new JButton("Jogar Dados");
            btnJogarDados.setBounds(750, 130, 150, 35);
            btnJogarDados.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    lancarDados();
                }
            });
            add(btnJogarDados);

            // ComboBoxes para definir valores manualmente (para testes)
            JLabel lblDado1 = new JLabel("Dado 1:");
            lblDado1.setBounds(920, 130, 60, 35);
            add(lblDado1);

            combo1 = new JComboBox<>(new Integer[]{1,2,3,4,5,6});
            combo1.setBounds(975, 130, 60, 35);
            add(combo1);

            JLabel lblDado2 = new JLabel("Dado 2:");
            lblDado2.setBounds(1050, 130, 60, 35);
            add(lblDado2);

            combo2 = new JComboBox<>(new Integer[]{1,2,3,4,5,6});
            combo2.setBounds(1105, 130, 60, 35);
            add(combo2);

            // Botão próximo jogador
            JButton btnProximo = new JButton("Próximo");
            btnProximo.setBounds(750, 180, 150, 35);
            btnProximo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    avancarJogador();
                }
            });
            add(btnProximo);
        }

        private void lancarDados() {
            // Usa os valores dos combos (para testes) ou aleatorio
            boolean usarCombo = (combo1.getSelectedIndex() > 0 || combo2.getSelectedIndex() > 0);
            if (usarCombo) {
                valoresDados[0] = (Integer) combo1.getSelectedItem();
                valoresDados[1] = (Integer) combo2.getSelectedItem();
            } else {
                Dado d1 = new Dado(6);
                Dado d2 = new Dado(6);
                valoresDados[0] = d1.lancar();
                valoresDados[1] = d2.lancar();
            }

            int totalPassos = valoresDados[0] + valoresDados[1];
            lblPassos.setText("Passos: " + totalPassos);

            casasAlcancaveis = jogo.getMapeaCasas().mapearCasas(
                valoresDados,
                jogo.getJogadorAtual().getPosicaoAtual()
            );

            dadosLancados = true;
            btnJogarDados.setEnabled(false);
            repaint();
        }

        private void avancarJogador() {
            jogo.proximoJogador();
            dadosLancados = false;
            casasAlcancaveis = new HashSet<>();
            btnJogarDados.setEnabled(true);
            lblJogadorAtual.setText("Vez de: " + jogo.getJogadorAtual().getNome());
            lblPassos.setText("Passos: -");
            combo1.setSelectedIndex(0);
            combo2.setSelectedIndex(0);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fundo
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Tabuleiro via drawImage (obrigatório pelo professor)
            if (imgTabuleiro != null) {
                int largura  = TabuleiroCasas.COLS * TabuleiroCasas.CELL_SIZE;
                int altura   = TabuleiroCasas.ROWS * TabuleiroCasas.CELL_SIZE;
                g2.drawImage(imgTabuleiro,
                    TabuleiroCasas.GRID_X0,
                    TabuleiroCasas.GRID_Y0,
                    largura, altura, null);
            }

            // Destaca casas alcançáveis em amarelo semitransparente
            if (dadosLancados) {
                g2.setColor(new Color(255, 255, 0, 80));
                for (int casaId : casasAlcancaveis) {
                    if (!TabuleiroCasas.isComodo(casaId)) {
                        int[] pos = TabuleiroCasas.cellPos(casaId);
                        int px = TabuleiroCasas.GRID_X0 + pos[1] * TabuleiroCasas.CELL_SIZE;
                        int py = TabuleiroCasas.GRID_Y0 + pos[0] * TabuleiroCasas.CELL_SIZE;
                        g2.fillRect(px, py, TabuleiroCasas.CELL_SIZE, TabuleiroCasas.CELL_SIZE);
                    }
                }
            }

            // Desenha os piões (círculos coloridos via Graphics2D)
            for (Jogador j : jogo.getJogadores()) {
                Color cor = CORES_PERSONAGENS.getOrDefault(j.getNome(), Color.GRAY);
                int[] pixel = TabuleiroCasas.casaParaPixel(j.getPosicaoAtual());
                int raio = 8;
                // Preenchimento
                g2.setColor(cor);
                g2.fillOval(pixel[0] - raio, pixel[1] - raio, raio * 2, raio * 2);
                // Borda preta
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(pixel[0] - raio, pixel[1] - raio, raio * 2, raio * 2);
            }

            // Destaca pião do jogador atual com borda mais grossa
            Jogador atual = jogo.getJogadorAtual();
            Color corAtual = CORES_PERSONAGENS.getOrDefault(atual.getNome(), Color.GRAY);
            int[] pixelAtual = TabuleiroCasas.casaParaPixel(atual.getPosicaoAtual());
            int raio = 8;
            g2.setColor(corAtual);
            g2.fillOval(pixelAtual[0] - raio, pixelAtual[1] - raio, raio * 2, raio * 2);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(pixelAtual[0] - raio, pixelAtual[1] - raio, raio * 2, raio * 2);

            // Dados via drawImage (obrigatório pelo professor)
            if (dadosLancados && imgDados.containsKey(valoresDados[0])
                               && imgDados.containsKey(valoresDados[1])) {
                g2.drawImage(imgDados.get(valoresDados[0]), 750, 230, 60, 60, null);
                g2.drawImage(imgDados.get(valoresDados[1]), 820, 230, 60, 60, null);
            }

            // Indicação visual do jogador da vez (fundo colorido atrás do painel lateral)
            g2.setColor(CORES_PERSONAGENS.getOrDefault(atual.getNome(), Color.GRAY));
            g2.fillRect(735, 50, 5, getHeight() - 50);
        }

        // Clique do mouse -> tenta deslocar o pião
        public void mouseClicked(MouseEvent e) {
            if (!dadosLancados) return;

            int casaClicada = TabuleiroCasas.pixelParaCasa(e.getX(), e.getY());
            if (casaClicada == -1) return;

            DeslocarPiao dp = new DeslocarPiao();
            boolean moveu = dp.deslocarPiao(
                jogo.getJogadorAtual(),
                casaClicada,
                casasAlcancaveis
            );

            if (moveu) {
                dadosLancados = false;
                casasAlcancaveis = new HashSet<>();
                btnJogarDados.setEnabled(false);
                repaint();
            }
        }

        public void mousePressed(MouseEvent e)  {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e)  {}
        public void mouseExited(MouseEvent e)   {}
    }
}