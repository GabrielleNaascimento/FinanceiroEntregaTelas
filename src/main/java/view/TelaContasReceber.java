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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    // refaz a tela toda, chamo depois de receber uma conta pra atualizar as listas e os cards
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
        TelaContasPagar.BotaoPrimario btnAcoes = new TelaContasPagar.BotaoPrimario("Ações", true);
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

        // so a primeira tem o menu de baixa
        adicionarTabela(conteudo, "A Receber", ABERTAS);
        adicionarTabela(conteudo, "Recebidas", CONCLUIDAS);
        adicionarTabela(conteudo, "Estornadas", ENCERRADAS);

        return conteudo;
    }

    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 4, 15, 0));
        painel.setOpaque(false);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // os cards de cima seguem o período filtrado; o saldo atual é sempre o real
        ArrayList<Contas> abertas = filtrarPeriodo(Contas.listar(Contas.RECEBER, false));
        ArrayList<Contas> recebidas = filtrarPeriodo(Contas.listar(Contas.RECEBER, true));
        ArrayList<Contas> estornadas = filtrarPeriodo(Contas.listarEncerradas(Contas.RECEBER));

        // a conta com baixa parcial ainda está aberta: entra no "a receber" só com o que falta
        // e o que já foi recebido dela entra no "total recebido"
        // conta estornada sai dos dois (o que tinha sido recebido foi devolvido) e entra no "total estornado"
        double totalReceber = somarEmAberto(abertas);
        double totalRecebido = somarRecebido(abertas) + somarRecebido(recebidas);
        double totalEstornado = somarEncerrado(estornadas);

        painel.add(cardMetrica("Total a receber", formatarValor(totalReceber),
                "Pendentes e atrasadas", COR_AZUL));
        painel.add(cardMetrica("Total recebido", formatarValor(totalRecebido),
                "Inclui recebimentos parciais", COR_VERDE));
        painel.add(cardMetrica("Total estornado", formatarValor(totalEstornado),
                "Contas estornadas", COR_CINZA));
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
                ? Contas.listarEncerradas(Contas.RECEBER)
                : Contas.listar(Contas.RECEBER, modo == CONCLUIDAS);
        ArrayList<Contas> dados = filtrarPeriodo(lista);
        // coluna 5: abertas = o que falta, concluidas = o que foi movimentado de verdade,
        // encerradas = o valor estornado (e mais uma coluna com a data)
        String nomeColuna5 = modo == ABERTAS ? "Em aberto" : modo == CONCLUIDAS ? "Recebido" : "Estornado";
        String[] colunas = modo == ENCERRADAS
                ? new String[]{"ID", "Cliente", "Descrição", "Vencimento", "Valor", nomeColuna5, "Estornada em", "Status"}
                : new String[]{"ID", "Cliente", "Descrição", "Vencimento", "Valor", nomeColuna5, "Status"};

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

        // cliente e descricao a esquerda, o resto centralizado
        tabela.setDefaultRenderer(Object.class, new CelulaRenderer(SwingConstants.CENTER, COR_TEXTO));
        tabela.getColumnModel().getColumn(1).setCellRenderer(new CelulaRenderer(SwingConstants.LEFT, COR_TEXTO));
        tabela.getColumnModel().getColumn(2).setCellRenderer(new CelulaRenderer(SwingConstants.LEFT, COR_TEXTO));
        tabela.getColumnModel().getColumn(4).setCellRenderer(new CelulaRenderer(SwingConstants.CENTER, COR_VERDE));
        // nas encerradas o valor cancelado/estornado fica em cinza e a coluna 6 é a data (o status vai pra 7)
        tabela.getColumnModel().getColumn(5).setCellRenderer(
                new CelulaRenderer(SwingConstants.CENTER, colunaData ? COR_CINZA : COR_VERDE));
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
                // atrasada
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
                    "Selecione uma conta na tabela \"A Receber\" para usar as ações.",
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

        menu.add(criarItemMenu("Baixa total do recebimento", COR_TEXTO, () -> abrirBaixa(id, false)));
        menu.add(criarItemMenu("Baixa parcial do recebimento", COR_TEXTO, () -> abrirBaixa(id, true)));
        menu.addSeparator();
        menu.add(criarItemMenu("Estornar conta", COR_VERMELHO, () -> estornarConta(id)));

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

    // a tela de baixa é a mesma da TelaContasPagar (ela se adapta ao tipo da conta)
    private void abrirBaixa(int id, boolean parcial) {
        Contas conta = Contas.buscarPorId(id);
        if (conta == null) {
            return;
        }

        TelaContasPagar.DialogBaixa dialog =
                new TelaContasPagar.DialogBaixa(SwingUtilities.getWindowAncestor(this), conta, parcial);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            atualizarTela();
        }
    }

    // nada é apagado: a conta fica registrada como Estornada
    private void estornarConta(int id) {
        Contas conta = Contas.buscarPorId(id);
        if (conta == null) {
            return;
        }

        String mensagem = "Estornar a conta \"" + conta.getDescricao() + "\" de " + conta.getEntidade() + "?\n"
                + "Ela não será apagada: continua registrada na lista de estornadas.";
        if (conta.getValorPago() > 0) {
            mensagem += "\nO que já foi recebido (" + formatarValor(conta.getValorPago()) + ") será devolvido e sai do saldo.";
        }

        Object[] opcoes = {"Estornar conta", "Voltar"};
        int escolha = JOptionPane.showOptionDialog(this, mensagem,
                "Estornar conta", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opcoes, opcoes[1]);

        if (escolha != 0) {
            return;
        }

        String erro = Contas.encerrar(id);
        if (erro == null) {
            atualizarTela();
        } else {
            JOptionPane.showMessageDialog(this, erro, "Não foi possível estornar", JOptionPane.WARNING_MESSAGE);
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
    // o calendario em si fica na TelaContasPagar (TelaContasPagar.mostrarCalendario)

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
                TelaContasPagar.mostrarCalendario(campo);
            }
        });
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

    // o que ainda falta receber das contas da lista
    private double somarEmAberto(ArrayList<Contas> lista) {
        double total = 0;
        for (Contas c : lista) {
            total += c.getValorEmAberto();
        }
        return total;
    }

    // o que ja foi recebido das contas da lista (inclui o recebido parcialmente)
    private double somarRecebido(ArrayList<Contas> lista) {
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