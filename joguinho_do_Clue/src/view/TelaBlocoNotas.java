package view;

import model.Jogador;
import javax.swing.*;
import java.awt.*;

public class TelaBlocoNotas extends JDialog {

    private Jogador jogador;
    private JTextArea areaTexto;

    public TelaBlocoNotas(JFrame parent, Jogador jogador) {
        super(parent, "Bloco de Notas - " + jogador.getNome(), true);
        this.jogador = jogador;

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.DARK_GRAY);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        areaTexto = new JTextArea(15, 30);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaTexto.setBackground(new Color(40, 40, 40));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setCaretColor(Color.WHITE);

        // Carrega anotações existentes
        for (String anotacao : jogador.getBlocoDeNotas()) {
            areaTexto.append(anotacao + "\n");
        }

        JScrollPane scroll = new JScrollPane(areaTexto);
        painel.add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.DARK_GRAY);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            jogador.getBlocoDeNotas().clear();
            for (String linha : areaTexto.getText().split("\n")) {
                if (!linha.trim().isEmpty())
                    jogador.anotarNoBloco(linha);
            }
            dispose();
        });

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        rodape.add(btnSalvar);
        rodape.add(btnFechar);
        painel.add(rodape, BorderLayout.SOUTH);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(parent);
    }
}