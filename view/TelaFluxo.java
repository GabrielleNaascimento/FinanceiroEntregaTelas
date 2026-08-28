package view;

import model.Fluxo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class TelaFluxo extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Color COR_MENU = new Color(27, 54, 93);
    private final Color COR_FUNDO = new Color(245, 247, 250);
    private final Color COR_BORDA = new Color(226, 232, 240);
    private final Color COR_TEXTO = new Color(30, 38, 52);
    private final Color COR_CINZA = new Color(105, 114, 128);
    private final Color COR_AZUL = new Color(37, 99, 235);
    private final Color COR_VERDE = new Color(16, 185, 129);
    private final Color COR_VERMELHO = new Color(239, 68, 68);

    private ArrayList<Fluxo> movimentacoes;

    public TelaFluxo() {

        setTitle("ERP Financeiro - Módulo Financeiro");
        setSize(1280, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarDados();
        add(criarSidebar(), BorderLayout.WEST);
        add(criarAreaPrincipal(), BorderLayout.CENTER);
    }


    private JPanel criarSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setBackground(COR_MENU);
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

        JButton btnContasPagar =
                criarBotaoMenu("Contas a Pagar e Receber");

        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));


        JButton btnFluxoCaixa =
                new JButton("Fluxo de Caixa");

        btnFluxoCaixa.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnFluxoCaixa.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        btnFluxoCaixa.setFocusPainted(false);
        btnFluxoCaixa.setHorizontalAlignment(
                SwingConstants.LEFT
        );
        btnFluxoCaixa.setBackground(Color.WHITE);
        btnFluxoCaixa.setForeground(COR_MENU);
        btnFluxoCaixa.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        sidebar.add(btnFluxoCaixa);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnConciliacao =
                criarBotaoMenu("Conciliação Bancária");

        sidebar.add(btnConciliacao);
        sidebar.add(Box.createVerticalStrut(5));


        JButton btnRelatorios =
                criarBotaoMenu("Relatórios");

        sidebar.add(btnRelatorios);
        sidebar.add(Box.createVerticalStrut(5));


        JButton btnFiscal =
                criarBotaoMenu("Fiscal");

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
        btn.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        btn.setForeground(
                new Color(203, 213, 225)
        );

        btn.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        return btn;
    }


    private JPanel criarAreaPrincipal() {

        JPanel area = new JPanel(
                new BorderLayout()
        );

        area.setBackground(COR_FUNDO);

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        15,
                        25,
                        15,
                        25
                )
        );


        JTextField txtBusca =
                new JTextField(
                        " Buscar transações, contas..."
                );

        txtBusca.setPreferredSize(
                new Dimension(280, 32)
        );

        txtBusca.setForeground(Color.GRAY);


        JLabel lblUser =
                new JLabel(
                        "Jefferson Riper (Administrador)"
                );

        lblUser.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        header.add(
                txtBusca,
                BorderLayout.WEST
        );

        header.add(
                lblUser,
                BorderLayout.EAST
        );


        area.add(
                header,
                BorderLayout.NORTH
        );

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

        JLabel lblTitulo =
                new JLabel(
                        "<html>" +
                        "<h2 style='margin:0;'>Fluxo de Caixa</h2>" +
                        "<span style='color:gray;'>" +
                        "Análise de entradas, saídas e saldos de caixa" +
                        "</span>" +
                        "</html>"
                );

        lblTitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        conteudo.add(lblTitulo);

        conteudo.add(
                Box.createVerticalStrut(15)
        );
        conteudo.add(criarCards());

        conteudo.add(
                Box.createVerticalStrut(15)
        );

        conteudo.add(criarGrafico());

        conteudo.add(
                Box.createVerticalStrut(15)
        );

        conteudo.add(criarTabela());


        area.add(
                conteudo,
                BorderLayout.CENTER
        );


        return area;
    }

    private JPanel criarCards() {

        JPanel painel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        painel.setOpaque(false);

        painel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );


        painel.add(
                criarCard(
                        "Saldo Atual",
                        "R$ 284.750,00",
                        "↗ +4.2% vs. mês anterior",
                        COR_AZUL
                )
        );


        painel.add(
                criarCard(
                        "Total Entradas",
                        "R$ 156.300,00",
                        "↗ +8.5% vs. mês anterior",
                        COR_VERDE
                )
        );


        painel.add(
                criarCard(
                        "Total Saídas",
                        "R$ 98.420,00",
                        "↘ -2.1% vs. mês anterior",
                        COR_VERMELHO
                )
        );


        return painel;
    }


    private JPanel criarCard(
            String titulo,
            String valor,
            String variacao,
            Color cor
    ) {

        JPanel card =
                new JPanel(
                        new GridLayout(
                                3,
                                1
                        )
                );


        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA,
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


        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        lblTitulo.setForeground(
                COR_CINZA
        );


        JLabel lblValor =
                new JLabel(valor);

        lblValor.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        lblValor.setForeground(
                COR_TEXTO
        );


        JLabel lblVariacao =
                new JLabel(variacao);

        lblVariacao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        lblVariacao.setForeground(cor);


        card.add(lblTitulo);
        card.add(lblValor);
        card.add(lblVariacao);


        return card;
    }

    private JPanel criarGrafico() {

        Grafico grafico =
                new Grafico();


        grafico.setBackground(
                Color.WHITE
        );


        grafico.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA,
                                1
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );


        grafico.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        grafico.setPreferredSize(
                new Dimension(
                        0,
                        230
                )
        );


        grafico.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        230
                )
        );


        return grafico;
    }

    private JPanel criarTabela() {

        JPanel painel =
                new JPanel(
                        new BorderLayout()
                );


        painel.setBackground(
                Color.WHITE
        );


        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA,
                                1
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );


        painel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JLabel titulo =
                new JLabel(
                        "Extrato de Movimentações"
                );


        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        titulo.setForeground(
                COR_TEXTO
        );


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
                "Descrição",
                "Tipo",
                "Categoria",
                "Valor",
                "Saldo Acumulado"
        };


        DefaultTableModel modelo =
                new DefaultTableModel(
                        colunas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int linha,
                            int coluna
                    ) {
                        return false;
                    }
                };


        for (Fluxo m : movimentacoes) {

            String valor;

            if (m.getValor() >= 0) {

                valor = String.format(
                        "+ R$ %,.2f",
                        m.getValor()
                );

            } else {

                valor = String.format(
                        "- R$ %,.2f",
                        Math.abs(m.getValor())
                );
            }


            String saldo =
                    String.format(
                            "R$ %,.2f",
                            m.getSaldo()
                    );


            modelo.addRow(
                    new Object[]{
                            m.getData(),
                            m.getDescricao(),
                            m.getTipo(),
                            m.getCategoria(),
                            valor,
                            saldo
                    }
            );
        }


        JTable tabela =
                new JTable(modelo);


        tabela.setRowHeight(30);


        tabela.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );


        tabela.setForeground(
                COR_TEXTO
        );


        tabela.setBackground(
                Color.WHITE
        );


        tabela.setShowGrid(false);


        tabela.setShowHorizontalLines(true);

        tabela.setGridColor(
                COR_BORDA
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
                .setForeground(
                        COR_CINZA
                );


        tabela.getTableHeader()
                .setBackground(
                        new Color(
                                248,
                                249,
                                251
                        )
                );


        tabela.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                32
                        )
                );

        DefaultTableCellRenderer centro =
                new DefaultTableCellRenderer();

        centro.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        tabela.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centro);

        tabela.getColumnModel()
                .getColumn(2)
                .setCellRenderer(centro);

        tabela.getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new DefaultTableCellRenderer() {

                            @Override
                            public Component
                            getTableCellRendererComponent(
                                    JTable table,
                                    Object value,
                                    boolean isSelected,
                                    boolean hasFocus,
                                    int row,
                                    int column
                            ) {

                                JLabel label =
                                        (JLabel)
                                        super.getTableCellRendererComponent(
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


                                String texto =
                                        value.toString();


                                if (texto.startsWith("+")) {

                                    label.setForeground(
                                            COR_VERDE
                                    );

                                } else {

                                    label.setForeground(
                                            COR_VERMELHO
                                    );
                                }


                                return label;
                            }
                        }
                );


        JScrollPane scroll =
                new JScrollPane(tabela);


        scroll.setBorder(null);


        scroll.getViewport()
                .setBackground(
                        Color.WHITE
                );


        painel.add(
                scroll,
                BorderLayout.CENTER
        );


        return painel;
    }

    private class Grafico extends JPanel {

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);


            Graphics2D g2 =
                    (Graphics2D) g;


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            int largura =
                    getWidth();


            int altura =
                    getHeight();


            int esquerda = 35;
            int direita = largura - 25;
            int topo = 45;
            int baixo = altura - 35;
            g2.setColor(
                    COR_TEXTO
            );


            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            13
                    )
            );


            g2.drawString(
                    "Fluxo de Caixa — Últimos 12 Meses",
                    esquerda,
                    22
            );

            g2.setColor(
                    new Color(
                            235,
                            237,
                            240
                    )
            );


            for (int i = 0; i < 5; i++) {

                int y =
                        topo +
                        i *
                        (baixo - topo) /
                        4;


                g2.drawLine(
                        esquerda,
                        y,
                        direita,
                        y
                );
            }

            int[] entradas = {
                    80, 76, 73, 69,
                    66, 62, 58, 54,
                    50, 46, 42, 38
            };


            int[] saidas = {
                    115, 125, 135, 145,
                    155, 165, 175, 185,
                    195, 205, 215, 225
            };


            int[] saldo = {
                    55, 53, 51, 49,
                    47, 45, 43, 41,
                    39, 37, 35, 33
            };


            desenharLinha(
                    g2,
                    entradas,
                    COR_VERDE,
                    esquerda,
                    direita,
                    topo,
                    baixo
            );


            desenharLinha(
                    g2,
                    saidas,
                    COR_VERMELHO,
                    esquerda,
                    direita,
                    topo,
                    baixo
            );


            desenharLinha(
                    g2,
                    saldo,
                    COR_AZUL,
                    esquerda,
                    direita,
                    topo,
                    baixo
            );

            String[] meses = {
                    "Jan", "Fev", "Mar", "Abr",
                    "Mai", "Jun", "Jul", "Ago",
                    "Set", "Out", "Nov", "Dez"
            };


            g2.setColor(
                    COR_CINZA
            );


            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            9
                    )
            );


            for (int i = 0; i < meses.length; i++) {

                int x =
                        esquerda +
                        i *
                        (direita - esquerda) /
                        (meses.length - 1);


                g2.drawString(
                        meses[i],
                        x - 8,
                        baixo + 18
                );
            }

            int legendaX =
                    direita - 205;


            desenharLegenda(
                    g2,
                    legendaX,
                    COR_VERDE,
                    "Entradas"
            );


            desenharLegenda(
                    g2,
                    legendaX + 70,
                    COR_VERMELHO,
                    "Saídas"
            );


            desenharLegenda(
                    g2,
                    legendaX + 125,
                    COR_AZUL,
                    "Saldo"
            );
        }


        private void desenharLinha(
                Graphics2D g2,
                int[] valores,
                Color cor,
                int esquerda,
                int direita,
                int topo,
                int baixo
        ) {

            g2.setColor(cor);


            g2.setStroke(
                    new BasicStroke(2)
            );


            Path2D caminho =
                    new Path2D.Double();


            for (int i = 0;
                 i < valores.length;
                 i++) {


                int x =
                        esquerda +
                        i *
                        (direita - esquerda) /
                        (valores.length - 1);


                int y =
                        topo +
                        valores[i];


                if (y > baixo) {
                    y = baixo;
                }


                if (i == 0) {

                    caminho.moveTo(
                            x,
                            y
                    );

                } else {

                    caminho.lineTo(
                            x,
                            y
                    );
                }
            }


            g2.draw(caminho);
        }


        private void desenharLegenda(
                Graphics2D g2,
                int x,
                Color cor,
                String texto
        ) {

            g2.setColor(cor);


            g2.fillRect(
                    x,
                    27,
                    9,
                    3
            );


            g2.setColor(
                    COR_CINZA
            );


            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            9
                    )
            );


            g2.drawString(
                    texto,
                    x + 13,
                    31
            );
        }
    }
    private void criarDados() {

        movimentacoes =
                new ArrayList<>();


        movimentacoes.add(
                new Fluxo(
                        "10/10/2025",
                        "Recebimento Tech Solutions",
                        "Entrada",
                        "Faturamento Serviços",
                        15000,
                        271300
                )
        );


        movimentacoes.add(
                new Fluxo(
                        "11/10/2025",
                        "Pgto AWS Cloud Server",
                        "Saída",
                        "Hospedagem e Infra",
                        -4850,
                        266450
                )
        );


        movimentacoes.add(
                new Fluxo(
                        "12/10/2025",
                        "Recebimento Metalúrgica",
                        "Entrada",
                        "Venda de Insumos",
                        28450,
                        294900
                )
        );


        movimentacoes.add(
                new Fluxo(
                        "14/10/2025",
                        "Pgto Aluguel Comercial",
                        "Saída",
                        "Aluguel Escritório",
                        -12000,
                        282900
                )
        );


        movimentacoes.add(
                new Fluxo(
                        "15/10/2025",
                        "Pgto Folha de Colaboradores",
                        "Saída",
                        "Folha de Pagamento",
                        -45200,
                        237700
                )
        );


        movimentacoes.add(
                new Fluxo(
                        "16/10/2025",
                        "Recebimento Vânia Vida",
                        "Entrada",
                        "Faturamento Serviços",
                        8900,
                        246600
                )
        );
    }
    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception ignored) {
        }


        SwingUtilities.invokeLater(() -> {

            new TelaFluxo()
                    .setVisible(true);

        });
    }
}

