
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import model.Conciliacao;
import model.Contas;

public class TelaConciliacaoBancaria extends JPanel {

    private static final long serialVersionUID = 1L;

    // ==========================================
    // COMPONENTES
    // ==========================================

    private JTable tabelaInstituicoes;
    private DefaultTableModel modeloInstituicoes;

    private JTable tabelaContas;
    private DefaultTableModel modeloContas;

    private JComboBox<String> filtroSituacao;

    private JLabel lblQuantidadeInstituicoes;
    private JLabel lblQuantidadeContas;
    private JLabel lblSaldoTotal;

    private JButton btnEditar;
    private JButton btnSituacao;
    private JButton btnCancelarSelecao;

    private boolean modoEdicao = false;

    private final Color FUNDO = new Color(245, 247, 250);
    private final Color BORDA = new Color(226, 232, 240);
    private final Color TEXTO = new Color(30, 38, 52);
    private final Color CINZA = new Color(105, 114, 128);
    private final Color AZUL = new Color(37, 99, 235);

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

public TelaConciliacaoBancaria() {

    setLayout(new BorderLayout());
    setBackground(FUNDO);

    add(criarAreaPrincipal(), BorderLayout.CENTER);

    atualizarTabelaInstituicoes();
    atualizarTabelaContas();
    atualizarCards();
}

    // ==========================================
    // ÁREA PRINCIPAL
    // ==========================================

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

        JLabel lblUser = new JLabel("Jefferson - Administrador");

        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setForeground(TEXTO);

        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {

        JPanel conteudo = new JPanel();

        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(FUNDO);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel titulo = new JLabel("Gestão de Bancos");

        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Gerencie instituições, contas bancárias e conciliações"
        );

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(CINZA);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        conteudo.add(titulo);
        conteudo.add(subtitulo);
        conteudo.add(Box.createVerticalStrut(15));
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));
        conteudo.add(criarAbas());
        conteudo.add(Box.createVerticalStrut(15));
        conteudo.add(criarPainelSolicitacoes());

        return conteudo;
    }

    // ==========================================
    // CARDS
    // ==========================================

    private JPanel criarCards() {

        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));

        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));

        JPanel cardInstituicoes = criarCard("Instituições cadastradas");
        JPanel cardContas = criarCard("Contas bancárias");
        JPanel cardSaldo = criarCard("Saldo total");

        lblQuantidadeInstituicoes = new JLabel("0");
        lblQuantidadeContas = new JLabel("0");
        lblSaldoTotal = new JLabel("R$ 0,00");

        configurarValorCard(lblQuantidadeInstituicoes);
        configurarValorCard(lblQuantidadeContas);
        configurarValorCard(lblSaldoTotal);

        cardInstituicoes.add(lblQuantidadeInstituicoes);
        cardContas.add(lblQuantidadeContas);
        cardSaldo.add(lblSaldoTotal);

        painel.add(cardInstituicoes);
        painel.add(cardContas);
        painel.add(cardSaldo);

        return painel;
    }

    private JPanel criarCard(String titulo) {

        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(13, 13, 13, 13)
        ));

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setForeground(CINZA);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(7));

        return card;
    }

    private void configurarValorCard(JLabel label) {

        label.setForeground(TEXTO);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
    }

    private void atualizarCards() {

        lblQuantidadeInstituicoes.setText(
                String.valueOf(Conciliacao.getInstituicoes().size())
        );

        lblQuantidadeContas.setText(
                String.valueOf(Conciliacao.getContasBancarias().size())
        );

        lblSaldoTotal.setText(
                formatoMoeda.format(Conciliacao.getSaldoTotal())
        );
    }

    // ==========================================
    // ABAS
    // ==========================================

  private JTabbedPane criarAbas() {

    JTabbedPane abas = new JTabbedPane();

    abas.setFont(new Font("SansSerif", Font.BOLD, 12));
    abas.setBackground(Color.WHITE);
    abas.setForeground(TEXTO);
    abas.setOpaque(true);

    abas.setUI(new BasicTabbedPaneUI() {

        @Override
        protected void installDefaults() {
            super.installDefaults();

            tabAreaInsets = new Insets(0, 0, 0, 0);
            contentBorderInsets = new Insets(1, 1, 1, 1);
        }

        @Override
        protected Insets getTabInsets(
                int tabPlacement,
                int tabIndex) {

            return new Insets(9, 15, 9, 15);
        }

        @Override
        protected void paintTabBackground(
                Graphics g,
                int tabPlacement,
                int tabIndex,
                int x,
                int y,
                int w,
                int h,
                boolean selecionada) {

            g.setColor(selecionada
                    ? Color.WHITE
                    : new Color(248, 250, 252));

            g.fillRect(x, y, w, h);
        }

        @Override
        protected void paintTabBorder(
                Graphics g,
                int tabPlacement,
                int tabIndex,
                int x,
                int y,
                int w,
                int h,
                boolean selecionada) {

            g.setColor(BORDA);

            if (selecionada) {
                g.drawLine(x, y, x + w - 1, y);
                g.drawLine(x, y, x, y + h - 1);
                g.drawLine(x + w - 1, y,
                        x + w - 1, y + h - 1);
            } else {
                g.drawRect(x, y, w - 1, h - 1);
            }
        }

        @Override
        protected void paintContentBorder(
                Graphics g,
                int tabPlacement,
                int selectedIndex) {

            g.setColor(BORDA);

            g.drawRect(
                    0,
                    0,
                    tabPane.getWidth() - 1,
                    tabPane.getHeight() - 1
            );
        }
    });

    abas.addTab("Instituições", criarAbaInstituicoes());
    abas.addTab("Contas", criarAbaContas());
    abas.addTab("Conciliação", criarAbaConciliacao());

    abas.setAlignmentX(Component.LEFT_ALIGNMENT);
    abas.setPreferredSize(new Dimension(0, 420));
    abas.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
    );

    return abas;
}

    //==========================================
    // ABA INSTITUIÇÕES
    // ==========================================

    private JPanel criarAbaInstituicoes() {

        JPanel painel = new JPanel(new BorderLayout(0, 12));

        painel.setBackground(Color.WHITE);
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        painel.add(criarAcoesInstituicoes(), BorderLayout.NORTH);
        painel.add(criarTabelaInstituicoes(), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarAcoesInstituicoes() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        painel.setOpaque(false);

        JButton btnCadastrar = criarBotao("Cadastrar instituição", true);

        btnEditar = criarBotao("Editar", false);
        btnSituacao = criarBotao("Desativar", false);
        btnCancelarSelecao = criarBotao("Cancelar", false);

        btnCancelarSelecao.setVisible(false);

        filtroSituacao = new JComboBox<>(
                new String[]{"Todas", "Ativas", "Inativas"}
        );

        configurarComboDesign(filtroSituacao);

        btnCadastrar.addActionListener(e -> cadastrarInstituicao());
        btnEditar.addActionListener(e -> editarInstituicao());
        btnSituacao.addActionListener(e -> alterarSituacaoInstituicao());
        btnCancelarSelecao.addActionListener(e -> cancelarSelecaoInstituicoes());

        filtroSituacao.addActionListener(e -> atualizarTabelaInstituicoes());

        painel.add(btnCadastrar);
        painel.add(btnEditar);
        painel.add(btnSituacao);
        painel.add(btnCancelarSelecao);

        painel.add(Box.createHorizontalStrut(15));

        painel.add(new JLabel("Situação:"));
        painel.add(filtroSituacao);

        return painel;
    }

    private JButton criarBotao(String texto, boolean principal) {

        JButton botao = new JButton(texto);

        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setOpaque(true);

        botao.setPreferredSize(new Dimension(
                texto.equals("Cadastrar instituição") ? 165 : 105,
                34
        ));

        if (principal) {

            botao.setBackground(AZUL);
            botao.setForeground(Color.WHITE);
            botao.setBorder(BorderFactory.createLineBorder(AZUL));

        } else {

            botao.setBackground(Color.WHITE);
            botao.setForeground(TEXTO);
            botao.setBorder(BorderFactory.createLineBorder(
                    new Color(148, 163, 184)
            ));
        }

        return botao;
    }

    private void configurarComboDesign(JComboBox<?> combo) {

    combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
    combo.setBackground(Color.WHITE);
    combo.setForeground(TEXTO);

    combo.setPreferredSize(new Dimension(130, 34));
    combo.setFocusable(true);

    combo.setBorder(
            BorderFactory.createLineBorder(
                    new Color(148, 163, 184)
            )
    );
}

    private JPanel criarTabelaInstituicoes() {

        JPanel painel = new JPanel(new BorderLayout());

        painel.setBackground(Color.WHITE);

        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titulo = new JLabel("Instituições cadastradas");

        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(TEXTO);

        painel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {
                "Nome",
                "Código COMPE",
                "CNPJ",
                "Situação"
        };

        modeloInstituicoes = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaInstituicoes = new JTable(modeloInstituicoes);

        tabelaInstituicoes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelaInstituicoes.getTableHeader().setResizingAllowed(false);
        tabelaInstituicoes.setRowHeight(32);
        tabelaInstituicoes.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabelaInstituicoes.setForeground(TEXTO);
        tabelaInstituicoes.setBackground(Color.WHITE);
        tabelaInstituicoes.setGridColor(BORDA);
        tabelaInstituicoes.setShowGrid(true);
        tabelaInstituicoes.setIntercellSpacing(new Dimension(1, 1));
        tabelaInstituicoes.setSelectionBackground(new Color(219, 234, 254));
        tabelaInstituicoes.setSelectionForeground(TEXTO);
        tabelaInstituicoes.setFillsViewportHeight(true);
        tabelaInstituicoes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        tabelaInstituicoes.setAutoCreateRowSorter(false);

        tabelaInstituicoes.getTableHeader().setPreferredSize(
                new Dimension(0, 31)
        );

        tabelaInstituicoes.getTableHeader().setBackground(
                new Color(248, 250, 252)
        );

        tabelaInstituicoes.getTableHeader().setForeground(CINZA);
        tabelaInstituicoes.getTableHeader().setFont(
                new Font("SansSerif", Font.PLAIN, 11)
        );

        tabelaInstituicoes.getTableHeader().setReorderingAllowed(false);

        tabelaInstituicoes.getTableHeader().setBorder(
                BorderFactory.createLineBorder(BORDA)
        );

        DefaultTableCellRenderer centralizar =
                new DefaultTableCellRenderer();

        centralizar.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabelaInstituicoes.getColumnCount(); i++) {

            tabelaInstituicoes.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centralizar);
        }

        JScrollPane scroll = new JScrollPane(tabelaInstituicoes);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private void atualizarTabelaInstituicoes() {

        if (modeloInstituicoes == null) {
            return;
        }

        modeloInstituicoes.setRowCount(0);

        String filtro = filtroSituacao == null
                ? "Todas"
                : (String) filtroSituacao.getSelectedItem();

        for (Conciliacao.Instituicao instituicao :
                Conciliacao.getInstituicoes()) {

            if (filtro.equals("Ativas") && !instituicao.isAtiva()) {
                continue;
            }

            if (filtro.equals("Inativas") && instituicao.isAtiva()) {
                continue;
            }

            modeloInstituicoes.addRow(new Object[]{
                    instituicao.getNome(),
                    formatarCompe(instituicao.getCodigoCompe()),
                    instituicao.getCnpj(),
                    instituicao.isAtiva() ? "Ativa" : "Inativa"
            });
        }
    }

    // ==========================================
    // FORMULÁRIO DE INSTITUIÇÃO
    // ==========================================

    
