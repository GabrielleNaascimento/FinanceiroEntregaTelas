
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import model.Conciliacao;

public class TelaConciliacaoBancaria extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JButton btnEditar;
        private JButton btnExcluir;
        private JButton btnSelecionarTodos;
        private JButton btnCancelarSelecao;

        private boolean modoSelecaoEdicao = false;
        private boolean modoSelecaoExclusao = false;

    private JLabel lblQuantidadeBancos;
    private JLabel lblSaldoTotal;

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

        atualizarTabela();
        atualizarCards();
    }

    // =====================================================
    // ÁREA PRINCIPAL
    // =====================================================

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
                "Cadastre bancos e acompanhe os saldos das contas bancárias"
        );
        
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        subtitulo.setForeground(new Color(105, 105, 105));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        conteudo.add(titulo);
        conteudo.add(subtitulo);
        conteudo.add(Box.createVerticalStrut(22));
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(18));
        conteudo.add(criarAcoes());
        conteudo.add(Box.createVerticalStrut(15));
        conteudo.add(criarTabela());

        return conteudo;
    }

    // =====================================================
    // CARDS
    // =====================================================

    private JPanel criarCards() {

        JPanel painel = new JPanel(new GridLayout(1, 2, 15, 0));

        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));

        JPanel cardQuantidade = criarCard("Bancos cadastrados");
        JPanel cardSaldo = criarCard("Saldo total");

        lblQuantidadeBancos = new JLabel("0");
        lblSaldoTotal = new JLabel("R$ 0,00");

        configurarValorCard(lblQuantidadeBancos);
        configurarValorCard(lblSaldoTotal);

        cardQuantidade.add(lblQuantidadeBancos);
        cardSaldo.add(lblSaldoTotal);

        painel.add(cardQuantidade);
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

        ArrayList<Conciliacao> bancos = Conciliacao.getBancos();

        lblQuantidadeBancos.setText(String.valueOf(bancos.size()));
        lblSaldoTotal.setText(
                formatoMoeda.format(Conciliacao.getSaldoTotal())
        );
    }

    // =====================================================
    // AÇÕES
    // =====================================================


