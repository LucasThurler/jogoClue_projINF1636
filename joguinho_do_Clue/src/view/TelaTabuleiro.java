package view;

import model.*;
import controller.Controller;
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

    class PainelTabuleiro extends JPanel implements MouseListener, ObservadorIF {

        private Jogo jogo;
        private Controller ctrl;
        private BufferedImage imgTabuleiro;
        private Map<Integer, BufferedImage> imgDados;
        private int[] valoresDados;
        private Set<Integer> casasAlcancaveis;
        private boolean dadosLancados;
        private boolean dadosDisponiveis; // flag separada: dados foram lancados mas piao ainda nao moveu
        private boolean passagemUsada;
        private boolean palpiteFeito;

        private static final Map<String, Color> CORES_PERSONAGENS = new HashMap<>();
        static {
            CORES_PERSONAGENS.put("Coronel Mustard", new Color(255, 215, 0));
            CORES_PERSONAGENS.put("Srta. Scarlet",   new Color(220, 20,  60));
            CORES_PERSONAGENS.put("Professor Plum",  new Color(128, 0,  128));
            CORES_PERSONAGENS.put("Reverendo Green", new Color(34,  139, 34));
            CORES_PERSONAGENS.put("Sra. White",      new Color(240, 240, 240));
            CORES_PERSONAGENS.put("Sra. Peacock",    new Color(0,   0,  205));
        }

        private JButton btnJogarDados;
        private JButton btnPassagemSecreta;
        private JButton btnSalvar;
        private JComboBox<Integer> combo1, combo2;
        private JLabel lblJogadorAtual;
        private JLabel lblPassos;
        private JLabel lblStatus;

        PainelTabuleiro(Jogo jogo) {
            this.jogo             = jogo;
            this.ctrl             = Controller.getInstance();
            this.valoresDados     = new int[]{1, 1};
            this.casasAlcancaveis = new HashSet<>();
            this.dadosLancados    = false;
            this.dadosDisponiveis = false;
            this.passagemUsada    = false;
            this.palpiteFeito     = false;

            carregarImagens();
            configurarLayout();
            addMouseListener(this);
            atualizarBotoesPassagem();
            ctrl.registra(this);
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

            lblJogadorAtual = new JLabel("Vez de: " + jogo.getJogadorAtual().getNome());
            lblJogadorAtual.setFont(new Font("Arial", Font.BOLD, 14));
            lblJogadorAtual.setBounds(750, 60, 400, 25);
            add(lblJogadorAtual);

            lblPassos = new JLabel("Passos: -");
            lblPassos.setFont(new Font("Arial", Font.PLAIN, 14));
            lblPassos.setBounds(750, 90, 200, 25);
            add(lblPassos);

            lblStatus = new JLabel("");
            lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
            lblStatus.setForeground(Color.YELLOW);
            lblStatus.setBounds(750, 115, 500, 20);
            add(lblStatus);

            btnJogarDados = new JButton("Jogar Dados");
            btnJogarDados.setBounds(750, 145, 150, 35);
            btnJogarDados.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    lancarDados();
                }
            });
            add(btnJogarDados);

            JLabel lblDado1 = new JLabel("Dado 1:");
            lblDado1.setBounds(920, 145, 60, 35);
            add(lblDado1);

            combo1 = new JComboBox<>(new Integer[]{1,2,3,4,5,6});
            combo1.setBounds(975, 145, 60, 35);
            add(combo1);

            JLabel lblDado2 = new JLabel("Dado 2:");
            lblDado2.setBounds(1050, 145, 60, 35);
            add(lblDado2);

            combo2 = new JComboBox<>(new Integer[]{1,2,3,4,5,6});
            combo2.setBounds(1105, 145, 60, 35);
            add(combo2);

            btnPassagemSecreta = new JButton("Passagem Secreta");
            btnPassagemSecreta.setBounds(750, 195, 180, 35);
            btnPassagemSecreta.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    usarPassagemSecreta();
                }
            });
            add(btnPassagemSecreta);

            // Afastado da passagem secreta
            JButton btnDadoManual = new JButton("Usar Valores");
            btnDadoManual.setBounds(1060, 195, 130, 35);
            btnDadoManual.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    lancarDadosManuais();
                }
            });
            add(btnDadoManual);

            JButton btnProximo = new JButton("Próximo");
            btnProximo.setBounds(750, 245, 150, 35);
            btnProximo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    avancarJogador();
                }
            });
            add(btnProximo);

            JButton btnVerCartas = new JButton("Ver Cartas");
            btnVerCartas.setBounds(750, 295, 150, 35);
            btnVerCartas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new TelaCartas(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelTabuleiro.this),
                        ctrl.getJogadorAtual()
                    );
                }
            });
            add(btnVerCartas);

            JButton btnSugestao = new JButton("Sugestão");
            btnSugestao.setBounds(750, 340, 150, 35);
            btnSugestao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int pos = ctrl.getJogadorAtual().getPosicaoAtual();
                    if (!TabuleiroCasas.isComodo(pos)) {
                        lblStatus.setText("Você precisa estar em um cômodo.");
                        return;
                    }
                    if (palpiteFeito) {
                        lblStatus.setText("Você já fez um palpite neste turno.");
                        return;
                    }
                    palpiteFeito = true;
                    new TelaSugestao(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelTabuleiro.this),
                        jogo
                    );
                }
            });
            add(btnSugestao);

            JButton btnBloco = new JButton("Bloco de Notas");
            btnBloco.setBounds(750, 385, 150, 35);
            btnBloco.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new TelaBlocoNotas(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelTabuleiro.this),
                        jogo
                    );
                }
            });
            add(btnBloco);

            JButton btnAcusacao = new JButton("Acusação Final");
            btnAcusacao.setBounds(750, 430, 150, 35);
            btnAcusacao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new TelaAcusacao(
                        (JFrame) SwingUtilities.getWindowAncestor(PainelTabuleiro.this)
                    );
                }
            });
            add(btnAcusacao);

            btnSalvar = new JButton("Salvar Jogo");
            btnSalvar.setBounds(750, 475, 150, 35);
            btnSalvar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File("partida.txt"));
                    int resultado = fc.showSaveDialog(null);
                    if (resultado == JFileChooser.APPROVE_OPTION) {
                        File arquivo = fc.getSelectedFile();
                        if (!arquivo.getName().endsWith(".txt")) {
                            arquivo = new File(arquivo.getAbsolutePath() + ".txt");
                        }
                        CarregarSave.salvar(ctrl.getJogo(), arquivo);
                    }
                }
            });
            add(btnSalvar);
        }

        private void atualizarBotoesPassagem() {
            int posAtual = ctrl.getJogadorAtual().getPosicaoAtual();
            boolean temPassagem = TabuleiroCasas.isComodo(posAtual)
                && TabuleiroCasas.passagemSecreta(posAtual) != -1
                && !dadosLancados
                && !passagemUsada;
            btnPassagemSecreta.setEnabled(temPassagem);
            btnJogarDados.setEnabled(!dadosLancados && !passagemUsada);
        }

        private void usarPassagemSecreta() {
            ctrl.usarPassagemSecreta();
            passagemUsada = true;
            btnPassagemSecreta.setEnabled(false);
            btnJogarDados.setEnabled(false);
            lblStatus.setText("Passagem secreta usada! Clique em Próximo.");
            lblPassos.setText("Passos: -");
            repaint();
        }

        private void lancarDados() {
            int d1 = new Dado(6).lancar();
            int d2 = new Dado(6).lancar();
            aplicarDados(d1, d2);
        }

        private void lancarDadosManuais() {
            int d1 = (Integer) combo1.getSelectedItem();
            int d2 = (Integer) combo2.getSelectedItem();
            aplicarDados(d1, d2);
        }

        private void aplicarDados(int d1, int d2) {
            valoresDados[0] = d1;
            valoresDados[1] = d2;
            ctrl.lancarDados(d1, d2);
            int totalPassos = d1 + d2;
            lblPassos.setText("Passos: " + totalPassos);
            casasAlcancaveis  = ctrl.getCasasAlcancaveis(valoresDados);
            dadosLancados     = true;
            dadosDisponiveis  = true; // marca que os dados foram lancados neste turno
            btnJogarDados.setEnabled(false);
            btnPassagemSecreta.setEnabled(false);
            lblStatus.setText("Clique numa casa destacada para mover.");
            repaint();
        }

        private void avancarJogador() {
            ctrl.passarTurno();
            if (ctrl.getJogadorAtual().isEliminado()) {
                lblStatus.setText("Jogador eliminado. Clique em Próximo.");
                btnJogarDados.setEnabled(false);
                return;
            }
            dadosLancados     = false;
            dadosDisponiveis  = false;
            passagemUsada     = false;
            palpiteFeito      = false;
            casasAlcancaveis  = new HashSet<>();
            lblJogadorAtual.setText("Vez de: " + ctrl.getJogadorAtual().getNome());
            lblPassos.setText("Passos: -");
            lblStatus.setText("");
            combo1.setSelectedIndex(0);
            combo2.setSelectedIndex(0);
            btnSalvar.setEnabled(true);
            atualizarBotoesPassagem();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());

            if (imgTabuleiro != null) {
                g2.drawImage(imgTabuleiro, 0, 0, 700, 725, null);
            }

            if (dadosLancados) {
                g2.setColor(new Color(255, 255, 0, 80));
                for (int casaId : casasAlcancaveis) {
                    if (!TabuleiroCasas.isComodo(casaId)) {
                        int[] pos = TabuleiroCasas.cellPos(casaId);
                        int px = TabuleiroCasas.GRID_X0 + pos[1] * TabuleiroCasas.CELL_SIZE;
                        int py = TabuleiroCasas.GRID_Y0 + pos[0] * TabuleiroCasas.CELL_SIZE;
                        g2.fillRect(px, py, TabuleiroCasas.CELL_SIZE, TabuleiroCasas.CELL_SIZE);
                    } else {
                        int[] centro = TabuleiroCasas.casaParaPixel(casaId);
                        g2.fillOval(centro[0] - 20, centro[1] - 20, 40, 40);
                    }
                }
            }

            for (Jogador j : jogo.getJogadores()) {
                Color cor = CORES_PERSONAGENS.getOrDefault(j.getNome(), Color.GRAY);
                int[] pixel = TabuleiroCasas.casaParaPixel(j.getPosicaoAtual());
                int raio = 8;
                g2.setColor(cor);
                g2.fillOval(pixel[0] - raio, pixel[1] - raio, raio * 2, raio * 2);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(pixel[0] - raio, pixel[1] - raio, raio * 2, raio * 2);
            }

            Jogador atual = ctrl.getJogadorAtual();
            Color corAtual = CORES_PERSONAGENS.getOrDefault(atual.getNome(), Color.GRAY);
            int[] pixelAtual = TabuleiroCasas.casaParaPixel(atual.getPosicaoAtual());
            int raio = 8;
            g2.setColor(corAtual);
            g2.fillOval(pixelAtual[0] - raio, pixelAtual[1] - raio, raio * 2, raio * 2);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(pixelAtual[0] - raio, pixelAtual[1] - raio, raio * 2, raio * 2);

            if (dadosLancados && imgDados.containsKey(valoresDados[0])
                              && imgDados.containsKey(valoresDados[1])) {
                g2.drawImage(imgDados.get(valoresDados[0]), 750, 520, 60, 60, null);
                g2.drawImage(imgDados.get(valoresDados[1]), 820, 520, 60, 60, null);
            }

            g2.setColor(CORES_PERSONAGENS.getOrDefault(atual.getNome(), Color.GRAY));
            g2.fillRect(735, 50, 5, getHeight() - 50);
        }

        @Override
        public void notify(ObservadoIF o) {
            lblJogadorAtual.setText("Vez de: " + ctrl.getJogadorAtual().getNome());

            // So recalcula casas se os dados foram lancados neste turno
            // Evita recalculo indevido quando sugestao ou outras acoes notificam o Observer
            if (dadosDisponiveis) {
                int d1 = o.get(1);
                int d2 = o.get(2);
                valoresDados[0] = d1;
                valoresDados[1] = d2;
            }

            atualizarBotoesPassagem();
            repaint();
        }

        public void mouseClicked(MouseEvent e) {
            if (!dadosLancados) return;

            int casaClicada = TabuleiroCasas.pixelParaCasa(e.getX(), e.getY());
            if (casaClicada == -1) return;

            boolean moveu = ctrl.moverJogador(casaClicada, casasAlcancaveis);

            if (moveu) {
                dadosLancados     = false;
                dadosDisponiveis  = false;
                casasAlcancaveis  = new HashSet<>();
                btnJogarDados.setEnabled(false);
                btnPassagemSecreta.setEnabled(false);
                lblStatus.setText("Peão movido! Clique em Próximo.");
                repaint();
            }
        }

        public void mousePressed(MouseEvent e)  {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e)  {}
        public void mouseExited(MouseEvent e)   {}
    }
}