private JPanel criarFormularioInstituicao(
        JTextField txtNome,
        JTextField txtCnpj,
        JTextField txtCompe) {

    JPanel formulario = new JPanel();

    formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
    formulario.setBackground(Color.WHITE);
    formulario.setBorder(new EmptyBorder(10, 5, 5, 5));
    formulario.setPreferredSize(new Dimension(350, 305));

    JLabel titulo = new JLabel("Informações da instituição");

    titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
    titulo.setForeground(TEXTO);
    titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

    formulario.add(titulo);
    formulario.add(Box.createVerticalStrut(18));

    JLabel erroCnpj = criarLabelErro();
    JLabel erroCompe = criarLabelErro();

    adicionarCampo(formulario, "Nome da instituição", txtNome, null);
    adicionarCampo(formulario, "CNPJ", txtCnpj, erroCnpj);
    adicionarCampo(formulario, "Código COMPE", txtCompe, erroCompe);

    // Enter no nome avança para o CNPJ.
    txtNome.addActionListener(e ->
            txtCnpj.requestFocusInWindow()
    );

    // Enter no CNPJ valida e avança para o COMPE.
    txtCnpj.addActionListener(e -> {
        validarCnpjInstituicao(txtCnpj, erroCnpj);
        txtCompe.requestFocusInWindow();
    });

    

    // Enter no COMPE valida o CNPJ e tenta confirmar o formulário.
txtCompe.addActionListener(e -> {

    boolean cnpjValido =
            validarCnpjInstituicao(txtCnpj, erroCnpj);

    if (cnpjValido) {

        javax.swing.JRootPane raiz =
                javax.swing.SwingUtilities.getRootPane(txtCompe);

        if (raiz != null && raiz.getDefaultButton() != null) {
            raiz.getDefaultButton().doClick();
        }

    } else {
        txtCnpj.requestFocusInWindow();
    }
});

    // Também valida quando a pessoa sai do campo usando o mouse.
    txtCnpj.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            validarCnpjInstituicao(txtCnpj, erroCnpj);
        }
    });

    aplicarFiltroNumerico(txtCnpj, 14);
    aplicarFiltroNumerico(txtCompe, 3);

    return formulario;
}

private JLabel criarLabelErro() {

    JLabel label = new JLabel(" ");

    label.setFont(new Font("SansSerif", Font.PLAIN, 11));
    label.setForeground(new Color(220, 38, 38));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);

    return label;
}

private void adicionarCampo(
        JPanel formulario,
        String titulo,
        JTextField campo,
        JLabel erro) {

    JLabel label = new JLabel(titulo);

    label.setFont(new Font("SansSerif", Font.BOLD, 12));
    label.setForeground(TEXTO);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);

    campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
    campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
    campo.setPreferredSize(new Dimension(300, 34));

    campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA),
            new EmptyBorder(5, 8, 5, 8)
    ));

    formulario.add(label);
    formulario.add(Box.createVerticalStrut(5));
    formulario.add(campo);

    if (erro != null) {
        formulario.add(erro);
    }

    formulario.add(Box.createVerticalStrut(12));
}

private boolean validarCnpjInstituicao(
        JTextField campo,
        JLabel erro) {

    String valor = campo.getText().trim();

    boolean valido = valor.matches("\\d{14}");

    erro.setText(valido ? " " :
            "Informe exatamente 14 números.");

    return valido;
}


    // ==========================================
    // CADASTRAR INSTITUIÇÃO
    // ==========================================

    private void cadastrarInstituicao() {

        JTextField txtNome = new JTextField();
        JTextField txtCnpj = new JTextField();
        JTextField txtCompe = new JTextField();

        JPanel formulario = criarFormularioInstituicao(
                txtNome, txtCnpj, txtCompe
        );

        int opcao = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Cadastrar instituição",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        String nome = txtNome.getText().trim();
        String cnpj = txtCnpj.getText().replaceAll("\\D", "");
        String compe = txtCompe.getText().trim();

    if (nome.isEmpty() || cnpj.length() != 14
            || compe.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Preencha o nome, informe um CNPJ com 14 dígitos "
                + "e o código COMPE.",
                "Dados inválidos",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }
        Conciliacao.Instituicao instituicao =
                new Conciliacao.Instituicao(nome, cnpj, compe);

        Conciliacao.cadastrarInstituicao(instituicao);

        atualizarTabelaInstituicoes();
        atualizarCards();
    }

    // ==========================================
    // EDITAR INSTITUIÇÃO
    // ==========================================

    private void editarInstituicao() {

        if (!modoEdicao) {

            modoEdicao = true;

            btnEditar.setText("Confirmar edição");
            btnSituacao.setEnabled(false);
            btnCancelarSelecao.setVisible(true);

            tabelaInstituicoes.clearSelection();

            revalidarAcoes();

            return;
        }

        int linha = tabelaInstituicoes.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma instituição na tabela.",
                    "Nenhuma instituição selecionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int indice = tabelaInstituicoes.convertRowIndexToModel(linha);

        Conciliacao.Instituicao atual =
                Conciliacao.getInstituicoes().get(indice);

        cancelarSelecaoInstituicoes();

        JTextField txtNome = new JTextField(atual.getNome());
        JTextField txtCnpj = new JTextField(atual.getCnpj());
        JTextField txtCompe = new JTextField(atual.getCodigoCompe());

        JPanel formulario = criarFormularioInstituicao(
                txtNome, txtCnpj, txtCompe
        );

        int opcao = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Editar instituição",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        String nome = txtNome.getText().trim();
        String cnpj = txtCnpj.getText().replaceAll("\\D", "");
        String compe = txtCompe.getText().trim();

    if (nome.isEmpty() || cnpj.length() != 14
            || compe.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Preencha o nome, informe um CNPJ com 14 dígitos "
                + "e o código COMPE.",
                "Dados inválidos",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

        Conciliacao.Instituicao atualizada =
                new Conciliacao.Instituicao(nome, cnpj, compe);

        atualizada.setAtiva(atual.isAtiva());

        Conciliacao.editarInstituicao(indice, atualizada);

        atualizarTabelaInstituicoes();
        atualizarCards();
    }

    // ==========================================
    // ATIVAR / DESATIVAR INSTITUIÇÃO
    // ==========================================

    private void alterarSituacaoInstituicao() {

        if (modoEdicao) {
            return;
        }

        int linha = tabelaInstituicoes.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma instituição na tabela.",
                    "Nenhuma instituição selecionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int indice = tabelaInstituicoes.convertRowIndexToModel(linha);

        Conciliacao.Instituicao instituicao =
                Conciliacao.getInstituicoes().get(indice);

        boolean novaSituacao = !instituicao.isAtiva();

        String acao = novaSituacao ? "ativar" : "desativar";

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente " + acao + " esta instituição?",
                "Confirmar alteração",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {

            Conciliacao.alterarSituacaoInstituicao(
                    indice,
                    novaSituacao
            );

            atualizarTabelaInstituicoes();
            atualizarCards();
        }
    }

    // ==========================================
    // SELEÇÃO
    // ==========================================

    private void cancelarSelecaoInstituicoes() {

        modoEdicao = false;

        tabelaInstituicoes.clearSelection();

        btnEditar.setText("Editar");
        btnSituacao.setEnabled(true);

        btnCancelarSelecao.setVisible(false);

        revalidarAcoes();
    }

    private void revalidarAcoes() {

        btnCancelarSelecao.getParent().revalidate();
        btnCancelarSelecao.getParent().repaint();
    }

    // ==========================================
    // ABA CONTAS
    // ==========================================


    // ==========================================
    // ABA CONTAS
    // ==========================================

    private JPanel criarAbaContas() {

        JPanel painel = new JPanel(new BorderLayout(0, 12));

        painel.setBackground(Color.WHITE);
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        painel.add(criarAcoesContas(), BorderLayout.NORTH);
        painel.add(criarTabelaContas(), BorderLayout.CENTER);

        return painel;
    }

    // ==========================================
    // AÇÕES DAS CONTAS
    // ==========================================

    private JPanel criarAcoesContas() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        painel.setOpaque(false);

        JButton btnCadastrar = criarBotao("Cadastrar conta", true);
        JButton btnEditarConta = criarBotao("Editar", false);
        JButton btnExcluir = criarBotao("Excluir", false);
        JButton btnMovimentacoes = criarBotao("Movimentações", false);

        btnMovimentacoes.setPreferredSize(new Dimension(135, 34));
        btnMovimentacoes.addActionListener(e -> abrirJanelaMovimentacoes());

        btnCadastrar.setPreferredSize(new Dimension(145, 34));
        btnEditarConta.setPreferredSize(new Dimension(95, 34));
        btnExcluir.setPreferredSize(new Dimension(95, 34));

        btnCadastrar.addActionListener(e -> abrirFormularioConta(null, -1));

        btnEditarConta.addActionListener(e -> editarConta());

        btnExcluir.addActionListener(e -> excluirConta());

        painel.add(btnCadastrar);
        painel.add(btnEditarConta);
        painel.add(btnExcluir);
        painel.add(btnMovimentacoes);

        return painel;
    }

    // ==========================================
    // TABELA DE CONTAS
    // ==========================================

    private JPanel criarTabelaContas() {

        JPanel painel = new JPanel(new BorderLayout());

        painel.setBackground(Color.WHITE);

        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titulo = new JLabel("Contas bancárias cadastradas");

        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(TEXTO);

        painel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {
                "Instituição",
                "Agência",
                "Número da conta",
                "Tipo",
                "Saldo atual"
        };

        modeloContas = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaContas = new JTable(modeloContas);

        tabelaContas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelaContas.getTableHeader().setResizingAllowed(false);
        tabelaContas.setRowHeight(32);
        tabelaContas.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabelaContas.setForeground(TEXTO);
        tabelaContas.setBackground(Color.WHITE);
        tabelaContas.setGridColor(BORDA);
        tabelaContas.setShowGrid(true);
        tabelaContas.setIntercellSpacing(new Dimension(1, 1));
        tabelaContas.setSelectionBackground(new Color(219, 234, 254));
        tabelaContas.setSelectionForeground(TEXTO);
        tabelaContas.setFillsViewportHeight(true);

        tabelaContas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabelaContas.setAutoCreateRowSorter(true);

        tabelaContas.getTableHeader().setPreferredSize(
                new Dimension(0, 31)
        );

        tabelaContas.getTableHeader().setBackground(
                new Color(248, 250, 252)
        );

        tabelaContas.getTableHeader().setForeground(CINZA);

        tabelaContas.getTableHeader().setFont(
                new Font("SansSerif", Font.PLAIN, 11)
        );

        tabelaContas.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centralizar =
                new DefaultTableCellRenderer();

        centralizar.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabelaContas.getColumnCount(); i++) {

            tabelaContas.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centralizar);
        }

        JScrollPane scroll = new JScrollPane(tabelaContas);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    // ==========================================
    // ATUALIZAR TABELA DE CONTAS
    // ==========================================

    private void atualizarTabelaContas() {

        if (modeloContas == null) {
            return;
        }

        modeloContas.setRowCount(0);

        for (Conciliacao.ContaBancaria conta :
                Conciliacao.getContasBancarias()) {

            modeloContas.addRow(new Object[]{
                    conta.getInstituicao().getNome(),
                    conta.getAgencia(),
                    conta.getNumeroConta(),
                    conta.getTipoConta(),
                    formatoMoeda.format(conta.getSaldoAtual())
            });
        }
    }

    // ==========================================
    // FORMULÁRIO DE CONTA
    // ==========================================


