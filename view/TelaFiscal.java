package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TelaFiscal extends JFrame {

    private static final long serialVersionUID = 1L;

    public TelaFiscal() { // configurações da janela, como altura, largura, titulo etc
        setTitle("ERP Financeiro - Módulo Financeiro"); 
        setSize(1280, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 'paineis principais', painel lateral e o centro
        add(criarSidebar(), BorderLayout.WEST);
        add(criarAreaPrincipal(), BorderLayout.CENTER);
    }

    // menu lateral
    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(27, 54, 93)); // ao se colocar o nome do 'painel' com setBackground(new Color) é possível colocar cores
        sidebar.setPreferredSize(new Dimension(220, 0)); // e o mesmo vale pros demais sets
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // serve pra organizar as caixas 'empilhadas' para o eixo Y
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Logo
        JLabel lblLogo = new JLabel("<html><b>ERP Finance</b><br/><small style='color:#A0AEC0;'>MÓDULO FINANCEIRO</small></html>"); // da pra colocar conf html ao se criar labels 
        lblLogo.setForeground(Color.WHITE); //seta as cores das coisas do primeiro plano( tipo texto) para branco
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 15)); //familia da fonte, o .PLAIN serve pra nn deixar o texto ficar negrito ou italico se houver algo q o altere
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT); //alinha o texto pra esquerda
        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30)); // cria um espaçamento invisivel

        
        //os demais botoes serao criados por metodo auxiliar
        JButton btnDashboard = criarBotaoMenu("Dashboard");
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));
        
        JButton btnContasPagar = criarBotaoMenu("Contas a Pagar e Receber");
        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnFluxoCaixa = criarBotaoMenu("Fluxo de Caixa");
        sidebar.add(btnFluxoCaixa);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnConciliacao = criarBotaoMenu("Conciliação Bancária");
        sidebar.add(btnConciliacao);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnRelatorios = criarBotaoMenu("Relatórios");
        sidebar.add(btnRelatorios);
        sidebar.add(Box.createVerticalStrut(5));

        // aqui é o botao selecionado, que no caso dessa página é o fiscal
        JButton btnFiscal = new JButton("Fiscal");
        btnFiscal.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnFiscal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnFiscal.setFocusPainted(false);
        btnFiscal.setHorizontalAlignment(SwingConstants.LEFT);
        btnFiscal.setBackground(Color.WHITE);
        btnFiscal.setForeground(new Color(27, 54, 93));
        btnFiscal.setFont(new Font("SansSerif", Font.BOLD, 12));
        sidebar.add(btnFiscal);
        sidebar.add(Box.createVerticalStrut(5));

        
        return sidebar;
        

    }
    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(new Color(203, 213, 225));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }

    
    // painel central
    private JPanel criarAreaPrincipal() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(new Color(245, 247, 250));

        // cabeçalho ou header pros intimos
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false); //deixa transparente
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JTextField txtBusca = new JTextField(" Buscar transações, contas...");
        txtBusca.setPreferredSize(new Dimension(280, 32));
        txtBusca.setForeground(Color.GRAY);

        JLabel lblUser = new JLabel("Jefferson Riper (Administrador)");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));

        header.add(txtBusca, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);
        area.add(header, BorderLayout.NORTH);

        // Painel Interno Vertical
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel lblTitulo = new JLabel("<html><h2 style='margin:0;'>Gestão Fiscal e Tributária</h2><span style='color:gray;'>Apuração de impostos federais, estaduais e municipais</span></html>");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(lblTitulo);
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' de card
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' dos graficos e da despesa
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' das tabelas e o alerta
        conteudo.add(criarTabelaEAlertas());

        area.add(conteudo, BorderLayout.CENTER);
        return area;
    }

    // cards de cima
    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        painel.add(cardMetrica("Total Impostos", "R$ 42.680,00", "+8.2% vs. mês anterior", new Color(34, 99, 212)));
        painel.add(cardMetrica("Pagos", "R$ 28.400,00", "+12.4% vs. mês anterior", new Color(111, 199, 101)));
        painel.add(cardMetrica("Pendentes", "R$ 14.280,00", "-4.5% vs. mês anterior", new Color(224, 78, 34)));

        return painel;
    }
    // metoxo auxiliar que vai criar os cards superiores
    private JPanel cardMetrica(String titulo, String valor, String variacao, Color corVariacao) {
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 17));

        JLabel var = new JLabel(variacao);
        var.setFont(new Font("SansSerif", Font.BOLD, 11));
        var.setForeground(corVariacao);

        card.add(t);
        card.add(v);
        card.add(var);
        return card;
    }

    // tabela e alertas
    private JPanel criarTabelaEAlertas() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 315));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // tabela
        gbc.gridx = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(0, 0, 0, 15);
        painel.add(criarPainelTabela(), gbc);

        // alerta 
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 0);
        painel.add(criarPainelAlertas(), gbc);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Próximas Contas a Pagar");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        painel.add(title, BorderLayout.NORTH);

        String[] colunas = {"Imposto", "Competência", "Vencimento", "Base de Cálculo", "Alíquota", "Valor", "Status"};
        Object[][] dados = {
            {"IRPJ", "09/2025", "25/10/2025", "R$ 156.300,00", "15.0%", "R$ 23.445,00", "Pago"},
            {"CSLL", "09/2025", "25/10/2025", "R$ 156.300,00", "9.0%", "R$ 14.067,00", "Pago"},
            {"PIS", "09/2025", "25/10/2025", "R$ 156.300,00", "0.65%", "R$ 1.015,95", "Pago"},
            {"COFINS", "09/2025", "25/10/2025", "R$ 156.300,00", "3.0%", "R$ 4.689,00", "Pago"},
            {"ISS", "09/2025", "10/10/2025", "R$ 85.000,00", "5.0%", "R$ 4.250,00", "Pago"},
            {"ICMS", "09/2025", "20/10/2025", "R$ 71.300,00", "12.0%", "R$ 8.556,00", "Pendente"},
            {"IRRF (S/Serviços)", "09/2025", "20/10/2025", "R$ 12.000,00", "1.5%", "R$ 180,00", "Pendente"},
            {"GPS (INSS Patronal)", "08/2025", "20/09/2025", "R$ 45.200,00", "20.0%", "R$ 9.040,00", "Vencido"}
        };

        DefaultTableModel model = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);

        // Estiliza coluna Status
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isSel, hasFocus, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String val = (String) v;
                if ("Pago".equals(val)) {
                    l.setBackground(new Color(209, 250, 229));
                    l.setForeground(new Color(6, 95, 70));
                } else if ("Vencido".equals(val)) {
                    l.setBackground(new Color(254, 226, 226));
                    l.setForeground(new Color(153, 27, 27));
                } else {
                    l.setBackground(new Color(254, 243, 199));
                    l.setForeground(new Color(146, 64, 14));
                }
                l.setOpaque(true);
                return l;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        painel.add(sp, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelAlertas() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Alertas de Vencimento");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel card1 = new JPanel(new BorderLayout());
        card1.setBackground(new Color(254, 242, 242));
        card1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(239, 68, 68)),
            new EmptyBorder(8, 8, 8, 8)
        ));
        card1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel msg1 = new JLabel("<html><b>ICMS em Atraso</b><br/><small style='color:gray;'>ICMS sobre vendas de insumos vencido em 20/10. Sujeito a multas.</small><br/><b style=\"color: #961006;\">ALTA</b></html>");
        card1.add(msg1, BorderLayout.CENTER);
        
        JPanel card2 = new JPanel(new BorderLayout());
        card2.setBackground(new Color(254, 242, 242));
        card2.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(239, 151, 68)),
            new EmptyBorder(8, 8, 8, 8)
        ));
        card2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel msg2 = new JLabel("<html><b>Geração DAS Simples</b><br/><small style='color:gray;'>Vencimento do DAS unificado dia 25/10. Valor estimado: R$ 14.280,00.</small><br/><b style=\"color: #eb7b0c;\">MÉDIA</b></html>");
        card2.add(msg2, BorderLayout.CENTER);
        
        JPanel card3 = new JPanel(new BorderLayout());
        card3.setBackground(new Color(254, 254, 242));
        card3.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(239, 228, 68)),
            new EmptyBorder(8, 8, 8, 8)
        ));
        card3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel msg3 = new JLabel("<html><b>EFD Reinf pendente</b><br/><small style='color:gray;'>Guia de retenções de terceiros deve ser transmitida até dia 30/10.</small><br/><b style=\"color: #ffcc00;\">BAIXA</b></html>");
        card3.add(msg3, BorderLayout.CENTER);
        
        painel.add(title);
        painel.add(Box.createVerticalStrut(12));
        painel.add(card1);
        painel.add(card2);
        painel.add(card3);
        
        return painel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaFiscal().setVisible(true);
        });
    }
}