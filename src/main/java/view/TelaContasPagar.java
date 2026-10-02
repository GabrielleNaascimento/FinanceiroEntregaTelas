package view;

import model.Contas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TelaContasPagar extends JPanel {

    private static final long serialVersionUID = 1L;

    // mesmas cores da TelaConciliacaoBancaria
    private static final Color COR_FUNDO = new Color(245, 247, 250);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO = new Color(30, 38, 52);
    private static final Color COR_AZUL = new Color(37, 99, 235);
    private static final Color COR_VERDE = new Color(16, 185, 129);
    private static final Color COR_VERMELHO = new Color(239, 68, 68);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private static final int ALTURA_LINHA = 30;
    private static final int ALTURA_CABECALHO = 30;
    private static final int ESPACO = 15;

    public TelaContasPagar() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
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

        JLabel titulo = new JLabel("<html><h2 style='margin:0;'>Contas a Pagar</h2>"
                + "<span style='color:gray;'>Acompanhe seus vencimentos e as contas já pagas</span></html>");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        JPanel cards = criarCards();
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(ESPACO));

        // so a primeira tem botao de pagar
        adicionarTabela(conteudo, "A Pagar", false, true);
        adicionarTabela(conteudo, "Pagas", true, false);

        return conteudo;
    }

    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        painel.add(cardMetrica("Total a pagar", formatarValor(Contas.getTotalPagar()),
                "Pendentes e vencidas", COR_VERMELHO));
        painel.add(cardMetrica("Total pago", formatarValor(Contas.getTotalPago()),
                "Contas já quitadas", COR_VERDE));
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

    private void adicionarTabela(JPanel conteudo, String titulo, boolean concluidas, boolean comBotao) {
        ArrayList<Contas> dados = Contas.listar(Contas.PAGAR, concluidas);

        String[] colunas = comBotao
                ? new String[]{"ID", "Fornecedor", "Descrição", "Vencimento", "Valor", "Status", "Ação"}
                : new String[]{"ID", "Fornecedor", "Descrição", "Vencimento", "Valor", "Status"};

        // so a coluna do botao é editavel, o resto é so leitura
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return comBotao && column == getColumnCount() - 1;
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
            if (comBotao) {
                linha[6] = "Pagar";
            }
            modelo.addRow(linha);
        }

        JPanel painel = criarPainelTabela(titulo, dados.size());

        if (dados.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma conta nesta lista", SwingConstants.CENTER);
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(Color.GRAY);
            vazio.setBorder(new EmptyBorder(25, 0, 25, 0));
            painel.add(vazio, BorderLayout.CENTER);
        } else {
            JTable tabela = new JTable(modelo);
            configurarTabela(tabela, comBotao);

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

    private void configurarTabela(JTable tabela, boolean comBotao) {
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
        tabela.getColumnModel().getColumn(4).setCellRenderer(new CelulaRenderer(SwingConstants.CENTER, COR_VERMELHO));
        tabela.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(120);

        if (comBotao) {
            tabela.getColumnModel().getColumn(6).setPreferredWidth(110);
            tabela.getColumnModel().getColumn(6).setCellRenderer(new BotaoRenderer("Pagar"));
            tabela.getColumnModel().getColumn(6).setCellEditor(new BotaoEditor("Pagar"));
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

    // botao pagar: azul arredondado, ocupa quase toda a celula (com margem)

    private static class BotaoAcao extends JButton {
        private static final long serialVersionUID = 1L;

        BotaoAcao(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 10));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(null);
            setContentAreaFilled(false);
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        // pinto o fundo na mao (o L&F as vezes ignora o background), com margem e canto arredondado
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? COR_AZUL.darker() : COR_AZUL);
            g2.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // o renderer só mostra o botao, quem faz o clique funcionar é o editor
    private static class BotaoRenderer implements TableCellRenderer {
        private final BotaoAcao botao;

        BotaoRenderer(String texto) {
            botao = new BotaoAcao(texto);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            return botao;
        }
    }

    private class BotaoEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;

        private final BotaoAcao botao;
        private JTable tabela;

        BotaoEditor(String texto) {
            botao = new BotaoAcao(texto);

            botao.addActionListener(e -> {
                int row = tabela.getEditingRow();
                if (row < 0) {
                    return;
                }

                // pega o id da linha antes de encerrar a edicao
                int id = (int) tabela.getValueAt(row, 0);
                fireEditingStopped();

                // invokeLater pq a tela é reconstruida e a tabela atual some
                SwingUtilities.invokeLater(() -> {
                    if (Contas.marcarComoConcluida(id)) {
                        atualizarTela();
                    } else {
                        JOptionPane.showMessageDialog(TelaContasPagar.this,
                                "Saldo insuficiente para pagar esta conta.",
                                "Saldo insuficiente", JOptionPane.WARNING_MESSAGE);
                    }
                });
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            this.tabela = table;
            return botao;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }


    private String formatarValor(double valor) {
        return FORMATO_MOEDA.format(valor);
    }

    private String formatarData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }
}