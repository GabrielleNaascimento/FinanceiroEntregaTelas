package view;

import model.Contas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TelaContas extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_TEXTO = new Color(35, 45, 60);
    private static final Color COR_TEXTO_SECUNDARIO = new Color(110, 120, 135);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_AZUL = new Color(37, 99, 235);
    private static final Color COR_VERDE = new Color(16, 185, 129);
    private static final Color COR_VERMELHO = new Color(239, 68, 68);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaContas() {
        setLayout(new BorderLayout());

        add(criarAreaPrincipal(), BorderLayout.CENTER);
    }

    private JPanel criarAreaPrincipal() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(COR_FUNDO);
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
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Contas a Pagar e Receber</h2>"
                + "<span style='color:gray;'>Gerencie suas contas, vencimentos e recebimentos</span></html>");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(15));

        JPanel cards = criarCards();
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(15));

        ArrayList<Contas> contas = Contas.getContas();

        JPanel tabelaPagar = criarTabelaPagar(contas);
        tabelaPagar.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(tabelaPagar);
        conteudo.add(Box.createVerticalStrut(15));

        JPanel tabelaReceber = criarTabela("RECEBER", contas);
        tabelaReceber.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(tabelaReceber);
        conteudo.add(Box.createVerticalStrut(15));

        JPanel tabelaPagas = criarTabela("PAGAS", contas);
        tabelaPagas.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(tabelaPagas);
        conteudo.add(Box.createVerticalStrut(15));

        JPanel tabelaRecebidas = criarTabela("RECEBIDAS", contas);
        tabelaRecebidas.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(tabelaRecebidas);

        return conteudo;
    }

    private JPanel criarCards() {
        ArrayList<Contas> contas = Contas.getContas();

        double saldoAtual = 50000.00;
        double totalPagar = 0;
        double totalReceber = 0;

        for (Contas conta : contas) {
            if (conta.getTipo().equals("PAGAR")) {
                if (conta.getStatus().equals("Paga")) {
                    saldoAtual -= conta.getValor();
                } else {
                    totalPagar += conta.getValor();
                }
            }

            if (conta.getTipo().equals("RECEBER")) {
                if (conta.getStatus().equals("Recebida")) {
                    saldoAtual += conta.getValor();
                } else {
                    totalReceber += conta.getValor();
                }
            }
        }

        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        painel.add(cardMetrica("Saldo atual", formatarValor(saldoAtual), "+4.2% vs. mês anterior", COR_AZUL));
        painel.add(cardMetrica("Contas a pagar", formatarValor(totalPagar), "-2.1% vs. mês anterior", COR_VERMELHO));
        painel.add(cardMetrica("Contas a receber", formatarValor(totalReceber), "+8.5% vs. mês anterior", COR_VERDE));

        return painel;
    }

    private JPanel cardMetrica(String titulo, String valor, String variacao, Color corVariacao) {
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

        JLabel var = new JLabel(variacao);
        var.setFont(new Font("SansSerif", Font.BOLD, 11));
        var.setForeground(corVariacao);

        card.add(t);
        card.add(v);
        card.add(var);

        return card;
    }

    private JPanel criarTabelaPagar(ArrayList<Contas> dados) {
        JPanel painel = criarPainelTabela("Contas a Pagar");

        String[] colunas = {"ID", "Fornecedor", "Descrição", "Vencimento", "Valor (R$)", "Status", "Ação"};
        DefaultTableModel modelo = criarModelo(colunas);

        for (Contas conta : dados) {
            if (!conta.getTipo().equals("PAGAR") || conta.getStatus().equals("Paga")) {
                continue;
            }

            modelo.addRow(new Object[]{
                    conta.getId(),
                    conta.getEntidade(),
                    conta.getDescricao(),
                    formatarData(conta.getDataVencimento()),
                    formatarValor(conta.getValor()),
                    conta.getStatus(),
                    "Pagar"
            });
        }

        JTable tabela = new JTable(modelo);
        configurarTabela(tabela);

        tabela.getColumnModel().getColumn(6).setCellRenderer(new BotaoRenderer("Pagar"));
        tabela.getColumnModel().getColumn(6).setCellEditor(new BotaoEditor(new JCheckBox(), "Pagar"));
        tabela.setRowHeight(36);

        JScrollPane scroll = new JScrollPane(tabela);
        configurarScroll(scroll);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarTabela(String tipo, ArrayList<Contas> dados) {
        String tituloTabela;
        String entidade;

        if (tipo.equals("PAGAS")) {
            tituloTabela = "Contas Pagas";
            entidade = "Fornecedor";
        } else if (tipo.equals("RECEBIDAS")) {
            tituloTabela = "Contas Recebidas";
            entidade = "Cliente";
        } else {
            tituloTabela = "Contas a Receber";
            entidade = "Cliente";
        }

        JPanel painel = criarPainelTabela(tituloTabela);

        String[] colunas = tipo.equals("RECEBER")
                ? new String[]{"ID", entidade, "Descrição", "Vencimento", "Valor (R$)", "Status", "Ação"}
                : new String[]{"ID", entidade, "Descrição", "Vencimento", "Valor (R$)", "Status"};

        DefaultTableModel modelo = criarModelo(colunas);

        for (Contas conta : dados) {
            boolean adicionar;

            if (tipo.equals("PAGAS")) {
                adicionar = conta.getTipo().equals("PAGAR") && conta.getStatus().equals("Paga");
            } else if (tipo.equals("RECEBIDAS")) {
                adicionar = conta.getTipo().equals("RECEBER") && conta.getStatus().equals("Recebida");
            } else {
                adicionar = conta.getTipo().equals("RECEBER") && !conta.getStatus().equals("Recebida");
            }

            if (!adicionar) {
                continue;
            }

            if (tipo.equals("RECEBER")) {
                modelo.addRow(new Object[]{
                        conta.getId(),
                        conta.getEntidade(),
                        conta.getDescricao(),
                        formatarData(conta.getDataVencimento()),
                        formatarValor(conta.getValor()),
                        conta.getStatus(),
                        "Receber"
                });
            } else {
                modelo.addRow(new Object[]{
                        conta.getId(),
                        conta.getEntidade(),
                        conta.getDescricao(),
                        formatarData(conta.getDataVencimento()),
                        formatarValor(conta.getValor()),
                        conta.getStatus()
                });
            }
        }

        JTable tabela = new JTable(modelo);
        configurarTabela(tabela);

        if (tipo.equals("RECEBER")) {
            tabela.getColumnModel().getColumn(6).setCellRenderer(new BotaoRenderer("Receber"));
            tabela.getColumnModel().getColumn(6).setCellEditor(new BotaoEditor(new JCheckBox(), "Receber"));
        }

        JScrollPane scroll = new JScrollPane(tabela);
        configurarScroll(scroll);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelTabela(String titulo) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelTitulo.setForeground(COR_TEXTO);
        labelTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));

        painel.add(labelTitulo, BorderLayout.NORTH);

        return painel;
    }

    private DefaultTableModel criarModelo(String[] colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return colunas[colunas.length - 1].equals("Ação") && column == colunas.length - 1;
            }
        };
    }

    private void configurarScroll(JScrollPane scroll) {
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(Color.WHITE);
    }

    private void configurarTabela(JTable tabela) {
        tabela.setRowHeight(32);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tabela.setForeground(COR_TEXTO);
        tabela.setBackground(Color.WHITE);
        tabela.setShowGrid(false);
        tabela.setRowSelectionAllowed(false);
        tabela.setColumnSelectionAllowed(false);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setResizingAllowed(false);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
        tabela.getTableHeader().setForeground(COR_TEXTO_SECUNDARIO);
        tabela.getTableHeader().setBackground(new Color(248, 249, 251));
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 32));

        DefaultTableCellRenderer valorRenderer = new DefaultTableCellRenderer();
        valorRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tabela.getColumnModel().getColumn(4).setCellRenderer(valorRenderer);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
    }

    private class StatusRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String status = value.toString();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            if (status.equals("Paga") || status.equals("Recebida")) {
                label.setForeground(new Color(6, 95, 70));
                label.setBackground(new Color(209, 250, 229));
            } else if (status.equals("Pendente")) {
                label.setForeground(new Color(146, 64, 14));
                label.setBackground(new Color(254, 243, 199));
            } else {
                label.setForeground(new Color(153, 27, 27));
                label.setBackground(new Color(254, 226, 226));
            }

            return label;
        }
    }

    private class BotaoRenderer extends JButton implements javax.swing.table.TableCellRenderer {

        public BotaoRenderer(String texto) {
            setFocusPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 10));
            setForeground(COR_AZUL);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(COR_BORDA));
            setText(texto);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    private class BotaoEditor extends DefaultCellEditor {

        private final JButton button;
        private JTable tabela;
        private final String tipoAcao;

        public BotaoEditor(JCheckBox checkBox, String tipoAcao) {
            super(checkBox);
            this.tipoAcao = tipoAcao;

            button = new JButton(tipoAcao);
            button.setFocusPainted(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 10));
            button.setForeground(COR_AZUL);
            button.setBackground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(COR_BORDA));
            button.addActionListener(this::realizarAcao);
        }

        private void realizarAcao(ActionEvent e) {
            int row = tabela.getEditingRow();
            if (row < 0) {
                return;
            }

            int id = (int) tabela.getValueAt(row, 0);
            ArrayList<Contas> contas = Contas.getContas();

            for (Contas conta : contas) {
                if (conta.getId() == id) {
                    if (tipoAcao.equals("Pagar")) {
                        conta.setStatus("Paga");
                    }
                    if (tipoAcao.equals("Receber")) {
                        conta.setStatus("Recebida");
                    }
                    break;
                }
            }

            fireEditingStopped();
            atualizarTela();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            this.tabela = table;
            button.setText(value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return tipoAcao;
        }
    }

    private void atualizarTela() {
        removeAll();
        add(criarAreaPrincipal(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private String formatarValor(double valor) {
        return String.format("R$ %.2f", valor);
    }

    private String formatarData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }
}