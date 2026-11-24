package view;

import model.Contas;
import model.NotaFiscal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class TelaContasPagar extends JPanel {

    private static final long serialVersionUID = 1L;

    // mesmas cores da TelaConciliacaoBancaria
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

    // qual das tres tabelas esta sendo montada
    private static final int ABERTAS = 0;
    private static final int CONCLUIDAS = 1;
    private static final int ENCERRADAS = 2;

    // tabela das contas em aberto: é nela que o usuario seleciona a conta pro botao Ações
    private JTable tabelaAbertas;

    public TelaContasPagar() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
        definirPeriodoInicial(Contas.PAGAR);
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
        header.setBorder(new EmptyBorder(15, 25, 5, 25));

        // por enquanto o usuario ta fixo aqui
        JLabel lblUser = new JLabel("Jefferson - Administrador");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // o botao de ações fica embaixo das informações do administrador
        BotaoPrimario btnAcoes = new BotaoPrimario("Ações", true);
        btnAcoes.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnAcoes.addActionListener(e -> mostrarMenuAcoes(btnAcoes));

        JPanel direita = new JPanel();
        direita.setLayout(new BoxLayout(direita, BoxLayout.Y_AXIS));
        direita.setOpaque(false);
        direita.add(lblUser);
        direita.add(Box.createVerticalStrut(8));
        direita.add(btnAcoes);

        header.add(direita, BorderLayout.EAST);
        return header;
    }

    private JPanel criarConteudo() {
        tabelaAbertas = null;

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(COR_FUNDO);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Contas a Pagar</h2>"
                + "<span style='color:gray;'>Acompanhe seus vencimentos e as contas já pagas</span></html>");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        conteudo.add(criarFiltroDatas());
        conteudo.add(Box.createVerticalStrut(ESPACO));

        JPanel cards = criarCards();
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        // so a primeira tem o menu de baixa
        adicionarTabela(conteudo, "A Pagar", ABERTAS);
        adicionarTabela(conteudo, "Pagas", CONCLUIDAS);
        adicionarTabela(conteudo, "Canceladas", ENCERRADAS);

        return conteudo;
    }

    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 4, 15, 0));
        painel.setOpaque(false);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // os cards de cima seguem o período filtrado; o saldo atual é sempre o real
        ArrayList<Contas> abertas = filtrarPeriodo(Contas.listar(Contas.PAGAR, false));
        ArrayList<Contas> pagas = filtrarPeriodo(Contas.listar(Contas.PAGAR, true));
        ArrayList<Contas> canceladas = filtrarPeriodo(Contas.listarEncerradas(Contas.PAGAR));

        // a conta com baixa parcial ainda está aberta: entra no "a pagar" só com o que falta
        // e o que já foi pago dela entra no "total pago"
        // conta cancelada sai do "a pagar", mas o que foi pago antes de cancelar continua no "total pago"
        double totalPagar = somarEmAberto(abertas);
        double totalPago = somarPago(abertas) + somarPago(pagas) + somarPago(canceladas);
        double totalCancelado = somarEncerrado(canceladas);

        painel.add(cardMetrica("Total a pagar", formatarValor(totalPagar),
                "Pendentes e vencidas", COR_VERMELHO));
        painel.add(cardMetrica("Total pago", formatarValor(totalPago),
                "Inclui pagamentos parciais", COR_VERDE));
        painel.add(cardMetrica("Total cancelado", formatarValor(totalCancelado),
                "Contas canceladas", COR_CINZA));
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

    private void adicionarTabela(JPanel conteudo, String titulo, int modo) {
        ArrayList<Contas> lista = modo == ENCERRADAS
                ? Contas.listarEncerradas(Contas.PAGAR)
                : Contas.listar(Contas.PAGAR, modo == CONCLUIDAS);
        ArrayList<Contas> dados = filtrarPeriodo(lista);
        // coluna 5: abertas = o que falta, concluidas = o que foi movimentado de verdade,
        // encerradas = o valor cancelado (e mais uma coluna com a data)
        String nomeColuna5 = modo == ABERTAS ? "Em aberto" : modo == CONCLUIDAS ? "Pago" : "Cancelado";
        String[] colunas = modo == ENCERRADAS
                ? new String[]{"ID", "Fornecedor", "Descrição", "Vencimento", "Valor", nomeColuna5, "Cancelada em", "Status"}
                : new String[]{"ID", "Fornecedor", "Descrição", "Vencimento", "Valor", nomeColuna5, "Status"};

        // tabela so de leitura: as ações ficam no botao do topo da tela
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
            if (modo == ABERTAS) {
                linha[5] = formatarValor(c.getValorEmAberto());
                linha[6] = c.getStatus();
            } else if (modo == CONCLUIDAS) {
                linha[5] = formatarValor(c.getValorPago());
                linha[6] = c.getStatus();
            } else {
                linha[5] = formatarValor(c.getValorEncerrado());
                linha[6] = c.getDataEncerramento() == null ? "-" : formatarData(c.getDataEncerramento());
                linha[7] = c.getStatus();
            }
            modelo.addRow(linha);
        }

        JPanel painel = criarPainelTabela(titulo, dados.size(),
                modo == ABERTAS ? "selecione uma conta para usar o botão Ações" : "");

        if (dados.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma conta neste período", SwingConstants.CENTER);
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(Color.GRAY);
            vazio.setBorder(new EmptyBorder(25, 0, 25, 0));
            painel.add(vazio, BorderLayout.CENTER);
        } else {
            JTable tabela = new JTable(modelo);
            configurarTabela(tabela, modo == ABERTAS, modo == ENCERRADAS);
            if (modo == ABERTAS) {
                tabelaAbertas = tabela;
            }

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

    private JPanel criarPainelTabela(String titulo, int quantidade, String dica) {
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
        String textoQtd = quantidade + (quantidade == 1 ? " conta" : " contas");
        if (!dica.isEmpty()) {
            textoQtd += "  -  " + dica;
        }
        JLabel labelQtd = new JLabel(textoQtd);
        labelQtd.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelQtd.setForeground(Color.GRAY);

        topo.add(labelTitulo, BorderLayout.WEST);
        topo.add(labelQtd, BorderLayout.EAST);
        painel.add(topo, BorderLayout.NORTH);

        return painel;
    }

    private void configurarTabela(JTable tabela, boolean selecionavel, boolean colunaData) {
        tabela.setRowHeight(ALTURA_LINHA);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tabela.setForeground(COR_TEXTO);
        tabela.setBackground(Color.WHITE);
        // sem linhas de grade, igual a Conciliacao (o espacamento padrao entre as celulas continua)
        tabela.setShowGrid(false);
        // so a tabela das contas em aberto deixa selecionar uma linha (uma conta por vez)
        tabela.setRowSelectionAllowed(selecionavel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        tabela.getColumnModel().getColumn(4).setCellRenderer(new CelulaRenderer(SwingConstants.CENTER, COR_VERMELHO));
        // nas encerradas o valor cancelado/estornado fica em cinza e a coluna 6 é a data (o status vai pra 7)
        tabela.getColumnModel().getColumn(5).setCellRenderer(
                new CelulaRenderer(SwingConstants.CENTER, colunaData ? COR_CINZA : COR_VERMELHO));
        int colunaStatus = colunaData ? 7 : 6;
        tabela.getColumnModel().getColumn(colunaStatus).setCellRenderer(new StatusRenderer());

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(105);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(105);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(95);
        if (colunaData) {
            tabela.getColumnModel().getColumn(7).setPreferredWidth(95);
        }

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

            if (status.equals("Paga") || status.equals("Recebida")) {
                setForeground(new Color(6, 95, 70));
                setBackground(new Color(209, 250, 229));
            } else if (status.equals("Pendente")) {
                setForeground(new Color(146, 64, 14));
                setBackground(new Color(254, 243, 199));
            } else if (status.equals(Contas.CANCELADA) || status.equals(Contas.ESTORNADA)) {
                setForeground(new Color(75, 85, 99));
                setBackground(new Color(229, 231, 235));
            } else if (status.equals(Contas.PARCIAL)) {
                setForeground(new Color(30, 64, 175));
                setBackground(new Color(219, 234, 254));
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

    // ===================== MENU DE AÇÕES (botão do topo) =====================

    // devolve a conta selecionada na tabela de contas em aberto (ou avisa que nao tem nenhuma)
    private Contas contaSelecionada() {
        if (tabelaAbertas == null || tabelaAbertas.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma conta na tabela \"A Pagar\" para usar as ações.",
                    "Nenhuma conta selecionada", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        int id = (int) tabelaAbertas.getValueAt(tabelaAbertas.getSelectedRow(), 0);
        return Contas.buscarPorId(id);
    }

    // abre o menu embaixo do botao Ações, alinhado na direita dele
    private void mostrarMenuAcoes(JComponent botao) {
        Contas conta = contaSelecionada();
        if (conta == null) {
            return;
        }
        int id = conta.getId();

        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createLineBorder(COR_BORDA));
        menu.setBackground(Color.WHITE);

        // mostra em qual conta as ações vao valer
        JMenuItem titulo = new JMenuItem("Conta " + id + " - " + conta.getEntidade());
        titulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        titulo.setEnabled(false);
        menu.add(titulo);
        menu.addSeparator();

        menu.add(criarItemMenu("Baixa total do pagamento", COR_TEXTO, () -> abrirBaixa(id, false)));
        menu.add(criarItemMenu("Baixa parcial do pagamento", COR_TEXTO, () -> abrirBaixa(id, true)));
        menu.addSeparator();
        menu.add(criarItemMenu("Cancelar conta", COR_VERMELHO, () -> cancelarConta(id)));

        int x = botao.getWidth() - menu.getPreferredSize().width;
        menu.show(botao, x, botao.getHeight());
    }

    private JMenuItem criarItemMenu(String texto, Color cor, Runnable acao) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("SansSerif", Font.PLAIN, 12));
        item.setForeground(cor);
        item.setBackground(Color.WHITE);
        item.setBorder(new EmptyBorder(7, 14, 7, 14));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // invokeLater pq a tela é reconstruida depois e a tabela atual some
        item.addActionListener(e -> SwingUtilities.invokeLater(acao));
        return item;
    }

    private void abrirBaixa(int id, boolean parcial) {
        Contas conta = Contas.buscarPorId(id);
        if (conta == null) {
            return;
        }

        DialogBaixa dialog = new DialogBaixa(SwingUtilities.getWindowAncestor(this), conta, parcial);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            atualizarTela();
        }
    }

    // nada é apagado: a conta fica registrada como Cancelada
    private void cancelarConta(int id) {
        Contas conta = Contas.buscarPorId(id);
        if (conta == null) {
            return;
        }

        String mensagem = "Cancelar a conta \"" + conta.getDescricao() + "\" de " + conta.getEntidade() + "?\n"
                + "Ela não será apagada: continua registrada na lista de canceladas.";
        if (conta.getValorPago() > 0) {
            mensagem += "\nO que já foi pago (" + formatarValor(conta.getValorPago()) + ") continua no saldo.";
        }

        Object[] opcoes = {"Cancelar conta", "Voltar"};
        int escolha = JOptionPane.showOptionDialog(this, mensagem,
                "Cancelar conta", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opcoes, opcoes[1]);

        if (escolha != 0) {
            return;
        }

        String erro = Contas.encerrar(id);
        if (erro == null) {
            atualizarTela();
        } else {
            JOptionPane.showMessageDialog(this, erro, "Não foi possível cancelar", JOptionPane.WARNING_MESSAGE);
        }
    }

    // botao azul arredondado (pintado na mao, o L&F as vezes ignora o background)
    // usado nas telas de contas, na tela de baixa e na tela de notas fiscais; comSeta desenha o triangulo do menu
    static class BotaoPrimario extends JButton {
        private static final long serialVersionUID = 1L;
        private final boolean comSeta;

        BotaoPrimario(String texto) {
            this(texto, false);
        }

        BotaoPrimario(String texto, boolean comSeta) {
            super(texto);
            this.comSeta = comSeta;
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(new EmptyBorder(9, 20, 9, comSeta ? 38 : 20));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? COR_AZUL.darker() : COR_AZUL);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            if (comSeta) {
                // triangulo apontando pra baixo, no canto direito
                int x = getWidth() - 26;
                int y = getHeight() / 2 - 2;
                g2.setColor(Color.WHITE);
                g2.fillPolygon(new int[]{x, x + 10, x + 5}, new int[]{y, y, y + 5}, 3);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===================== TELA DE BAIXA =====================
    // public static pra TelaContasReceber usar a mesma tela (nao preciso de arquivo novo)

    public static class DialogBaixa extends JDialog {
        private static final long serialVersionUID = 1L;

        private static final String[] CONTAS_FINANCEIRAS = {"Caixa", "Conta Corrente", "Poupança"};
        private static final String[] CATEGORIAS_PAGAR = {"Aluguel", "Serviços de Terceiros",
                "Licenças de Software", "Equipamentos", "Impostos", "Outros"};
        private static final String[] CATEGORIAS_RECEBER = {"Prestação de Serviços", "Consultoria",
                "Suporte", "Desenvolvimento", "Outros"};

        private static final NumberFormat FORMATO_NUMERO = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

        static {
            FORMATO_NUMERO.setMinimumFractionDigits(2);
            FORMATO_NUMERO.setMaximumFractionDigits(2);
        }

        private final Contas conta;
        private final boolean parcial;
        private final boolean pagar;
        private boolean confirmado = false;

        private final JTextField txtData = new JTextField();
        private final JTextField txtValorTotal = new JTextField();
        private final JTextArea txtHistorico = new JTextArea(3, 20);
        private final JComboBox<String> cmbContaFinanceira = new JComboBox<>(CONTAS_FINANCEIRAS);
        private final JComboBox<String> cmbCategoria;
        private final JTextField txtJuros = new JTextField("0,00");
        private final JTextField txtDesconto = new JTextField("0,00");
        private final JTextField txtMulta = new JTextField("0,00");
        private final JTextField txtTarifa = new JTextField("0,00");
        private final JTextField txtValorPago = new JTextField("0,00");
        private final JLabel lblResumo = new JLabel(" ");

        public DialogBaixa(Window dono, Contas conta, boolean parcial) {
            super(dono, parcial ? "Baixa parcial" : "Baixa total", ModalityType.APPLICATION_MODAL);
            this.conta = conta;
            this.parcial = parcial;
            this.pagar = conta.getTipo().equals(Contas.PAGAR);
            this.cmbCategoria = new JComboBox<>(pagar ? CATEGORIAS_PAGAR : CATEGORIAS_RECEBER);

            configurarComponentes();
            setContentPane(criarConteudo());
            ligarEventos();
            atualizar();

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            pack();
            setLocationRelativeTo(dono);
        }

        public boolean isConfirmado() {
            return confirmado;
        }

        // ---------- montagem da tela ----------

        private void configurarComponentes() {
            txtData.setText(FORMATO_DATA.format(LocalDate.now()));
            txtData.setEditable(false);
            txtData.setHorizontalAlignment(SwingConstants.CENTER);
            txtData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            txtData.setToolTipText("Clique para escolher a data");
            estilizarCampo(txtData);

            txtValorTotal.setEditable(false);
            txtValorTotal.setHorizontalAlignment(SwingConstants.RIGHT);
            estilizarCampo(txtValorTotal);
            txtValorTotal.setBackground(COR_FUNDO);
            txtValorTotal.setFont(new Font("SansSerif", Font.BOLD, 12));

            txtHistorico.setText(parcial
                    ? "Baixa parcial de conta a " + (pagar ? "pagar" : "receber")
                    : "Liquidação de conta a " + (pagar ? "pagar" : "receber"));
            txtHistorico.setLineWrap(true);
            txtHistorico.setWrapStyleWord(true);
            txtHistorico.setFont(new Font("SansSerif", Font.PLAIN, 12));
            txtHistorico.setForeground(COR_TEXTO);
            txtHistorico.setBorder(new EmptyBorder(6, 8, 6, 8));

            for (JComboBox<String> combo : java.util.List.of(cmbContaFinanceira, cmbCategoria)) {
                combo.setBackground(Color.WHITE);
                combo.setForeground(COR_TEXTO);
                combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
                combo.setPreferredSize(new Dimension(100, 32));
            }

            for (JTextField campo : java.util.List.of(txtJuros, txtDesconto, txtMulta, txtTarifa, txtValorPago)) {
                campo.setHorizontalAlignment(SwingConstants.RIGHT);
                estilizarCampo(campo);
            }

            // na baixa total o valor pago é calculado, so na parcial o usuario digita
            txtValorPago.setEditable(parcial);
            if (!parcial) {
                txtValorPago.setBackground(COR_FUNDO);
            }
            txtValorPago.setFont(new Font("SansSerif", Font.BOLD, 12));
        }

        private void estilizarCampo(JTextField campo) {
            campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
            campo.setForeground(COR_TEXTO);
            campo.setBackground(Color.WHITE);
            campo.setPreferredSize(new Dimension(100, 32));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(4, 8, 4, 8)));
        }

        private JPanel criarConteudo() {
            JPanel raiz = new JPanel(new BorderLayout(0, 14));
            raiz.setBackground(Color.WHITE);
            raiz.setBorder(new EmptyBorder(20, 24, 16, 24));

            String subtitulo = parcial
                    ? "Informe quanto está sendo " + (pagar ? "pago" : "recebido") + " agora. O restante continua em aberto."
                    : "Inclua as informações necessárias para a baixa do lançamento.";
            JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Baixa de lançamentos</h2>"
                    + "<span style='color:gray;'>" + subtitulo + "</span></html>");

            raiz.add(titulo, BorderLayout.NORTH);
            raiz.add(criarFormulario(), BorderLayout.CENTER);
            raiz.add(criarRodape(), BorderLayout.SOUTH);
            return raiz;
        }

        private JPanel criarFormulario() {
            JPanel form = new JPanel();
            form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
            form.setOpaque(false);

            // data e valor total
            JPanel linhaData = new JPanel(new GridLayout(1, 2, 15, 0));
            linhaData.setOpaque(false);
            linhaData.add(grupo(pagar ? "Data do pagamento" : "Data do recebimento", txtData));
            linhaData.add(grupo("Valor total", txtValorTotal));
            adicionar(form, linhaData);

            // historico
            JScrollPane scrollHistorico = new JScrollPane(txtHistorico);
            scrollHistorico.setBorder(BorderFactory.createLineBorder(COR_BORDA));
            scrollHistorico.setPreferredSize(new Dimension(100, 66));
            adicionar(form, grupo("Histórico", scrollHistorico));

            // dados da conta que está sendo baixada
            JLabel secao = new JLabel("Lançamento selecionado");
            secao.setFont(new Font("SansSerif", Font.BOLD, 12));
            secao.setForeground(COR_TEXTO);
            adicionar(form, secao);
            adicionar(form, criarInfoConta());

            // conta financeira e categoria
            JPanel linhaConta = new JPanel(new GridLayout(1, 2, 15, 0));
            linhaConta.setOpaque(false);
            linhaConta.add(grupo("Conta financeira", cmbContaFinanceira));
            linhaConta.add(grupo("Categoria", cmbCategoria));
            adicionar(form, linhaConta);

            // encargos e valor pago
            JPanel linhaValores = new JPanel(new GridLayout(1, 5, 10, 0));
            linhaValores.setOpaque(false);
            linhaValores.add(grupo("Juros", txtJuros));
            linhaValores.add(grupo("Desconto", txtDesconto));
            linhaValores.add(grupo("Multa", txtMulta));
            linhaValores.add(grupo("Tarifa", txtTarifa));
            linhaValores.add(grupo(pagar ? "Valor pago" : "Valor recebido", txtValorPago));
            adicionar(form, linhaValores);

            return form;
        }

        private JPanel criarInfoConta() {
            JPanel info = new JPanel(new GridLayout(1, 5, 10, 0));
            info.setBackground(COR_FUNDO);
            info.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(10, 12, 10, 12)));

            info.add(itemInfo(pagar ? "Fornecedor" : "Cliente", conta.getEntidade()));
            info.add(itemInfo("Nº do documento", String.format("%06d", conta.getId())));
            info.add(itemInfo("Vencimento", FORMATO_DATA.format(conta.getDataVencimento())));
            info.add(itemInfo("Valor original", FORMATO_MOEDA.format(conta.getValor())));
            info.add(itemInfo("Em aberto", FORMATO_MOEDA.format(conta.getValorEmAberto())));
            return info;
        }

        private JLabel itemInfo(String rotulo, String valor) {
            return new JLabel("<html><span style='color:gray;font-size:9px;'>" + escapar(rotulo)
                    + "</span><br><b>" + escapar(valor) + "</b></html>");
        }

        private String escapar(String texto) {
            return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }

        private JPanel criarRodape() {
            JPanel rodape = new JPanel(new BorderLayout(0, 12));
            rodape.setOpaque(false);

            lblResumo.setFont(new Font("SansSerif", Font.BOLD, 11));
            rodape.add(lblResumo, BorderLayout.NORTH);

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btnCancelar.setBackground(Color.WHITE);
            btnCancelar.setForeground(COR_TEXTO);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(7, 18, 7, 18)));
            btnCancelar.addActionListener(e -> dispose());

            BotaoPrimario btnBaixar = new BotaoPrimario(pagar ? "Baixar pagamento" : "Baixar recebimento");
            btnBaixar.addActionListener(e -> confirmar());

            JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            botoes.setOpaque(false);
            botoes.add(btnCancelar);
            botoes.add(btnBaixar);
            rodape.add(botoes, BorderLayout.SOUTH);

            getRootPane().setDefaultButton(btnBaixar);
            // ESC fecha a tela
            getRootPane().registerKeyboardAction(e -> dispose(),
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

            return rodape;
        }

        // rotulo pequeno em cima e o campo embaixo
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

        // ---------- comportamento ----------

        private void ligarEventos() {
            txtData.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mostrarCalendario(txtData);
                }
            });

            DocumentListener ouvinte = new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    atualizar();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    atualizar();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    atualizar();
                }
            };

            txtJuros.getDocument().addDocumentListener(ouvinte);
            txtDesconto.getDocument().addDocumentListener(ouvinte);
            txtMulta.getDocument().addDocumentListener(ouvinte);
            txtTarifa.getDocument().addDocumentListener(ouvinte);
            if (parcial) {
                txtValorPago.getDocument().addDocumentListener(ouvinte);
            }

            // seleciona tudo ao entrar no campo e deixa no formato 0,00 ao sair
            for (JTextField campo : java.util.List.of(txtJuros, txtDesconto, txtMulta, txtTarifa, txtValorPago)) {
                if (!campo.isEditable()) {
                    continue;
                }
                campo.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        SwingUtilities.invokeLater(campo::selectAll);
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            campo.setText(FORMATO_NUMERO.format(lerValor(campo.getText())));
                        } catch (NumberFormatException ex) {
                            // deixa o texto como esta, o erro aparece quando clicar em baixar
                        }
                    }
                });
            }
        }

        // recalcula o valor devido e o resumo toda vez que algum campo muda
        private void atualizar() {
            double juros = lerTolerante(txtJuros);
            double desconto = lerTolerante(txtDesconto);
            double multa = lerTolerante(txtMulta);
            double tarifa = lerTolerante(txtTarifa);

            double devido = conta.calcularDevido(juros, multa, desconto, tarifa);
            double pago;

            if (parcial) {
                pago = lerTolerante(txtValorPago);
            } else {
                pago = Math.max(devido, 0);
                txtValorPago.setText(FORMATO_NUMERO.format(pago));
            }

            txtValorTotal.setText(FORMATO_MOEDA.format(pago));

            String texto = "Valor devido com encargos: " + FORMATO_MOEDA.format(devido);
            Color cor = COR_CINZA;

            if (devido <= 0) {
                texto += "  -  o desconto não pode ser maior que o valor devido";
                cor = COR_VERMELHO;
            } else if (parcial && pago > 0) {
                double restante = Math.round((devido - pago) * 100.0) / 100.0;
                if (restante < 0) {
                    texto += "  -  o valor informado é maior que o devido";
                    cor = COR_VERMELHO;
                } else if (restante == 0) {
                    texto += "  -  a conta será quitada por completo";
                    cor = COR_VERDE;
                } else {
                    texto += "  -  restará em aberto: " + FORMATO_MOEDA.format(restante);
                    cor = COR_AZUL;
                }
            }

            lblResumo.setText(texto);
            lblResumo.setForeground(cor);
        }

        private void confirmar() {
            LocalDate data;
            try {
                data = LocalDate.parse(txtData.getText().trim(), FORMATO_DATA);
            } catch (DateTimeParseException ex) {
                aviso("Escolha uma data válida.", "Data inválida");
                return;
            }

            double juros, desconto, multa, tarifa, pago;
            try {
                juros = lerValor(txtJuros.getText());
                desconto = lerValor(txtDesconto.getText());
                multa = lerValor(txtMulta.getText());
                tarifa = lerValor(txtTarifa.getText());
                pago = lerValor(txtValorPago.getText());
            } catch (NumberFormatException ex) {
                aviso("Digite apenas números positivos nos campos de valor. Exemplo: 1.250,50",
                        "Valor inválido");
                return;
            }

            Contas.Baixa baixa = new Contas.Baixa(data,
                    (String) cmbContaFinanceira.getSelectedItem(),
                    (String) cmbCategoria.getSelectedItem(),
                    txtHistorico.getText().trim(),
                    juros, desconto, multa, tarifa, pago);

            String erro = Contas.baixar(conta.getId(), baixa);
            if (erro != null) {
                aviso(erro, "Não foi possível dar baixa");
                return;
            }

            confirmado = true;
            dispose();

            // a baixa gerou uma nota fiscal: avisa e deixa abrir na hora
            NotaFiscal nota = NotaFiscal.getUltima();
            if (nota != null) {
                Object[] opcoes = {"Ver nota fiscal", "Fechar"};
                int escolha = JOptionPane.showOptionDialog(getOwner(),
                        "Baixa registrada com sucesso!\nNota fiscal nº " + nota.getNumeroFormatado()
                                + " gerada (ela também fica na tela Notas Fiscais).",
                        "Baixa concluída", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, opcoes, opcoes[1]);

                if (escolha == 0) {
                    new TelaNotasFiscais.DialogNota(getOwner(), nota).setVisible(true);
                }
            }
        }

        private void aviso(String mensagem, String titulo) {
            JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.WARNING_MESSAGE);
        }

        // aceita "1.250,50", "1250,50" e "1250.50"; vazio vale 0
        static double lerValor(String texto) {
            String t = texto.trim().replace("R$", "").replace(" ", "");
            if (t.isEmpty()) {
                return 0;
            }
            if (t.contains(",")) {
                t = t.replace(".", "").replace(",", ".");
            }
            double v = Double.parseDouble(t);
            if (v < 0 || Double.isNaN(v) || Double.isInfinite(v)) {
                throw new NumberFormatException("valor invalido");
            }
            return v;
        }

        // pra atualizar o resumo enquanto digita: texto invalido conta como 0
        private static double lerTolerante(JTextField campo) {
            try {
                return lerValor(campo.getText());
            } catch (NumberFormatException e) {
                return 0;
            }
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

    // os metodos do calendario sao static pra tela de baixa e a TelaContasReceber usarem o mesmo
    static void mostrarCalendario(JTextField campo) {
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
    private static void construirCalendario(JPanel calendario, JPopupMenu popup, JTextField campo,
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

    private static void construirSelecaoDia(JPanel calendario, JPopupMenu popup, JTextField campo,
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

    private static void construirSelecaoMes(JPanel calendario, JPopupMenu popup, JTextField campo,
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

    private static JButton criarBotaoNavegacao(String texto) {
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

    private static String formatarMesAno(YearMonth mes) {
        String texto = mes.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR")));
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    private static String formatarNomeMes(Month mes) {
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

    // o que ainda falta pagar das contas da lista
    private double somarEmAberto(ArrayList<Contas> lista) {
        double total = 0;
        for (Contas c : lista) {
            total += c.getValorEmAberto();
        }
        return total;
    }

    // o que ja foi pago das contas da lista (inclui o pago parcialmente)
    private double somarPago(ArrayList<Contas> lista) {
        double total = 0;
        for (Contas c : lista) {
            total += c.getValorPago();
        }
        return total;
    }

    // valor das contas canceladas/estornadas da lista (o mesmo que aparece na tabela delas)
    private double somarEncerrado(ArrayList<Contas> lista) {
        double total = 0;
        for (Contas c : lista) {
            total += c.getValorEncerrado();
        }
        return total;
    }

    // período inicial: do 1º dia do mês da menor data até o último dia do mês da maior data
    private void definirPeriodoInicial(String tipo) {
        ArrayList<Contas> todas = new ArrayList<>();
        todas.addAll(Contas.listar(tipo, false));
        todas.addAll(Contas.listar(tipo, true));
        // as canceladas/estornadas tambem contam pro periodo, senao somem da tela
        todas.addAll(Contas.listarEncerradas(tipo));

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