private void abrirFormularioConta(
        Conciliacao.ContaBancaria contaAtual,
        int indice) {

    java.util.ArrayList<Conciliacao.Instituicao> instituicoesDisponiveis =
            new java.util.ArrayList<>();

    JComboBox<String> cbInstituicao = new JComboBox<>();

    for (Conciliacao.Instituicao instituicao :
            Conciliacao.getInstituicoes()) {

        if (instituicao.isAtiva()
                || instituicao == (contaAtual == null
                ? null : contaAtual.getInstituicao())) {

            instituicoesDisponiveis.add(instituicao);

            cbInstituicao.addItem(
                    instituicao.getNome() + " ("
                    + instituicao.getCodigoCompe() + ")"
            );
        }
    }

    if (instituicoesDisponiveis.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Cadastre uma instituição ativa antes de cadastrar uma conta.",
                "Nenhuma instituição disponível",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    JTextField txtCnpj = new JTextField(
            contaAtual == null ? "" : contaAtual.getCnpjEmpresa()
    );

    JTextField txtAgencia = new JTextField(
            contaAtual == null ? "" : contaAtual.getAgencia()
    );

    JTextField txtNumero = new JTextField(
            contaAtual == null ? "" : contaAtual.getNumeroConta()
    );

    JTextField txtSaldo = new JTextField();

    txtSaldo.setText("0,00");
txtSaldo.setForeground(Color.GRAY);

txtSaldo.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusGained(java.awt.event.FocusEvent e) {
        if (txtSaldo.getText().equals("0,00")
                && txtSaldo.getForeground().equals(Color.GRAY)) {
            txtSaldo.setText("");
            txtSaldo.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        if (txtSaldo.getText().trim().isEmpty()) {
            txtSaldo.setText("0,00");
            txtSaldo.setForeground(Color.GRAY);
        }
    }
});

    aplicarFiltroNumerico(txtCnpj, 14);
    aplicarFiltroAgencia(txtAgencia);
    aplicarFiltroNumerico(txtNumero, 20);
    aplicarFiltroSaldo(txtSaldo);

    JComboBox<String> cbTipo = new JComboBox<>(
            new String[]{
                "Corrente",
                "Poupança",
                "Pagamento",
                "Outro"
            }
    );

    if (contaAtual != null) {

        cbInstituicao.setSelectedIndex(
                instituicoesDisponiveis.indexOf(
                        contaAtual.getInstituicao()
                )
        );

        cbTipo.setSelectedItem(contaAtual.getTipoConta());

        if (cbTipo.getSelectedIndex() == -1) {
            cbTipo.addItem(contaAtual.getTipoConta());
            cbTipo.setSelectedItem(contaAtual.getTipoConta());
        }
    }

    // Avisos de validação.
    JLabel erroCnpj = criarLabelErro();
    JLabel erroAgencia = criarLabelErro();
    JLabel erroNumero = criarLabelErro();

    // Montagem do formulário.
    JPanel formulario = new JPanel();

    formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
    formulario.setBackground(Color.WHITE);
    formulario.setBorder(new EmptyBorder(10, 10, 10, 10));
    formulario.setPreferredSize(new Dimension(360, 490));

    JLabel titulo = new JLabel("Informações da conta bancária");

    titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
    titulo.setForeground(TEXTO);
    titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

    formulario.add(titulo);
    formulario.add(Box.createVerticalStrut(15));

    adicionarCampo(formulario, "CNPJ da empresa", txtCnpj, erroCnpj);
    adicionarCampo(formulario, "Agência", txtAgencia, erroAgencia);
    adicionarCampo(formulario, "Número da conta", txtNumero, erroNumero);

    // Tipo da conta.
    JLabel lblTipo = new JLabel("Tipo da conta");
    lblTipo.setFont(new Font("SansSerif", Font.BOLD, 12));
    lblTipo.setForeground(TEXTO);
    lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

    cbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
    cbTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

    formulario.add(lblTipo);
    formulario.add(Box.createVerticalStrut(5));
    formulario.add(cbTipo);
    formulario.add(Box.createVerticalStrut(12));

    // Instituição.
    JLabel lblInstituicao = new JLabel("Instituição");
    lblInstituicao.setFont(new Font("SansSerif", Font.BOLD, 12));
    lblInstituicao.setForeground(TEXTO);
    lblInstituicao.setAlignmentX(Component.LEFT_ALIGNMENT);

    cbInstituicao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
    cbInstituicao.setAlignmentX(Component.LEFT_ALIGNMENT);

    formulario.add(lblInstituicao);
    formulario.add(Box.createVerticalStrut(5));
    formulario.add(cbInstituicao);
    formulario.add(Box.createVerticalStrut(12));

    adicionarCampo(formulario, "Saldo inicial", txtSaldo, null);

    // Validação ao sair dos campos.
    txtCnpj.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            validarCnpjInstituicao(txtCnpj, erroCnpj);
        }
    });

    txtAgencia.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            validarAgenciaConta(txtAgencia, erroAgencia);
        }
    });

    txtNumero.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            validarNumeroConta(txtNumero, erroNumero);
        }
    });

    // Navegação com Enter.
    txtCnpj.addActionListener(e -> {
        validarCnpjInstituicao(txtCnpj, erroCnpj);
        txtAgencia.requestFocusInWindow();
    });

    txtAgencia.addActionListener(e -> {
        validarAgenciaConta(txtAgencia, erroAgencia);
        txtNumero.requestFocusInWindow();
    });

    txtNumero.addActionListener(e -> {
        validarNumeroConta(txtNumero, erroNumero);
        cbTipo.requestFocusInWindow();
    });

    cbTipo.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                txtSaldo.requestFocusInWindow();
            }
        }
    });

    cbInstituicao.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                txtCnpj.requestFocusInWindow();
            }
        }
    });

    int opcao = JOptionPane.showConfirmDialog(
            this,
            formulario,
            contaAtual == null
                    ? "Cadastrar conta"
                    : "Editar conta",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (opcao != JOptionPane.OK_OPTION) {
        return;
    }

    String cnpj = txtCnpj.getText().trim();
    String agencia = txtAgencia.getText().trim();
    String numero = txtNumero.getText().trim();
    String tipo = (String) cbTipo.getSelectedItem();

    boolean cnpjValido =
            validarCnpjInstituicao(txtCnpj, erroCnpj);

    boolean agenciaValida =
            validarAgenciaConta(txtAgencia, erroAgencia);

    boolean numeroValido =
            validarNumeroConta(txtNumero, erroNumero);

    if (!cnpjValido || !agenciaValida || !numeroValido) {
        return;
    }

    double saldo;

    try {

        String valor = txtSaldo.getText()
        .replace("R$", "")
        .replace(" ", "")
        .trim();

if (valor.isEmpty() || valor.equals("0,00")) {
    valor = "0";
}

valor = valor.replace(".", "").replace(",", ".");

saldo = Double.parseDouble(valor);

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Informe um saldo inicial válido.",
                "Dados inválidos",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (Double.isNaN(saldo)
            || Double.isInfinite(saldo)) {

        JOptionPane.showMessageDialog(
                this,
                "Informe um saldo inicial válido.",
                "Dados inválidos",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    Conciliacao.Instituicao instituicao =
            instituicoesDisponiveis.get(
                    cbInstituicao.getSelectedIndex()
            );

    Conciliacao.ContaBancaria conta =
            new Conciliacao.ContaBancaria(
                    instituicao,
                    cnpj.replaceAll("\\D", ""),
                    agencia,
                    numero,
                    tipo,
                    saldo
            );

    if (contaAtual == null) {
        Conciliacao.cadastrarConta(conta);
    } else {
        Conciliacao.editarConta(indice, conta);
    }

    atualizarTabelaContas();
    atualizarCards();
}


private boolean validarAgenciaConta(
        JTextField campo,
        JLabel erro) {

    String valor = campo.getText().trim();

    boolean valido = valor.matches("\\d{4}/\\d");

    erro.setText(valido ? " " :
            "A agência deve ter 4 números e 1 dígito verificador (0000/0).");

    return valido;
}

private boolean validarNumeroConta(
        JTextField campo,
        JLabel erro) {

    boolean valido = !campo.getText().trim().isEmpty();

    erro.setText(valido ? " " :
            "Informe o número da conta.");

    return valido;
}


    // ==========================================
    // EDITAR CONTA
    // ==========================================

    private void editarConta() {

        int linha = tabelaContas.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma conta na tabela.",
                    "Nenhuma conta selecionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int indice = tabelaContas.convertRowIndexToModel(linha);

        Conciliacao.ContaBancaria conta =
                Conciliacao.getContasBancarias().get(indice);

        abrirFormularioConta(conta, indice);
    }

    // ==========================================
    // EXCLUIR CONTA
    // ==========================================

    private void excluirConta() {

        int linha = tabelaContas.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma conta na tabela.",
                    "Nenhuma conta selecionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int indice = tabelaContas.convertRowIndexToModel(linha);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir esta conta?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean excluida = Conciliacao.excluirConta(indice);

        if (!excluida) {

            JOptionPane.showMessageDialog(
                    this,
                    "Esta conta possui movimentações ou conciliações "
                    + "e não pode ser excluída.",
                    "Não foi possível excluir",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        atualizarTabelaContas();
        atualizarCards();
    }
 

// ==========================================
// JANELA DE MOVIMENTAÇÕES
// ==========================================

private void abrirJanelaMovimentacoes() {

    if (Conciliacao.getContasBancarias().isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Cadastre uma conta bancária antes de registrar movimentações.",
                "Nenhuma conta cadastrada",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    JDialog dialog = new JDialog();
    dialog.setTitle("Movimentações bancárias");
    dialog.setSize(1050, 580);
    dialog.setModal(true);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout(10, 10));

    JPanel principal = new JPanel(new BorderLayout(10, 10));
    principal.setBackground(Color.WHITE);
    principal.setBorder(new EmptyBorder(15, 15, 15, 15));

    // Filtros
    JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
    filtros.setBackground(Color.WHITE);

    JComboBox<String> cbConta = new JComboBox<>();
    cbConta.addItem("Todas as contas");

    configurarComboDesign(cbConta);

    for (Conciliacao.ContaBancaria conta :
            Conciliacao.getContasBancarias()) {
        cbConta.addItem(nomeConta(conta));
    }

    JComboBox<String> cbTipo = new JComboBox<>(
            new String[]{"Todos", "ENTRADA", "SAIDA", "TRANSFERENCIA"}
    );

    JTextField txtInicio = new JTextField(9);
    JTextField txtFim = new JTextField(9);
    JTextField txtBusca = new JTextField(12);

    JButton btnFiltrar = criarBotao("Filtrar", false);

    filtros.add(new JLabel("Conta:"));
    filtros.add(cbConta);
    filtros.add(new JLabel("Tipo:"));
    filtros.add(cbTipo);
    filtros.add(new JLabel("De (AAAA-MM-DD):"));
    filtros.add(txtInicio);
    filtros.add(new JLabel("Até:"));
    filtros.add(txtFim);
    filtros.add(new JLabel("Descrição:"));
    filtros.add(txtBusca);
    filtros.add(btnFiltrar);

    // Tabela
    String[] colunas = {
        "ID", "Conta", "Data", "Descrição",
        "Valor", "Tipo", "Conta destino"
    };

    DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    JTable tabela = new JTable(modelo);
    tabela.setRowHeight(30);
    tabela.setFont(new Font("SansSerif", Font.PLAIN, 12));
    tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabela.setAutoCreateRowSorter(true);
    tabela.setFillsViewportHeight(true);

    tabela.getTableHeader().setReorderingAllowed(false);
    tabela.getTableHeader().setBackground(new Color(248, 250, 252));

    JScrollPane scroll = new JScrollPane(tabela);
    scroll.setBorder(BorderFactory.createLineBorder(BORDA));

    // Ações
    JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    acoes.setBackground(Color.WHITE);

    JButton btnNova = criarBotao("Nova movimentação", true);
    JButton btnEditarMov = criarBotao("Editar", false);
    JButton btnExcluirMov = criarBotao("Excluir", false);
    JButton btnVisualizar = criarBotao("Visualizar", false);
    JButton btnFechar = criarBotao("Fechar", false);

    acoes.add(btnNova);
    acoes.add(btnEditarMov);
    acoes.add(btnExcluirMov);
    acoes.add(btnVisualizar);
    acoes.add(btnFechar);

    // Atualização da tabela
    Runnable atualizar = () -> atualizarTabelaMovimentacoes(
            modelo, cbConta, cbTipo, txtInicio, txtFim, txtBusca
    );

    btnFiltrar.addActionListener(e -> atualizar.run());

    cbConta.addActionListener(e -> atualizar.run());
    cbTipo.addActionListener(e -> atualizar.run());

    btnNova.addActionListener(e -> {
        Conciliacao.MovimentacaoBancaria nova =
                abrirFormularioMovimentacao(dialog, null);

        if (nova != null) {
            Conciliacao.cadastrarMovimentacao(nova);
            atualizar.run();
            atualizarTabelaContas();
            atualizarCards();
        }
    });

    btnEditarMov.addActionListener(e -> {
        Conciliacao.MovimentacaoBancaria selecionada =
                obterMovimentacaoSelecionada(tabela);

        if (selecionada == null) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Selecione uma movimentação.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Conciliacao.MovimentacaoBancaria atualizada =
                abrirFormularioMovimentacao(dialog, selecionada);

        if (atualizada != null) {
            int indice = Conciliacao.getMovimentacoes().indexOf(selecionada);

            Conciliacao.editarMovimentacao(indice, atualizada);

            atualizar.run();
            atualizarTabelaContas();
            atualizarCards();
        }
    });

    btnExcluirMov.addActionListener(e -> {
        Conciliacao.MovimentacaoBancaria selecionada =
                obterMovimentacaoSelecionada(tabela);

        if (selecionada == null) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Selecione uma movimentação.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Não permite excluir movimentações já utilizadas
        // em registros de conciliação.
        for (Conciliacao.RegistroConciliacao conciliacao :
                Conciliacao.getConciliacoes()) {

            for (Conciliacao.ItemConciliacao item :
                    conciliacao.getItens()) {

                if (item.getMovimentacao() == selecionada) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Esta movimentação está vinculada a uma conciliação.",
                            "Não foi possível excluir",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                dialog,
                "Deseja realmente excluir esta movimentação?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            int indice = Conciliacao.getMovimentacoes().indexOf(selecionada);

            Conciliacao.excluirMovimentacao(indice);

            atualizar.run();
            atualizarTabelaContas();
            atualizarCards();
        }
    });

    btnVisualizar.addActionListener(e -> {
        Conciliacao.MovimentacaoBancaria movimentacao =
                obterMovimentacaoSelecionada(tabela);

        if (movimentacao == null) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Selecione uma movimentação.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String destino = movimentacao.getContaDestino() == null
                ? "Não se aplica"
                : nomeConta(movimentacao.getContaDestino());

        String detalhes =
                "ID: " + movimentacao.getId()
                + "\nConta: " + nomeConta(movimentacao.getConta())
                + "\nData: " + movimentacao.getData()
                + "\nDescrição: " + movimentacao.getDescricao()
                + "\nValor: " + formatoMoeda.format(movimentacao.getValor())
                + "\nTipo: " + movimentacao.getTipo()
                + "\nConta destino: " + destino;

        JOptionPane.showMessageDialog(
                dialog,
                detalhes,
                "Detalhes da movimentação",
                JOptionPane.INFORMATION_MESSAGE
        );
    });

    btnFechar.addActionListener(e -> dialog.dispose());

    principal.add(filtros, BorderLayout.NORTH);
    principal.add(scroll, BorderLayout.CENTER);
    principal.add(acoes, BorderLayout.SOUTH);

    dialog.add(principal);

    atualizar.run();

    dialog.setVisible(true);
}


// ==========================================
// ATUALIZAR TABELA DE MOVIMENTAÇÕES
// ==========================================

private void atualizarTabelaMovimentacoes(
        DefaultTableModel modelo,
        JComboBox<String> cbConta,
        JComboBox<String> cbTipo,
        JTextField txtInicio,
        JTextField txtFim,
        JTextField txtBusca) {

    modelo.setRowCount(0);

    java.time.LocalDate inicio = null;
    java.time.LocalDate fim = null;

    try {
        if (!txtInicio.getText().trim().isEmpty()) {
            inicio = java.time.LocalDate.parse(txtInicio.getText().trim());
        }

        if (!txtFim.getText().trim().isEmpty()) {
            fim = java.time.LocalDate.parse(txtFim.getText().trim());
        }

        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new IllegalArgumentException();
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                "Informe datas válidas no formato AAAA-MM-DD e confira o período.",
                "Período inválido",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int indiceConta = cbConta.getSelectedIndex();
    String tipo = (String) cbTipo.getSelectedItem();
    String busca = txtBusca.getText().trim().toLowerCase();

    for (Conciliacao.MovimentacaoBancaria movimentacao :
            Conciliacao.getMovimentacoes()) {

        if (indiceConta > 0) {
            Conciliacao.ContaBancaria contaFiltro =
                    Conciliacao.getContasBancarias().get(indiceConta - 1);

            if (movimentacao.getConta() != contaFiltro
                    && movimentacao.getContaDestino() != contaFiltro) {
                continue;
            }
        }

        if (!"Todos".equals(tipo)
                && !movimentacao.getTipo().equals(tipo)) {
            continue;
        }

        if (inicio != null && movimentacao.getData().isBefore(inicio)) {
            continue;
        }

        if (fim != null && movimentacao.getData().isAfter(fim)) {
            continue;
        }

        if (!movimentacao.getDescricao().toLowerCase().contains(busca)) {
            continue;
        }

        String destino = movimentacao.getContaDestino() == null
                ? "-"
                : nomeConta(movimentacao.getContaDestino());

        modelo.addRow(new Object[]{
            movimentacao.getId(),
            nomeConta(movimentacao.getConta()),
            movimentacao.getData(),
            movimentacao.getDescricao(),
            formatoMoeda.format(movimentacao.getValor()),
            movimentacao.getTipo(),
            destino
        });
    }
}


// ==========================================
// FORMULÁRIO DE MOVIMENTAÇÃO
// ==========================================

private Conciliacao.MovimentacaoBancaria abrirFormularioMovimentacao(
        java.awt.Component pai,
        Conciliacao.MovimentacaoBancaria atual) {

    java.util.ArrayList<Conciliacao.ContaBancaria> contas =
            Conciliacao.getContasBancarias();

    JComboBox<String> cbConta = new JComboBox<>();
    JComboBox<String> cbDestino = new JComboBox<>();

    for (Conciliacao.ContaBancaria conta : contas) {
        cbConta.addItem(nomeConta(conta));
        cbDestino.addItem(nomeConta(conta));
    }

    if (atual != null) {
        cbConta.setSelectedIndex(contas.indexOf(atual.getConta()));

        if (atual.getContaDestino() != null) {
            cbDestino.setSelectedIndex(
                    contas.indexOf(atual.getContaDestino())
            );
        }
    }

    JTextField txtData = new JTextField(
            atual == null
                    ? java.time.LocalDate.now().toString()
                    : atual.getData().toString()
    );

    JTextField txtDescricao = new JTextField(
            atual == null ? "" : atual.getDescricao()
    );

    JTextField txtValor = new JTextField(
            atual == null
                    ? ""
                    : String.valueOf(atual.getValor()).replace(".", ",")
    );

    JComboBox<String> cbTipo = new JComboBox<>(
            new String[]{"ENTRADA", "SAIDA", "TRANSFERENCIA"}
    );

    if (atual != null) {
        cbTipo.setSelectedItem(atual.getTipo());
    }

    cbDestino.setEnabled(
            "TRANSFERENCIA".equals(cbTipo.getSelectedItem())
    );

    cbTipo.addActionListener(e ->
            cbDestino.setEnabled(
                    "TRANSFERENCIA".equals(cbTipo.getSelectedItem())
            )
    );

    JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
    formulario.setBackground(Color.WHITE);
    formulario.setBorder(new EmptyBorder(12, 12, 12, 12));

    formulario.add(new JLabel("Conta:"));
    formulario.add(cbConta);

    formulario.add(new JLabel("Data (AAAA-MM-DD):"));
    formulario.add(txtData);

    formulario.add(new JLabel("Descrição:"));
    formulario.add(txtDescricao);

    formulario.add(new JLabel("Valor:"));
    formulario.add(txtValor);

    formulario.add(new JLabel("Tipo:"));
    formulario.add(cbTipo);

    formulario.add(new JLabel("Conta destino:"));
    formulario.add(cbDestino);

    int opcao = JOptionPane.showConfirmDialog(
            pai,
            formulario,
            atual == null
                    ? "Nova movimentação"
                    : "Editar movimentação",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (opcao != JOptionPane.OK_OPTION) {
        return null;
    }

    try {
        java.time.LocalDate data =
                java.time.LocalDate.parse(txtData.getText().trim());

        String descricao = txtDescricao.getText().trim();

        String valorTexto = txtValor.getText()
                .replace("R$", "")
                .replace(" ", "")
                .trim();

        if (valorTexto.contains(",")) {
            valorTexto = valorTexto.replace(".", "")
                    .replace(",", ".");
        }

        double valor = Double.parseDouble(valorTexto);

        String tipo = (String) cbTipo.getSelectedItem();

        Conciliacao.ContaBancaria conta =
                contas.get(cbConta.getSelectedIndex());

        Conciliacao.ContaBancaria destino = null;

        if ("TRANSFERENCIA".equals(tipo)) {
            destino = contas.get(cbDestino.getSelectedIndex());

            if (conta == destino) {
                JOptionPane.showMessageDialog(
                        pai,
                        "A conta de origem e a conta de destino devem ser diferentes.",
                        "Transferência inválida",
                        JOptionPane.WARNING_MESSAGE
                );
                return null;
            }
        }

        if (descricao.isEmpty()
                || valor <= 0
                || Double.isInfinite(valor)
                || Double.isNaN(valor)) {

            throw new IllegalArgumentException();
        }

        int id = atual == null
                ? proximoIdMovimentacao()
                : atual.getId();

        return new Conciliacao.MovimentacaoBancaria(
                id,
                conta,
                destino,
                data,
                descricao,
                valor,
                tipo
        );

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
                pai,
                "Confira a data, a descrição e o valor informado.",
                "Dados inválidos",
                JOptionPane.WARNING_MESSAGE
        );
        return null;
    }
}


// ==========================================
// UTILITÁRIOS DE MOVIMENTAÇÃO
// ==========================================

private Conciliacao.MovimentacaoBancaria obterMovimentacaoSelecionada(
        JTable tabela) {

    int linha = tabela.getSelectedRow();

    if (linha == -1) {
        return null;
    }

    int linhaModelo = tabela.convertRowIndexToModel(linha);
    int id = (int) tabela.getModel().getValueAt(linhaModelo, 0);

    return Conciliacao.buscarMovimentacaoPorId(id);
}


private int proximoIdMovimentacao() {

    int maiorId = 0;

    for (Conciliacao.MovimentacaoBancaria movimentacao :
            Conciliacao.getMovimentacoes()) {

        maiorId = Math.max(maiorId, movimentacao.getId());
    }

    return maiorId + 1;
}


private String nomeConta(Conciliacao.ContaBancaria conta) {

    return conta.getInstituicao().getNome()
            + " - Ag. " + conta.getAgencia()
            + " / Conta " + conta.getNumeroConta();
}


    // ==========================================
    // ABA CONCILIAÇÃO
    // ==========================================

private JPanel criarAbaConciliacao() {

    JPanel painel = new JPanel(new BorderLayout(0, 12));

    painel.setBackground(Color.WHITE);
    painel.setBorder(new EmptyBorder(15, 15, 15, 15));

    JLabel titulo = new JLabel("Conciliação bancária");

    titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
    titulo.setForeground(TEXTO);

    JLabel descricao = new JLabel(
            "Compare as movimentações bancárias com os lançamentos do financeiro."
    );

    descricao.setFont(new Font("SansSerif", Font.PLAIN, 12));
    descricao.setForeground(CINZA);

    JPanel topo = new JPanel();
    topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
    topo.setOpaque(false);

    titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
    descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

    topo.add(titulo);
    topo.add(Box.createVerticalStrut(5));
    topo.add(descricao);
    topo.add(Box.createVerticalStrut(15));

    ArrayList<Conciliacao.ContaBancaria> contas =
            Conciliacao.getContasBancarias();

    JComboBox<String> cbConta = new JComboBox<>();

    configurarComboDesign(cbConta);

    for (Conciliacao.ContaBancaria conta : contas) {
        cbConta.addItem(nomeContaConciliacao(conta));
    }

    JTextField txtInicio = new JTextField(
            LocalDate.now()
                    .withDayOfMonth(1)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    );

    JTextField txtFim = new JTextField(
            LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    );

    JTextField txtResponsavel = new JTextField();
    JTextField txtSaldoInicial = new JTextField();

    JPanel painelSaldo = new JPanel(new BorderLayout(5, 0));
    painelSaldo.setBackground(Color.WHITE);
    painelSaldo.setOpaque(true);

    painelSaldo.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(
                            new Color(160, 180, 205)),
                    new EmptyBorder(2, 5, 2, 5)
            )
    );

    JLabel lblMoeda = new JLabel("R$");
    lblMoeda.setForeground(new Color(105, 114, 128));

    txtSaldoInicial.setBorder(null);
    txtSaldoInicial.setBackground(Color.WHITE);

    painelSaldo.add(lblMoeda, BorderLayout.WEST);
    painelSaldo.add(txtSaldoInicial, BorderLayout.CENTER);

    configurarCampoSaldo(txtSaldoInicial);

    JPanel formulario = new JPanel(new GridLayout(2, 5, 8, 5));
    formulario.setOpaque(false);

    formulario.add(new JLabel("Conta bancária"));
    formulario.add(new JLabel("Data inicial"));
    formulario.add(new JLabel("Data final"));
    formulario.add(new JLabel("Responsável"));
    formulario.add(new JLabel("Saldo inicial"));

    formulario.add(cbConta);
    formulario.add(txtInicio);
    formulario.add(txtFim);
    formulario.add(txtResponsavel);
    formulario.add(painelSaldo);

    configurarCampoData(txtInicio);
    configurarCampoData(txtFim);

    JButton btnIniciar = criarBotao("Iniciar conciliação", true);

    btnIniciar.addActionListener(e -> {

        try {

            int indiceConta = cbConta.getSelectedIndex();

            if (indiceConta < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cadastre uma conta bancária primeiro."
                );
                return;
            }

            LocalDate inicio = LocalDate.parse(
                    txtInicio.getText().trim(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );

            LocalDate fim = LocalDate.parse(
                    txtFim.getText().trim(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );

            if (fim.isBefore(inicio)) {
                JOptionPane.showMessageDialog(
                        this,
                        "A data final não pode ser anterior à inicial."
                );
                return;
            }

            if (txtResponsavel.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe o responsável pela conciliação."
                );
                return;
            }

            double saldoInicial = parseValorConciliacao(
                    txtSaldoInicial.getText()
            );

            if (saldoInicial < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe um saldo inicial válido."
                );
                return;
            }

            Conciliacao.ContaBancaria conta =
                    contas.get(indiceConta);

            abrirDialogoConciliacao(
                    conta,
                    inicio,
                    fim,
                    txtResponsavel.getText().trim(),
                    saldoInicial
            );

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe as datas no formato DD/MM/AAAA."
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe um saldo inicial válido."
            );
        }
    });

    JPanel acao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    acao.setOpaque(false);
    acao.add(btnIniciar);

    topo.add(formulario);
    topo.add(Box.createVerticalStrut(10));
    topo.add(acao);

    painel.add(topo, BorderLayout.NORTH);

    JLabel aviso = new JLabel(
            "As associações são feitas manualmente durante a conferência."
    );

    aviso.setForeground(CINZA);
    aviso.setFont(new Font("SansSerif", Font.ITALIC, 11));

    painel.add(aviso, BorderLayout.SOUTH);

    return painel;
}

private void abrirDialogoConciliacao(
        Conciliacao.ContaBancaria conta,
        LocalDate inicio,
        LocalDate fim,
        String responsavel,
        double saldoInicial) {

    Conciliacao.RegistroConciliacao registro =
            new Conciliacao.RegistroConciliacao(
                    conta,
                    inicio,
                    fim,
                    responsavel,
                    saldoInicial
            );

    ArrayList<Conciliacao.MovimentacaoBancaria> bancos =
            new ArrayList<>();

    ArrayList<LinhaFluxo> fluxos =
            new ArrayList<>();

    // Movimentações bancárias da conta e do período.
    for (Conciliacao.MovimentacaoBancaria m :
            Conciliacao.getMovimentacoes()) {

        boolean pertenceConta =
                m.getConta() == conta
                || m.getContaDestino() == conta;

        boolean pertencePeriodo =
                !m.getData().isBefore(inicio)
                && !m.getData().isAfter(fim);

        if (pertenceConta && pertencePeriodo) {
            bancos.add(m);
        }
    }

    // Baixas registradas no fluxo financeiro.
    for (Contas c : Contas.getContas()) {

        for (Contas.Baixa baixa : c.getBaixas()) {

            boolean pertencePeriodo =
                    !baixa.getData().isBefore(inicio)
                    && !baixa.getData().isAfter(fim);

            if (pertencePeriodo) {
                fluxos.add(new LinhaFluxo(c, baixa));
            }
        }
    }

    JDialog dialog = new JDialog();

    dialog.setTitle("Conciliação bancária");
    dialog.setModal(true);
    dialog.setSize(1100, 700);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout(10, 10));

    JLabel titulo = new JLabel(
            "Conciliação: " + nomeContaConciliacao(conta)
    );

    titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
    titulo.setForeground(TEXTO);

    JLabel periodo = new JLabel(
            "Período: "
            + inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            + " até "
            + fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    );

    periodo.setForeground(CINZA);

    JPanel cabecalho = new JPanel();
    cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
    cabecalho.setBorder(new EmptyBorder(15, 15, 5, 15));
    cabecalho.setBackground(Color.WHITE);

    cabecalho.add(titulo);
    cabecalho.add(Box.createVerticalStrut(5));
    cabecalho.add(periodo);

    String[] colunasBanco = {
        "Data", "Descrição", "Tipo", "Valor"
    };

    String[] colunasFluxo = {
        "Data", "Lançamento", "Conta financeira", "Valor"
    };

    String[] colunasAssociados = {
        "Movimentação bancária",
        "Valor banco",
        "Lançamento financeiro",
        "Valor sistema",
        "Diferença"
    };

    DefaultTableModel modeloBanco =
            new DefaultTableModel(colunasBanco, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    DefaultTableModel modeloFluxo =
            new DefaultTableModel(colunasFluxo, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    DefaultTableModel modeloAssociados =
            new DefaultTableModel(colunasAssociados, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    JTable tabelaBanco = new JTable(modeloBanco);
    JTable tabelaFluxo = new JTable(modeloFluxo);
    JTable tabelaAssociados = new JTable(modeloAssociados);

    configurarTabelaConciliacao(tabelaBanco);
    configurarTabelaConciliacao(tabelaFluxo);
    configurarTabelaConciliacao(tabelaAssociados);

    Runnable atualizarTabelas = () -> {

        modeloBanco.setRowCount(0);
        modeloFluxo.setRowCount(0);
        modeloAssociados.setRowCount(0);

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Conciliacao.MovimentacaoBancaria m : bancos) {

            String tipo = m.getTipo();

            if (m.getTipo().equals(Conciliacao.TRANSFERENCIA)
                    && m.getContaDestino() == conta) {
                tipo = "TRANSFERÊNCIA RECEBIDA";
            }

            modeloBanco.addRow(new Object[]{
                m.getData().format(formato),
                m.getDescricao(),
                tipo,
                formatoMoeda.format(
                        valorMovimentacaoNaConta(m, conta)
                )
            });
        }

        for (LinhaFluxo linha : fluxos) {

            modeloFluxo.addRow(new Object[]{
                linha.baixa.getData().format(formato),
                linha.conta.getDescricao(),
                linha.baixa.getContaFinanceira(),
                formatoMoeda.format(
                        valorFluxo(linha)
                )
            });
        }

        for (Conciliacao.ItemConciliacao item :
                registro.getItens()) {

            double valorBanco = valorMovimentacaoNaConta(
                    item.getMovimentacao(),
                    conta
            );

            double valorSistema = valorFluxo(
                    new LinhaFluxo(
                            item.getContaFluxo(),
                            item.getBaixa()
                    )
            );

            modeloAssociados.addRow(new Object[]{
                item.getMovimentacao().getDescricao(),
                formatoMoeda.format(valorBanco),
                item.getContaFluxo().getDescricao(),
                formatoMoeda.format(valorSistema),
                formatoMoeda.format(valorBanco - valorSistema)
            });
        }
    };

    atualizarTabelas.run();

    JPanel comparacao = new JPanel(new GridLayout(1, 2, 12, 0));
    comparacao.setBackground(Color.WHITE);
    comparacao.setBorder(new EmptyBorder(10, 15, 10, 15));

    JPanel painelBanco = criarPainelTabelaConciliacao(
            "Movimentações bancárias",
            tabelaBanco
    );

    JPanel painelFluxo = criarPainelTabelaConciliacao(
            "Baixas do financeiro",
            tabelaFluxo
    );

    comparacao.add(painelBanco);
    comparacao.add(painelFluxo);

    JButton btnAssociar = criarBotao("Associar movimentações", true);
    JButton btnDesassociar = criarBotao("Desassociar", false);
    JButton btnFinalizar = criarBotao("Finalizar conciliação", true);
    JButton btnFechar = criarBotao("Fechar", false);

    btnAssociar.addActionListener(e -> {

        int linhaBanco = tabelaBanco.getSelectedRow();
        int linhaFluxo = tabelaFluxo.getSelectedRow();

        if (linhaBanco < 0 || linhaFluxo < 0) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Selecione uma movimentação de cada tabela."
            );
            return;
        }

        Conciliacao.MovimentacaoBancaria movimento =
                bancos.get(linhaBanco);

        LinhaFluxo fluxo = fluxos.get(linhaFluxo);

        registro.adicionarItem(
                new Conciliacao.ItemConciliacao(
                        fluxo.conta,
                        fluxo.baixa,
                        movimento
                )
        );

        bancos.remove(linhaBanco);
        fluxos.remove(linhaFluxo);

        atualizarTabelas.run();
    });

    btnDesassociar.addActionListener(e -> {

        int linha = tabelaAssociados.getSelectedRow();

        if (linha < 0) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Selecione uma associação para desassociar."
            );
            return;
        }

        Conciliacao.ItemConciliacao item =
                registro.getItens().get(linha);

        registro.removerItem(item);

        bancos.add(item.getMovimentacao());

        fluxos.add(
                new LinhaFluxo(
                        item.getContaFluxo(),
                        item.getBaixa()
                )
        );

        atualizarTabelas.run();
    });

    btnFinalizar.addActionListener(e -> {

        JTextField txtSaldoBanco = new JTextField();
        JTextField txtSaldoSistema = new JTextField();

        JPanel formulario = new JPanel(new GridLayout(2, 2, 8, 8));

        formulario.add(new JLabel("Saldo final do banco:"));
        formulario.add(txtSaldoBanco);

        formulario.add(new JLabel("Saldo final do sistema:"));
        formulario.add(txtSaldoSistema);

        int resposta = JOptionPane.showConfirmDialog(
                dialog,
                formulario,
                "Finalizar conciliação",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            double saldoBanco = parseValorConciliacao(
                    txtSaldoBanco.getText()
            );

            double saldoSistema = parseValorConciliacao(
                    txtSaldoSistema.getText()
            );

            registro.finalizar(saldoBanco, saldoSistema);

            Conciliacao.cadastrarConciliacao(registro);

            String relatorio =
                    "CONCILIAÇÃO FINALIZADA\n\n"
                    + "Conta: " + nomeContaConciliacao(conta) + "\n"
                    + "Período: "
                    + inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " até "
                    + fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + "\n\n"
                    + "Responsável: " + responsavel + "\n"
                    + "Saldo inicial: " + formatoMoeda.format(saldoInicial) + "\n"
                    + "Saldo final do banco: "
                    + formatoMoeda.format(registro.getSaldoFinalBanco()) + "\n"
                    + "Saldo final do sistema: "
                    + formatoMoeda.format(registro.getSaldoFinalSistema()) + "\n"
                    + "Diferença: "
                    + formatoMoeda.format(registro.getDiferenca()) + "\n\n"
                    + "Movimentações associadas: "
                    + registro.getItens().size() + "\n"
                    + "Movimentações bancárias pendentes: "
                    + bancos.size() + "\n"
                    + "Baixas financeiras pendentes: "
                    + fluxos.size();

            JOptionPane.showMessageDialog(
                    dialog,
                    relatorio,
                    "Relatório da conciliação",
                    JOptionPane.INFORMATION_MESSAGE
            );

            atualizarCards();
            dialog.dispose();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    dialog,
                    "Informe os dois saldos finais corretamente."
            );
        }
    });

    btnFechar.addActionListener(e -> dialog.dispose());

    JPanel painelBotoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 8, 5)
    );

    painelBotoes.setBackground(Color.WHITE);

    painelBotoes.add(btnAssociar);
    painelBotoes.add(btnDesassociar);
    painelBotoes.add(btnFechar);
    painelBotoes.add(btnFinalizar);

    JPanel painelInferior = new JPanel(new BorderLayout(0, 8));
    painelInferior.setBackground(Color.WHITE);
    painelInferior.setBorder(new EmptyBorder(5, 15, 15, 15));

    painelInferior.add(
            criarPainelTabelaConciliacao(
                    "Movimentações associadas",
                    tabelaAssociados
            ),
            BorderLayout.CENTER
    );

    painelInferior.add(painelBotoes, BorderLayout.SOUTH);

    dialog.add(cabecalho, BorderLayout.NORTH);
    dialog.add(comparacao, BorderLayout.CENTER);
    dialog.add(painelInferior, BorderLayout.SOUTH);

    dialog.setVisible(true);
}

