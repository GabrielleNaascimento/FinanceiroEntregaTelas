package view;

import model.Contas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

public class TelaContas extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_TEXTO = new Color(35, 45, 60);
    private static final Color COR_TEXTO_SECUNDARIO = new Color(110, 120, 135);
    private static final Color COR_BORDA = new Color(226, 232, 240);

    private static final Color COR_AZUL = new Color(37, 99, 235);
    private static final Color COR_VERDE = new Color(16, 185, 129);
    private static final Color COR_AMARELO = new Color(245, 158, 11);
    private static final Color COR_VERMELHO = new Color(239, 68, 68);

    private JPanel painelCards;
    private JPanel painelTabela;

    private JButton btnPagar;
    private JButton btnReceber;

    private String tipoAtual = "PAGAR";

    public TelaContas() {

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

        JButton btnContasPagar = new JButton(
                "Contas a Pagar e Receber"
        );

        btnContasPagar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnContasPagar.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        btnContasPagar.setFocusPainted(false);
        btnContasPagar.setHorizontalAlignment(
                SwingConstants.LEFT
        );
        btnContasPagar.setBackground(Color.WHITE);
        btnContasPagar.setForeground(
                new Color(27, 54, 93)
        );
        btnContasPagar.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnFluxoCaixa =
                criarBotaoMenu("Fluxo de Caixa");

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

        JPanel area =
                new JPanel(
                        new BorderLayout()
                );

        area.setBackground(COR_FUNDO);

        area.add(
                criarHeader(),
                BorderLayout.NORTH
        );

        area.add(
                criarConteudo(),
                BorderLayout.CENTER
        );

        return area;
    }

    private JPanel criarHeader() {

        JPanel header =
                new JPanel(
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
                new Dimension(
                        280,
                        32
                )
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

        return header;
    }

    private JPanel criarConteudo() {

        JPanel conteudo =
                new JPanel();

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

        JLabel titulo =
                new JLabel(
                        "<html><h2 style='margin:0;'>Contas a Pagar e Receber</h2>" +
                        "<span style='color:gray;'>Gerencie suas contas, vencimentos e recebimentos</span></html>"
                );

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(titulo);

        conteudo.add(
                Box.createVerticalStrut(15)
        );


        JPanel seletor = criarSeletor();

        conteudo.add(seletor);

        conteudo.add(
                Box.createVerticalStrut(15)
        );

        painelCards = criarCards();

        painelCards.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(painelCards);

        conteudo.add(
                Box.createVerticalStrut(15)
        );

        painelTabela =
                criarTabela(tipoAtual);

        painelTabela.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(painelTabela);

        return conteudo;
    }


    private JPanel criarSeletor() {

        JPanel painel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                5,
                                0
                        )
                );

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA
                        ),
                        new EmptyBorder(
                                4,
                                4,
                                4,
                                4
                        )
                )
        );

        painel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        btnPagar =
                new JButton(
                        "Contas a Pagar"
                );

        btnReceber =
                new JButton(
                        "Contas a Receber"
                );

        configurarBotaoAba(btnPagar);
        configurarBotaoAba(btnReceber);

        btnPagar.addActionListener(e ->
                trocarTipo("PAGAR")
        );

        btnReceber.addActionListener(e ->
                trocarTipo("RECEBER")
        );

        painel.add(btnPagar);
        painel.add(btnReceber);

        atualizarBotoes();

        return painel;
    }

    private void configurarBotaoAba(
            JButton botao
    ) {

        botao.setFocusPainted(false);
        botao.setBorderPainted(false);

        botao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        botao.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    private void trocarTipo(
            String novoTipo
    ) {

        tipoAtual = novoTipo;

        painelCards.removeAll();

        JPanel novosCards =
                criarCards();

        Component[] componentes =
                novosCards.getComponents();

        for (Component componente : componentes) {
            painelCards.add(componente);
        }

        painelTabela.removeAll();

        painelTabela.setLayout(
                new BorderLayout()
        );

        painelTabela.add(
                criarTabela(tipoAtual),
                BorderLayout.CENTER
        );

        atualizarBotoes();

        painelCards.revalidate();
        painelCards.repaint();

        painelTabela.revalidate();
        painelTabela.repaint();
    }

    private void atualizarBotoes() {

        if (tipoAtual.equals("PAGAR")) {

            btnPagar.setBackground(
                    COR_AZUL
            );

            btnPagar.setForeground(
                    Color.WHITE
            );

            btnReceber.setBackground(
                    Color.WHITE
            );

            btnReceber.setForeground(
                    COR_TEXTO
            );

        } else {

            btnReceber.setBackground(
                    COR_AZUL
            );

            btnReceber.setForeground(
                    Color.WHITE
            );

            btnPagar.setBackground(
                    Color.WHITE
            );

            btnPagar.setForeground(
                    COR_TEXTO
            );
        }
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

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );

        ArrayList<Contas> dados =
                criarDados(tipoAtual);

        double total = 0;
        double pendente = 0;
        double atrasado = 0;

        for (Contas conta : dados) {

            total += conta.getValor();

            if (conta.getStatus().equals("Pendente")) {
                pendente += conta.getValor();
            }

            if (conta.getStatus().equals("Vencida")
                    || conta.getStatus().equals("Atrasada")) {

                atrasado += conta.getValor();
            }
        }

        painel.add(
                criarCard(
                        "Total",
                        formatarValor(total),
                        COR_AZUL
                )
        );

        painel.add(
                criarCard(
                        "Pendente",
                        formatarValor(pendente),
                        COR_AMARELO
                )
        );

        painel.add(
                criarCard(
                        "Em atraso",
                        formatarValor(atrasado),
                        COR_VERMELHO
                )
        );

        return painel;
    }

    private JPanel criarCard(
            String titulo,
            String valor,
            Color cor
    ) {

        JPanel card =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA
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
                COR_TEXTO_SECUNDARIO
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

        lblValor.setForeground(cor);

        card.add(lblTitulo);
        card.add(lblValor);

        return card;
    }

    private JPanel criarTabela(
            String tipo
    ) {

        JPanel painel =
                new JPanel(
                        new BorderLayout()
                );

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );

        String entidade =
                tipo.equals("PAGAR")
                        ? "Fornecedor"
                        : "Cliente";

        JLabel titulo =
                new JLabel(
                        tipo.equals("PAGAR")
                                ? "Contas a Pagar"
                                : "Contas a Receber"
                );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        titulo.setForeground(COR_TEXTO);

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
                entidade,
                "Descrição",
                "Vencimento",
                "Valor (R$)",
                "Status"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(
                        colunas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        ArrayList<Contas> dados =
                criarDados(tipo);

        for (Contas conta : dados) {

            modelo.addRow(
                    new Object[]{
                            conta.getEntidade(),
                            conta.getDescricao(),
                            conta.getVencimento(),
                            formatarValor(
                                    conta.getValor()
                            ),
                            conta.getStatus()
                    }
            );
        }

        JTable tabela =
                new JTable(modelo);

        configurarTabela(tabela);

        JScrollPane scroll =
                new JScrollPane(tabela);

        scroll.setBorder(null);

        painel.add(
                scroll,
                BorderLayout.CENTER
        );

        return painel;
    }

    private void configurarTabela(
            JTable tabela
    ) {

        tabela.setRowHeight(32);

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

        tabela.setRowSelectionAllowed(false);

        tabela.setColumnSelectionAllowed(false);

        tabela.getTableHeader()
                .setReorderingAllowed(false);

        tabela.getTableHeader()
                .setResizingAllowed(false);

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
                        COR_TEXTO_SECUNDARIO
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

        DefaultTableCellRenderer valorRenderer =
                new DefaultTableCellRenderer();

        valorRenderer.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        tabela.getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        valorRenderer
                );

        tabela.getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new StatusRenderer()
                );
    }
    private class StatusRenderer
            extends DefaultTableCellRenderer {

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

            String status =
                    value.toString();

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setOpaque(true);

            if (status.equals("Paga")
                    || status.equals("Recebida")) {

                label.setForeground(
                        new Color(
                                6,
                                95,
                                70
                        )
                );

                label.setBackground(
                        new Color(
                                209,
                                250,
                                229
                        )
                );

            } else if (status.equals("Pendente")) {

                label.setForeground(
                        new Color(
                                146,
                                64,
                                14
                        )
                );

                label.setBackground(
                        new Color(
                                254,
                                243,
                                199
                        )
                );

            } else {

                label.setForeground(
                        new Color(
                                153,
                                27,
                                27
                        )
                );

                label.setBackground(
                        new Color(
                                254,
                                226,
                                226
                        )
                );
            }

            return label;
        }
    }
    private ArrayList<Contas> criarDados(
            String tipo
    ) {

        ArrayList<Contas> dados =
                new ArrayList<>();

        if (tipo.equals("PAGAR")) {

            dados.add(
                    new Contas(
                            "Amazon",
                            "Locação Servidores",
                            "15/08/2026",
                            4500.00,
                            "PAGAR",
                            "Vencida"
                    )
            );

            dados.add(
                    new Contas(
                            "Microsoft",
                            "Licenças Office",
                            "20/09/2026",
                            3200.00,
                            "PAGAR",
                            "Pendente"
                    )
            );

            dados.add(
                    new Contas(
                            "Xavier",
                            "Assessoria Jurídica",
                            "05/08/2026",
                            8500.00,
                            "PAGAR",
                            "Paga"
                    )
            );

            dados.add(
                    new Contas(
                            "Imobiliária",
                            "Aluguel Sala",
                            "10/10/2026",
                            12000.00,
                            "PAGAR",
                            "Pendente"
                    )
            );

        } else {

            dados.add(
                    new Contas(
                            "Tech Solutions",
                            "Impl. ERP",
                            "01/08/2026",
                            15000.00,
                            "RECEBER",
                            "Recebida"
                    )
            );

            dados.add(
                    new Contas(
                            "Metalúrgica",
                            "Consultoria",
                            "12/08/2026",
                            8540.00,
                            "RECEBER",
                            "Atrasada"
                    )
            );

            dados.add(
                    new Contas(
                            "Hospital",
                            "Manut. Hardware",
                            "25/08/2026",
                            3800.00,
                            "RECEBER",
                            "Pendente"
                    )
            );

            dados.add(
                    new Contas(
                            "Banco Nacional",
                            "Suporte TI",
                            "18/08/2026",
                            4500.00,
                            "RECEBER",
                            "Recebida"
                    )
            );
        }

        return dados;
    }

 private String formatarValor(
            double valor
    ) {

        return String.format(
                "R$ %.2f",
                valor
        );
    }
    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() ->
                new TelaContas()
                        .setVisible(true)
        );
    }
}