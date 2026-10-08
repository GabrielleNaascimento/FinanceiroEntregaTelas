package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.Conciliacao;

public class TelaConciliacaoBancaria extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final Color AZUL_MENU = new Color(27, 54, 93);
    private final Color FUNDO = new Color(245, 247, 250);
    private final Color BORDA = new Color(226, 232, 240);
    private final Color TEXTO = new Color(30, 38, 52);
    private final Color BRANCO = Color.WHITE;
    private final Color VERDE = new Color(16, 185, 129);
    private final Color VERMELHO = new Color(239, 68, 68);
    private final Color LARANJA = new Color(245, 158, 11);

    private ArrayList<Conciliacao> movimentacoes;

    public TelaConciliacaoBancaria() {
        setLayout(new BorderLayout());

        movimentacoes = Conciliacao.getMovimentacoes();

        add(criarAreaPrincipal(), BorderLayout.CENTER);
    }

    private JPanel criarSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(27, 54, 93));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        JLabel lblLogo = new JLabel(
                "<html><b>ERP Finance</b><br/>"
                + "<small style='color:#A0AEC0;'>MÓDULO FINANCEIRO</small></html>"
        );

        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30));

        JButton btnDashboard = criarBotaoMenu("Dashboard");
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnContasPagar = criarBotaoMenu("Contas a Pagar e Receber");
        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnFluxoCaixa = criarBotaoMenu("Fluxo de Caixa");
        sidebar.add(btnFluxoCaixa);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnConciliacao = new JButton("Conciliação Bancária");
        btnConciliacao.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnConciliacao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnConciliacao.setFocusPainted(false);
        btnConciliacao.setHorizontalAlignment(SwingConstants.LEFT);
        btnConciliacao.setBackground(Color.WHITE);
        btnConciliacao.setForeground(new Color(27, 54, 93));
        btnConciliacao.setFont(new Font("SansSerif", Font.BOLD, 12));

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

    private JPanel criarAreaPrincipal() {

        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(FUNDO);

        area.add(criarHeader(), BorderLayout.NORTH);
        area.add(criarConteudo(), BorderLayout.CENTER);

        return area;
    }

    private JPanel criarHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblUser
                = new JLabel("Jefferson - Administrador");

        lblUser.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {

        JPanel conteudo = new JPanel();

        conteudo.setLayout(
                new BoxLayout(
                        conteudo,
                        BoxLayout.Y_AXIS
                )
        );

        conteudo.setOpaque(false);

        conteudo.setBorder(
                new EmptyBorder(
                        10,
                        25,
                        25,
                        25
                )
        );

        JLabel titulo = new JLabel(
                "<html><h2 style='margin:0;'>Conciliação Bancária</h2>"
                + "<span style='color:gray;'>Compare o extrato bancário com os lançamentos do sistema ERP</span></html>"
        );

        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // TÍTULO
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(15));

        // FILTRO DE DATAS
        conteudo.add(criarFiltroDatas());
        conteudo.add(Box.createVerticalStrut(15));

        // CARDS
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        // TABELA
        conteudo.add(criarTabela());

        return conteudo;
    }

    private JPanel criarFiltroDatas() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] opcoes = {
            "Último Semestre",
            "Último Ano",
            "Últimos 30 dias"
        };

        JComboBox<String> filtro = new JComboBox<>(opcoes);

        filtro.setPreferredSize(new Dimension(160, 28));
        filtro.setBackground(Color.WHITE);

        filtro.addActionListener(e -> {

            String opcaoSelecionada
                    = (String) filtro.getSelectedItem();

            LocalDate hoje = LocalDate.now();
            LocalDate dataInicial;

            switch (opcaoSelecionada) {

                case "Último Ano":
                    dataInicial = hoje.minusYears(1);
                    break;

                case "Últimos 30 dias":
                    dataInicial = hoje.minusDays(30);
                    break;

                case "Último Semestre":
                default:
                    dataInicial = hoje.minusMonths(6);
                    break;
            }

            ArrayList<Conciliacao> dadosFiltrados
                    = Conciliacao.listarPorPeriodo(
                            dataInicial,
                            hoje
                    );

            atualizarTabela(dadosFiltrados);

        });

        painel.add(filtro);

        return painel;
    }

    private JPanel criarCards() {

        JPanel painel = new JPanel(
                new GridLayout(
                        2,
                        3,
                        15,
                        10
                )
        );

        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        // 1. CONCILIAÇÃO TOTAL
        painel.add(criarCard(
                "Conciliação Total",
                String.valueOf(Conciliacao.getTotalConciliadas()),
                "movimentações conciliadas",
                VERDE
        ));

        painel.add(criarCard(
                "Diferença Bancária",
                String.format("R$ %.2f", Conciliacao.getTotalDiferenca()),
                "requerem verificação",
                VERMELHO
        ));

        painel.add(criarCard(
                "Pendentes a Pagar",
                String.valueOf(Conciliacao.getPendentes(Conciliacao.PAGAR)),
                "movimentações pendentes",
                LARANJA
        ));

        painel.add(criarCard(
                "Pendentes a Receber",
                String.valueOf(Conciliacao.getPendentes(Conciliacao.RECEBER)),
                "movimentações pendentes",
                LARANJA
        ));

        int atrasadosReceber = Conciliacao.getAtrasados(Conciliacao.RECEBER);
        int atrasadosPagar = Conciliacao.getAtrasados(Conciliacao.PAGAR);

        painel.add(criarCard(
                "Atraso a Receber",
                String.valueOf(atrasadosReceber),
                atrasadosReceber == 0 ? "sem atrasos" : "contas atrasadas",
                atrasadosReceber == 0 ? VERDE : VERMELHO
        ));

        painel.add(criarCard(
                "Atraso a Pagar",
                String.valueOf(atrasadosPagar),
                atrasadosPagar == 0 ? "sem atrasos" : "contas vencidas",
                atrasadosPagar == 0 ? VERDE : VERMELHO
        ));

        return painel;
    }

    private JPanel criarCard(
            String titulo,
            String valor,
            String variacao,
            Color corVariacao
    ) {

        JPanel card
                = new JPanel(
                        new GridLayout(
                                3,
                                1
                        )
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDA,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        lblTitulo.setForeground(Color.GRAY);

        JLabel lblValor = new JLabel(valor);

        lblValor.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        lblValor.setForeground(TEXTO);

        JLabel lblVariacao = new JLabel(variacao);

        lblVariacao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        lblVariacao.setForeground(corVariacao);

        card.add(lblTitulo);
        card.add(lblValor);
        card.add(lblVariacao);

        return card;
    }

    private JPanel criarTabela() {

    JPanel painel = new JPanel(new BorderLayout());

    painel.setBackground(Color.WHITE);

    painel.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDA, 1),
                    new EmptyBorder(12, 12, 12, 12)
            )
    );

    painel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel titulo = new JLabel("Confronto de Lançamentos");

    titulo.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    13
            )
    );

    titulo.setForeground(TEXTO);

    titulo.setBorder(
            new EmptyBorder(
                    0,
                    0,
                    8,
                    0
            )
    );

    painel.add(
            titulo,
            BorderLayout.NORTH
    );

    String[] colunas = {
        "Data",
        "Descrição (Extrato)",
        "Valor Extrato",
        "Descrição (Sistema)",
        "Valor Sistema",
        "Diferença",
        "Status"
    };

    modeloTabela = new DefaultTableModel(
            colunas,
            0
    ) {
        @Override
        public boolean isCellEditable(
                int row,
                int col
        ) {
            return false;
        }
    };

    DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Adiciona todas as movimentações na tabela
    for (Conciliacao m : movimentacoes) {

        String diferenca;

        if (m.getDiferenca() == 0) {
            diferenca = "R$ 0,00";
        } else {
            diferenca = formatarMoeda(m.getDiferenca());
        }

        modeloTabela.addRow(
                new Object[]{
                    m.getData().format(formato),
                    m.getDescricaoExtrato(),
                    m.getValorExtrato(),
                    m.getDescricaoSistema(),
                    m.getValorSistema(),
                    diferenca,
                    m.getStatus()
                }
        );
    }

    // Cria a tabela somente depois de adicionar os dados
    tabela = new JTable(modeloTabela);

    DefaultTableCellRenderer centro =
            new DefaultTableCellRenderer();

    centro.setHorizontalAlignment(
            SwingConstants.CENTER
    );

    tabela.getColumnModel()
            .getColumn(0)
            .setCellRenderer(centro);

    tabela.getColumnModel()
            .getColumn(1)
            .setCellRenderer(centro);

    tabela.getColumnModel()
            .getColumn(2)
            .setCellRenderer(
                    criarRendererMoeda()
            );

    tabela.getColumnModel()
            .getColumn(3)
            .setCellRenderer(centro);

    tabela.getColumnModel()
            .getColumn(4)
            .setCellRenderer(
                    criarRendererMoeda()
            );

    tabela.setRowHeight(30);

    tabela.setIntercellSpacing(
            new Dimension(0, 1)
    );

    tabela.setSelectionBackground(
            new Color(239, 246, 255)
    );

    tabela.setSelectionForeground(TEXTO);

    tabela.setShowHorizontalLines(true);
    tabela.setShowVerticalLines(false);
    tabela.setShowGrid(false);

    tabela.setFont(
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    11
            )
    );

    tabela.setForeground(TEXTO);

    tabela.getTableHeader().setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    10
            )
    );

    tabela.getTableHeader().setForeground(
            Color.GRAY
    );

    tabela.getTableHeader().setBackground(
            new Color(248, 249, 250)
    );

    tabela.getTableHeader().setPreferredSize(
            new Dimension(0, 30)
    );

    tabela.getTableHeader().setReorderingAllowed(
            false
    );

    tabela.getTableHeader().setResizingAllowed(
            false
    );

    tabela.getTableHeader().setOpaque(true);

    // Renderer da diferença
    tabela.getColumnModel()
            .getColumn(5)
            .setCellRenderer(
                    new DefaultTableCellRenderer() {

                        @Override
                        public Component getTableCellRendererComponent(
                                JTable table,
                                Object value,
                                boolean isSelected,
                                boolean hasFocus,
                                int row,
                                int column
                        ) {

                            JLabel label =
                                    (JLabel) super
                                            .getTableCellRendererComponent(
                                                    table,
                                                    value,
                                                    isSelected,
                                                    hasFocus,
                                                    row,
                                                    column
                                            );

                            label.setHorizontalAlignment(
                                    SwingConstants.CENTER
                            );

                            String texto =
                                    value.toString();

                            if (!texto.equals("R$ 0,00")) {

                                label.setForeground(
                                        VERMELHO
                                );

                            } else {

                                label.setForeground(
                                        Color.GRAY
                                );
                            }

                            return label;
                        }
                    }
            );

   // Renderer do status
