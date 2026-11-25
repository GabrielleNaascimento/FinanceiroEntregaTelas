package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import java.awt.Desktop;
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import java.time.LocalDate;
=======
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
>>>>>>> Stashed changes
=======
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
>>>>>>> Stashed changes
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
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
<<<<<<< Updated upstream
=======
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
>>>>>>> Stashed changes

import model.Relatorio;
import model.Relatorio.ResumoMensal;

=======
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

=======
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

>>>>>>> Stashed changes
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
<<<<<<< Updated upstream
=======

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import dao.DespesasDAO;
import model.Contas;
import model.Despesas;
import model.Relatorio;
import model.Relatorio.ResumoMensal;
>>>>>>> Stashed changes

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import dao.DespesasDAO;
import model.Contas;
import model.Despesas;
import model.Relatorio;
import model.Relatorio.ResumoMensal;

>>>>>>> Stashed changes
public class TelaRelatorios extends JPanel {

    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO = new Color(30, 38, 52);
    private static final Color COR_CINZA = new Color(105, 114, 128);
    private static final Color COR_AZUL = new Color(37, 99, 235);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final String USUARIO = "Jefferson Riper (Administrador)";

    private static final String DRE = "DRE — Demonstração de Resultado";
    private static final String CATEGORIAS = "Despesas por Categoria";
    private static final String AGING = "Inadimplência e Aging";
    private static final String CLIENTES = "Faturamento por Cliente";
    private static final String FORNECEDORES = "Pagamentos por Fornecedor";
    private static final String CENTROS = "Centro de Custos";
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

    private static final int ESPACO = 15;
    private static final int LARGURA_MINIMA = 800;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    private static final int ALTURA_CARD = 120;
    private static final int ESPACO_CARDS = 12;

    private static final String[][] RELATORIOS = {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            {"DRE — Demonstração de Resultado",
                    "Balanço consolidado de receitas, custos, provisões e margem de lucro líquido."},
            {"Despesas por Categoria",
                    "Breakdown detalhado e agrupado de gastos operacionais e fixos."},
            {"Inadimplência e Aging",
                    "Títulos vencidos agrupados por faixa de atraso (1-30, 31-60, 61-90 e +90 dias)."},
            {"Faturamento por Cliente",
                    "Ranking de clientes por receita, ticket médio e participação no faturamento."},
            {"Pagamentos por Fornecedor",
                    "Total pago e a pagar por fornecedor, com prazos médios de pagamento."},
            {"Centro de Custos",
                    "Distribuição de despesas e resultados por departamento ou centro de custo."}
    };

>>>>>>> Stashed changes
=======
=======
>>>>>>> Stashed changes
            {DRE,
                    "Balanço consolidado de receitas, custos, provisões e margem de lucro líquido."},
            {CATEGORIAS,
                    "Breakdown detalhado e agrupado de gastos operacionais e fixos."},
            {AGING,
                    "Títulos vencidos agrupados por faixa de atraso (1-30, 31-60, 61-90 e +90 dias)."},
            {CLIENTES,
                    "Ranking de clientes por receita, ticket médio e participação no faturamento."},
            {FORNECEDORES,
                    "Total pago e a pagar por fornecedor, com prazos médios de pagamento."},
            {CENTROS,
                    "Distribuição de despesas e resultados por departamento ou centro de custo."}
    };

<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
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
                new JLabel(USUARIO);

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
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        painelCards.setPreferredSize(new Dimension(0, estreito ? 500 : 240));
=======
        painelCards.setPreferredSize(new Dimension(0, calcularAlturaCards(estreito)));
>>>>>>> Stashed changes
=======
        painelCards.setPreferredSize(new Dimension(0, calcularAlturaCards(estreito)));
>>>>>>> Stashed changes
=======
        painelCards.setPreferredSize(new Dimension(0, calcularAlturaCards(estreito)));
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
                new GridLayout(0, 2, 12, 12)
=======
                new GridLayout(0, 2, ESPACO_CARDS, ESPACO_CARDS)
>>>>>>> Stashed changes
=======
                new GridLayout(0, 2, ESPACO_CARDS, ESPACO_CARDS)
>>>>>>> Stashed changes
=======
                new GridLayout(0, 2, ESPACO_CARDS, ESPACO_CARDS)