private JPanel criarAcoes() {

    JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

    painel.setOpaque(false);
    painel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JButton btnCadastrar = criarBotao("Cadastrar banco", true);
    JButton btnConciliacao = criarBotao("Conciliação", false);

    // Utiliza os atributos da classe, sem declarar outros botões
    btnEditar = criarBotao("Editar", false);
    btnExcluir = criarBotao("Excluir", false);
    btnSelecionarTodos = criarBotao("Selecionar tudo", false);
    btnSelecionarTodos.setPreferredSize(new Dimension(110, 34));
    btnCancelarSelecao = criarBotao("Cancelar", false);

    btnSelecionarTodos.setVisible(false);
    btnCancelarSelecao.setVisible(false);

    btnCadastrar.addActionListener(e -> cadastrarBanco());
    btnConciliacao.addActionListener(e -> abrirConciliacao());
    btnEditar.addActionListener(e -> editarBanco());
    btnExcluir.addActionListener(e -> excluirBanco());

    btnSelecionarTodos.addActionListener(e -> tabela.selectAll());
    btnCancelarSelecao.addActionListener(e -> cancelarSelecao());

    painel.add(btnCadastrar);
    painel.add(btnEditar);
    painel.add(btnExcluir);
    painel.add(btnConciliacao);
    painel.add(btnSelecionarTodos);
    painel.add(btnCancelarSelecao);

    return painel;
}


    private JButton criarBotao(String texto, boolean principal) {

        JButton botao = new JButton(texto);

        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setPreferredSize(new Dimension(
                texto.equals("Cadastrar banco") ? 130 : 95,
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

    // =====================================================
    // TABELA
    // =====================================================

    private JPanel criarTabela() {

        JPanel painel = new JPanel(new BorderLayout());

        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                new EmptyBorder(12, 12, 12, 12)
        ));

        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setPreferredSize(new Dimension(0, 360));
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel tituloTabela = new JLabel("Bancos cadastrados");
        tituloTabela.setFont(new Font("SansSerif", Font.BOLD, 13));
        tituloTabela.setForeground(TEXTO);

        painel.add(tituloTabela, BorderLayout.NORTH);

        String[] colunas = {
            "Código",
            "Agência",
            "Conta",
            "Saldo"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.getTableHeader().setResizingAllowed(false);
        tabela.setRowHeight(32);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabela.setForeground(TEXTO);
        tabela.setBackground(Color.WHITE);
        tabela.setGridColor(BORDA);
        tabela.setShowGrid(true);
        tabela.setIntercellSpacing(new Dimension(1, 1));
        tabela.setSelectionBackground(new Color(219, 234, 254));
        tabela.setSelectionForeground(TEXTO);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(true);

        tabela.getTableHeader().setPreferredSize(new Dimension(0, 31));
        tabela.getTableHeader().setBackground(new Color(248, 250, 252));
        tabela.getTableHeader().setForeground(CINZA);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 11));
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setBorder(
                BorderFactory.createLineBorder(BORDA)
        );

        DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
        centralizar.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centralizar);
        }

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        for (Conciliacao banco : Conciliacao.getBancos()) {

            modeloTabela.addRow(new Object[]{
                banco.getCodigoBanco(),
                banco.getAgencia(),
                banco.getNumeroConta(),
                formatoMoeda.format(banco.getSaldo())
            });
        }
    }

    // =====================================================
    // FORMULÁRIO PERSONALIZADO
    // =====================================================

    private JPanel criarFormulario(
            JTextField txtCodigo,
            JTextField txtAgencia,
            JTextField txtConta,
            JTextField txtSaldo) {

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(10, 5, 5, 5));
        formulario.setPreferredSize(new Dimension(350, 330));

        JLabel titulo = new JLabel("Informações bancárias");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setForeground(TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        formulario.add(titulo);
        formulario.add(Box.createVerticalStrut(18));

        adicionarCampo(formulario, "Código do banco (COMPE)", txtCodigo);
        adicionarCampo(formulario, "Agência", txtAgencia);
        adicionarCampo(formulario, "Número da conta", txtConta);
        adicionarCampo(formulario, "Saldo inicial", txtSaldo);

        return formulario;
    }

    private void adicionarCampo(
            JPanel formulario,
            String titulo,
            JTextField campo) {

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
        formulario.add(Box.createVerticalStrut(12));
    }

    // =====================================================
    // MÁSCARAS
    // =====================================================

    
private void limitarDigitos(JTextField campo, int limite) {

    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
            new DocumentFilter() {

        @Override
        public void insertString(
                FilterBypass fb,
                int offset,
                String texto,
                AttributeSet attr) throws BadLocationException {

            substituir(fb, offset, 0, texto, attr);
        }

        @Override
        public void replace(
                FilterBypass fb,
                int offset,
                int tamanho,
                String texto,
                AttributeSet attr) throws BadLocationException {

            substituir(fb, offset, tamanho, texto, attr);
        }

        private void substituir(
                FilterBypass fb,
                int offset,
                int tamanho,
                String texto,
                AttributeSet attr) throws BadLocationException {

            String atual = fb.getDocument().getText(
                    0, fb.getDocument().getLength()
            );

            String novo = atual.substring(0, offset)
                    + (texto == null ? "" : texto)
                    + atual.substring(offset + tamanho);

            if (novo.matches("\\d{0," + limite + "}")) {
                fb.replace(offset, tamanho, texto, attr);
            }
        }
    });
}

private String completarCodigoBanco(JTextField campo) {

    String codigo = campo.getText().trim();

    if (!codigo.isEmpty()) {
        codigo = String.format("%03d", Integer.parseInt(codigo));
        campo.setText(codigo);
    }

    return codigo;
}