tabela.getColumnModel()
        .getColumn(6)
        .setCellRenderer(
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        JLabel label =
                                (JLabel) super
                                        .getTableCellRendererComponent(
                                                table,
                                                value,
                                                isSelected,
                                                hasFocus,
                                                row,
                                                column
                                        );

                        label.setHorizontalAlignment(
                                SwingConstants.CENTER
                        );

                        String status = value.toString();

                        if ("Conciliado".equals(status)) {

                            label.setBackground(
                                    new Color(209, 250, 229)
                            );

                            label.setForeground(
                                    new Color(6, 95, 70)
                            );

                        } else if ("Pendente".equals(status)) {

                            label.setBackground(
                                    new Color(254, 243, 199)
                            );

                            label.setForeground(
                                    new Color(146, 64, 14)
                            );

                        } else if ("Divergente".equals(status)) {

                            label.setBackground(
                                    new Color(254, 215, 170)
                            );

                            label.setForeground(
                                    new Color(154, 52, 18)
                            );

                        } else if ("Atrasada".equals(status)) {

                            label.setBackground(
                                    new Color(254, 226, 226)
                            );

                            label.setForeground(
                                    new Color(153, 27, 27)
                            );

                        } else {

                            label.setBackground(Color.WHITE);
                            label.setForeground(TEXTO);
                        }

                        label.setOpaque(true);

                        return label;
                    }
                }
        );

    JScrollPane scroll =
        new JScrollPane(tabela);

scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

@Override
protected void configureScrollBarColors() {
    thumbColor = new Color(203, 213, 225);
    trackColor = new Color(248, 249, 250);
}

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return criarBotaoScroll();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return criarBotaoScroll();
    }

    private JButton criarBotaoScroll() {
        JButton botao = new JButton();
        botao.setPreferredSize(new Dimension(0, 0));
        botao.setMinimumSize(new Dimension(0, 0));
        botao.setMaximumSize(new Dimension(0, 0));
        return botao;
    }

    @Override
    protected void paintThumb(
            Graphics g,
            JComponent c,
            java.awt.Rectangle thumbBounds
    ) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(new Color(203, 213, 225));

        g2.fillRoundRect(
                thumbBounds.x + 2,
                thumbBounds.y + 2,
                thumbBounds.width - 4,
                thumbBounds.height - 4,
                8,
                8
        );

        g2.dispose();
    }
});

scroll.getVerticalScrollBar().setPreferredSize(
    new Dimension(14, 0)
);

    int alturaLinha =
            tabela.getRowHeight();

    int quantidadeLinhas =
            tabela.getRowCount();

    int alturaTabela =
            (quantidadeLinhas * alturaLinha) + 30;

    scroll.setPreferredSize(
            new Dimension(
                    0,
                    alturaTabela
            )
    );

    scroll.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    alturaTabela
            )
    );

    scroll.setBorder(null);

    painel.add(
            scroll,
            BorderLayout.CENTER
    );

    return painel;
}

    

    private String formatarMoeda(double valor) {
        return String.format(
                "R$ %,.2f",
                valor
        ).replace(",", "X")
                .replace(".", ",")
                .replace("X", ".");
    }

    private DefaultTableCellRenderer criarRendererCentralizado() {
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();

        renderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        return renderer;
    }

    private DefaultTableCellRenderer criarRendererMoeda() {
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                JLabel label
                        = (JLabel) super.getTableCellRendererComponent(
                                table,
                                value,
                                isSelected,
                                hasFocus,
                                row,
                                column
                        );

                label.setHorizontalAlignment(
                        SwingConstants.RIGHT
                );

                if (value instanceof Number) {
                    label.setText(
                            formatarMoeda(
                                    ((Number) value).doubleValue()
                            )
                    );
                }

                return label;
            }
        };

        return renderer;
    }

    private void atualizarTabela(ArrayList<Conciliacao> dados) {

        modeloTabela.setRowCount(0);

        DateTimeFormatter formato
                = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Conciliacao m : dados) {

            String diferenca;

            if (m.getDiferenca() == 0) {
                diferenca = "R$ 0,00";
            } else {
                diferenca = formatarMoeda(m.getDiferenca());
            }

            modeloTabela.addRow(
                    new Object[]{
                        m.getData().format(formato),
                        m.getDescricaoExtrato(),
                        m.getValorExtrato(),
                        m.getDescricaoSistema(),
                        m.getValorSistema(),
                        diferenca,
                        m.getStatus()
                    }
            );
        }

        tabela.revalidate();
        tabela.repaint();
    }
}
