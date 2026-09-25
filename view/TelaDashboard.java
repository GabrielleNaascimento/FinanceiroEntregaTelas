package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints; //aproveitei e peguei mais importações do próprio awt

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

public class TelaDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    public TelaDashboard() { // configurações da janela, como altura, largura, titulo etc
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

        
        // aqui é o botao selecionado, que no caso dessa página é o dashboard
        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(SwingConstants.LEFT);
        btnDashboard.setBackground(Color.WHITE);
        btnDashboard.setForeground(new Color(27, 54, 93));
        btnDashboard.setFont(new Font("SansSerif", Font.BOLD, 12));
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));

        //os demais botoes serao criados por metodo auxiliar
        
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

        JButton btnFiscal = criarBotaoMenu("Fiscal");
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

        JLabel lblUser = new JLabel("Jefferson Riper (Administrador)");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));

        header.add(lblUser, BorderLayout.EAST);
        area.add(header, BorderLayout.NORTH);

        // Painel Interno Vertical
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel lblTitulo = new JLabel("<html><h2 style='margin:0;'>Módulo Financeiro</h2><span style='color:gray;'>Visão geral financeira da organização</span></html>");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(lblTitulo);
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' de card
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' dos graficos e da despesa
        conteudo.add(criarGraficos());
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' das tabelas e o alerta
        conteudo.add(criarTabelaEAlertas());

        area.add(conteudo, BorderLayout.CENTER);
        return area;
    }

    // cards de cima
    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 4, 15, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        painel.add(cardMetrica("Saldo Atual", "R$ 284.750,00", "+4.2% vs. mês anterior", new Color(16, 185, 129)));
        painel.add(cardMetrica("Receitas", "R$ 156.300,00", "+8.5% vs. mês anterior", new Color(16, 185, 129)));
        painel.add(cardMetrica("Despesas", "R$ 98.420,00", "-2.1% vs. mês anterior", new Color(239, 68, 68)));
        painel.add(cardMetrica("Fluxo de Caixa", "R$ 57.880,00", "+12.4% vs. mês anterior", new Color(16, 185, 129)));

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

    // graficos do SWING (Graphics2D)
    private JPanel criarGraficos() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // preenche todo o espaço vertical e horizontal
        gbc.weighty = 1.0; // expande o grafico ate ocupar 100% da altura

        // grafico de linhas (despesas x receitas)
        gbc.gridx = 0; // define a primeira coluna 
        gbc.weightx = 0.65; //tamanho
        gbc.insets = new Insets(0, 0, 0, 15);
        painel.add(new PainelGraficoLinhas(), gbc); // chama um metodo que vai desenhar as linhas

        // grafico de pizza 
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        painel.add(new PainelGraficoPizza(), gbc);

        return painel;
    }

    // tabela e alertas
    private JPanel criarTabelaEAlertas() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

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

        String[] colunas = {"Descrição", "Fornecedor", "Vencimento", "Valor", "Status"};
        Object[][] dados = {
            {"Servidor AWS Cloud", "Amazon WS", "15/10/2025", "R$ 8.450,00", "Pendente"},
            {"Licenças ERP Office", "Microsoft", "18/10/2025", "R$ 3.200,00", "Pendente"},
            {"Consultoria Contábil", "Lopes Adv.", "20/10/2025", "R$ 5.000,00", "Paga"},
            {"Aluguel Escritório", "Imob. Central", "22/10/2025", "R$ 12.000,00", "Pendente"},
            {"Energia Elétrica", "Copel S/A", "25/10/2025", "R$ 1.850,00", "Vencida"}
        };

        DefaultTableModel model = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);

        // Estiliza coluna Status
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isSel, hasFocus, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String val = (String) v;
                if ("Paga".equals(val)) {
                    l.setBackground(new Color(209, 250, 229));
                    l.setForeground(new Color(6, 95, 70));
                } else if ("Vencida".equals(val)) {
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

        JLabel title = new JLabel("Alertas Críticos");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(254, 242, 242));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(239, 68, 68)),
            new EmptyBorder(8, 8, 8, 8)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel msg = new JLabel("<html><b>Boleto Copel Vencido</b><br/><small style='color:gray;'>Atraso de 3 dias no pagamento.</small></html>");
        card.add(msg, BorderLayout.CENTER);

        painel.add(title);
        painel.add(Box.createVerticalStrut(12));
        painel.add(card);

        return painel;
    }

    // classe para desenho do grafico de linhas (só SWING)
    private static class PainelGraficoLinhas extends JPanel {
        public PainelGraficoLinhas() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(Color.BLACK);
            g2.drawString("Receitas x Despesas", 12, 22);

            int w = getWidth() - 40;
            int h = getHeight() - 60;
            int startX = 20;
            int startY = 40;

            // Linha de base
            g2.setColor(new Color(226, 232, 240));
            g2.drawLine(startX, startY + h, startX + w, startY + h);

            // Linha Receitas (Verde)
            g2.setColor(new Color(16, 185, 129));
            g2.drawLine(startX, startY + (int)(h * 0.7), startX + w, startY + (int)(h * 0.2));

            // Linha Despesas (Vermelha)
            g2.setColor(new Color(239, 68, 68));
            g2.drawLine(startX, startY + (int)(h * 0.8), startX + w, startY + (int)(h * 0.9));
        }
    }

    // classe para desenhar o grafico de pizza (só SWING)
    private static class PainelGraficoPizza extends JPanel {
        public PainelGraficoPizza() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(Color.BLACK);
            g2.drawString("Despesas por Categoria", 12, 22);

            int size = Math.min(getWidth(), getHeight()) - 70;
            int x = 20;
            int y = 45;

            // fatias do grafico de pizza
            g2.setColor(new Color(37, 99, 235)); // Folha Pag. (35%)
            g2.fillArc(x, y, size, size, 0, 126);

            g2.setColor(new Color(16, 185, 129)); // Fornecedores (25%)
            g2.fillArc(x, y, size, size, 126, 90);

            g2.setColor(new Color(245, 158, 11)); // Impostos (20%)
            g2.fillArc(x, y, size, size, 216, 72);

            g2.setColor(new Color(239, 68, 68)); // Operacional (12%)
            g2.fillArc(x, y, size, size, 288, 43);

            g2.setColor(new Color(168, 85, 247)); // Outros (8%)
            g2.fillArc(x, y, size, size, 331, 29);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaDashboard().setVisible(true);
        });
    }
}