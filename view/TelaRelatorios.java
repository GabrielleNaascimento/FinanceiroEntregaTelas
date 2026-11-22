package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import model.Relatorio;
import model.Relatorio.ResumoMensal;

public class TelaRelatorios extends JPanel {

    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO = new Color(30, 38, 52);
    private static final Color COR_CINZA = new Color(105, 114, 128);
    private static final Color COR_AZUL = new Color(37, 99, 235);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final int ESPACO = 15;
    private static final int LARGURA_MINIMA = 800;

    private Relatorio relatorio;

    private DefaultTableModel modelTabela;
    private JLabel lblTituloTabela;
    private PainelGraficoMensal grafico;

    private JTextField txtDataInicial;
    private JTextField txtDataFinal;

    private JPanel painelCards;
    private JPanel painelInferior;
    private boolean layoutEstreito;

    public TelaRelatorios() {

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);

        relatorio = new Relatorio();

        add(criarAreaPrincipal(), BorderLayout.CENTER);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                relatorio.atualizar();
                atualizarTela();
            }
        });
    }

    private JPanel criarAreaPrincipal() {

        JPanel area = new JPanel(new BorderLayout());

        area.setBackground(COR_FUNDO);

        area.add(criarHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(criarConteudo());
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COR_FUNDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        area.add(scroll, BorderLayout.CENTER);

        return area;
    }

    private JPanel criarHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);
        header.setBorder(
                new EmptyBorder(15, 25, 15, 25)
        );

        JLabel lblUser =
                new JLabel("Jefferson Riper (Administrador)");

        lblUser.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {

        JPanel conteudo = new PainelRolavel(
                new BorderLayout(0, 15)
        );

        conteudo.setOpaque(false);

        conteudo.setBorder(
                new EmptyBorder(10, 25, 25, 25)
        );

        JPanel topo = new JPanel();

        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setOpaque(false);

        JPanel titulo = criarTitulo();
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(ESPACO));
        topo.add(criarFiltroDatas());

        conteudo.add(
                topo,
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

        conteudo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarLayout(conteudo.getWidth());
            }
        });

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

        return painel;
    }

    private void ajustarLayout(int largura) {

        boolean estreito = largura < LARGURA_MINIMA;

        if (estreito == layoutEstreito) {
            return;
        }

        layoutEstreito = estreito;

        ((GridLayout) painelCards.getLayout()).setColumns(estreito ? 1 : 2);
        painelCards.setPreferredSize(new Dimension(0, estreito ? 500 : 240));

        GridLayout gradeInferior = (GridLayout) painelInferior.getLayout();
        gradeInferior.setRows(estreito ? 2 : 1);
        gradeInferior.setColumns(estreito ? 1 : 2);
        gradeInferior.setVgap(estreito ? 12 : 0);
        painelInferior.setPreferredSize(new Dimension(0, estreito ? 680 : 340));

        painelCards.revalidate();
        painelInferior.revalidate();
    }

    private void atualizarTela() {

        lblTituloTabela.setText(
                "Resumo Financeiro (" +
                relatorio.getPeriodo() +
                ")"
        );

        preencherTabela();

        grafico.repaint();
    }

    private void preencherTabela() {

        modelTabela.setRowCount(0);

        for (ResumoMensal item :
                relatorio.getResumoMensal()) {

            modelTabela.addRow(
                    new Object[] {
                            item.getMesFormatado(),
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
    }

    private JPanel criarCards() {

        JPanel painel = new JPanel(
                new GridLayout(0, 2, 12, 12)
        );

        painel.setOpaque(false);

        painel.setPreferredSize(
                new Dimension(0, 240)
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

        painelCards = painel;

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

        JTextArea lblDescricao =
                new JTextArea(descricao);

        lblDescricao.setLineWrap(true);
        lblDescricao.setWrapStyleWord(true);
        lblDescricao.setEditable(false);
        lblDescricao.setFocusable(false);
        lblDescricao.setOpaque(false);
        lblDescricao.setBorder(null);
        lblDescricao.setForeground(
                new Color(100, 116, 139)
        );

        lblDescricao.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        JPanel informacoes =
                new JPanel(
                        new BorderLayout(0, 7)
                );

        informacoes.setOpaque(false);

        informacoes.add(
                lblTitulo,
                BorderLayout.NORTH
        );
        informacoes.add(
                lblDescricao,
                BorderLayout.CENTER
        );

        card.add(
                informacoes,
                BorderLayout.CENTER
        );

        JButton btnGerar =
                new JButton("Gerar Relatório");

        btnGerar.setPreferredSize(
                new Dimension(135, 30)
        );

        btnGerar.addActionListener(e -> gerarWord(titulo));

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
                        new FlowLayout(
                                FlowLayout.RIGHT,
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

    private void gerarWord(String titulo) {

        try {

            XWPFDocument documento = new XWPFDocument();

            File arquivo = File.createTempFile("relatorio_", ".docx");

            FileOutputStream saida = new FileOutputStream(arquivo);

            documento.write(saida);

            saida.close();
            documento.close();

            Desktop.getDesktop().open(arquivo);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar o relatório."
            );
        }
    }

    private JPanel criarParteInferior() {

        JPanel painel =
                new JPanel(
                        new GridLayout(1, 2, 12, 0)
                );

        painel.setOpaque(false);

        painel.setPreferredSize(
                new Dimension(0, 340)
        );

        painel.add(
                criarGraficoMensal()
        );

        painel.add(
                criarTabelaResumo()
        );

        painelInferior = painel;

        return painel;
    }

    private JPanel criarGraficoMensal() {

        grafico = new PainelGraficoMensal(relatorio);

        return grafico;
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

        lblTituloTabela =
                new JLabel(
                        "Resumo Financeiro (" +
                        relatorio.getPeriodo() +
                        ")"
                );

        lblTituloTabela.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        lblTituloTabela.setBorder(
                new EmptyBorder(
                        0, 0, 8, 0
                )
        );

        painel.add(
                lblTituloTabela,
                BorderLayout.NORTH
        );

        String[] colunas = {
                "Mês",
                "Receita",
                "Despesa",
                "Lucro",
                "Margem%"
        };

        modelTabela =
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

        preencherTabela();

        JTable tabela =
                new JTable(modelTabela);

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
        scroll.getViewport().setBackground(Color.WHITE);

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

    // ===================== FILTRO POR PERÍODO =====================

    private JPanel criarFiltroDatas() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtDataInicial = new JTextField(8);
        txtDataFinal = new JTextField(8);

        txtDataInicial.setText(FORMATO_DATA.format(relatorio.getFiltroInicio()));
        txtDataFinal.setText(FORMATO_DATA.format(relatorio.getFiltroFim()));

        configurarCampoData(txtDataInicial);
        configurarCampoData(txtDataFinal);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(Color.WHITE);
        btnFiltrar.setForeground(COR_TEXTO);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                new EmptyBorder(6, 14, 6, 14)));
        btnFiltrar.addActionListener(e -> filtrarPorPeriodo());

        painel.add(new JLabel("De:"));
        painel.add(txtDataInicial);
        painel.add(new JLabel("Até:"));
        painel.add(txtDataFinal);
        painel.add(btnFiltrar);

        return painel;
    }

    private void configurarCampoData(JTextField campo) {
        campo.setEditable(false);
        campo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        campo.setBackground(Color.WHITE);
        campo.setForeground(COR_TEXTO);
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                new EmptyBorder(6, 8, 6, 8)));

        campo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarCalendario(campo);
            }
        });
    }

    private void mostrarCalendario(JTextField campo) {
        LocalDate dataAtual;
        try {
            dataAtual = LocalDate.parse(campo.getText().trim(), FORMATO_DATA);
        } catch (Exception e) {
            dataAtual = LocalDate.now();
        }

        final JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(COR_BORDA));
        popup.setBackground(Color.WHITE);

        JPanel calendario = new JPanel();
        calendario.setLayout(new BoxLayout(calendario, BoxLayout.Y_AXIS));
        calendario.setBackground(Color.WHITE);
        calendario.setBorder(new EmptyBorder(12, 12, 12, 12));

        final LocalDate[] dataSelecionada = {dataAtual};
        final YearMonth[] mesAtual = {YearMonth.from(dataAtual)};
        final boolean[] selecionandoMes = {false};

        construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);

        popup.add(calendario);
        popup.show(campo, 0, campo.getHeight() + 4);
    }

    private void construirCalendario(JPanel calendario, JPopupMenu popup, JTextField campo,
                                     LocalDate[] dataSelecionada, YearMonth[] mesAtual,
                                     boolean[] selecionandoMes) {
        calendario.removeAll();

        if (selecionandoMes[0]) {
            construirSelecaoMes(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        } else {
            construirSelecaoDia(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        }

        calendario.revalidate();
        calendario.repaint();
    }

    private void construirSelecaoDia(JPanel calendario, JPopupMenu popup, JTextField campo,
                                     LocalDate[] dataSelecionada, YearMonth[] mesAtual,
                                     boolean[] selecionandoMes) {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setMaximumSize(new Dimension(260, 40));

        JButton anterior = criarBotaoNavegacao("‹");
        JButton proximo = criarBotaoNavegacao("›");

        JButton mesAno = new JButton(formatarMesAno(mesAtual[0]));
        mesAno.setFont(new Font("SansSerif", Font.BOLD, 13));
        mesAno.setForeground(COR_TEXTO);
        mesAno.setBackground(Color.WHITE);
        mesAno.setFocusPainted(false);
        mesAno.setBorderPainted(false);
        mesAno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        anterior.addActionListener(e -> {
            mesAtual[0] = mesAtual[0].minusMonths(1);
            construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        });

        proximo.addActionListener(e -> {
            mesAtual[0] = mesAtual[0].plusMonths(1);
            construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        });

        mesAno.addActionListener(e -> {
            selecionandoMes[0] = true;
            construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        });

        cabecalho.add(anterior, BorderLayout.WEST);
        cabecalho.add(mesAno, BorderLayout.CENTER);
        cabecalho.add(proximo, BorderLayout.EAST);

        calendario.add(cabecalho);
        calendario.add(Box.createVerticalStrut(8));

        JPanel diasSemana = new JPanel(new GridLayout(1, 7, 2, 2));
        diasSemana.setOpaque(false);
        diasSemana.setMaximumSize(new Dimension(260, 25));

        String[] nomesDias = {"D", "S", "T", "Q", "Q", "S", "S"};
        for (String nome : nomesDias) {
            JLabel label = new JLabel(nome, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 10));
            label.setForeground(COR_CINZA);
            diasSemana.add(label);
        }

        calendario.add(diasSemana);
        calendario.add(Box.createVerticalStrut(4));

        JPanel gradeDias = new JPanel(new GridLayout(6, 7, 2, 2));
        gradeDias.setOpaque(false);
        gradeDias.setPreferredSize(new Dimension(260, 210));

        LocalDate primeiroDia = mesAtual[0].atDay(1);

        int espacosAntes = primeiroDia.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < espacosAntes; i++) {
            gradeDias.add(new JLabel(""));
        }

        for (int dia = 1; dia <= mesAtual[0].lengthOfMonth(); dia++) {
            LocalDate data = mesAtual[0].atDay(dia);

            JButton botaoDia = new JButton(String.valueOf(dia));
            botaoDia.setFocusPainted(false);
            botaoDia.setMargin(new Insets(0, 0, 0, 0));
            botaoDia.setFont(new Font("SansSerif", Font.PLAIN, 11));
            botaoDia.setForeground(COR_TEXTO);
            botaoDia.setBackground(Color.WHITE);
            botaoDia.setBorderPainted(false);
            botaoDia.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if (data.equals(dataSelecionada[0])) {
                botaoDia.setBackground(COR_AZUL);
                botaoDia.setForeground(Color.WHITE);
                botaoDia.setFont(new Font("SansSerif", Font.BOLD, 11));
            }

            botaoDia.addActionListener(e -> {
                dataSelecionada[0] = data;
                campo.setText(FORMATO_DATA.format(data));
                popup.setVisible(false);
            });

            gradeDias.add(botaoDia);
        }

        calendario.add(gradeDias);
    }

    private void construirSelecaoMes(JPanel calendario, JPopupMenu popup, JTextField campo,
                                     LocalDate[] dataSelecionada, YearMonth[] mesAtual,
                                     boolean[] selecionandoMes) {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setMaximumSize(new Dimension(260, 40));

        JButton anterior = criarBotaoNavegacao("‹");
        JButton proximo = criarBotaoNavegacao("›");

        JButton ano = new JButton(String.valueOf(mesAtual[0].getYear()));
        ano.setFont(new Font("SansSerif", Font.BOLD, 13));
        ano.setForeground(COR_TEXTO);
        ano.setBackground(Color.WHITE);
        ano.setFocusPainted(false);
        ano.setBorderPainted(false);

        anterior.addActionListener(e -> {
            mesAtual[0] = mesAtual[0].minusYears(1);
            construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        });

        proximo.addActionListener(e -> {
            mesAtual[0] = mesAtual[0].plusYears(1);
            construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        });

        cabecalho.add(anterior, BorderLayout.WEST);
        cabecalho.add(ano, BorderLayout.CENTER);
        cabecalho.add(proximo, BorderLayout.EAST);

        calendario.add(cabecalho);
        calendario.add(Box.createVerticalStrut(10));

        JPanel gradeMeses = new JPanel(new GridLayout(4, 3, 6, 6));
        gradeMeses.setOpaque(false);
        gradeMeses.setPreferredSize(new Dimension(260, 180));

        for (Month mes : Month.values()) {
            JButton botaoMes = new JButton(formatarNomeMes(mes));
            botaoMes.setFocusPainted(false);
            botaoMes.setBackground(Color.WHITE);
            botaoMes.setForeground(COR_TEXTO);
            botaoMes.setBorder(BorderFactory.createLineBorder(COR_BORDA));
            botaoMes.setFont(new Font("SansSerif", Font.PLAIN, 11));

            if (mesAtual[0].getMonth() == mes) {
                botaoMes.setBackground(COR_AZUL);
                botaoMes.setForeground(Color.WHITE);
                botaoMes.setFont(new Font("SansSerif", Font.BOLD, 11));
            }

            botaoMes.addActionListener(e -> {
                mesAtual[0] = YearMonth.of(mesAtual[0].getYear(), mes);
                selecionandoMes[0] = false;
                construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
            });

            gradeMeses.add(botaoMes);
        }

        calendario.add(gradeMeses);
    }

    private JButton criarBotaoNavegacao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 18));
        botao.setForeground(COR_TEXTO);
        botao.setBackground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setMargin(new Insets(0, 8, 0, 8));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private String formatarMesAno(YearMonth mes) {
        String texto = mes.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR")));
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    private String formatarNomeMes(Month mes) {
        String texto = mes.getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    private void filtrarPorPeriodo() {
        try {
            LocalDate inicio = LocalDate.parse(txtDataInicial.getText().trim(), FORMATO_DATA);
            LocalDate fim = LocalDate.parse(txtDataFinal.getText().trim(), FORMATO_DATA);

            if (fim.isBefore(inicio)) {
                JOptionPane.showMessageDialog(this,
                        "A data final não pode ser anterior à data inicial.",
                        "Período inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            relatorio.setPeriodo(inicio, fim);
            atualizarTela();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Digite as datas no formato dd/MM/yyyy.",
                    "Data inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static class PainelRolavel extends JPanel implements Scrollable {

        PainelRolavel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return visibleRect.height;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            Container pai = getParent();
            return pai instanceof JViewport && pai.getHeight() > getPreferredSize().height;
        }
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
                    Math.max(getHeight() - 85, 1);

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
                    Math.max(largura / quantidade, 1);

            int larguraBarra =
                    Math.max(2, Math.min(10, espacamento / 3));

            int passoRotulo = 1;

            while (espacamento * passoRotulo < 28) {
                passoRotulo++;
            }

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
                maiorValor = 1;
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
                        Math.max((espacamento - (larguraBarra * 2 + 4)) / 2, 0);

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
                        x + larguraBarra + 4,
                        inicioY +
                        altura -
                        alturaDespesa,
                        larguraBarra,
                        alturaDespesa
                );

                if (i % passoRotulo != 0) {
                    continue;
                }

                String mes =
                        item.getMesFormatado();

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