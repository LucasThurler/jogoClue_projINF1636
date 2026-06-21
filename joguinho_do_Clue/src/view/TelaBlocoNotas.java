package view;

import model.Jogo;
import model.Jogador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TelaBlocoNotas extends JDialog {

    private Jogo jogo;
    private Jogador jogador;
    private JTextArea areaTexto;

    public TelaBlocoNotas(JFrame parent, Jogo jogo) {
        super(parent, "Bloco de Notas - " + jogo.getJogadorAtual().getNome(), true);
        this.jogo    = jogo;
        this.jogador = jogo.getJogadorAtual();

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.DARK_GRAY);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        areaTexto = new JTextArea(15, 30);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaTexto.setBackground(new Color(40, 40, 40));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setCaretColor(Color.WHITE);

        for (String anotacao : jogador.getBlocoDeNotas()) {
            areaTexto.append(anotacao + "\n");
        }

        JScrollPane scroll = new JScrollPane(areaTexto);
        painel.add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.DARK_GRAY);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> linhas = new ArrayList<>();
                for (String linha : areaTexto.getText().split("\n")) {
                    if (!linha.trim().isEmpty()) linhas.add(linha);
                }
                jogo.atualizarBlocoDeNotas(jogador.getNome(), linhas);
                dispose();
            }
        });

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        rodape.add(btnSalvar);
        rodape.add(btnFechar);
        painel.add(rodape, BorderLayout.SOUTH);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}