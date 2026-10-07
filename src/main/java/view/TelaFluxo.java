package view;

import model.Contas;
import model.Fluxo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class TelaFluxo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COR_MENU = new Color(27, 54, 93);
    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO = new Color(30, 38, 52);
    private static final Color COR_CINZA = new Color(105, 114, 128);
    private static final Color COR_AZUL = new Color(37, 99, 235);
    private static final Color COR_VERDE = new Color(16, 185, 129);
    private static final Color COR_VERMELHO = new Color(239, 68, 68);

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat FORMATO_MOEDA =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private static final int ALTURA_LINHA = 30;
    private static final int ALTURA_CABECALHO = 30;
    private static final int ESPACO = 15;

    private final ArrayList<Fluxo> movimentacoes = new ArrayList<>();

    private ArrayList<Fluxo> movimentacoesFiltradas =
            new ArrayList<>();

    private JTextField txtDataInicial;
    private JTextField txtDataFinal;

    private JPanel painelCards;
    private JPanel painelGrafico;
    private JPanel painelTabela;

    public TelaFluxo() {

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);

        carregarMovimentacoes();
        definirPeriodoInicial();
        aplicarFiltro();
        atualizarTela();
    }

    private void carregarMovimentacoes() {

        movimentacoes.clear();

        adicionarContasConcluidas(
                Contas.listar(Contas.RECEBER, true),
                true
        );

        adicionarContasConcluidas(
                Contas.listar(Contas.PAGAR, true),
                false
        );

        movimentacoes.sort(
                Comparator.comparing(Fluxo::getData)
        );

        recalcularSaldos();
    }

    private void adicionarContasConcluidas(
            ArrayList<Contas> contas,
            boolean entrada) {

        for (Contas conta : contas) {

            double valor = entrada
                    ? conta.getValor()
                    : -conta.getValor();

            String tipo = entrada
                    ? "Entrada"
                    : "Saída";

            String categoria = entrada
                    ? "Contas a Receber"
                    : "Contas a Pagar";

            movimentacoes.add(
                    new Fluxo(
                            conta.getDataVencimento(),
                            conta.getDescricao(),
                            tipo,
                            categoria,
                            valor,
                            0
                    )
            );
        }
    }

    private void recalcularSaldos() {

        double saldo = 0;

        for (Fluxo fluxo : movimentacoes) {

            saldo += fluxo.getValor();

            fluxo.setSaldo(saldo);
        }
    }

    private void definirPeriodoInicial() {

        if (movimentacoes.isEmpty()) {
            return;
        }

        LocalDate menor =
                movimentacoes.get(0).getData();

        LocalDate maior =
                movimentacoes.get(
                        movimentacoes.size() - 1
                ).getData();

        txtDataInicial = new JTextField(8);
        txtDataFinal = new JTextField(8);

        txtDataInicial.setText(
                FORMATO_DATA.format(
                        menor.withDayOfMonth(1)
                )
        );

        txtDataFinal.setText(
                FORMATO_DATA.format(
                        maior.withDayOfMonth(
                                maior.lengthOfMonth()
                        )
                )
        );
    }

    private void atualizarTela() {

        removeAll();

        add(
                criarAreaPrincipal(),
                BorderLayout.CENTER
        );

        revalidate();
        repaint();
    }

    private JPanel criarAreaPrincipal() {

        JPanel area =
                new JPanel(new BorderLayout());

        area.setBackground(COR_FUNDO);

        area.add(
                criarHeader(),
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

        JLabel titulo = new JLabel(
                "<html><h2 style='margin:0;'>Fluxo de Caixa</h2>"
                + "<span style='color:gray;'>"
                + "Análise de entradas, saídas e saldos de caixa"
                + "</span></html>"
        );

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(titulo);

        conteudo.add(
                Box.createVerticalStrut(ESPACO)
        );

        conteudo.add(criarFiltroDatas());

        conteudo.add(
                Box.createVerticalStrut(ESPACO)
        );

        painelCards = criarCards();

        painelCards.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(painelCards);

        conteudo.add(
                Box.createVerticalStrut(ESPACO)
        );

        painelGrafico = criarGrafico();

        painelGrafico.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(painelGrafico);

        conteudo.add(
                Box.createVerticalStrut(ESPACO)
        );

        painelTabela = criarTabela();

        painelTabela.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteudo.add(painelTabela);

        area.add(
                conteudo,
                BorderLayout.CENTER
        );

        return area;
    }

    private JPanel criarHeader() {

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        15,
                        25,
                        15,
                        25
                )
        );

        JLabel usuario =
                new JLabel(
                        "Jefferson - Administrador"
                );

        usuario.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        header.add(
                usuario,
                BorderLayout.EAST
        );

        return header;
    }

    /*
     * FILTRO DE DATAS
     */

    private JPanel criarFiltroDatas() {

        JPanel painel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        painel.setOpaque(false);

        painel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblDe =
                new JLabel("De:");

        JLabel lblAte =
                new JLabel("Até:");

        if (txtDataInicial == null) {

            txtDataInicial =
                    new JTextField(8);

            txtDataInicial.setText(
                    FORMATO_DATA.format(
                            LocalDate.now()
                                    .withDayOfMonth(1)
                    )
            );
        }

        if (txtDataFinal == null) {

            txtDataFinal =
                    new JTextField(8);

            txtDataFinal.setText(
                    FORMATO_DATA.format(
                            LocalDate.now()
                    )
            );
        }

        configurarCampoData(
                txtDataInicial
        );

        configurarCampoData(
                txtDataFinal
        );

        JButton btnFiltrar =
                new JButton("Filtrar");

        btnFiltrar.setBackground(
                Color.WHITE
        );

        btnFiltrar.setForeground(
                COR_TEXTO
        );

        btnFiltrar.setFocusPainted(false);

        btnFiltrar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA
                        ),
                        new EmptyBorder(
                                6,
                                14,
                                6,
                                14
                        )
                )
        );

        btnFiltrar.addActionListener(
                e -> filtrarPorPeriodo()
        );

        painel.add(lblDe);
        painel.add(txtDataInicial);
        painel.add(lblAte);
        painel.add(txtDataFinal);
        painel.add(btnFiltrar);

        return painel;
    }

    /*
     * Configura o campo para abrir o calendário
     * quando o usuário clicar nele.
     */
    private void configurarCampoData(
            JTextField campo) {

        campo.setEditable(false);

        campo.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        campo.setBackground(
                Color.WHITE
        );

        campo.setForeground(
                COR_TEXTO
        );

        campo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA
                        ),
                        new EmptyBorder(
                                6,
                                8,
                                6,
                                8
                        )
                )
        );

        /*
         * Evita adicionar vários MouseListeners
         * quando a tela é atualizada.
         */
        if (Boolean.TRUE.equals(
                campo.getClientProperty(
                        "calendarioConfigurado"
                ))) {

            return;
        }

        campo.putClientProperty(
                "calendarioConfigurado",
                Boolean.TRUE
        );

        campo.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e) {

                        mostrarCalendario(
                                campo
                        );
                    }
                }
        );
    }

    /*
     * Abre o calendário.
     */
    private void mostrarCalendario(
            JTextField campo) {

        LocalDate dataAtual;

        try {

            dataAtual =
                    LocalDate.parse(
                            campo.getText().trim(),
                            FORMATO_DATA
                    );

        } catch (Exception e) {

            dataAtual =
                    LocalDate.now();
        }

        final JPopupMenu popup =
                new JPopupMenu();

        popup.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA
                )
        );

        popup.setBackground(
                Color.WHITE
        );

        JPanel calendario =
                new JPanel();

        calendario.setLayout(
                new BoxLayout(
                        calendario,
                        BoxLayout.Y_AXIS
                )
        );

        calendario.setBackground(
                Color.WHITE
        );

        calendario.setBorder(
                new EmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        final LocalDate[] dataSelecionada = {
                dataAtual
        };

        final YearMonth[] mesAtual = {
                YearMonth.from(dataAtual)
        };

        final boolean[] selecionandoMes = {
                false
        };

        construirCalendario(
                calendario,
                popup,
                campo,
                dataSelecionada,
                mesAtual,
                selecionandoMes
        );

        popup.add(calendario);

        popup.show(
                campo,
                0,
                campo.getHeight() + 4
        );
    }

    /*
     * Decide se o calendário vai mostrar
     * os dias ou os meses.
     */
    private void construirCalendario(
            JPanel calendario,
            JPopupMenu popup,
            JTextField campo,
            LocalDate[] dataSelecionada,
            YearMonth[] mesAtual,
            boolean[] selecionandoMes) {

        calendario.removeAll();

        if (selecionandoMes[0]) {

            construirSelecaoMes(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );

        } else {

            construirSelecaoDia(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );
        }

        calendario.revalidate();
        calendario.repaint();
    }

    /*
     * Calendário com seleção de dias.
     */
    private void construirSelecaoDia(
            JPanel calendario,
            JPopupMenu popup,
            JTextField campo,
            LocalDate[] dataSelecionada,
            YearMonth[] mesAtual,
            boolean[] selecionandoMes) {

        JPanel cabecalho =
                new JPanel(
                        new BorderLayout()
                );

        cabecalho.setOpaque(false);

        cabecalho.setMaximumSize(
                new Dimension(260, 40)
        );

        JButton anterior =
                criarBotaoNavegacao("‹");

        JButton proximo =
                criarBotaoNavegacao("›");

        JButton mesAno =
                new JButton(
                        formatarMesAno(
                                mesAtual[0]
                        )
                );

        mesAno.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        mesAno.setForeground(
                COR_TEXTO
        );

        mesAno.setBackground(
                Color.WHITE
        );

        mesAno.setFocusPainted(false);

        mesAno.setBorderPainted(false);

        mesAno.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        anterior.addActionListener(e -> {

            mesAtual[0] =
                    mesAtual[0].minusMonths(1);

            construirCalendario(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );
        });

        proximo.addActionListener(e -> {

            mesAtual[0] =
                    mesAtual[0].plusMonths(1);

            construirCalendario(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );
        });

        /*
         * Clicar em "Outubro 2026", por exemplo,
         * abre a seleção dos 12 meses.
         */
        mesAno.addActionListener(e -> {

            selecionandoMes[0] = true;

            construirCalendario(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );
        });

        cabecalho.add(
                anterior,
                BorderLayout.WEST
        );

        cabecalho.add(
                mesAno,
                BorderLayout.CENTER
        );

        cabecalho.add(
                proximo,
                BorderLayout.EAST
        );

        calendario.add(cabecalho);

        calendario.add(
                Box.createVerticalStrut(8)
        );

        /*
         * Dias da semana.
         */
        JPanel diasSemana =
                new JPanel(
                        new GridLayout(
                                1,
                                7,
                                2,
                                2
                        )
                );

        diasSemana.setOpaque(false);

        diasSemana.setMaximumSize(
                new Dimension(260, 25)
        );

        String[] nomesDias = {
                "D",
                "S",
                "T",
                "Q",
                "Q",
                "S",
                "S"
        };

        for (String nome : nomesDias) {

            JLabel label =
                    new JLabel(
                            nome,
                            SwingConstants.CENTER
                    );

            label.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            10
                    )
            );

            label.setForeground(
                    COR_CINZA
            );

            diasSemana.add(label);
        }

        calendario.add(diasSemana);

        calendario.add(
                Box.createVerticalStrut(4)
        );

        /*
         * Grade dos dias.
         */
        JPanel gradeDias =
                new JPanel(
                        new GridLayout(
                                6,
                                7,
                                2,
                                2
                        )
                );

        gradeDias.setOpaque(false);

        gradeDias.setPreferredSize(
                new Dimension(
                        260,
                        210
                )
        );

        LocalDate primeiroDia =
                mesAtual[0].atDay(1);

        /*
         * Java:
         * segunda = 1
         * ...
         * domingo = 7
         *
         * Como queremos domingo primeiro,
         * usamos % 7.
         */
        int espacosAntes =
                primeiroDia
                        .getDayOfWeek()
                        .getValue() % 7;

        for (int i = 0;
             i < espacosAntes;
             i++) {

            gradeDias.add(
                    new JLabel("")
            );
        }

        for (int dia = 1;
             dia <= mesAtual[0].lengthOfMonth();
             dia++) {

            LocalDate data =
                    mesAtual[0].atDay(dia);

            JButton botaoDia =
                    new JButton(
                            String.valueOf(dia)
                    );

            botaoDia.setFocusPainted(
                    false
            );

            botaoDia.setMargin(
                    new Insets(
                            0,
                            0,
                            0,
                            0
                    )
            );

            botaoDia.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            11
                    )
            );

            botaoDia.setForeground(
                    COR_TEXTO
            );

            botaoDia.setBackground(
                    Color.WHITE
            );

            botaoDia.setBorderPainted(
                    false
            );

            botaoDia.setCursor(
                    new java.awt.Cursor(
                            java.awt.Cursor.HAND_CURSOR
                    )
            );

            /*
             * Destaca a data que está atualmente
             * selecionada.
             */
            if (data.equals(
                    dataSelecionada[0])) {

                botaoDia.setBackground(
                        COR_AZUL
                );

                botaoDia.setForeground(
                        Color.WHITE
                );

                botaoDia.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                11
                        )
                );
            }

            botaoDia.addActionListener(e -> {

                dataSelecionada[0] =
                        data;

                campo.setText(
                        FORMATO_DATA.format(
                                data
                        )
                );

                popup.setVisible(
                        false
                );
            });

            gradeDias.add(
                    botaoDia
            );
        }

        calendario.add(gradeDias);
    }

    /*
     * Tela para escolher o mês.
     */
    private void construirSelecaoMes(
            JPanel calendario,
            JPopupMenu popup,
            JTextField campo,
            LocalDate[] dataSelecionada,
            YearMonth[] mesAtual,
            boolean[] selecionandoMes) {

        JPanel cabecalho =
                new JPanel(
                        new BorderLayout()
                );

        cabecalho.setOpaque(false);

        cabecalho.setMaximumSize(
                new Dimension(
                        260,
                        40
                )
        );

        JButton anterior =
                criarBotaoNavegacao("‹");

        JButton proximo =
                criarBotaoNavegacao("›");

        JButton ano =
                new JButton(
                        String.valueOf(
                                mesAtual[0]
                                        .getYear()
                        )
                );

        ano.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        ano.setForeground(
                COR_TEXTO
        );

        ano.setBackground(
                Color.WHITE
        );

        ano.setFocusPainted(false);

        ano.setBorderPainted(false);

        anterior.addActionListener(e -> {

            mesAtual[0] =
                    mesAtual[0].minusYears(1);

            construirCalendario(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );
        });

        proximo.addActionListener(e -> {

            mesAtual[0] =
                    mesAtual[0].plusYears(1);

            construirCalendario(
                    calendario,
                    popup,
                    campo,
                    dataSelecionada,
                    mesAtual,
                    selecionandoMes
            );
        });

        cabecalho.add(
                anterior,
                BorderLayout.WEST
        );

        cabecalho.add(
                ano,
                BorderLayout.CENTER
        );

        cabecalho.add(
                proximo,
                BorderLayout.EAST
        );

        calendario.add(cabecalho);

        calendario.add(
                Box.createVerticalStrut(10)
        );

        JPanel gradeMeses =
                new JPanel(
                        new GridLayout(
                                4,
                                3,
                                6,
                                6
                        )
                );

        gradeMeses.setOpaque(false);

        gradeMeses.setPreferredSize(
                new Dimension(
                        260,
                        180
                )
        );

        for (Month mes :
                Month.values()) {

            JButton botaoMes =
                    new JButton(
                            formatarNomeMes(
                                    mes
                            )
                    );

            botaoMes.setFocusPainted(
                    false
            );

            botaoMes.setBackground(
                    Color.WHITE
            );

            botaoMes.setForeground(
                    COR_TEXTO
            );

            botaoMes.setBorder(
                    BorderFactory.createLineBorder(
                            COR_BORDA
                    )
            );

            botaoMes.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            11
                    )
            );

            /*
             * Destaca o mês atualmente selecionado.
             */
            if (mesAtual[0].getMonth()
                    == mes) {

                botaoMes.setBackground(
                        COR_AZUL
                );

                botaoMes.setForeground(
                        Color.WHITE
                );

                botaoMes.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                11
                        )
                );
            }

            botaoMes.addActionListener(e -> {

                mesAtual[0] =
                        YearMonth.of(
                                mesAtual[0]
                                        .getYear(),
                                mes
                        );

                selecionandoMes[0] =
                        false;

                construirCalendario(
                        calendario,
                        popup,
                        campo,
                        dataSelecionada,
                        mesAtual,
                        selecionandoMes
                );
            });

            gradeMeses.add(
                    botaoMes
            );
        }

        calendario.add(
                gradeMeses
        );
    }

    private JButton criarBotaoNavegacao(
            String texto) {

        JButton botao =
                new JButton(texto);

        botao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        botao.setForeground(
                COR_TEXTO
        );

        botao.setBackground(
                Color.WHITE
        );

        botao.setFocusPainted(
                false
        );

        botao.setBorderPainted(
                false
        );

        botao.setMargin(
                new Insets(
                        0,
                        8,
                        0,
                        8
                )
        );

        botao.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        return botao;
    }

    private String formatarMesAno(
            YearMonth mes) {

        String texto =
                mes.format(
                        DateTimeFormatter.ofPattern(
                                "MMMM yyyy",
                                new Locale(
                                        "pt",
                                        "BR"
                                )
                        )
                );

        return texto.substring(0, 1)
                .toUpperCase()
                + texto.substring(1);
    }

    private String formatarNomeMes(
            Month mes) {

        String texto =
                mes.getDisplayName(
                        java.time.format.TextStyle.SHORT,
                        new Locale(
                                "pt",
                                "BR"
                        )
                );

        return texto.substring(0, 1)
                .toUpperCase()
                + texto.substring(1);
    }

    /*
     * FILTRO ORIGINAL
     */

    private void filtrarPorPeriodo() {

        try {

            LocalDate inicio =
                    LocalDate.parse(
                            txtDataInicial
                                    .getText()
                                    .trim(),
                            FORMATO_DATA
                    );

            LocalDate fim =
                    LocalDate.parse(
                            txtDataFinal
                                    .getText()
                                    .trim(),
                            FORMATO_DATA
                    );

            if (fim.isBefore(inicio)) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "A data final não pode ser anterior à data inicial.",
                        "Período inválido",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            aplicarFiltro(
                    inicio,
                    fim
            );

            atualizarTela();

        } catch (
                DateTimeParseException ex) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Digite as datas no formato dd/MM/yyyy.",
                    "Data inválida",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void aplicarFiltro() {

        if (movimentacoes.isEmpty()) {

            movimentacoesFiltradas =
                    new ArrayList<>();

            return;
        }

        LocalDate inicio =
                movimentacoes
                        .get(0)
                        .getData()
                        .withDayOfMonth(1);

        LocalDate fim =
                movimentacoes
                        .get(
                                movimentacoes.size() - 1
                        )
                        .getData();

        fim = fim.withDayOfMonth(
                fim.lengthOfMonth()
        );

        aplicarFiltro(
                inicio,
                fim
        );
    }

    private void aplicarFiltro(
            LocalDate inicio,
            LocalDate fim) {

        movimentacoesFiltradas =
                new ArrayList<>();

        for (Fluxo fluxo :
                movimentacoes) {

            if (!fluxo.getData()
                    .isBefore(inicio)
                    && !fluxo.getData()
                    .isAfter(fim)) {

                movimentacoesFiltradas.add(
                        fluxo
                );
            }
        }
    }

    /*
     * CARDS
     */

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

        double entradas =
                calcularEntradas();

        double saidas =
                calcularSaidas();

        double saldoFinal =
                calcularSaldoFinal();

        painel.add(
                criarCard(
                        "Saldo no período",
                        formatarMoeda(
                                saldoFinal
                        ),
                        "Saldo acumulado até a última movimentação",
                        COR_AZUL
                )
        );

        painel.add(
                criarCard(
                        "Total Entradas",
                        formatarMoeda(
                                entradas
                        ),
                        "Recebimentos no período",
                        COR_VERDE
                )
        );

        painel.add(
                criarCard(
                        "Total Saídas",
                        formatarMoeda(
                                saidas
                        ),
                        "Pagamentos no período",
                        COR_VERMELHO
                )
        );

        return painel;
    }

    private JPanel criarCard(
            String titulo,
            String valor,
            String descricao,
            Color cor) {

        JPanel card =
                new JPanel(
                        new GridLayout(
                                3,
                                1
                        )
                );

        card.setBackground(
                Color.WHITE
        );

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

        JLabel t =
                new JLabel(titulo);

        t.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        t.setForeground(
                Color.GRAY
        );

        JLabel v =
                new JLabel(valor);

        v.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        v.setForeground(
                COR_TEXTO
        );

        JLabel d =
                new JLabel(descricao);

        d.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        d.setForeground(cor);

        card.add(t);
        card.add(v);
        card.add(d);

        return card;
    }

    private double calcularEntradas() {

        double total = 0;

        for (Fluxo fluxo :
                movimentacoesFiltradas) {

            if ("Entrada".equals(
                    fluxo.getTipo())) {

                total += fluxo.getValor();
            }
        }

        return total;
    }

    private double calcularSaidas() {

        double total = 0;

        for (Fluxo fluxo :
                movimentacoesFiltradas) {

            if ("Saída".equals(
                    fluxo.getTipo())) {

                total += Math.abs(
                        fluxo.getValor()
                );
            }
        }

        return total;
    }

    private double calcularSaldoInicial() {

        if (movimentacoesFiltradas.isEmpty()) {

            if (movimentacoes.isEmpty()) {
                return 0;
            }

            LocalDate inicio =
                    obterInicioFiltro();

            double saldo = 0;

            for (Fluxo fluxo :
                    movimentacoes) {

                if (fluxo.getData()
                        .isBefore(inicio)) {

                    saldo += fluxo.getValor();
                }
            }

            return saldo;
        }

        LocalDate inicio =
                movimentacoesFiltradas
                        .get(0)
                        .getData();

        double saldo = 0;

        for (Fluxo fluxo :
                movimentacoes) {

            if (fluxo.getData()
                    .isBefore(inicio)) {

                saldo += fluxo.getValor();
            }
        }

        return saldo;
    }

    private double calcularSaldoFinal() {

        if (movimentacoesFiltradas.isEmpty()) {

            return calcularSaldoInicial();
        }

        return calcularSaldoInicial()
                + movimentacoesFiltradas
                        .stream()
                        .mapToDouble(
                                Fluxo::getValor
                        )
                        .sum();
    }

    private LocalDate obterInicioFiltro() {

        try {

            return LocalDate.parse(
                    txtDataInicial
                            .getText()
                            .trim(),
                    FORMATO_DATA
            );

        } catch (Exception e) {

            return movimentacoes.isEmpty()
                    ? LocalDate.now()
                    : movimentacoes
                            .get(0)
                            .getData();
        }
    }

    /*
     * GRÁFICO
     */

    private JPanel criarGrafico() {

        JPanel painel =
                new JPanel(
                        new BorderLayout()
                );

        painel.setBackground(
                Color.WHITE
        );

        painel.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA,
                        1
                )
        );

        painel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painel.setPreferredSize(
                new Dimension(
                        0,
                        310
                )
        );

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        310
                )
        );

        DefaultCategoryDataset dataset =
                criarDatasetGrafico();

        JFreeChart chart =
                criarChart(dataset);

        ChartPanel chartPanel =
                new ChartPanel(chart);

        chartPanel.setBackground(
                Color.WHITE
        );

        chartPanel.setBorder(
                new EmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        chartPanel.setMouseWheelEnabled(
                false
        );

        painel.add(
                chartPanel,
                BorderLayout.CENTER
        );

        return painel;
    }

    private DefaultCategoryDataset criarDatasetGrafico() {

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        if (movimentacoesFiltradas.isEmpty()) {

            return dataset;
        }

        Map<LocalDate, Double> entradas =
                new LinkedHashMap<>();

        Map<LocalDate, Double> saidas =
                new LinkedHashMap<>();

        for (Fluxo fluxo :
                movimentacoesFiltradas) {

            entradas.putIfAbsent(
                    fluxo.getData(),
                    0.0
            );

            saidas.putIfAbsent(
                    fluxo.getData(),
                    0.0
            );

            if ("Entrada".equals(
                    fluxo.getTipo())) {

                entradas.put(
                        fluxo.getData(),
                        entradas.get(
                                fluxo.getData()
                        ) + fluxo.getValor()
                );

            } else {

                saidas.put(
                        fluxo.getData(),
                        saidas.get(
                                fluxo.getData()
                        ) + Math.abs(
                                fluxo.getValor()
                        )
                );
            }
        }

        double saldo =
                calcularSaldoInicial();

        for (LocalDate data :
                entradas.keySet()) {

            saldo +=
                    entradas.get(data);

            saldo -=
                    saidas.get(data);

            String categoria =
                    FORMATO_DATA.format(data);

            dataset.addValue(
                    entradas.get(data),
                    "Entradas",
                    categoria
            );

            dataset.addValue(
                    saidas.get(data),
                    "Saídas",
                    categoria
            );

            dataset.addValue(
                    saldo,
                    "Saldo",
                    categoria
            );
        }

        return dataset;
    }

    private JFreeChart criarChart(
            CategoryDataset dataset) {

        JFreeChart chart =
                ChartFactory.createLineChart(
                        "Fluxo de Caixa",
                        "Data",
                        "Valor (R$)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );

        chart.setBackgroundPaint(
                Color.WHITE
        );

        chart.setTitle(
                new TextTitle(
                        "Entradas x Saídas x Saldo",
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                15
                        )
                )
        );

        if (chart.getLegend() != null) {

            chart.getLegend()
                    .setFrame(
                            BlockBorder.NONE
                    );
        }

        CategoryPlot plot =
                chart.getCategoryPlot();

        plot.setBackgroundPaint(
                Color.WHITE
        );

        plot.setOutlineVisible(false);

        plot.setRangeGridlinesVisible(
                true
        );

        plot.setRangeGridlinePaint(
                Color.BLACK
        );

        plot.setDomainGridlinesVisible(
                true
        );

        plot.setDomainGridlinePaint(
                Color.BLACK
        );

        LineAndShapeRenderer renderer =
                new LineAndShapeRenderer(
                        true,
                        true
                );

        renderer.setSeriesPaint(
                0,
                COR_VERDE
        );

        renderer.setSeriesPaint(
                1,
                COR_VERMELHO
        );

        renderer.setSeriesPaint(
                2,
                COR_AZUL
        );

        renderer.setSeriesStroke(
                0,
                new BasicStroke(2.0f)
        );

        renderer.setSeriesStroke(
                1,
                new BasicStroke(2.0f)
        );

        renderer.setSeriesStroke(
                2,
                new BasicStroke(2.0f)
        );

        plot.setRenderer(renderer);

        return chart;
    }

    /*
     * TABELA
     */

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

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE
                )
        );

        painel.setPreferredSize(
                new Dimension(
                        0,
                        280
                )
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

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        for (Fluxo fluxo :
                movimentacoesFiltradas) {

            modelo.addRow(
                    new Object[]{

                            FORMATO_DATA.format(
                                    fluxo.getData()
                            ),

                            fluxo.getDescricao(),

                            fluxo.getTipo(),

                            fluxo.getCategoria(),

                            formatarValorMovimentacao(
                                    fluxo.getValor()
                            ),

                            formatarMoeda(
                                    fluxo.getSaldo()
                            )
                    }
            );
        }

        JTable tabela =
                new JTable(modelo);

        tabela.setRowHeight(
                ALTURA_LINHA
        );

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

        tabela.setIntercellSpacing(
                new Dimension(0, 0)
        );

        tabela.setSelectionBackground(
                Color.WHITE
        );

        tabela.setSelectionForeground(
                COR_TEXTO
        );

        tabela.getTableHeader()
                .setReorderingAllowed(false);

        tabela.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                ALTURA_CABECALHO
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
                .setForeground(
                        Color.GRAY
                );

        tabela.getTableHeader()
                .setBackground(
                        new Color(
                                248,
                                249,
                                250
                        )
                );

        DefaultTableCellRenderer centro =
                new DefaultTableCellRenderer();

        centro.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (int i = 0;
             i < tabela
                     .getColumnModel()
                     .getColumnCount();
             i++) {

            tabela.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            centro
                    );

            tabela.getColumnModel()
                    .getColumn(i)
                    .setResizable(false);
        }

        tabela.getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new DefaultTableCellRenderer() {

                            private static final long serialVersionUID = 1L;

                            @Override
                            public Component getTableCellRendererComponent(
                                    JTable table,
                                    Object value,
                                    boolean isSelected,
                                    boolean hasFocus,
                                    int row,
                                    int column) {

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
                                        SwingConstants.CENTER
                                );

                                String texto =
                                        value == null
                                                ? ""
                                                : value.toString();

                                label.setForeground(
                                        texto.startsWith("+")
                                                ? COR_VERDE
                                                : COR_VERMELHO
                                );

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

    private String formatarMoeda(
            double valor) {

        return FORMATO_MOEDA.format(
                valor
        );
    }

    private String formatarValorMovimentacao(
            double valor) {

        if (valor >= 0) {

            return "+ "
                    + formatarMoeda(
                            valor
                    );
        }

        return "- "
                + formatarMoeda(
                        Math.abs(valor)
                );
    }
}