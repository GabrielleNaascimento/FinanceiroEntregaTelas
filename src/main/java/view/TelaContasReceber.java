package view;

import model.Contas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class TelaContasReceber extends JPanel {

    private static final long serialVersionUID = 1L;

    // mesmas cores da TelaConciliacaoBancaria
    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO = new Color(30, 38, 52);
    private static final Color COR_CINZA = new Color(105, 114, 128);
    private static final Color COR_AZUL = new Color(37, 99, 235);
    private static final Color COR_VERDE = new Color(16, 185, 129);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private static final int ALTURA_LINHA = 30;
    private static final int ALTURA_CABECALHO = 30;
    private static final int ESPACO = 15;

    public TelaContasReceber() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
        definirPeriodoInicial(Contas.RECEBER);
        atualizarTela();

        // refaz os cards toda vez que a tela volta a aparecer (o saldo muda quando se paga uma conta em outra tela)
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                atualizarTela();
            }
        });
    }

    // refaz a tela toda, chamo depois de pagar uma conta pra atualizar as listas e os cards
    private void atualizarTela() {
        removeAll();
        add(criarAreaPrincipal(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel criarAreaPrincipal() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(COR_FUNDO);
        area.add(criarHeader(), BorderLayout.NORTH);

        // scroll pq as duas tabelas juntas passam da altura da janela
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
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        // por enquanto o usuario ta fixo aqui
        JLabel lblUser = new JLabel("Jefferson - Administrador");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(COR_FUNDO);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Contas a Receber</h2>"
                + "<span style='color:gray;'>Acompanhe seus recebimentos previstos e já realizados</span></html>");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        conteudo.add(criarFiltroDatas());
        conteudo.add(Box.createVerticalStrut(ESPACO));

        JPanel cards = criarCards();
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        adicionarTabela(conteudo, "A Receber", false);
        adicionarTabela(conteudo, "Recebidas", true);

        return conteudo;
    }

    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // os dois primeiros cards seguem o período filtrado; o saldo atual é sempre o real
        double totalReceber = somarValores(filtrarPeriodo(Contas.listar(Contas.RECEBER, false)));
        double totalRecebido = somarValores(filtrarPeriodo(Contas.listar(Contas.RECEBER, true)));

        painel.add(cardMetrica("Total a receber", formatarValor(totalReceber),
                "Pendentes e atrasadas no período", COR_AZUL));
        painel.add(cardMetrica("Total recebido", formatarValor(totalRecebido),
                "Contas já recebidas no período", COR_VERDE));
        painel.add(cardMetrica("Saldo atual", formatarValor(Contas.getSaldoAtual()),
                "Recebido menos pago", COR_AZUL));

        return painel;
    }

    // card igual ao da conciliacao: titulo cinza, valor grande e a linha colorida embaixo
    private JPanel cardMetrica(String titulo, String valor, String descricao, Color cor) {
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 17));
        v.setForeground(COR_TEXTO);

        JLabel d = new JLabel(descricao);
        d.setFont(new Font("SansSerif", Font.BOLD, 11));
        d.setForeground(cor);

        card.add(t);
        card.add(v);
        card.add(d);
        return card;
    }

    private void adicionarTabela(JPanel conteudo, String titulo, boolean concluidas) {
        ArrayList<Contas> dados = filtrarPeriodo(Contas.listar(Contas.RECEBER, concluidas));

        String[] colunas = {"ID", "Cliente", "Descrição", "Vencimento", "Valor", "Status"};

        // aqui nenhuma celula é editavel, nao tem botao
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Contas c : dados) {
            Object[] linha = new Object[colunas.length];
            linha[0] = c.getId();
            linha[1] = c.getEntidade();
            linha[2] = c.getDescricao();
            linha[3] = formatarData(c.getDataVencimento());
            linha[4] = formatarValor(c.getValor());
            linha[5] = c.getStatus();
            modelo.addRow(linha);
        }

        JPanel painel = criarPainelTabela(titulo, dados.size());

        if (dados.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma conta neste período", SwingConstants.CENTER);
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(Color.GRAY);
            vazio.setBorder(new EmptyBorder(25, 0, 25, 0));
            painel.add(vazio, BorderLayout.CENTER);
        } else {
            JTable tabela = new JTable(modelo);
            configurarTabela(tabela);

            // header e tabela direto no painel, sem JScrollPane, pra tabela ocupar a altura toda
            JPanel areaTabela = new JPanel(new BorderLayout());
            areaTabela.setBackground(Color.WHITE);
            areaTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
            areaTabela.add(tabela, BorderLayout.CENTER);
            painel.add(areaTabela, BorderLayout.CENTER);
        }

        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, painel.getPreferredSize().height));
        conteudo.add(painel);
        conteudo.add(Box.createVerticalStrut(ESPACO));
    }

    private JPanel criarPainelTabela(String titulo, int quantidade) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(12, 12, 12, 12)));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelTitulo.setForeground(COR_TEXTO);

        // "1 conta" / "2 contas"
        JLabel labelQtd = new JLabel(quantidade + (quantidade == 1 ? " conta" : " contas"));
        labelQtd.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelQtd.setForeground(Color.GRAY);

        topo.add(labelTitulo, BorderLayout.WEST);
        topo.add(labelQtd, BorderLayout.EAST);
        painel.add(topo, BorderLayout.NORTH);

        return painel;
    }

    private void configurarTabela(JTable tabela) {
        tabela.setRowHeight(ALTURA_LINHA);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tabela.setForeground(COR_TEXTO);
        tabela.setBackground(Color.WHITE);
        // sem linhas de grade, igual a Conciliacao (o espacamento padrao entre as celulas continua)
        tabela.setShowGrid(false);
        tabela.setRowSelectionAllowed(false);
        tabela.setColumnSelectionAllowed(false);
        tabela.setFocusable(false);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setResizingAllowed(false);
        cabecalho.setFont(new Font("SansSerif", Font.BOLD, 10));
        cabecalho.setForeground(Color.GRAY);
        cabecalho.setBackground(new Color(248, 249, 250));
        cabecalho.setPreferredSize(new Dimension(0, ALTURA_CABECALHO));

        // fornecedor e descricao a esquerda, o resto centralizado
        tabela.setDefaultRenderer(Object.class, new CelulaRenderer(SwingConstants.CENTER, COR_TEXTO));
        tabela.getColumnModel().getColumn(1).setCellRenderer(new CelulaRenderer(SwingConstants.LEFT, COR_TEXTO));
        tabela.getColumnModel().getColumn(2).setCellRenderer(new CelulaRenderer(SwingConstants.LEFT, COR_TEXTO));
        tabela.getColumnModel().getColumn(4).setCellRenderer(new CelulaRenderer(SwingConstants.CENTER, COR_VERDE));
        tabela.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(120);

        for (int i = 0; i < tabela.getColumnModel().getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setResizable(false);
        }
    }

    private static class CelulaRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private final int alinhamento;
        private final Color cor;

        CelulaRenderer(int alinhamento, Color cor) {
            this.alinhamento = alinhamento;
            this.cor = cor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, false, false, row, column);
            setHorizontalAlignment(alinhamento);
            setForeground(cor);
            setBackground(Color.WHITE);
            if (alinhamento == SwingConstants.LEFT) {
                setBorder(new EmptyBorder(0, 10, 0, 10));
            }
            return this;
        }
    }

    // status preenche a celula inteira com a cor
    private static class StatusRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, false, false, row, column);
            String status = value == null ? "" : value.toString();

            if (status.equals("Paga") || status.equals("Recebida")) {
                setForeground(new Color(6, 95, 70));
                setBackground(new Color(209, 250, 229));
            } else if (status.equals("Pendente")) {
                setForeground(new Color(146, 64, 14));
                setBackground(new Color(254, 243, 199));
            } else {
                // vencida
                setForeground(new Color(153, 27, 27));
                setBackground(new Color(254, 226, 226));
            }

            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
            return this;
        }
    }

    private String formatarValor(double valor) {
        return FORMATO_MOEDA.format(valor);
    }

    private String formatarData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }

    // ===================== FILTRO POR PERÍODO =====================
    // mesmo filtro da TelaFluxo: campos De/Até com calendário popup e botão Filtrar

    private JTextField txtDataInicial;
    private JTextField txtDataFinal;

    // período realmente aplicado nas listas (só muda quando clica em Filtrar)
    private LocalDate filtroInicio;
    private LocalDate filtroFim;

    private JPanel criarFiltroDatas() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // a tela é refeita várias vezes, entao sempre mostra o período que está aplicado
        txtDataInicial.setText(FORMATO_DATA.format(filtroInicio));
        txtDataFinal.setText(FORMATO_DATA.format(filtroFim));

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

    // campo só de leitura: o usuário escolhe a data pelo calendário
    private void configurarCampoData(JTextField campo) {
        campo.setEditable(false);
        campo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        campo.setBackground(Color.WHITE);
        campo.setForeground(COR_TEXTO);
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                new EmptyBorder(6, 8, 6, 8)));

        // evita adicionar vários MouseListeners quando a tela é refeita
        if (Boolean.TRUE.equals(campo.getClientProperty("calendarioConfigurado"))) {
            return;
        }
        campo.putClientProperty("calendarioConfigurado", Boolean.TRUE);

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

    // decide se o calendário mostra os dias ou os meses
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

        // clicar em "Outubro 2026", por exemplo, abre a seleção dos 12 meses
        mesAno.addActionListener(e -> {
            selecionandoMes[0] = true;
            construirCalendario(calendario, popup, campo, dataSelecionada, mesAtual, selecionandoMes);
        });

        cabecalho.add(anterior, BorderLayout.WEST);
        cabecalho.add(mesAno, BorderLayout.CENTER);
        cabecalho.add(proximo, BorderLayout.EAST);

        calendario.add(cabecalho);
        calendario.add(Box.createVerticalStrut(8));

        // dias da semana
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

        // grade dos dias
        JPanel gradeDias = new JPanel(new GridLayout(6, 7, 2, 2));
        gradeDias.setOpaque(false);
        gradeDias.setPreferredSize(new Dimension(260, 210));

        LocalDate primeiroDia = mesAtual[0].atDay(1);

        // Java: segunda = 1 ... domingo = 7. Como o domingo vem primeiro, usa % 7
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

            // destaca a data atualmente selecionada
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

            // destaca o mês atualmente selecionado
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

    // clique no botão Filtrar: valida as datas, guarda o período e refaz a tela
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

            filtroInicio = inicio;
            filtroFim = fim;
            atualizarTela();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Digite as datas no formato dd/MM/yyyy.",
                    "Data inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    // deixa só as contas cujo vencimento está dentro do período aplicado
    private ArrayList<Contas> filtrarPeriodo(ArrayList<Contas> lista) {
        ArrayList<Contas> resultado = new ArrayList<>();

        for (Contas c : lista) {
            LocalDate data = c.getDataVencimento();
            if (!data.isBefore(filtroInicio) && !data.isAfter(filtroFim)) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    private double somarValores(ArrayList<Contas> lista) {
        double total = 0;
        for (Contas c : lista) {
            total += c.getValor();
        }
        return total;
    }

    // período inicial: do 1º dia do mês da menor data até o último dia do mês da maior data
    private void definirPeriodoInicial(String tipo) {
        ArrayList<Contas> todas = new ArrayList<>();
        todas.addAll(Contas.listar(tipo, false));
        todas.addAll(Contas.listar(tipo, true));

        LocalDate menor = LocalDate.now();
        LocalDate maior = LocalDate.now();

        if (!todas.isEmpty()) {
            menor = todas.get(0).getDataVencimento();
            maior = menor;

            for (Contas c : todas) {
                if (c.getDataVencimento().isBefore(menor)) {
                    menor = c.getDataVencimento();
                }
                if (c.getDataVencimento().isAfter(maior)) {
                    maior = c.getDataVencimento();
                }
            }
        }

        filtroInicio = menor.withDayOfMonth(1);
        filtroFim = maior.withDayOfMonth(maior.lengthOfMonth());

        txtDataInicial = new JTextField(8);
        txtDataFinal = new JTextField(8);
    }
}