>>>>>>> Stashed changes
        );

        painel.setOpaque(false);

        painel.setPreferredSize(
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
                new Dimension(0, 240)
=======
                new Dimension(0, calcularAlturaCards(false))
>>>>>>> Stashed changes
=======
                new Dimension(0, calcularAlturaCards(false))
>>>>>>> Stashed changes
=======
                new Dimension(0, calcularAlturaCards(false))
>>>>>>> Stashed changes
        );

        for (String[] relatorioInfo : RELATORIOS) {
            painel.add(
                    criarCardRelatorio(
                            relatorioInfo[0],
                            relatorioInfo[1]
                    )
            );
        }

        painelCards = painel;
<<<<<<< Updated upstream
<<<<<<< Updated upstream

        painelCards = painel;
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

        return painel;
    }

    private int calcularAlturaCards(boolean estreito) {

        int colunas = estreito ? 1 : 2;

        int linhas = (int) Math.ceil(RELATORIOS.length / (double) colunas);

        return linhas * ALTURA_CARD + (linhas - 1) * ESPACO_CARDS;
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

            DadosRelatorio dados = montarRelatorio(titulo);
<<<<<<< Updated upstream

<<<<<<< Updated upstream
            XWPFParagraph paragrafoTitulo = documento.createParagraph();
            XWPFRun runTitulo = paragrafoTitulo.createRun();
            runTitulo.setBold(true);
            runTitulo.setFontSize(18);
            runTitulo.setText(titulo);

            XWPFParagraph paragrafoPeriodo = documento.createParagraph();
            XWPFRun runPeriodo = paragrafoPeriodo.createRun();
            runPeriodo.setFontSize(11);
            runPeriodo.setText("Período: " + relatorio.getPeriodo());

            File arquivo = File.createTempFile("relatorio_", ".docx");
=======
            JFileChooser seletor = new JFileChooser();
            seletor.setDialogTitle("Salvar relatório");
            seletor.setSelectedFile(new File("Relatorio_" + nomeArquivo(titulo) + ".docx"));
>>>>>>> Stashed changes
=======

            JFileChooser seletor = new JFileChooser();
            seletor.setDialogTitle("Salvar relatório");
            seletor.setSelectedFile(new File("Relatorio_" + nomeArquivo(titulo) + ".docx"));
>>>>>>> Stashed changes

            if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File arquivo = seletor.getSelectedFile();

            if (!arquivo.getName().toLowerCase().endsWith(".docx")) {
                arquivo = new File(arquivo.getAbsolutePath() + ".docx");
            }

            try (XWPFDocument documento = new XWPFDocument();
                 FileOutputStream saida = new FileOutputStream(arquivo)) {

                adicionarParagrafo(documento, titulo, 16, true);
                adicionarParagrafo(documento, "Período: "
                        + formatarData(relatorio.getFiltroInicio())
                        + " até "
                        + formatarData(relatorio.getFiltroFim()), 11, false);
                adicionarParagrafo(documento, "Data utilizada: " + dados.criterioData, 10, false);
                adicionarParagrafo(documento, "Gerado em "
                        + LocalDateTime.now().format(FORMATO_DATA_HORA)
                        + " por " + USUARIO, 10, false);

                documento.createParagraph();

                if (!dados.resumo.isEmpty()) {
                    adicionarParagrafo(documento, "Resumo", 12, true);
                    adicionarTabela(documento, new String[] {"Indicador", "Valor"}, dados.resumo, null);
                    documento.createParagraph();
                    adicionarParagrafo(documento, "Detalhamento", 12, true);
                }

                adicionarTabela(documento, dados.colunas, dados.linhas, dados.totais);

                if (dados.aviso != null) {
                    documento.createParagraph();
                    adicionarParagrafo(documento, dados.aviso, 9, false);
                }

                documento.write(saida);
            }

            JOptionPane.showMessageDialog(this,
                    "Relatório exportado com sucesso.",
                    "Exportação concluída", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Não foi possível exportar o relatório: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarParagrafo(XWPFDocument documento, String texto, int tamanho, boolean negrito) {

        XWPFParagraph paragrafo = documento.createParagraph();
        paragrafo.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun run = paragrafo.createRun();
        run.setText(texto);
        run.setBold(negrito);
        run.setFontSize(tamanho);
    }

    private void adicionarTabela(XWPFDocument documento, String[] colunas,
                                 List<String[]> linhas, String[] totais) {

        XWPFTable tabelaWord = documento.createTable();
        XWPFTableRow cabecalho = tabelaWord.getRow(0);

        for (int i = 0; i < colunas.length; i++) {
            XWPFTableCell celula = i == 0 ? cabecalho.getCell(0) : cabecalho.addNewTableCell();
            escreverCelula(celula, colunas[i], true);
        }

        for (String[] linha : linhas) {
            XWPFTableRow linhaWord = tabelaWord.createRow();
            for (int i = 0; i < linha.length; i++) {
                escreverCelula(linhaWord.getCell(i), linha[i], false);
            }
        }

        if (linhas.isEmpty()) {
            XWPFTableRow vazia = tabelaWord.createRow();
            escreverCelula(vazia.getCell(0), "Nenhum registro encontrado no período.", false);
        }

        if (totais != null) {
            XWPFTableRow linhaTotal = tabelaWord.createRow();
            for (int i = 0; i < totais.length; i++) {
                escreverCelula(linhaTotal.getCell(i), totais[i], true);
            }
        }
    }

    private void escreverCelula(XWPFTableCell celula, String texto, boolean negrito) {

        XWPFRun run = celula.getParagraphs().get(0).createRun();
        run.setText(texto == null ? "" : texto);
        run.setBold(negrito);
    }

    private String nomeArquivo(String titulo) {

        String semAcento = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return semAcento
                .replaceAll("[^A-Za-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }

    private DadosRelatorio montarRelatorio(String titulo) {

        switch (titulo) {
            case DRE:
                return montarDre();
            case CATEGORIAS:
                return montarDespesasDao("Categoria", "Maior categoria", null);
            case AGING:
                return montarAging();
            case CLIENTES:
                return montarClientes();
            case FORNECEDORES:
                return montarFornecedores();
            case CENTROS:
                return montarDespesasDao("Centro de Custo", "Maior centro",
<<<<<<< Updated upstream
                        ""
                        + " ");
=======
                        "Os dados ainda não possuem centro de custo; as categorias do DespesasDAO foram usadas como centros. "
                        + "Receitas e resultado por centro dependem de um campo de centro de custo nas contas.");
>>>>>>> Stashed changes
            default:
                throw new IllegalArgumentException("Relatório desconhecido: " + titulo);
        }
    }

    private DadosRelatorio montarDre() {

        DadosRelatorio dados = new DadosRelatorio();

        LocalDate inicio = relatorio.getFiltroInicio();
        LocalDate fim = relatorio.getFiltroFim();
        LocalDate[] anterior = periodoAnterior();

        double receita = somar(contasNoPeriodo(Contas.RECEBER, true, inicio, fim, false));
        double despesa = somar(contasNoPeriodo(Contas.PAGAR, true, inicio, fim, false));
        double receitaAnterior = somar(contasNoPeriodo(Contas.RECEBER, true, anterior[0], anterior[1], false));
        double despesaAnterior = somar(contasNoPeriodo(Contas.PAGAR, true, anterior[0], anterior[1], false));

        double resultado = receita - despesa;
        double resultadoAnterior = receitaAnterior - despesaAnterior;

        double margem = receita == 0 ? 0 : resultado * 100 / receita;
        double margemAnterior = receitaAnterior == 0 ? 0 : resultadoAnterior * 100 / receitaAnterior;

        dados.criterioData = "Data de vencimento das contas recebidas e pagas";
<<<<<<< Updated upstream
        dados.aviso = "Somente contas concluídas."
=======
        dados.aviso = "Considera somente contas concluídas. Como as contas não possuem classificação de custos e deduções, "
>>>>>>> Stashed changes
                + "o resultado é calculado como receitas menos despesas.";
        dados.colunas = new String[] {"Descrição", "Período atual", "Período anterior", "Variação"};

        dados.linhas.add(new String[] {"Receita bruta (contas recebidas)",
                formatarMoeda(receita), formatarMoeda(receitaAnterior),
                formatarVariacao(receita, receitaAnterior)});
        dados.linhas.add(new String[] {"Despesas realizadas (contas pagas)",
                formatarMoeda(despesa), formatarMoeda(despesaAnterior),
                formatarVariacao(despesa, despesaAnterior)});
        dados.linhas.add(new String[] {"Resultado do período",
                formatarMoeda(resultado), formatarMoeda(resultadoAnterior),
                formatarVariacao(resultado, resultadoAnterior)});
        dados.linhas.add(new String[] {"Margem líquida",
                formatarPercentual(margem), formatarPercentual(margemAnterior),
                String.format("%+.1f p.p.", margem - margemAnterior)});

        dados.resumo.add(new String[] {"Período atual",
                formatarData(inicio) + " a " + formatarData(fim)});
        dados.resumo.add(new String[] {"Período anterior",
                formatarData(anterior[0]) + " a " + formatarData(anterior[1])});

        return dados;
    }

    private DadosRelatorio montarDespesasDao(String nomeColuna, String rotuloMaior, String aviso) {

        DadosRelatorio dados = new DadosRelatorio();

        dados.criterioData = "Sem data. O DespesasDAO não informa datas, então o período não altera estes valores";
        dados.aviso = aviso;
        dados.colunas = new String[] {nomeColuna, "Valor", "Participação"};

        List<Despesas> despesas = new ArrayList<>(new DespesasDAO().listarDespesas());
        despesas.sort((a, b) -> Double.compare(b.getValor(), a.getValor()));

        double total = 0;

        for (Despesas despesa : despesas) {
            total += despesa.getValor();
        }

        for (Despesas despesa : despesas) {
            double percentual = total == 0 ? 0 : despesa.getValor() * 100 / total;
            dados.linhas.add(new String[] {
                    despesa.getCategoria(),
                    formatarMoeda(despesa.getValor()),
                    formatarPercentual(percentual)});
        }

        dados.totais = new String[] {"Total", formatarMoeda(total),
                formatarPercentual(total == 0 ? 0 : 100)};

        if (!despesas.isEmpty()) {
            Despesas maior = despesas.get(0);
            dados.resumo.add(new String[] {rotuloMaior,
                    maior.getCategoria() + " — " + formatarMoeda(maior.getValor())});
        }

        dados.resumo.add(new String[] {"Registros", String.valueOf(despesas.size())});

        return dados;
    }

    private DadosRelatorio montarAging() {

        DadosRelatorio dados = new DadosRelatorio();

        LocalDate inicio = relatorio.getFiltroInicio();
        LocalDate fim = relatorio.getFiltroFim();
        LocalDate hoje = LocalDate.now();

        dados.criterioData = "Data de vencimento dentro do período; dias em atraso contados até "
                + formatarData(hoje);
        dados.colunas = new String[] {"Cliente", "Descrição", "Vencimento",
                "Dias em atraso", "Faixa", "Valor"};

        List<Contas> vencidas = new ArrayList<>();

        for (Contas conta : contasNoPeriodo(Contas.RECEBER, false, inicio, fim, false)) {
            if (vencida(conta)) {
                vencidas.add(conta);
            }
        }

        vencidas.sort(Comparator.comparing(Contas::getDataVencimento));

        String[] faixas = {"1-30 dias", "31-60 dias", "61-90 dias", "+90 dias"};
        double[] totaisFaixas = new double[4];
        Map<String, Double> devedores = new LinkedHashMap<>();
        double totalVencido = 0;

        for (Contas conta : vencidas) {
            long dias = Math.max(ChronoUnit.DAYS.between(conta.getDataVencimento(), hoje), 0);
            int faixa = dias <= 30 ? 0 : dias <= 60 ? 1 : dias <= 90 ? 2 : 3;

            totaisFaixas[faixa] += conta.getValor();
            totalVencido += conta.getValor();
            devedores.merge(conta.getEntidade(), conta.getValor(), Double::sum);

            dados.linhas.add(new String[] {
                    conta.getEntidade(),
                    conta.getDescricao(),
                    formatarData(conta.getDataVencimento()),
                    String.valueOf(dias),
                    faixas[faixa],
                    formatarMoeda(conta.getValor())});
        }

        double carteira = somar(contasNoPeriodo(Contas.RECEBER, inicio, fim, false));

        dados.totais = new String[] {"Total vencido", "", "", "", "", formatarMoeda(totalVencido)};

        for (int i = 0; i < faixas.length; i++) {
            dados.resumo.add(new String[] {faixas[i], formatarMoeda(totaisFaixas[i])});
        }

        dados.resumo.add(new String[] {"Total vencido", formatarMoeda(totalVencido)});
        dados.resumo.add(new String[] {"Inadimplência sobre a carteira",
                formatarPercentual(carteira == 0 ? 0 : totalVencido * 100 / carteira)});

        String maiorDevedor = "-";
        double maiorValor = 0;

        for (Map.Entry<String, Double> devedor : devedores.entrySet()) {
            if (devedor.getValue() > maiorValor) {
                maiorValor = devedor.getValue();
                maiorDevedor = devedor.getKey() + " — " + formatarMoeda(devedor.getValue());
            }
        }

        dados.resumo.add(new String[] {"Maior devedor", maiorDevedor});

        return dados;
    }

    private DadosRelatorio montarClientes() {

        DadosRelatorio dados = new DadosRelatorio();

        LocalDate inicio = relatorio.getFiltroInicio();
        LocalDate fim = relatorio.getFiltroFim();
        LocalDate[] anterior = periodoAnterior();

        List<Contas> atual = contasNoPeriodo(Contas.RECEBER, inicio, fim, true);
        List<Contas> anteriores = contasNoPeriodo(Contas.RECEBER, anterior[0], anterior[1], true);

        dados.criterioData = "Data de emissão (início) das contas a receber, concluídas e pendentes";
        dados.colunas = new String[] {"Pos.", "Cliente", "Qtd", "Faturado", "Ticket médio",
                "Participação", "Var. período anterior", "Curva"};

        Map<String, Double> faturado = agrupar(atual);
        Map<String, Double> faturadoAnterior = agrupar(anteriores);
        Map<String, Integer> quantidades = new LinkedHashMap<>();

        for (Contas conta : atual) {
            quantidades.merge(conta.getEntidade(), 1, Integer::sum);
        }

        double total = somar(atual);
        double totalAnterior = somar(anteriores);

        List<Map.Entry<String, Double>> ranking = new ArrayList<>(faturado.entrySet());
        ranking.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        double acumulado = 0;
        int posicao = 1;

        for (Map.Entry<String, Double> item : ranking) {
            String cliente = item.getKey();
            double valor = item.getValue();
            int quantidade = quantidades.get(cliente);

            double participacao = total == 0 ? 0 : valor * 100 / total;
            String curva = acumulado < 80 ? "A" : acumulado < 95 ? "B" : "C";
            acumulado += participacao;

            Double valorAnterior = faturadoAnterior.get(cliente);

            dados.linhas.add(new String[] {
                    String.valueOf(posicao++),
                    cliente,
                    String.valueOf(quantidade),
                    formatarMoeda(valor),
                    formatarMoeda(valor / quantidade),
                    formatarPercentual(participacao),
                    valorAnterior == null ? "-" : formatarVariacao(valor, valorAnterior),
                    curva});
        }

        dados.totais = new String[] {"", "Total", String.valueOf(atual.size()),
                formatarMoeda(total),
                formatarMoeda(atual.isEmpty() ? 0 : total / atual.size()),
                formatarPercentual(total == 0 ? 0 : 100),
                formatarVariacao(total, totalAnterior), ""};

        dados.resumo.add(new String[] {"Período anterior",
                formatarData(anterior[0]) + " a " + formatarData(anterior[1])});
        dados.resumo.add(new String[] {"Clientes no período", String.valueOf(ranking.size())});

        return dados;
    }

    private DadosRelatorio montarFornecedores() {

        DadosRelatorio dados = new DadosRelatorio();

        LocalDate inicio = relatorio.getFiltroInicio();
        LocalDate fim = relatorio.getFiltroFim();

        dados.criterioData = "Data de vencimento das contas a pagar; prazo médio = vencimento menos data de emissão (início)";
        dados.colunas = new String[] {"Fornecedor", "Títulos", "Pago", "A pagar", "Vencido",
                "Total", "Participação", "Prazo médio"};

        Map<String, double[]> fornecedores = new LinkedHashMap<>();
        List<Contas> proximas = new ArrayList<>();

        for (Contas conta : contasNoPeriodo(Contas.PAGAR, true, inicio, fim, false)) {
            double[] valores = fornecedores.computeIfAbsent(conta.getEntidade(), k -> new double[6]);
            valores[0] += conta.getValor();
            registrarTitulo(valores, conta);
        }

        for (Contas conta : contasNoPeriodo(Contas.PAGAR, false, inicio, fim, false)) {
            double[] valores = fornecedores.computeIfAbsent(conta.getEntidade(), k -> new double[6]);

            if (vencida(conta)) {
                valores[2] += conta.getValor();
            } else {
                valores[1] += conta.getValor();
                proximas.add(conta);
            }

            registrarTitulo(valores, conta);
        }

        List<Map.Entry<String, double[]>> ranking = new ArrayList<>(fornecedores.entrySet());
        ranking.sort((a, b) -> Double.compare(
                b.getValue()[0] + b.getValue()[1] + b.getValue()[2],
                a.getValue()[0] + a.getValue()[1] + a.getValue()[2]));

        double[] soma = new double[6];

        for (Map.Entry<String, double[]> item : ranking) {
            for (int i = 0; i < soma.length; i++) {
                soma[i] += item.getValue()[i];
            }
        }

        double totalGeral = soma[0] + soma[1] + soma[2];

        for (Map.Entry<String, double[]> item : ranking) {
            double[] v = item.getValue();
            double total = v[0] + v[1] + v[2];

            dados.linhas.add(new String[] {
                    item.getKey(),
                    String.valueOf((int) v[3]),
                    formatarMoeda(v[0]),
                    formatarMoeda(v[1]),
                    formatarMoeda(v[2]),
                    formatarMoeda(total),
                    formatarPercentual(totalGeral == 0 ? 0 : total * 100 / totalGeral),
                    v[5] == 0 ? "-" : String.format("%.0f dias", v[4] / v[5])});
        }

        dados.totais = new String[] {"Total", String.valueOf((int) soma[3]),
                formatarMoeda(soma[0]), formatarMoeda(soma[1]), formatarMoeda(soma[2]),
                formatarMoeda(totalGeral), formatarPercentual(totalGeral == 0 ? 0 : 100),
                soma[5] == 0 ? "-" : String.format("%.0f dias", soma[4] / soma[5])};

        proximas.sort(Comparator.comparing(Contas::getDataVencimento));

        for (int i = 0; i < Math.min(3, proximas.size()); i++) {
            Contas conta = proximas.get(i);
            dados.resumo.add(new String[] {"Próximo vencimento",
                    formatarData(conta.getDataVencimento()) + " — "
                    + conta.getEntidade() + " — "
                    + formatarMoeda(conta.getValor())});
        }

        return dados;
    }

    private void registrarTitulo(double[] valores, Contas conta) {

        valores[3]++;

        if (conta.getDataInicio() != null && conta.getDataVencimento() != null) {
            valores[4] += ChronoUnit.DAYS.between(conta.getDataInicio(), conta.getDataVencimento());
            valores[5]++;
        }
    }

    private LocalDate[] periodoAnterior() {

        LocalDate inicio = relatorio.getFiltroInicio();
        LocalDate fim = relatorio.getFiltroFim();

        long dias = ChronoUnit.DAYS.between(inicio, fim) + 1;
        LocalDate fimAnterior = inicio.minusDays(1);

        return new LocalDate[] {fimAnterior.minusDays(dias - 1), fimAnterior};
    }

    private List<Contas> contasNoPeriodo(String tipo, boolean concluida,
                                         LocalDate inicio, LocalDate fim, boolean pelaEmissao) {

        List<Contas> resultado = new ArrayList<>();

        for (Contas conta : Contas.listar(tipo, concluida)) {
            LocalDate data = pelaEmissao ? conta.getDataInicio() : conta.getDataVencimento();

            if (data != null && !data.isBefore(inicio) && !data.isAfter(fim)) {
                resultado.add(conta);
            }
        }

        return resultado;
    }

    private List<Contas> contasNoPeriodo(String tipo, LocalDate inicio, LocalDate fim, boolean pelaEmissao) {

        List<Contas> resultado = contasNoPeriodo(tipo, true, inicio, fim, pelaEmissao);
        resultado.addAll(contasNoPeriodo(tipo, false, inicio, fim, pelaEmissao));

        return resultado;
    }

    private double somar(List<Contas> contas) {

        double total = 0;

        for (Contas conta : contas) {
            total += conta.getValor();
        }

        return total;
    }

    private Map<String, Double> agrupar(List<Contas> contas) {

        Map<String, Double> totais = new LinkedHashMap<>();

        for (Contas conta : contas) {
            totais.merge(conta.getEntidade(), conta.getValor(), Double::sum);
        }

        return totais;
    }

    private boolean vencida(Contas conta) {

        String status = conta.getStatus() == null ? "" : conta.getStatus().toLowerCase();
        LocalDate vencimento = conta.getDataVencimento();

        return status.contains("vencid")
                || status.contains("atras")
                || (vencimento != null && vencimento.isBefore(LocalDate.now()));
    }

    private String formatarData(LocalDate data) {

        return data == null ? "-" : FORMATO_DATA.format(data);
    }

    private String formatarPercentual(double valor) {

        return String.format("%.1f%%", valor);
    }

    private String formatarVariacao(double atual, double anterior) {

        if (anterior == 0) {
            return "-";
        }

        return String.format("%+.1f%%", (atual - anterior) * 100 / anterior);
    }

    private static class DadosRelatorio {

        String criterioData;
        String aviso;
        String[] colunas;
        String[] totais;
        List<String[]> linhas = new ArrayList<>();
        List<String[]> resumo = new ArrayList<>();
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