private static class LinhaFluxo {

    private Contas conta;
    private Contas.Baixa baixa;

    public LinhaFluxo(Contas conta, Contas.Baixa baixa) {
        this.conta = conta;
        this.baixa = baixa;
    }
}


private JPanel criarPainelTabelaConciliacao(
        String titulo,
        JTable tabela) {

    JPanel painel = new JPanel(new BorderLayout(0, 8));

    painel.setBackground(Color.WHITE);

    painel.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDA),
                    new EmptyBorder(10, 10, 10, 10)
            )
    );

    JLabel label = new JLabel(titulo);

    label.setFont(new Font("SansSerif", Font.BOLD, 12));
    label.setForeground(TEXTO);

    JScrollPane scroll = new JScrollPane(tabela);

    painel.add(label, BorderLayout.NORTH);
    painel.add(scroll, BorderLayout.CENTER);

    return painel;
}


private void configurarTabelaConciliacao(JTable tabela) {

    tabela.setRowHeight(28);
    tabela.setFont(new Font("SansSerif", Font.PLAIN, 11));
    tabela.setForeground(TEXTO);
    tabela.setBackground(Color.WHITE);
    tabela.setGridColor(BORDA);

    tabela.setSelectionBackground(new Color(219, 234, 254));
    tabela.setSelectionForeground(TEXTO);

    tabela.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 11)
    );

    tabela.getTableHeader().setBackground(
            new Color(248, 250, 252)
    );

    tabela.getTableHeader().setForeground(CINZA);

    tabela.getTableHeader().setReorderingAllowed(false);
}