private void configurarEnter(
        JTextField txtCodigo,
        JTextField txtAgencia,
        JTextField txtConta,
        JTextField txtSaldo) {

    txtCodigo.addActionListener(e -> {
        completarCodigoBanco(txtCodigo);
        txtAgencia.requestFocusInWindow();
    });

    txtAgencia.addActionListener(e ->
        txtConta.requestFocusInWindow()
    );

    txtConta.addActionListener(e ->
        txtSaldo.requestFocusInWindow()
    );

    txtSaldo.addActionListener(e -> {
        javax.swing.JRootPane raiz =
                javax.swing.SwingUtilities.getRootPane(txtSaldo);

        if (raiz != null && raiz.getDefaultButton() != null) {
            raiz.getDefaultButton().doClick();
        }
    });
}

    private void configurarAgencia(JTextField campo) {

        ((AbstractDocument) campo.getDocument()).setDocumentFilter(
                new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String texto,
                    AttributeSet attr) throws BadLocationException {

                substituir(fb, offset, 0, texto, attr);
            }

            @Override
            public void replace(
                    FilterBypass fb,
                    int offset,
                    int tamanho,
                    String texto,
                    AttributeSet attr) throws BadLocationException {

                substituir(fb, offset, tamanho, texto, attr);
            }

            private void substituir(
                    FilterBypass fb,
                    int offset,
                    int tamanho,
                    String texto,
                    AttributeSet attr) throws BadLocationException {

                String atual = fb.getDocument().getText(
                        0, fb.getDocument().getLength()
                );

                String novo = atual.substring(0, offset)
                        + (texto == null ? "" : texto)
                        + atual.substring(offset + tamanho);

                String digitos = novo.replaceAll("\\D", "");

                if (digitos.length() > 5) {
                    return;
                }

                String formatado = digitos;

                if (digitos.length() > 4) {
                    formatado = digitos.substring(0, 4)
                            + "/" + digitos.substring(4);
                }

                fb.replace(0, fb.getDocument().getLength(),
                        formatado, attr);
            }
        });
    }
        
    private void configurarSaldo(JTextField campo) {

    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
            new DocumentFilter() {

        @Override
        public void insertString(
                FilterBypass fb,
                int offset,
                String texto,
                AttributeSet attr) throws BadLocationException {

            replace(fb, offset, 0, texto, attr);
        }

        @Override
        public void replace(
                FilterBypass fb,
                int offset,
                int tamanho,
                String texto,
                AttributeSet attr) throws BadLocationException {

            String atual = fb.getDocument().getText(
                    0, fb.getDocument().getLength()
            );

            String novo = atual.substring(0, offset)
                    + (texto == null ? "" : texto)
                    + atual.substring(offset + tamanho);

            if (novo.matches("-?[0-9.,]*")) {
                fb.replace(offset, tamanho, texto, attr);
            }
        }
    });
}

