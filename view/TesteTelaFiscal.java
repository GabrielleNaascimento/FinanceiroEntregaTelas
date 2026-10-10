package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.FiscalRepository;
import model.FiscalRepositoryTeste;

public class TesteTelaFiscal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            FiscalRepository fonteTeste =
                new FiscalRepositoryTeste();

            TelaFiscal tela = new TelaFiscal(fonteTeste);

            JFrame janela = new JFrame("Gestão Fiscal");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setSize(1100, 650);
            janela.setLocationRelativeTo(null);
            janela.setContentPane(tela);
            janela.setVisible(true);
        });
    }
}
