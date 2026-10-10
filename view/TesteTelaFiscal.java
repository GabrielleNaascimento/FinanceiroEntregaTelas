package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TesteTelaFiscal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Gestão Fiscal");

            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            janela.setSize(1100, 650);
            janela.setLocationRelativeTo(null);
            janela.setContentPane(new TelaFiscal());
            janela.setVisible(true);
        });
    }
}
