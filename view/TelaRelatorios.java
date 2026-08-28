package view;

import model.Relatorio;
import model.Relatorio.ResumoMensal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class TelaRelatorios extends JFrame {

    private static final long serialVersionUID = 1L;

    private Relatorio relatorio;

    public TelaRelatorios() {

        relatorio = new Relatorio();

        setTitle("ERP Financeiro - Módulo Financeiro");
        setSize(1280, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(criarSidebar(), BorderLayout.WEST);
        add(criarAreaPrincipal(), BorderLayout.CENTER);
    }

    private JPanel criarSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setBackground(new Color(27, 54, 93));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel lblLogo = new JLabel(
                "<html><b>ERP Finance</b><br/>" +
                "<small style='color:#A0AEC0;'>MÓDULO FINANCEIRO</small></html>"
        );

        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30));

        JButton btnDashboard = criarBotaoMenu("Dashboard");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnContasPagar = criarBotaoMenu(
                "Contas a Pagar e Receber"
        );

        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnFluxoCaixa = criarBotaoMenu(
                "Fluxo de Caixa"
        );

        sidebar.add(btnFluxoCaixa);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnConciliacao = criarBotaoMenu(
                "Conciliação Bancária"
        );

        sidebar.add(btnConciliacao);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnRelatorios = new JButton("Relatórios");

        btnRelatorios.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRelatorios.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        btnRelatorios.setFocusPainted(false);
        btnRelatorios.setHorizontalAlignment(SwingConstants.LEFT);
        btnRelatorios.setBackground(Color.WHITE);
        btnRelatorios.setForeground(new Color(27, 54, 93));
        btnRelatorios.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

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
        btn.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(new Color(203, 213, 225));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));

        return btn;
    }

    private JPanel criarAreaPrincipal() {

        JPanel area = new JPanel(new BorderLayout());

        area.setBackground(new Color(245, 247, 250));

        area.add(criarHeader(), BorderLayout.NORTH);
        area.add(criarConteudo(), BorderLayout.CENTER);

        return area;
    }

    private JPanel criarHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);
        header.setBorder(
                new EmptyBorder(15, 25, 15, 25)
        );

        JTextField txtBusca =
                new JTextField(" Buscar transações, contas...");

        txtBusca.setPreferredSize(
                new Dimension(280, 32)
        );

        txtBusca.setForeground(Color.GRAY);

        JLabel lblUser =
                new JLabel("Jefferson Riper (Administrador)");

        lblUser.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        header.add(txtBusca, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {

        JPanel conteudo = new JPanel(
                new BorderLayout(0, 15)
        );

        conteudo.setOpaque(false);

        conteudo.setBorder(
                new EmptyBorder(10, 25, 25, 25)
        );

        conteudo.add(
                criarTitulo(),
                BorderLayout.NORTH
        );

        JPanel centro = new JPanel(
                new BorderLayout(0, 15)
        );

        centro.setOpaque(false);

        centro.add(
                criarCards(),
                BorderLayout.NORTH
        );

        centro.add(
                criarParteInferior(),
                BorderLayout.CENTER
        );

        conteudo.add(
                centro,
                BorderLayout.CENTER
        );

        return conteudo;
    }

    private JPanel criarTitulo() {

        JPanel painel = new JPanel(new BorderLayout());

        painel.setOpaque(false);

        painel.setPreferredSize(
                new Dimension(0, 55)
        );

        JLabel titulo = new JLabel(
                "<html>" +
                "<b style='font-size:20px;'>Relatórios Financeiros</b>" +
                "<br/>" +
                "<span style='color:gray;'>Acesse demonstrativos e balanços consolidados do seu negócio</span>" +
                "</html>"
        );

        painel.add(
                titulo,
                BorderLayout.WEST
        );

        JComboBox<String> periodo =
                new JComboBox<>();

        periodo.addItem("Último Semestre");
        periodo.addItem("Último Ano");
        periodo.addItem("Últimos 30 dias");

        periodo.setPreferredSize(
                new Dimension(160, 32)
        );

        painel.add(
                periodo,
                BorderLayout.EAST
        );

        return painel;
    }

    private JPanel criarCards() {

        JPanel painel = new JPanel(
                new GridLayout(2, 2, 12, 12)
        );

        painel.setOpaque(false);

        painel.setPreferredSize(
                new Dimension(0, 190)
        );

        painel.add(
                criarCardRelatorio(
                        "Fluxo de Caixa",
                        "Análise detalhada de entradas e saídas de caixa previstas e realizadas."
                )
        );

        painel.add(
                criarCardRelatorio(
                        "DRE — Demonstração de Resultado",
                        "Balanço consolidado de receitas, custos, provisões e margem de lucro líquido."
                )
        );

        painel.add(
                criarCardRelatorio(
                        "Contas a Pagar e Receber",
                        "Posição consolidada de obrigações em aberto, vencidas e pagas no período."
                )
        );

        painel.add(
                criarCardRelatorio(
                        "Despesas por Categoria",
                        "Breakdown detalhado e agrupado de gastos operacionais e fixos."
                )
        );

        return painel;
    }

    private JPanel criarCardRelatorio(
            String titulo,
            String descricao) {

        JPanel card = new JPanel(
                new BorderLayout(0, 8)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(226, 232, 240)
                        ),
                        new EmptyBorder(
                                14, 16, 14, 16
                        )
                )
        );

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        JLabel lblDescricao =
                new JLabel(
                        "<html><div style='width:400px;'>" +
                        "<span style='color:#64748B;'>" +
                        descricao +
                        "</span></div></html>"
                );

        lblDescricao.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        JPanel informacoes =
                new JPanel();

        informacoes.setOpaque(false);

        informacoes.setLayout(
                new BoxLayout(
                        informacoes,
                        BoxLayout.Y_AXIS
                )
        );

        informacoes.add(lblTitulo);
        informacoes.add(
                Box.createVerticalStrut(7)
        );
        informacoes.add(lblDescricao);

        card.add(
                informacoes,
                BorderLayout.CENTER
        );

        JButton btnGerar =
                new JButton("Gerar Relatório");

        btnGerar.setPreferredSize(
                new Dimension(135, 30)
        );

        btnGerar.setFocusPainted(false);

        btnGerar.setBackground(
                new Color(37, 99, 235)
        );

        btnGerar.setForeground(Color.WHITE);

        btnGerar.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        JPanel painelBotao =
                new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        painelBotao.setOpaque(false);

        painelBotao.add(btnGerar);

        card.add(
                painelBotao,
                BorderLayout.SOUTH
        );

        return card;
    }

    private JPanel criarParteInferior() {

        JPanel painel =
                new JPanel(
                        new GridLayout(1, 2, 12, 0)
                );

        painel.setOpaque(false);

        painel.add(
                criarGraficoMensal()
        );

        painel.add(
                criarTabelaResumo()
        );

        return painel;
    }

    private JPanel criarGraficoMensal() {

        return new PainelGraficoMensal(relatorio);
    }

    private JPanel criarTabelaResumo() {

        JPanel painel =
                new JPanel(new BorderLayout());

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(226, 232, 240)
                        ),
                        new EmptyBorder(
                                12, 12, 12, 12
                        )
                )
        );

        JLabel titulo =
                new JLabel(
                        "Resumo Financeiro (Últimos 6 meses)"
                );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        titulo.setBorder(
                new EmptyBorder(
                        0, 0, 8, 0
                )
        );

        painel.add(
                titulo,
                BorderLayout.NORTH
        );

        String[] colunas = {
                "Mês",
                "Receita",
                "Despesa",
                "Lucro",
                "Margem%"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        colunas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        for (ResumoMensal item :
                relatorio.getResumoMensal()) {

            model.addRow(
                    new Object[] {
                            item.getMes(),
                            formatarMoeda(
                                    item.getReceita()
                            ),
                            formatarMoeda(
                                    item.getDespesa()
                            ),
                            formatarMoeda(
                                    item.getLucro()
                            ),
                            String.format(
                                    "%.1f%%",
                                    item.getMargem()
                            )
                    }
            );
        }

        JTable tabela =
                new JTable(model);

        tabela.setRowHeight(30);
        tabela.setShowGrid(false);

        tabela.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        tabela.getTableHeader()
                .setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                10
                        )
                );

        tabela.getTableHeader()
                .setBackground(
                        Color.WHITE
                );

        tabela.getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        criarRenderer(
                                new Color(5, 150, 105)
                        )
                );

        tabela.getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        criarRenderer(
                                new Color(239, 68, 68)
                        )
                );

        tabela.getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        criarRenderer(
                                new Color(15, 23, 42)
                        )
                );

        JScrollPane scroll =
                new JScrollPane(tabela);

        scroll.setBorder(null);

        painel.add(
                scroll,
                BorderLayout.CENTER
        );

        return painel;
    }

    private DefaultTableCellRenderer criarRenderer(
            Color cor) {

        return new DefaultTableCellRenderer() {

            @Override
            public Component
            getTableCellRendererComponent(
                    JTable tabela,
                    Object valor,
                    boolean selecionado,
                    boolean foco,
                    int linha,
                    int coluna) {

                JLabel label =
                        (JLabel) super
                                .getTableCellRendererComponent(
                                        tabela,
                                        valor,
                                        selecionado,
                                        foco,
                                        linha,
                                        coluna
                                );

                label.setForeground(cor);

                label.setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                return label;
            }
        };
    }

    private String formatarMoeda(
            double valor) {

        return String.format(
                "R$ %,.2f",
                valor
        ).replace(
                ',',
                'X'
        ).replace(
                '.',
                ','
        ).replace(
                'X',
                '.'
        );
    }

    private static class PainelGraficoMensal
            extends JPanel {

        private Relatorio relatorio;

        public PainelGraficoMensal(
                Relatorio relatorio) {

            this.relatorio = relatorio;

            setBackground(Color.WHITE);

            setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(226, 232, 240)
                            ),
                            new EmptyBorder(
                                    12, 12, 12, 12
                            )
                    )
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            13
                    )
            );

            g2.setColor(
                    new Color(15, 23, 42)
            );

            g2.drawString(
                    "Comparativo Mensal",
                    12,
                    22
            );

            g2.setColor(
                    new Color(16, 185, 129)
            );

            g2.fillRect(
                    getWidth() - 68,
                    14,
                    7,
                    7
            );

            g2.setColor(Color.GRAY);

            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            9
                    )
            );

            g2.drawString(
                    "Rec",
                    getWidth() - 56,
                    21
            );

            g2.setColor(
                    new Color(239, 68, 68)
            );

            g2.fillRect(
                    getWidth() - 38,
                    14,
                    7,
                    7
            );

            g2.setColor(Color.GRAY);

            g2.drawString(
                    "Desp",
                    getWidth() - 26,
                    21
            );

            int inicioX = 25;
            int inicioY = 45;

            int largura =
                    getWidth() - 50;

            int altura =
                    getHeight() - 85;

            g2.setColor(
                    new Color(226, 232, 240)
            );

            g2.drawLine(
                    inicioX,
                    inicioY + altura,
                    inicioX + largura,
                    inicioY + altura
            );

            int quantidade =
                    relatorio
                            .getResumoMensal()
                            .size();

            if (quantidade == 0) {
                return;
            }

            int espacamento =
                    largura / quantidade;

            if (espacamento < 28) {
                espacamento = 28;
            }

            int larguraBarra = 10;

            double maiorValor = 0;

            for (ResumoMensal item :
                    relatorio.getResumoMensal()) {

                if (item.getReceita() > maiorValor) {
                    maiorValor =
                            item.getReceita();
                }

                if (item.getDespesa() > maiorValor) {
                    maiorValor =
                            item.getDespesa();
                }
            }

            if (maiorValor == 0) {
                return;
            }

            for (int i = 0;
                 i < quantidade;
                 i++) {

                ResumoMensal item =
                        relatorio
                                .getResumoMensal()
                                .get(i);

                int x =
                        inicioX +
                        (i * espacamento) +
                        15;

                int alturaReceita =
                        (int) (
                                (item.getReceita()
                                / maiorValor)
                                * altura
                        );

                int alturaDespesa =
                        (int) (
                                (item.getDespesa()
                                / maiorValor)
                                * altura
                        );

                g2.setColor(
                        new Color(16, 185, 129)
                );

                g2.fillRect(
                        x,
                        inicioY +
                        altura -
                        alturaReceita,
                        larguraBarra,
                        alturaReceita
                );

                g2.setColor(
                        new Color(239, 68, 68)
                );

                g2.fillRect(
                        x + 14,
                        inicioY +
                        altura -
                        alturaDespesa,
                        larguraBarra,
                        alturaDespesa
                );

                String mes =
                        item.getMes();

                String nomeMes =
                        mes.length() >= 3
                        ? mes.substring(0, 3)
                        : mes;

                g2.setColor(Color.GRAY);

                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                9
                        )
                );

                g2.drawString(
                        nomeMes,
                        x,
                        inicioY +
                        altura +
                        15
                );
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new TelaRelatorios()
                    .setVisible(true);
        });
    }
}