private String nomeContaConciliacao(
        Conciliacao.ContaBancaria conta) {

    return conta.getInstituicao().getNome()
            + " | Ag. " + conta.getAgencia()
            + " | Conta " + conta.getNumeroConta();
}


private double valorMovimentacaoNaConta(
        Conciliacao.MovimentacaoBancaria movimentacao,
        Conciliacao.ContaBancaria conta) {

    if (movimentacao.getTipo().equals(Conciliacao.TRANSFERENCIA)) {

        return movimentacao.getContaDestino() == conta
                ? movimentacao.getValor()
                : -movimentacao.getValor();
    }

    if (movimentacao.getTipo().equals(Conciliacao.ENTRADA)) {
        return movimentacao.getValor();
    }

    return -movimentacao.getValor();
}


private double valorFluxo(LinhaFluxo linha) {

    double valor = linha.baixa.getValorPago();

    if (linha.conta.getTipo().equals(Contas.PAGAR)) {
        return -valor;
    }

    return valor;
}


private double parseValorConciliacao(String texto) {

    String valor = texto.trim()
            .replace("R$", "")
            .replace(" ", "")
            .replace(".", "")
            .replace(",", ".");

    return Double.parseDouble(valor);
}

    private JPanel criarPainelSolicitacoes() {

        JPanel painel = new JPanel(new BorderLayout(10, 10));

        painel.setBackground(Color.WHITE);

        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(12, 15, 12, 15)
        ));

        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Resumo de solicitações");

        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(TEXTO);

        JPanel resumo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));

        resumo.setOpaque(false);

        JLabel pendentes = new JLabel("Pendentes: 0");
        JLabel aprovadas = new JLabel("Aprovadas: 0");
        JLabel rejeitadas = new JLabel("Rejeitadas: 0");

        pendentes.setForeground(CINZA);
        aprovadas.setForeground(CINZA);
        rejeitadas.setForeground(CINZA);

        resumo.add(pendentes);
        resumo.add(aprovadas);
        resumo.add(rejeitadas);

        JButton btnSolicitacoes = criarBotao("Abrir solicitações", false);

        btnSolicitacoes.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "A lista de solicitações ainda será inclementada",
                        "Solicitações",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(resumo, BorderLayout.CENTER);
        painel.add(btnSolicitacoes, BorderLayout.EAST);

        return painel;
    }// Aceita somente números e limita a quantidade de dígitos
