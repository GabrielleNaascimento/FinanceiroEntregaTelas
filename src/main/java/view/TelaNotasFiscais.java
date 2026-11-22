package view;

import model.NotaFiscal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

public class TelaNotasFiscais extends JPanel {

    private static final long serialVersionUID = 1L;

    // mesmas cores das telas de contas
    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO = new Color(30, 38, 52);
    private static final Color COR_CINZA = new Color(105, 114, 128);
    private static final Color COR_AZUL = new Color(37, 99, 235);
    private static final Color COR_VERDE = new Color(16, 185, 129);
    private static final Color COR_VERMELHO = new Color(239, 68, 68);
    private static final Color COR_SELECAO = new Color(239, 246, 255);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private static final int ALTURA_LINHA = 30;
    private static final int ALTURA_CABECALHO = 30;
    private static final int ESPACO = 15;

    // tabela atual e as notas que ela mostra (na mesma ordem das linhas), pra saber qual nota foi selecionada
    private JTable tabela;
    private ArrayList<NotaFiscal> notasExibidas = new ArrayList<>();

    public TelaNotasFiscais() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
        atualizarTela();

        // as notas sao geradas nas telas de contas, entao refaz tudo toda vez que esta tela aparece
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                atualizarTela();
            }
        });
    }

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
        header.setBorder(new EmptyBorder(15, 25, 5, 25));

        // por enquanto o usuario ta fixo aqui
        JLabel lblUser = new JLabel("Jefferson - Administrador");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // botoes embaixo das informações do administrador, igual as telas de contas
        JButton btnVisualizar = new JButton("Visualizar nota");
        btnVisualizar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVisualizar.setBackground(Color.WHITE);
        btnVisualizar.setForeground(COR_TEXTO);
        btnVisualizar.setFocusPainted(false);
        btnVisualizar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                new EmptyBorder(8, 16, 8, 16)));
        btnVisualizar.addActionListener(e -> visualizarSelecionada());

        TelaContasPagar.BotaoPrimario btnNova = new TelaContasPagar.BotaoPrimario("Nova nota fiscal");
        btnNova.addActionListener(e -> abrirNovaNota());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        botoes.setAlignmentX(Component.RIGHT_ALIGNMENT);
        botoes.add(btnVisualizar);
        botoes.add(btnNova);

        JPanel direita = new JPanel();
        direita.setLayout(new BoxLayout(direita, BoxLayout.Y_AXIS));
        direita.setOpaque(false);
        direita.add(lblUser);
        direita.add(Box.createVerticalStrut(8));
        direita.add(botoes);

        header.add(direita, BorderLayout.EAST);
        return header;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(COR_FUNDO);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Notas Fiscais</h2>"
                + "<span style='color:gray;'>Notas geradas nos pagamentos e recebimentos, e notas emitidas manualmente</span></html>");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        notasExibidas = listarNotas();

        JPanel cards = criarCards(notasExibidas);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        conteudo.add(criarPainelNotas());
        return conteudo;
    }

    // mais nova primeiro
    private ArrayList<NotaFiscal> listarNotas() {
        ArrayList<NotaFiscal> lista = new ArrayList<>(NotaFiscal.getNotas());
        lista.sort((a, b) -> Integer.compare(b.getNumero(), a.getNumero()));
        return lista;
    }

    private JPanel criarCards(ArrayList<NotaFiscal> notas) {
        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // nota estornada nao entra nos totais
        int quantidade = 0;
        double totalEntrada = 0;
        double totalSaida = 0;
        for (NotaFiscal n : notas) {
            if (!n.getStatus().equals(NotaFiscal.EMITIDA)) {
                continue;
            }
            quantidade++;
            if (n.isEntrada()) {
                totalEntrada += n.getValorTotal();
            } else {
                totalSaida += n.getValorTotal();
            }
        }

        painel.add(cardMetrica("Notas emitidas", String.valueOf(quantidade),
                "Notas válidas no sistema", COR_AZUL));
        painel.add(cardMetrica("Notas de entrada", FORMATO_MOEDA.format(totalEntrada),
                "Pagamentos: compras e serviços tomados", COR_VERMELHO));
        painel.add(cardMetrica("Notas de saída", FORMATO_MOEDA.format(totalSaida),
                "Recebimentos: vendas e serviços prestados", COR_VERDE));
        return painel;
    }

    private JPanel cardMetrica(String titulo, String valor, String descricao, Color cor) {
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(10, 12, 10, 12)));

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

    private JPanel criarPainelNotas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(12, 12, 12, 12)));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel labelTitulo = new JLabel("Notas emitidas");
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelTitulo.setForeground(COR_TEXTO);

        int qtd = notasExibidas.size();
        String texto = qtd + (qtd == 1 ? " nota" : " notas");
        if (qtd > 0) {
            texto += "  -  selecione uma e clique em Visualizar nota (ou dê dois cliques)";
        }
        JLabel labelQtd = new JLabel(texto);
        labelQtd.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelQtd.setForeground(Color.GRAY);

        topo.add(labelTitulo, BorderLayout.WEST);
        topo.add(labelQtd, BorderLayout.EAST);
        painel.add(topo, BorderLayout.NORTH);

        tabela = null;

        if (notasExibidas.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma nota fiscal emitida ainda", SwingConstants.CENTER);
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(Color.GRAY);
            vazio.setBorder(new EmptyBorder(25, 0, 25, 0));
            painel.add(vazio, BorderLayout.CENTER);
        } else {
            tabela = criarTabela();

            // header e tabela direto no painel, sem JScrollPane, igual as telas de contas
            JPanel areaTabela = new JPanel(new BorderLayout());
            areaTabela.setBackground(Color.WHITE);
            areaTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
            areaTabela.add(tabela, BorderLayout.CENTER);
            painel.add(areaTabela, BorderLayout.CENTER);
        }

        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, painel.getPreferredSize().height));
        return painel;
    }

    private JTable criarTabela() {
        String[] colunas = {"Nº", "Emissão", "Tipo", "Fornecedor / Cliente", "Descrição", "Valor", "Origem", "Status"};

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (NotaFiscal n : notasExibidas) {
            modelo.addRow(new Object[]{
                    n.getNumeroFormatado(),
                    FORMATO_DATA.format(n.getDataEmissao()),
                    n.getTipo(),
                    n.getEntidade(),
                    n.getDescricao(),
                    FORMATO_MOEDA.format(n.getValorTotal()),
                    n.isManual() ? "Manual" : "Conta " + n.getIdConta(),
                    n.getStatus()
            });
        }

        JTable t = new JTable(modelo);
        t.setRowHeight(ALTURA_LINHA);
        t.setFont(new Font("SansSerif", Font.PLAIN, 11));
        t.setForeground(COR_TEXTO);
        t.setBackground(Color.WHITE);
        t.setShowGrid(false);
        t.setRowSelectionAllowed(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setColumnSelectionAllowed(false);
        t.setFocusable(false);

        JTableHeader cabecalho = t.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setResizingAllowed(false);
        cabecalho.setFont(new Font("SansSerif", Font.BOLD, 10));
        cabecalho.setForeground(Color.GRAY);
        cabecalho.setBackground(new Color(248, 249, 250));
        cabecalho.setPreferredSize(new Dimension(0, ALTURA_CABECALHO));

        // nome e descricao a esquerda, o resto centralizado
        t.setDefaultRenderer(Object.class, new CelulaRenderer(SwingConstants.CENTER, COR_TEXTO));
        t.getColumnModel().getColumn(3).setCellRenderer(new CelulaRenderer(SwingConstants.LEFT, COR_TEXTO));
        t.getColumnModel().getColumn(4).setCellRenderer(new CelulaRenderer(SwingConstants.LEFT, COR_TEXTO));
        t.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());

        t.getColumnModel().getColumn(0).setPreferredWidth(70);
        t.getColumnModel().getColumn(1).setPreferredWidth(95);
        t.getColumnModel().getColumn(2).setPreferredWidth(70);
        t.getColumnModel().getColumn(3).setPreferredWidth(160);
        t.getColumnModel().getColumn(4).setPreferredWidth(200);
        t.getColumnModel().getColumn(5).setPreferredWidth(110);
        t.getColumnModel().getColumn(6).setPreferredWidth(80);
        t.getColumnModel().getColumn(7).setPreferredWidth(90);

        for (int i = 0; i < t.getColumnModel().getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setResizable(false);
        }

        // dois cliques abrem a nota
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linha = t.rowAtPoint(e.getPoint());
                    if (linha >= 0 && linha < notasExibidas.size()) {
                        new DialogNota(SwingUtilities.getWindowAncestor(TelaNotasFiscais.this),
                                notasExibidas.get(linha)).setVisible(true);
                    }
                }
            }
        });

        return t;
    }

    private void visualizarSelecionada() {
        if (tabela == null || tabela.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota na tabela para visualizar.",
                    "Nenhuma nota selecionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        NotaFiscal nota = notasExibidas.get(tabela.getSelectedRow());
        new DialogNota(SwingUtilities.getWindowAncestor(this), nota).setVisible(true);
    }

    private void abrirNovaNota() {
        DialogNovaNota dialog = new DialogNovaNota(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        if (dialog.getNotaGerada() != null) {
            atualizarTela();
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
            setBackground(isSelected ? COR_SELECAO : Color.WHITE);
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

            if (status.equals(NotaFiscal.EMITIDA)) {
                setForeground(new Color(6, 95, 70));
                setBackground(new Color(209, 250, 229));
            } else {
                // estornada
                setForeground(new Color(75, 85, 99));
                setBackground(new Color(229, 231, 235));
            }

            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
            return this;
        }
    }

    // ===================== VISUALIZAR NOTA =====================
    // public static pra tela de baixa poder abrir a nota logo depois de gerar

    public static class DialogNota extends JDialog {
        private static final long serialVersionUID = 1L;

        public DialogNota(Window dono, NotaFiscal nota) {
            super(dono, "Nota Fiscal " + nota.getNumeroFormatado(), ModalityType.APPLICATION_MODAL);

            JPanel raiz = new JPanel(new BorderLayout(0, 14));
            raiz.setBackground(Color.WHITE);
            raiz.setBorder(new EmptyBorder(20, 24, 16, 24));

            raiz.add(criarCabecalho(nota), BorderLayout.NORTH);
            raiz.add(criarCorpo(nota), BorderLayout.CENTER);

            JButton btnFechar = new JButton("Fechar");
            btnFechar.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btnFechar.setBackground(Color.WHITE);
            btnFechar.setForeground(COR_TEXTO);
            btnFechar.setFocusPainted(false);
            btnFechar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(7, 22, 7, 22)));
            btnFechar.addActionListener(e -> dispose());

            JLabel aviso = new JLabel("<html><span style='color:gray;font-size:9px;'>Documento gerado pelo sistema "
                    + "apenas para controle interno, sem valor fiscal.</span></html>");
            aviso.setFont(new Font("SansSerif", Font.PLAIN, 10));

            JPanel rodape = new JPanel(new BorderLayout());
            rodape.setOpaque(false);
            rodape.add(aviso, BorderLayout.WEST);
            rodape.add(btnFechar, BorderLayout.EAST);
            raiz.add(rodape, BorderLayout.SOUTH);

            setContentPane(raiz);
            getRootPane().setDefaultButton(btnFechar);
            getRootPane().registerKeyboardAction(e -> dispose(),
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            pack();
            setSize(Math.max(getWidth(), 560), getHeight());
            setLocationRelativeTo(dono);
        }

        private JPanel criarCabecalho(NotaFiscal nota) {
            JPanel cab = new JPanel(new BorderLayout());
            cab.setBackground(COR_FUNDO);
            cab.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(12, 14, 12, 14)));

            JLabel titulo = new JLabel("<html><span style='font-size:15px;font-weight:bold;'>NOTA FISCAL</span><br>"
                    + "<span style='color:gray;'>Nota de " + nota.getTipo().toLowerCase() + "</span></html>");

            JLabel numero = new JLabel("<html><div style='text-align:right;'><b>Nº " + nota.getNumeroFormatado()
                    + "</b>  -  Série " + nota.getSerie() + "<br>"
                    + "<span style='color:gray;'>Emitida em " + FORMATO_DATA.format(nota.getDataEmissao())
                    + "  -  " + nota.getStatus() + "</span></div></html>");
            numero.setHorizontalAlignment(SwingConstants.RIGHT);

            cab.add(titulo, BorderLayout.WEST);
            cab.add(numero, BorderLayout.EAST);
            return cab;
        }

        private JPanel criarCorpo(NotaFiscal nota) {
            JPanel corpo = new JPanel();
            corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
            corpo.setOpaque(false);

            // entrada: quem emite é o fornecedor e quem recebe é a empresa; saída: o contrario
            String empresa = NotaFiscal.EMPRESA_NOME + "<br><span style='color:gray;'>CNPJ "
                    + NotaFiscal.EMPRESA_CNPJ + "</span>";
            String outro = escapar(nota.getEntidade());

            JPanel partes = new JPanel(new GridLayout(1, 2, 15, 0));
            partes.setOpaque(false);
            partes.add(bloco("Emitente", nota.isEntrada() ? outro : empresa));
            partes.add(bloco("Destinatário", nota.isEntrada() ? empresa : outro));
            adicionar(corpo, partes);

            String origem = nota.isManual() ? "Nota emitida manualmente" : "Baixa da conta " + nota.getIdConta();
            adicionar(corpo, bloco("Descrição", escapar(nota.getDescricao())
                    + "<br><span style='color:gray;'>" + origem + "</span>"));

            adicionar(corpo, criarValores(nota));

            if (!nota.isManual()) {
                String pagamento = nota.isEntrada() ? "Pago" : "Recebido";
                String conta = vazioPraTraco(nota.getContaFinanceira());
                String categoria = vazioPraTraco(nota.getCategoria());
                adicionar(corpo, bloco(pagamento + " em", "Conta financeira: " + escapar(conta)
                        + "  -  Categoria: " + escapar(categoria)));
            }

            if (nota.getObservacao() != null && !nota.getObservacao().trim().isEmpty()) {
                adicionar(corpo, bloco("Observações", escapar(nota.getObservacao())));
            }

            return corpo;
        }

        // linhas de valor: so mostra juros, multa, desconto e tarifa se tiverem valor
        private JPanel criarValores(NotaFiscal nota) {
            JPanel painel = new JPanel();
            painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
            painel.setBackground(Color.WHITE);
            painel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(10, 14, 10, 14)));

            painel.add(linhaValor("Valor do lançamento", nota.getValorBase(), false));
            if (nota.getJuros() > 0) {
                painel.add(linhaValor("(+) Juros", nota.getJuros(), false));
            }
            if (nota.getMulta() > 0) {
                painel.add(linhaValor("(+) Multa", nota.getMulta(), false));
            }
            if (nota.getDesconto() > 0) {
                painel.add(linhaValor("(-) Desconto", nota.getDesconto(), false));
            }
            if (nota.getTarifa() > 0) {
                // a tarifa soma em quem paga e desconta de quem recebe
                painel.add(linhaValor(nota.isEntrada() ? "(+) Tarifa" : "(-) Tarifa", nota.getTarifa(), false));
            }
            painel.add(Box.createVerticalStrut(6));
            painel.add(linhaValor("Valor total", nota.getValorTotal(), true));
            return painel;
        }

        private JPanel linhaValor(String rotulo, double valor, boolean destaque) {
            JPanel linha = new JPanel(new BorderLayout());
            linha.setOpaque(false);
            linha.setBorder(new EmptyBorder(2, 0, 2, 0));

            JLabel l = new JLabel(rotulo);
            l.setFont(new Font("SansSerif", destaque ? Font.BOLD : Font.PLAIN, destaque ? 13 : 12));
            l.setForeground(destaque ? COR_TEXTO : COR_CINZA);

            JLabel v = new JLabel(FORMATO_MOEDA.format(valor));
            v.setFont(new Font("SansSerif", destaque ? Font.BOLD : Font.PLAIN, destaque ? 14 : 12));
            v.setForeground(COR_TEXTO);

            linha.add(l, BorderLayout.WEST);
            linha.add(v, BorderLayout.EAST);
            linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, linha.getPreferredSize().height));
            return linha;
        }

        // rotulo pequeno em cima e o texto embaixo (aceita html simples)
        private JPanel bloco(String rotulo, String htmlConteudo) {
            JPanel painel = new JPanel(new BorderLayout(0, 4));
            painel.setOpaque(false);

            JLabel l = new JLabel(rotulo);
            l.setFont(new Font("SansSerif", Font.PLAIN, 11));
            l.setForeground(COR_CINZA);

            JLabel c = new JLabel("<html><b>" + htmlConteudo + "</b></html>");
            c.setFont(new Font("SansSerif", Font.PLAIN, 12));
            c.setForeground(COR_TEXTO);
            // texto sempre colado em cima, mesmo quando o bloco do lado é mais alto
            c.setVerticalAlignment(SwingConstants.TOP);

            painel.add(l, BorderLayout.NORTH);
            painel.add(c, BorderLayout.CENTER);
            return painel;
        }

        private void adicionar(JPanel corpo, JComponent componente) {
            componente.setAlignmentX(Component.LEFT_ALIGNMENT);
            componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, componente.getPreferredSize().height));
            corpo.add(componente);
            corpo.add(Box.createVerticalStrut(12));
        }

        private String vazioPraTraco(String texto) {
            return texto == null || texto.trim().isEmpty() ? "-" : texto;
        }

        private String escapar(String texto) {
            return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }
    }

    // ===================== NOVA NOTA FISCAL (manual) =====================

    public static class DialogNovaNota extends JDialog {
        private static final long serialVersionUID = 1L;

        private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{
                "Saída (venda ou serviço prestado)", "Entrada (compra ou serviço tomado)"});
        private final JTextField txtEntidade = new JTextField();
        private final JTextField txtDescricao = new JTextField();
        private final JTextField txtValor = new JTextField("0,00");
        private final JTextField txtData = new JTextField();
        private final JTextArea txtObservacao = new JTextArea(3, 20);

        private NotaFiscal notaGerada;

        public DialogNovaNota(Window dono) {
            super(dono, "Nova nota fiscal", ModalityType.APPLICATION_MODAL);

            configurarComponentes();

            JPanel raiz = new JPanel(new BorderLayout(0, 14));
            raiz.setBackground(Color.WHITE);
            raiz.setBorder(new EmptyBorder(20, 24, 16, 24));

            JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Nova nota fiscal</h2>"
                    + "<span style='color:gray;'>Emita uma nota que não veio de um pagamento ou recebimento. "
                    + "Ela não altera o saldo.</span></html>");
            raiz.add(titulo, BorderLayout.NORTH);
            raiz.add(criarFormulario(), BorderLayout.CENTER);
            raiz.add(criarRodape(), BorderLayout.SOUTH);

            setContentPane(raiz);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            pack();
            setSize(Math.max(getWidth(), 520), getHeight());
            setLocationRelativeTo(dono);
        }

        public NotaFiscal getNotaGerada() {
            return notaGerada;
        }

        private void configurarComponentes() {
            txtData.setText(FORMATO_DATA.format(LocalDate.now()));
            txtData.setEditable(false);
            txtData.setHorizontalAlignment(SwingConstants.CENTER);
            txtData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            txtData.setToolTipText("Clique para escolher a data");
            txtData.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    TelaContasPagar.mostrarCalendario(txtData);
                }
            });

            txtValor.setHorizontalAlignment(SwingConstants.RIGHT);
            txtValor.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    SwingUtilities.invokeLater(txtValor::selectAll);
                }
            });

            for (JTextField campo : java.util.List.of(txtEntidade, txtDescricao, txtValor, txtData)) {
                campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
                campo.setForeground(COR_TEXTO);
                campo.setBackground(Color.WHITE);
                campo.setPreferredSize(new Dimension(100, 32));
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COR_BORDA),
                        new EmptyBorder(4, 8, 4, 8)));
            }

            cmbTipo.setBackground(Color.WHITE);
            cmbTipo.setForeground(COR_TEXTO);
            cmbTipo.setFont(new Font("SansSerif", Font.PLAIN, 12));
            cmbTipo.setPreferredSize(new Dimension(100, 32));

            txtObservacao.setLineWrap(true);
            txtObservacao.setWrapStyleWord(true);
            txtObservacao.setFont(new Font("SansSerif", Font.PLAIN, 12));
            txtObservacao.setForeground(COR_TEXTO);
            txtObservacao.setBorder(new EmptyBorder(6, 8, 6, 8));
        }

        private JPanel criarFormulario() {
            JPanel form = new JPanel();
            form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
            form.setOpaque(false);

            adicionar(form, grupo("Tipo da nota", cmbTipo));
            adicionar(form, grupo("Cliente / Fornecedor", txtEntidade));
            adicionar(form, grupo("Descrição", txtDescricao));

            JPanel linha = new JPanel(new GridLayout(1, 2, 15, 0));
            linha.setOpaque(false);
            linha.add(grupo("Valor total", txtValor));
            linha.add(grupo("Data de emissão", txtData));
            adicionar(form, linha);

            JScrollPane scroll = new JScrollPane(txtObservacao);
            scroll.setBorder(BorderFactory.createLineBorder(COR_BORDA));
            scroll.setPreferredSize(new Dimension(100, 66));
            adicionar(form, grupo("Observações (opcional)", scroll));

            return form;
        }

        private JPanel criarRodape() {
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btnCancelar.setBackground(Color.WHITE);
            btnCancelar.setForeground(COR_TEXTO);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(7, 18, 7, 18)));
            btnCancelar.addActionListener(e -> dispose());

            TelaContasPagar.BotaoPrimario btnGerar = new TelaContasPagar.BotaoPrimario("Gerar nota fiscal");
            btnGerar.addActionListener(e -> gerar());

            JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            botoes.setOpaque(false);
            botoes.add(btnCancelar);
            botoes.add(btnGerar);

            getRootPane().setDefaultButton(btnGerar);
            getRootPane().registerKeyboardAction(e -> dispose(),
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
            return botoes;
        }

        private void gerar() {
            String entidade = txtEntidade.getText().trim();
            String descricao = txtDescricao.getText().trim();

            if (entidade.isEmpty()) {
                aviso("Informe o cliente ou fornecedor.");
                return;
            }
            if (descricao.isEmpty()) {
                aviso("Informe a descrição da nota.");
                return;
            }

            double valor;
            try {
                valor = TelaContasPagar.DialogBaixa.lerValor(txtValor.getText());
            } catch (NumberFormatException ex) {
                aviso("Digite um valor numérico válido. Exemplo: 1.250,50");
                return;
            }
            if (valor <= 0) {
                aviso("O valor da nota deve ser maior que zero.");
                return;
            }

            LocalDate data;
            try {
                data = LocalDate.parse(txtData.getText().trim(), FORMATO_DATA);
            } catch (DateTimeParseException ex) {
                aviso("Escolha uma data válida.");
                return;
            }

            String tipo = cmbTipo.getSelectedIndex() == 0 ? NotaFiscal.SAIDA : NotaFiscal.ENTRADA;
            notaGerada = NotaFiscal.emitirManual(tipo, entidade, descricao,
                    Math.round(valor * 100.0) / 100.0, data, txtObservacao.getText().trim());
            dispose();
        }

        private void aviso(String mensagem) {
            JOptionPane.showMessageDialog(this, mensagem, "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        }

        private JPanel grupo(String rotulo, JComponent campo) {
            JPanel painel = new JPanel(new BorderLayout(0, 4));
            painel.setOpaque(false);

            JLabel label = new JLabel(rotulo);
            label.setFont(new Font("SansSerif", Font.PLAIN, 11));
            label.setForeground(COR_CINZA);

            painel.add(label, BorderLayout.NORTH);
            painel.add(campo, BorderLayout.CENTER);
            return painel;
        }

        private void adicionar(JPanel form, JComponent componente) {
            componente.setAlignmentX(Component.LEFT_ALIGNMENT);
            componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, componente.getPreferredSize().height));
            form.add(componente);
            form.add(Box.createVerticalStrut(12));
        }
    }
}