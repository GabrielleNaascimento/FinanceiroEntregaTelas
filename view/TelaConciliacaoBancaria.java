package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import model.Conciliacao;

import java.util.ArrayList;

public class TelaConciliacaoBancaria extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Color AZUL_MENU = new Color(27, 54, 93);
    private final Color FUNDO = new Color(245, 247, 250);
    private final Color BORDA = new Color(226, 232, 240);
    private final Color TEXTO = new Color(30, 38, 52);
    private final Color CINZA = Color.GRAY;
    private final Color VERDE = new Color(16, 185, 129);
    private final Color VERMELHO = new Color(239, 68, 68);
    private final Color LARANJA = new Color(245, 158, 11);

    private ArrayList<Conciliacao> movimentacoes;

    public TelaConciliacaoBancaria() {

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

        JTextField txtBusca =
                new JTextField(" Buscar transações, contas...");

        txtBusca.setPreferredSize(new Dimension(280, 32));
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
                "<html><h2 style='margin:0;'>Conciliação Bancária</h2>" +
                "<span style='color:gray;'>Compare o extrato bancário com os lançamentos do sistema ERP</span></html>"
        );

        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(15));

        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        conteudo.add(criarTabela());

        return conteudo;
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
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );

        painel.add(
                criarCard(
                        "Total Conciliado",
                        "R$ 51.870,00",
                        "4 movimentações conciliadas",
                        VERDE
                )
        );

        painel.add(
                criarCard(
                        "Pendências",
                        "3",
                        "movimentações pendentes",
                        VERMELHO
                )
        );

        painel.add(
                criarCard(
                        "Diferenças Encontradas",
                        "R$ 650,00",
                        "requerem verificação",
                        new Color(37, 99, 235)
                )
        );

        return painel;
    }

    private JPanel criarCard(
            String titulo,
            String valor,
            String variacao,
            Color corVariacao
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

        JPanel painel =
                new JPanel(
                        new BorderLayout()
                );

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDA,
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

        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo =
                new JLabel(
                        "Confronto de Lançamentos"
                );

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

        DefaultTableModel model =
                new DefaultTableModel(
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

        for (Conciliacao m : movimentacoes) {

            String diferenca;

            if (m.getDiferenca() == 0) {

                diferenca = "R$ 0,00";

            } else {

                diferenca = String.format(
                        "R$ %,.2f",
                        m.getDiferenca()
                );
            }

            model.addRow(
                    new Object[]{
                            m.getData(),
                            m.getDescricaoExtrato(),
                            m.getValorExtrato(),
                            m.getDescricaoExtrato(),

                            m.getValorSistema(),
                            diferenca,
                            m.getStatus()
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

        tabela.setForeground(TEXTO);

        tabela.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        tabela.getTableHeader().setForeground(Color.GRAY);

        tabela.getTableHeader().setBackground(
                new Color(248, 249, 250)
        );

        tabela.getTableHeader().setPreferredSize(
                new Dimension(
                        0,
                        30
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
                .setCellRenderer(centro);

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
                                        (JLabel) super.getTableCellRendererComponent(
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
                                        (JLabel) super.getTableCellRendererComponent(
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

                                String status =
                                        value.toString();

                                if ("Conciliado".equals(status)) {

                                    label.setBackground(
                                            new Color(
                                                    209,
                                                    250,
                                                    229
                                            )
                                    );

                                    label.setForeground(
                                            new Color(
                                                    6,
                                                    95,
                                                    70
                                            )
                                    );

                                } else if ("Pendente".equals(status)) {

                                    label.setBackground(
                                            new Color(
                                                    254,
                                                    243,
                                                    199
                                            )
                                    );

                                    label.setForeground(
                                            new Color(
                                                    146,
                                                    64,
                                                    14
                                            )
                                    );

                                } else {

                                    label.setBackground(
                                            new Color(
                                                    254,
                                                    226,
                                                    226
                                            )
                                    );

                                    label.setForeground(
                                            new Color(
                                                    153,
                                                    27,
                                                    27
                                            )
                                    );
                                }

                                label.setOpaque(true);

                                return label;
                            }
                        }
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
    private void criarDados() {

        movimentacoes =
                new ArrayList<>();

        movimentacoes.add(
                new Conciliacao(
                        "05/10/2025",
                        "Recebimento Tech Solutions",
                        "R$ 15.000,00",
                        "R$ 15.350,00",
                        -350.00,
                        "Divergente"
                )
        );

        movimentacoes.add(
                new Conciliacao(
                        "06/10/2025",
                        "Pgto AWS Cloud Server",
                        "R$ 4.850,00",
                        "R$ 4.500,00",
                        350.00,
                        "Pendente"
                )
        );

        movimentacoes.add(
                new Conciliacao(
                        "08/10/2025",
                        "Recebimento Metalúrgica",
                        "R$ 28.450,00",
                        "R$ 28.450,00",
                        0.00,
                        "Conciliado"
                )
        );

        movimentacoes.add(
                new Conciliacao(
                        "09/10/2025",
                        "Pgto Aluguel Comercial",
                        "R$ 12.800,00",
                        "R$ 12.550,00",
                        250.00,
                        "Pendente"
                )
        );

        movimentacoes.add(
                new Conciliacao(
                        "10/10/2025",
                        "Pgto Folha de Colaboradores",
                        "R$ 4.800,00",
                        "R$ 4.800,00",
                        0.00,
                        "Conciliado"
                )
        );

        movimentacoes.add(
                new Conciliacao(
                        "12/10/2025",
                        "Recebimento Vânia Vida",
                        "R$ 3.420,00",
                        "R$ 3.420,00",
                        0.00,
                        "Conciliado"
                )
        );

        movimentacoes.add(
                new Conciliacao(
                        "14/10/2025",
                        "Recebimento Vânia Vida",
                        "R$ 8.500,00",
                        "R$ 8.450,00",
                        50.00,
                        "Pendente"
                )
        );
    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new TelaConciliacaoBancaria()
                    .setVisible(true);
        });
    }
}