private void iniciarSelecao(boolean multipla) {

    modoSelecaoEdicao = !multipla;
    modoSelecaoExclusao = multipla;

    tabela.clearSelection();

    if (multipla) {

        tabela.setSelectionMode(
                javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        btnExcluir.setText("Confirmar exclusão");
        btnEditar.setEnabled(false);

        btnSelecionarTodos.setVisible(true);

    } else {

        tabela.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        btnEditar.setText("Confirmar edição");
        btnExcluir.setEnabled(false);

        btnSelecionarTodos.setVisible(false);
    }

    btnCancelarSelecao.setVisible(true);

    btnSelecionarTodos.getParent().revalidate();
    btnSelecionarTodos.getParent().repaint();
}


private void cancelarSelecao() {

    modoSelecaoEdicao = false;
    modoSelecaoExclusao = false;

    tabela.clearSelection();

    tabela.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
    );

    btnEditar.setText("Editar");
    btnExcluir.setText("Excluir");

    btnEditar.setEnabled(true);
    btnExcluir.setEnabled(true);

    btnSelecionarTodos.setVisible(false);
    btnCancelarSelecao.setVisible(false);

    btnEditar.setPreferredSize(new Dimension(95, 34));
    btnExcluir.setPreferredSize(new Dimension(95, 34));

    btnSelecionarTodos.getParent().revalidate();
    btnSelecionarTodos.getParent().repaint();
}
    // =====================================================
    // CADASTRAR BANCO
    // =====================================================

    private void cadastrarBanco() {

        JTextField txtCodigo = new JTextField();
        JTextField txtAgencia = new JTextField();
        JTextField txtConta = new JTextField();
        JTextField txtSaldo = new JTextField();

        limitarDigitos(txtCodigo, 3);
        configurarAgencia(txtAgencia);
        configurarSaldo(txtSaldo);
        configurarEnter(txtCodigo, txtAgencia, txtConta, txtSaldo);
        

        JPanel formulario = criarFormulario(
                txtCodigo, txtAgencia, txtConta, txtSaldo
        );

        int opcao = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Cadastrar banco",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String codigo = completarCodigoBanco(txtCodigo);
            String agencia = txtAgencia.getText().trim();
            String conta = txtConta.getText().trim();
            double saldo = converterSaldo(txtSaldo.getText());

            if (codigo.length() != 3
                    || !agencia.matches("\\d{4}/\\d")
                    || conta.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Confira o código, a agência e a conta.",
                        "Dados inválidos",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Conciliacao banco = new Conciliacao(
                    codigo, agencia, conta, saldo
            );

            Conciliacao.cadastrarBanco(banco);

            atualizarTabela();
            atualizarCards();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe um saldo válido.",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =====================================================
    // EDITAR BANCO
    // =====================================================

    private void editarBanco() {

    if (!modoSelecaoEdicao) {
        iniciarSelecao(false);
        return;
    }

    int linha = tabela.getSelectedRow();

    if (linha == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Selecione um banco na tabela.",
                "Nenhum banco selecionado",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int indice = tabela.convertRowIndexToModel(linha);

    cancelarSelecao();

    Conciliacao bancoAtual = Conciliacao.getBancos().get(indice);

        JTextField txtCodigo = new JTextField(bancoAtual.getCodigoBanco());
        JTextField txtAgencia = new JTextField(bancoAtual.getAgencia());
        JTextField txtConta = new JTextField(bancoAtual.getNumeroConta());
        JTextField txtSaldo = new JTextField(
                String.valueOf(bancoAtual.getSaldo()).replace(".", ",")
        );

        limitarDigitos(txtCodigo, 3);
        configurarAgencia(txtAgencia);
        configurarSaldo(txtSaldo);

        JPanel formulario = criarFormulario(
                txtCodigo, txtAgencia, txtConta, txtSaldo
        );

        int opcao = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Editar banco",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String codigo = completarCodigoBanco(txtCodigo);
            String agencia = txtAgencia.getText().trim();
            String conta = txtConta.getText().trim();
            double saldo = converterSaldo(txtSaldo.getText());

            if (codigo.length() != 3
                    || !agencia.matches("\\d{4}/\\d")
                    || conta.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Confira o código, a agência e a conta.",
                        "Dados inválidos",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Conciliacao bancoAtualizado = new Conciliacao(
                    codigo, agencia, conta, saldo
            );

            Conciliacao.editarBanco(indice, bancoAtualizado);

            atualizarTabela();
            atualizarCards();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe um saldo válido.",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =====================================================
    // EXCLUIR BANCO
    // =====================================================

   private void excluirBanco() {

    if (!modoSelecaoExclusao) {
        iniciarSelecao(true);
        return;
    }

    int[] linhas = tabela.getSelectedRows();

    if (linhas.length == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Selecione pelo menos um banco.",
                "Nenhum banco selecionado",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir os bancos selecionados?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );

    if (confirmacao == JOptionPane.YES_OPTION) {

        ArrayList<Integer> indices = new ArrayList<>();

        for (int linha : linhas) {
            indices.add(tabela.convertRowIndexToModel(linha));
        }

        indices.sort(java.util.Collections.reverseOrder());

        for (int indice : indices) {
            Conciliacao.excluirBanco(indice);
        }

        atualizarTabela();
        atualizarCards();
    }

    cancelarSelecao();
}
    // =====================================================
    // CONCILIAÇÃO BANCÁRIA
    // =====================================================

    private void abrirConciliacao() {

        if (Conciliacao.getBancos().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cadastre um banco antes de realizar a conciliação.",
                    "Conciliação bancária",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Ainda não há movimentações bancárias para conciliar.",
                "Conciliação bancária",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // CONVERSÃO DE SALDO
    // =====================================================

    private double converterSaldo(String valor) {

        valor = valor.trim()
                .replace("R$", "")
                .replace(" ", "");

        if (valor.contains(",")) {
            valor = valor.replace(".", "")
                    .replace(",", ".");
        }

        return Double.parseDouble(valor);
    }
    
}
