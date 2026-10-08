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

    // Identifica a versão da classe para controle de serialização.
    private static final long serialVersionUID = 1L;

    // Tabela que exibe os dados das conciliações bancárias.
    // Tipo: JTable. Função: mostrar as movimentações na interface.
    private JTable tabela;

    // Modelo responsável por armazenar e controlar os dados exibidos na tabela.
    // Tipo: DefaultTableModel. Função: adicionar, remover e atualizar linhas.
    private DefaultTableModel modeloTabela;

    // Cores utilizadas na construção visual da tela.
    private final Color AZUL_MENU = new Color(27, 54, 93);
    private final Color FUNDO = new Color(245, 247, 250);
    private final Color BORDA = new Color(226, 232, 240);
    private final Color TEXTO = new Color(30, 38, 52);
    private final Color BRANCO = Color.WHITE;
    private final Color VERDE = new Color(16, 185, 129);
    private final Color VERMELHO = new Color(239, 68, 68);
    private final Color LARANJA = new Color(245, 158, 11);

    // Lista que armazena as movimentações usadas na tela.
    // Tipo: ArrayList<Conciliacao>. Função: guardar os dados das conciliações.
    private ArrayList<Conciliacao> movimentacoes;

    public TelaConciliacaoBancaria() {

        // Define o layout principal da tela.
        setLayout(new BorderLayout());

        // Busca as movimentações cadastradas no Model.
        movimentacoes = Conciliacao.getMovimentacoes();

        // Adiciona a área principal no centro da tela.
        add(criarAreaPrincipal(), BorderLayout.CENTER);
    }

    private JPanel criarSidebar() {

        // Painel que representa a barra lateral do sistema.
        JPanel sidebar = new JPanel();

        sidebar.setBackground(new Color(27, 54, 93));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Texto/logo exibido no topo da barra lateral.
        JLabel lblLogo = new JLabel(
                "<html><b>ERP Finance</b><br/>"
                + "<small style='color:#A0AEC0;'>MÓDULO FINANCEIRO</small></html>"
        );

        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30));

        // Botão de acesso ao Dashboard.
        JButton btnDashboard = criarBotaoMenu("Dashboard");
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));

        // Botão de acesso às contas a pagar e receber.
        JButton btnContasPagar = criarBotaoMenu("Contas a Pagar e Receber");
        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));

        // Botão de acesso ao fluxo de caixa.
        JButton btnFluxoCaixa = criarBotaoMenu("Fluxo de Caixa");
        sidebar.add(btnFluxoCaixa);
        sidebar.add(Box.createVerticalStrut(5));

        // Botão que representa a tela atualmente selecionada.
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

        // Botão de acesso aos relatórios.
        JButton btnRelatorios = criarBotaoMenu("Relatórios");
        sidebar.add(btnRelatorios);
        sidebar.add(Box.createVerticalStrut(5));

        // Botão de acesso à área fiscal.
        JButton btnFiscal = criarBotaoMenu("Fiscal");
        sidebar.add(btnFiscal);
        sidebar.add(Box.createVerticalStrut(5));

        return sidebar;
    }

    private JButton criarBotaoMenu(String texto) {

        // Texto recebido para definir o nome do botão.
        // Tipo: String. Função: definir o texto exibido no botão.
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

        // Painel que reúne o cabeçalho e o conteúdo principal.
        JPanel area = new JPanel(new BorderLayout());

        area.setBackground(FUNDO);

        // Adiciona o cabeçalho na parte superior.
        area.add(criarHeader(), BorderLayout.NORTH);

        // Adiciona o conteúdo no centro.
        area.add(criarConteudo(), BorderLayout.CENTER);

        return area;
    }

    private JPanel criarHeader() {

        // Painel responsável pelo cabeçalho da tela.
        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Nome do usuário exibido no cabeçalho.
        JLabel lblUser = new JLabel("Jefferson - Administrador");

        lblUser.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {

        // Painel que reúne título, filtros, cards e tabela.
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

        // Título principal da tela.
        JLabel titulo = new JLabel(
                "<html><h2 style='margin:0;'>Conciliação Bancária</h2>"
                + "<span style='color:gray;'>Compare o extrato bancário com os lançamentos do sistema ERP</span></html>"
        );

        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Adiciona o título.
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(15));

        // Adiciona o filtro de datas.
        conteudo.add(criarFiltroDatas());
        conteudo.add(Box.createVerticalStrut(15));

        // Adiciona os cards de informações.
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        // Adiciona a tabela.
        conteudo.add(criarTabela());

        return conteudo;
    }

    private JPanel criarFiltroDatas() {

        // Painel que contém o filtro de período.
        JPanel painel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 0)
        );

        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Opções disponíveis para filtrar as movimentações.
        // Tipo: String[]. Função: armazenar os períodos do filtro.
        String[] opcoes = {
            "Último Semestre",
            "Último Ano",
            "Últimos 30 dias"
        };

        // Caixa de seleção utilizada para escolher o período.
        // Tipo: JComboBox<String>. Função: permitir ao usuário selecionar um período.
        JComboBox<String> filtro = new JComboBox<>(opcoes);

        filtro.setPreferredSize(new Dimension(160, 28));
        filtro.setBackground(Color.WHITE);

        filtro.addActionListener(e -> {

            // Opção selecionada pelo usuário.
            // Tipo: String. Função: identificar qual período deve ser filtrado.
            String opcaoSelecionada =
                    (String) filtro.getSelectedItem();

            // Data atual utilizada como final do período.
            // Tipo: LocalDate.
            LocalDate hoje = LocalDate.now();

            // Data inicial calculada de acordo com o filtro escolhido.
            // Tipo: LocalDate.
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

            // Lista contendo somente os dados dentro do período escolhido.
            // Tipo: ArrayList<Conciliacao>.
            ArrayList<Conciliacao> dadosFiltrados =
                    Conciliacao.listarPorPeriodo(
                            dataInicial,
                            hoje
                    );

            // Atualiza a tabela com os dados filtrados.
            atualizarTabela(dadosFiltrados);

        });

        painel.add(filtro);

        return painel;
    }

    private JPanel criarCards() {

        // Painel responsável por organizar os cards em linhas e colunas.
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

        // Card com o total de movimentações conciliadas.
        painel.add(criarCard(
                "Conciliação Total",
                String.valueOf(Conciliacao.getTotalConciliadas()),
                "movimentações conciliadas",
                VERDE
        ));

        // Card com o total das diferenças encontradas.
        painel.add(criarCard(
                "Diferença Bancária",
                String.format("R$ %.2f", Conciliacao.getTotalDiferenca()),
                "requerem verificação",
                VERMELHO
        ));

        // Card com o total de contas pendentes a pagar.
        painel.add(criarCard(
                "Pendentes a Pagar",
                String.valueOf(Conciliacao.getPendentes(Conciliacao.PAGAR)),
                "movimentações pendentes",
                LARANJA
        ));

        // Card com o total de contas pendentes a receber.
        painel.add(criarCard(
                "Pendentes a Receber",
                String.valueOf(Conciliacao.getPendentes(Conciliacao.RECEBER)),
                "movimentações pendentes",
                LARANJA
        ));

        // Quantidade de contas atrasadas a receber.
        // Tipo: int. Função: armazenar a quantidade encontrada.
        int atrasadosReceber =
                Conciliacao.getAtrasados(Conciliacao.RECEBER);

        // Quantidade de contas atrasadas a pagar.
        // Tipo: int. Função: armazenar a quantidade encontrada.
        int atrasadosPagar =
                Conciliacao.getAtrasados(Conciliacao.PAGAR);

        // Card com informações de atrasos a receber.
        painel.add(criarCard(
                "Atraso a Receber",
                String.valueOf(atrasadosReceber),
                atrasadosReceber == 0 ? "sem atrasos" : "contas atrasadas",
                atrasadosReceber == 0 ? VERDE : VERMELHO
        ));

        // Card com informações de atrasos a pagar.
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

        // Título exibido no card.
        // Tipo: String. Função: identificar a informação.
        
        // Valor principal exibido no card.
        // Tipo: String. Função: mostrar o resultado.
        
        // Texto complementar do card.
        // Tipo: String. Função: explicar o valor apresentado.
        
        // Cor usada no texto complementar.
        // Tipo: Color. Função: destacar visualmente o estado.
        
        // Painel que representa o card.
        JPanel card = new JPanel(
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

        // Label que exibe o título do card.
        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        lblTitulo.setForeground(Color.GRAY);

        // Label que exibe o valor principal.
        JLabel lblValor = new JLabel(valor);

        lblValor.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        lblValor.setForeground(TEXTO);

        // Label que exibe a descrição complementar.
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

        // Painel que contém a tabela e seu título.
        JPanel painel = new JPanel(new BorderLayout());

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDA, 1),
                        new EmptyBorder(12, 12, 12, 12)
                )
        );

        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Título da tabela.
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

        // Nomes das colunas da tabela.
        // Tipo: String[]. Função: definir os títulos das colunas.
        String[] colunas = {
            "Data",
            "Descrição (Extrato)",
            "Valor Extrato",
            "Descrição (Sistema)",
            "Valor Sistema",
            "Diferença",
            "Status"
        };

        // Modelo que armazena os dados exibidos pela tabela.
        modeloTabela = new DefaultTableModel(
                colunas,
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int col
            ) {
                // Impede que o usuário edite as células.
                return false;
            }
        };

        // Formato usado para exibir as datas.
        // Tipo: DateTimeFormatter.
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Percorre todas as movimentações para preencher a tabela.
        for (Conciliacao m : movimentacoes) {

            // Texto que representa a diferença entre os valores.
            // Tipo: String.
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

        // Cria a tabela utilizando o modelo de dados.
        tabela = new JTable(modeloTabela);

        // Renderer utilizado para centralizar conteúdos das células.
        DefaultTableCellRenderer centro =
                new DefaultTableCellRenderer();

        centro.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Define o alinhamento das colunas.
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

        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setResizingAllowed(false);
        tabela.getTableHeader().setOpaque(true);

        // Renderer responsável por alterar a aparência da coluna Diferença.
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

                                // Label que representa a célula atual.
                                // Tipo: JLabel. Função: exibir o valor da célula.
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

                                // Texto da diferença da movimentação.
                                // Tipo: String. Função: verificar se existe diferença.
                                String texto = value.toString();

                                if (!texto.equals("R$ 0,00")) {

                                    label.setForeground(VERMELHO);

                                } else {

                                    label.setForeground(Color.GRAY);
                                }

                                return label;
                            }
                        }
                );

        // Renderer responsável por definir as cores da coluna Status.
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

                                // Label que representa a célula do status.
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

                                // Status atual da movimentação.
                                // Tipo: String. Função: definir a cor da célula.
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

        // Componente que adiciona rolagem à tabela.
        JScrollPane scroll =
                new JScrollPane(tabela);

        // Personaliza a barra de rolagem vertical.
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {

                // Cor da parte móvel da barra.
                thumbColor = new Color(203, 213, 225);

                // Cor do fundo da barra.
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

            // Cria os botões invisíveis das extremidades do scroll.
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

                // Verifica se a área da barra existe e está habilitada.
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }

                // Cria uma cópia do objeto Graphics para desenhar o scroll.
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setColor(new Color(203, 213, 225));

                // Desenha a barra com bordas arredondadas.
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

        // Largura da barra de rolagem vertical.
        scroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(14, 0)
        );

        // Altura de cada linha da tabela.
        // Tipo: int. Função: calcular a altura total necessária.
        int alturaLinha = tabela.getRowHeight();

        // Quantidade de linhas existentes na tabela.
        // Tipo: int. Função: calcular o tamanho da tabela.
        int quantidadeLinhas = tabela.getRowCount();

        // Altura total calculada para a tabela.
        // Tipo: int. Função: definir o tamanho do JScrollPane.
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

        // Valor numérico que será convertido para formato monetário.
        // Tipo: double.
        return String.format(
                "R$ %,.2f",
                valor
        ).replace(",", "X")
                .replace(".", ",")
                .replace("X", ".");
    }

    private DefaultTableCellRenderer criarRendererCentralizado() {

        // Renderer responsável por centralizar o conteúdo da célula.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();

        renderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        return renderer;
    }

    private DefaultTableCellRenderer criarRendererMoeda() {

        // Renderer utilizado para formatar valores monetários.
        DefaultTableCellRenderer renderer =
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

                        // Label que representa o conteúdo da célula.
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
                                SwingConstants.RIGHT
                        );

                        // Verifica se o conteúdo é um número.
                        if (value instanceof Number) {

                            // Converte o número para o formato R$.
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

        // Lista com os dados filtrados que serão exibidos.
        // Tipo: ArrayList<Conciliacao>.
        modeloTabela.setRowCount(0);

        // Formato usado para apresentar as datas.
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Percorre cada movimentação filtrada.
        for (Conciliacao m : dados) {

            // Diferença entre os valores do extrato e do sistema.
            String diferenca;

            if (m.getDiferenca() == 0) {
                diferenca = "R$ 0,00";
            } else {
                diferenca = formatarMoeda(m.getDiferenca());
            }

            // Adiciona a movimentação filtrada na tabela.
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

        // Atualiza a aparência da tabela depois da alteração dos dados.
        tabela.revalidate();
        tabela.repaint();
    }
}