private void aplicarFiltroNumerico(JTextField campo, int limite) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
        new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                    String texto, javax.swing.text.AttributeSet attrs)
                    throws BadLocationException {

                String atual = fb.getDocument().getText(
                    0, fb.getDocument().getLength()
                );

                String novo = atual.substring(0, offset)
                        + (texto == null ? "" : texto)
                        + atual.substring(offset + length);

                novo = novo.replaceAll("\\D", "");

                if (novo.length() > limite) {
                    novo = novo.substring(0, limite);
                }

                fb.replace(0, fb.getDocument().getLength(),
                        novo, attrs);
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                    String texto, javax.swing.text.AttributeSet attrs)
                    throws BadLocationException {
                replace(fb, offset, 0, texto, attrs);
            }
        }
    );
}

// Agência: insere automaticamente a barra após quatro dígitos
private void aplicarFiltroAgencia(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
        new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                    String texto, javax.swing.text.AttributeSet attrs)
                    throws BadLocationException {

                String atual = fb.getDocument().getText(
                    0, fb.getDocument().getLength()
                );

                String novo = atual.substring(0, offset)
                        + (texto == null ? "" : texto)
                        + atual.substring(offset + length);

                novo = novo.replaceAll("\\D", "");

                if (novo.length() > 5) {
                    novo = novo.substring(0, 5);
                }

                if (novo.length() > 4) {
                    novo = novo.substring(0, 4) + "/" + novo.substring(4);
                }

                fb.replace(0, fb.getDocument().getLength(), novo, attrs);
                campo.setCaretPosition(novo.length());
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                    String texto, javax.swing.text.AttributeSet attrs)
                    throws BadLocationException {
                replace(fb, offset, 0, texto, attrs);
            }
        }
    );
}

