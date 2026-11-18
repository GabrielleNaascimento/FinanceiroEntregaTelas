package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import model.Fiscal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import java.awt.event.HierarchyEvent;
import model.Contas;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TelaFiscal extends JPanel {

    private static final long serialVersionUID = 1L;

    public TelaFiscal() {
        setLayout(new BorderLayout());

        // agora só o centro, a sidebar é a do TelaDashboard
        atualizarTela();
        // Atualiza os dados ao voltar para a tela, inclusive pagamentos feitos em Contas a Pagar.
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

    // painel central
    private JPanel criarAreaPrincipal() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(new Color(245, 247, 250));

        // cabeçalho ou header pros intimos
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false); //deixa transparente
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblUser = new JLabel("Jefferson - Administrador");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));

        header.add(lblUser, BorderLayout.EAST);
        area.add(header, BorderLayout.NORTH);

        // Painel Interno Vertical
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel lblTitulo = new JLabel("<html><h2 style='margin:0;'>Gestão Fiscal e Tributária</h2><span style='color:gray;'>Apuração de impostos federais, estaduais e municipais</span></html>");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(lblTitulo);
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' de card
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' dos graficos e da despesa
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' das tabelas e o alerta
        conteudo.add(criarTabelaEAlertas());

        area.add(conteudo, BorderLayout.CENTER);
        return area;
    }

    // cards de cima
    private JPanel criarCards() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        double total = 0;
        double pagos = 0;
        double pendentes = 0;
        for (Fiscal imposto : Fiscal.listarImpostos()) {
            total += imposto.getValor();
            if ("Pago".equals(imposto.getStatusAtual())) {
                pagos += imposto.getValor();
            } else {
                pendentes += imposto.getValor();
            }
        }
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        painel.add(cardMetrica("Total Impostos", moeda.format(total), new Color(34, 99, 212)));
        painel.add(cardMetrica("Pagos", moeda.format(pagos), new Color(111, 199, 101)));
        painel.add(cardMetrica("Pendentes", moeda.format(pendentes), new Color(224, 78, 34)));

        return painel;
    }
    // metoxo auxiliar que vai criar os cards superiores
    private JPanel cardMetrica(String titulo, String valor, Color corVariacao) {
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 17));

        card.add(t);
        card.add(v);
        return card;
    }

    // tabela e alertas
    private JPanel criarTabelaEAlertas() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 315));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // tabela
        gbc.gridx = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(0, 0, 0, 15);
        painel.add(criarPainelTabela(), gbc);

        // alerta
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 0);
        painel.add(criarPainelAlertas(), gbc);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Próximas Contas a Pagar");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        painel.add(title, BorderLayout.NORTH);

        String[] colunas = {"ID", "Imposto", "Competência", "Vencimento", "Base de Cálculo", "Alíquota", "Valor", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        for (Fiscal imposto : Fiscal.listarImpostos()) {
            model.addRow(new Object[]{
                    imposto.getId(),
                    imposto.getNome(),
                    imposto.getCompetencia(),
                    imposto.getVencimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    moeda.format(imposto.getBaseCalculo()),
                    String.format(Locale.forLanguageTag("pt-BR"), "%.2f%%", imposto.getAliquota()),
                    moeda.format(imposto.getValor()),
                    imposto.getStatusAtual()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);

        // ID é usado internamente para localizar a conta correspondente em Contas a Pagar.
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Estiliza coluna Status
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isSel, hasFocus, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String val = (String) v;
                if ("Pago".equals(val)) {
                    l.setBackground(new Color(209, 250, 229));
                    l.setForeground(new Color(6, 95, 70));
                } else if ("Vencido".equals(val)) {
                    l.setBackground(new Color(254, 226, 226));
                    l.setForeground(new Color(153, 27, 27));
                } else {
                    l.setBackground(new Color(254, 243, 199));
                    l.setForeground(new Color(146, 64, 14));
                }
                l.setOpaque(true);
                return l;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        painel.add(sp, BorderLayout.CENTER);

        JButton btnPagar = new JButton("Registrar pagamento do imposto selecionado");
        btnPagar.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = table.getSelectedRow();
                btnPagar.setEnabled(linha >= 0 && !"Pago".equalsIgnoreCase(String.valueOf(table.getValueAt(linha, 7))));
            }
        });
        btnPagar.addActionListener(e -> {
            int linha = table.getSelectedRow();
            if (linha < 0) return;
            int id = Integer.parseInt(String.valueOf(table.getValueAt(linha, 0)));
            boolean sucesso = Contas.marcarComoConcluida(id);
            if (sucesso) {
                atualizarTela(); // atualiza Fiscal; Contas a Pagar usa a mesma lista/status
            } else {
                JOptionPane.showMessageDialog(this,
                        "Saldo insuficiente para registrar esse pagamento.",
                        "Pagamento não realizado", JOptionPane.WARNING_MESSAGE);
            }
        });
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(new EmptyBorder(8, 0, 0, 0));
        rodape.add(btnPagar, BorderLayout.EAST);
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelAlertas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Alertas de Vencimento");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        painel.add(title, BorderLayout.NORTH);

        JPanel listaAlertas = new JPanel();
        listaAlertas.setLayout(new BoxLayout(listaAlertas, BoxLayout.Y_AXIS));
        listaAlertas.setOpaque(false);

        LocalDate hoje = LocalDate.now();
        boolean temAlerta = false;
        for (Fiscal imposto : Fiscal.listarImpostos()) {
            if ("Pago".equalsIgnoreCase(imposto.getStatusAtual())) {
                continue; // impostos pagos não geram alertas
            }

            LocalDate vencimento = imposto.getVencimento();
            long dias = ChronoUnit.DAYS.between(hoje, vencimento);
            String nivel;
            Color fundo;
            Color borda;
            String detalhe;

            if (dias < 0 || "Vencido".equalsIgnoreCase(imposto.getStatusAtual())) {
                nivel = "ALTA";
                fundo = new Color(254, 242, 242);
                borda = new Color(239, 68, 68);
                detalhe = "Vencido há " + Math.max(0, -dias) + " dia(s).";
            } else if (dias <= 7) {
                nivel = "MÉDIA";
                fundo = new Color(255, 247, 237);
                borda = new Color(249, 115, 22);
                detalhe = dias == 0 ? "Vence hoje." : "Vence em " + dias + " dia(s).";
            } else {
                continue; // pendência distante não é alerta de vencimento
            }

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(fundo);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, borda),
                    new EmptyBorder(8, 8, 8, 8)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

            String vencimentoTexto = vencimento.format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            JLabel mensagem = new JLabel("<html><b>" + imposto.getNome() + "</b><br/>"
                    + "<small>Competência " + imposto.getCompetencia() + " — " + detalhe
                    + " Vencimento: " + vencimentoTexto + "</small><br/>"
                    + "<b style='color:" + (nivel.equals("ALTA") ? "#961006" : "#c2410c")
                    + ";'>" + nivel + "</b></html>");
            card.add(mensagem, BorderLayout.CENTER);
            listaAlertas.add(card);
            listaAlertas.add(Box.createVerticalStrut(6));
            temAlerta = true;
        }

        if (!temAlerta) {
            listaAlertas.add(new JLabel("Nenhum imposto próximo do vencimento."));
        }

        JScrollPane sp = new JScrollPane(listaAlertas);
        sp.setBorder(null);
        sp.getViewport().setBackground(Color.WHITE);
        painel.add(sp, BorderLayout.CENTER);
        return painel;
    }
}