// Saldo: permite números e uma vírgula ou ponto, com até duas casas
private void aplicarFiltroSaldo(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
        new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                    String texto, javax.swing.text.AttributeSet attrs)
                    throws BadLocationException {

                String atual = fb.getDocument().getText(
                    0, fb.getDocument().getLength()
                );

                String novo = atual.substring(0, offset)
                        + (texto == null ? "" : texto)
                        + atual.substring(offset + length);

                novo = novo.replace(".", ",");

                if (novo.matches("\\d*(,\\d{0,2})?")) {
                    fb.replace(0, fb.getDocument().getLength(), novo, attrs);
                    campo.setCaretPosition(novo.length());
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                    String texto, javax.swing.text.AttributeSet attrs)
                    throws BadLocationException {
                replace(fb, offset, 0, texto, attrs);
            }
        }
    );
}

private String formatarCompe(String compe) {
    if (compe == null || compe.trim().isEmpty()) {
        return "";
    }

    return String.format("%03d", Integer.parseInt(compe));
}

private void configurarCampoSaldo(JTextField campo) {

    ((javax.swing.text.AbstractDocument) campo.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {

        @Override
        public void replace(FilterBypass fb, int offset, int length,
                String texto, javax.swing.text.AttributeSet attrs)
                throws javax.swing.text.BadLocationException {

            String atual = fb.getDocument().getText(
                    0, fb.getDocument().getLength());

            String novo = atual.substring(0, offset)
                    + (texto == null ? "" : texto)
                    + atual.substring(offset + length);

            novo = novo.replaceAll("[^0-9,]", "");

            int virgula = novo.indexOf(",");

            if (virgula >= 0) {
                novo = novo.substring(0, virgula + 1)
                        + novo.substring(virgula + 1).replace(",", "");

                if (novo.length() - virgula - 1 > 2) {
                    novo = novo.substring(0, virgula + 3);
                }
            }

            fb.replace(0, fb.getDocument().getLength(), novo, attrs);
        }

        @Override
        public void insertString(FilterBypass fb, int offset,
                String texto, javax.swing.text.AttributeSet attrs)
                throws javax.swing.text.BadLocationException {
            replace(fb, offset, 0, texto, attrs);
        }
    });

    campo.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            String valor = campo.getText().trim();

            if (!valor.isEmpty()) {
                double numero = Double.parseDouble(
                        valor.replace(",", "."));

                campo.setText(
                        String.format(java.util.Locale.US, "%.2f", numero)
                                .replace(".", ","));
            }
        }
    });
}

// Configura o campo para abrir o calendário ao clicar.
private void configurarCampoData(JTextField campo) {

    campo.setEditable(false);
    campo.setCursor(new java.awt.Cursor(
            java.awt.Cursor.HAND_CURSOR));
    campo.setBackground(Color.WHITE);
    campo.setHorizontalAlignment(
            javax.swing.SwingConstants.CENTER);

    campo.setToolTipText("Clique para escolher uma data");

    // Evita adicionar o mesmo evento mais de uma vez.
    if (Boolean.TRUE.equals(
            campo.getClientProperty("calendarioConfigurado"))) {
        return;
    }

    campo.putClientProperty("calendarioConfigurado", true);

    campo.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            mostrarCalendario(campo);
        }
    });
}


// Abre o calendário.
private void mostrarCalendario(JTextField campo) {

    java.time.LocalDate dataAtual;

    try {
        dataAtual = java.time.LocalDate.parse(
                campo.getText().trim(),
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    } catch (Exception e) {
        dataAtual = java.time.LocalDate.now();
    }

    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();

    popup.setBorder(javax.swing.BorderFactory.createLineBorder(
            new Color(226, 232, 240)));

    popup.setBackground(Color.WHITE);

    java.time.YearMonth mesAtual =
            java.time.YearMonth.from(dataAtual);

    popup.add(construirCalendario(campo, popup, mesAtual));

    popup.show(campo, 0, campo.getHeight() + 4);
}


// Monta o calendário com os dias do mês.
private JPanel construirCalendario(
        JTextField campo,
        javax.swing.JPopupMenu popup,
        java.time.YearMonth mesAtual) {

    JPanel calendario = new JPanel(new BorderLayout(5, 8));

    calendario.setBackground(Color.WHITE);
    calendario.setBorder(new EmptyBorder(10, 10, 10, 10));
    calendario.setPreferredSize(new Dimension(270, 270));

    // Cabeçalho com navegação entre meses.
    JPanel cabecalho = new JPanel(new BorderLayout());
    cabecalho.setOpaque(false);

    JButton anterior = new JButton("‹");
    JButton proximo = new JButton("›");

    java.time.format.DateTimeFormatter formatoMes =
            java.time.format.DateTimeFormatter.ofPattern(
                    "MMMM yyyy",
                    new java.util.Locale("pt", "BR"));

    String nomeMes = mesAtual.format(formatoMes);
    nomeMes = nomeMes.substring(0, 1).toUpperCase()
            + nomeMes.substring(1);

    JLabel tituloMes = new JLabel(
            nomeMes,
            javax.swing.SwingConstants.CENTER);

    tituloMes.setFont(new Font("SansSerif", Font.BOLD, 13));
    tituloMes.setForeground(new Color(30, 38, 52));

    configurarBotaoCalendario(anterior);
    configurarBotaoCalendario(proximo);

    anterior.addActionListener(e -> {
        popup.removeAll();
        popup.add(construirCalendario(
                campo, popup, mesAtual.minusMonths(1)));
        popup.revalidate();
        popup.repaint();
    });

    proximo.addActionListener(e -> {
        popup.removeAll();
        popup.add(construirCalendario(
                campo, popup, mesAtual.plusMonths(1)));
        popup.revalidate();
        popup.repaint();
    });

    cabecalho.add(anterior, BorderLayout.WEST);
    cabecalho.add(tituloMes, BorderLayout.CENTER);
    cabecalho.add(proximo, BorderLayout.EAST);

    calendario.add(cabecalho, BorderLayout.NORTH);

    // Grade dos dias da semana e dos dias do mês.
    JPanel grade = new JPanel(new GridLayout(7, 7, 3, 3));
    grade.setOpaque(false);

    String[] diasSemana = {
        "D", "S", "T", "Q", "Q", "S", "S"
    };

    for (String dia : diasSemana) {
        JLabel label = new JLabel(
                dia, javax.swing.SwingConstants.CENTER);

        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(105, 114, 128));

        grade.add(label);
    }

    java.time.LocalDate primeiroDia = mesAtual.atDay(1);

    // Domingo será a primeira coluna.
    int espacosAntes =
            primeiroDia.getDayOfWeek().getValue() % 7;

    for (int i = 0; i < espacosAntes; i++) {
        grade.add(new JLabel(""));
    }

    java.time.LocalDate hoje = java.time.LocalDate.now();

    java.time.LocalDate selecionada;

    try {
        selecionada = java.time.LocalDate.parse(
                campo.getText().trim(),
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (Exception e) {
        selecionada = hoje;
    }

    for (int dia = 1; dia <= mesAtual.lengthOfMonth(); dia++) {

        final java.time.LocalDate data =
                mesAtual.atDay(dia);

        JButton botaoDia = new JButton(String.valueOf(dia));

        botaoDia.setFont(new Font("SansSerif", Font.PLAIN, 11));
        botaoDia.setFocusPainted(false);
        botaoDia.setBorderPainted(false);
        botaoDia.setMargin(new java.awt.Insets(2, 2, 2, 2));
        botaoDia.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        botaoDia.setBackground(Color.WHITE);
        botaoDia.setForeground(new Color(30, 38, 52));

        if (data.equals(selecionada)) {
            botaoDia.setBackground(new Color(37, 99, 235));
            botaoDia.setForeground(Color.WHITE);
        } else if (data.equals(hoje)) {
            botaoDia.setBackground(new Color(219, 234, 254));
        }

        botaoDia.addActionListener(e -> {
            campo.setText(data.format(
                    java.time.format.DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy")));
            popup.setVisible(false);
        });

        grade.add(botaoDia);
    }

    calendario.add(grade, BorderLayout.CENTER);

    return calendario;
}


// Estiliza os botões de navegação do calendário.
private void configurarBotaoCalendario(JButton botao) {

    botao.setFont(new Font("SansSerif", Font.BOLD, 18));
    botao.setForeground(new Color(30, 38, 52));
    botao.setBackground(Color.WHITE);
    botao.setFocusPainted(false);
    botao.setBorderPainted(false);
    botao.setCursor(new java.awt.Cursor(
            java.awt.Cursor.HAND_CURSOR));
}

}
