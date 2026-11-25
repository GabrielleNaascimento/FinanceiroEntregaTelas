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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Contas;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
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
        add(criarAreaPrincipal(), BorderLayout.CENTER);
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

    // cards de cima: valores calculados a partir dos impostos compartilhados com Contas a Pagar
    private JPanel criarCards() {
        ArrayList<Contas> impostos = listarImpostos();
        double total = 0, pago = 0, aberto = 0;
        for (Contas conta : impostos) {
            if (Contas.CANCELADA.equals(conta.getStatus())) continue;
            total += conta.getValor();
            pago += conta.getValorPago();
            aberto += conta.getValorEmAberto();
        }

        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        painel.add(cardMetrica("Total Impostos", formatarMoeda(total), new Color(34, 99, 212)));
        painel.add(cardMetrica("Pagos", formatarMoeda(pago), new Color(111, 199, 101)));
        painel.add(cardMetrica("Pendentes", formatarMoeda(aberto), new Color(224, 78, 34)));
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

        // Registros fiscais de exemplo (2026), cadastrados na mesma lista usada por Contas a Pagar.

        String[] colunas = {"Imposto", "Vencimento", "Valor", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<Contas> impostos = listarImpostos();
        for (Contas conta : impostos) {
            String nome = conta.getDescricao().replace("IMPOSTO FISCAL - ", "");
            String vencimento = conta.getDataVencimento() == null ? "—" : conta.getDataVencimento().format(formato);
            String statusExibido = conta.getStatus();
            if (!Contas.CANCELADA.equals(statusExibido) && !"Paga".equals(statusExibido)
                    && conta.getDataVencimento() != null && conta.getDataVencimento().isBefore(LocalDate.now())) {
                statusExibido = "Vencida";
            }
            model.addRow(new Object[]{nome, vencimento, formatarMoeda(conta.getValor()), statusExibido});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isSel, hasFocus, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String val = String.valueOf(v);
                if ("Paga".equals(val)) {
                    l.setBackground(new Color(209, 250, 229));
                    l.setForeground(new Color(6, 95, 70));
                } else if ("Vencida".equals(val)) {
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

        return painel;
    }


    private ArrayList<Contas> listarImpostos() {
        ArrayList<Contas> resultado = new ArrayList<>();
        for (Contas conta : Contas.getContas()) {
            if (conta.getTipo().equals(Contas.PAGAR) && conta.getDescricao().startsWith("IMPOSTO FISCAL - ")) {
                resultado.add(conta);
            }
        }
        resultado.sort(java.util.Comparator.comparing(Contas::getDataVencimento));
        return resultado;
    }

    private String formatarMoeda(double valor) {
        return java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt", "BR")).format(valor);
    }

    private JPanel criarPainelAlertas() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Alertas de Vencimento");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        painel.add(title);
        painel.add(Box.createVerticalStrut(12));

        LocalDate hoje = LocalDate.now();
        ArrayList<Contas> alertas = new ArrayList<>();
        for (Contas imposto : listarImpostos()) {
            if (Contas.CANCELADA.equals(imposto.getStatus()) || "Paga".equals(imposto.getStatus())) continue;
            LocalDate venc = imposto.getDataVencimento();
            if (venc != null && !venc.isAfter(hoje.plusDays(7))) alertas.add(imposto);
        }
        // Prioriza impostos vencidos; em seguida, os que vencem mais perto.
        alertas.sort(java.util.Comparator
                .comparing((Contas c) -> !c.getDataVencimento().isBefore(hoje))
                .thenComparing(Contas::getDataVencimento));

        int totalAlertas = alertas.size();
        final int limiteAlertasVisiveis = 3;
        if (alertas.isEmpty()) {
            JLabel semAlertas = new JLabel("Nenhum imposto vencido ou próximo do vencimento.");
            semAlertas.setForeground(new Color(6, 95, 70));
            painel.add(semAlertas);
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (int i = 0; i < Math.min(alertas.size(), limiteAlertasVisiveis); i++) {
                Contas imposto = alertas.get(i);
                boolean vencido = imposto.getDataVencimento().isBefore(hoje);
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(vencido ? new Color(254, 242, 242) : new Color(254, 243, 199));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, vencido ? new Color(239, 68, 68) : new Color(239, 151, 68)),
                        new EmptyBorder(8, 8, 8, 8)));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
                String nome = imposto.getDescricao().replace("IMPOSTO FISCAL - ", "");
                String textoStatus = vencido ? "VENCIDO" : "VENCE EM BREVE";
                JLabel mensagem = new JLabel("<html><b>" + nome + "</b><br/><small>Vencimento: " + imposto.getDataVencimento().format(fmt)
                        + " | Em aberto: " + formatarMoeda(imposto.getValorEmAberto()) + "</small><br/><b>" + textoStatus + "</b></html>");
                card.add(mensagem, BorderLayout.CENTER);
                painel.add(card);
                painel.add(Box.createVerticalStrut(6));
            }
            if (totalAlertas > limiteAlertasVisiveis) {
                JLabel restantes = new JLabel("+ " + (totalAlertas - limiteAlertasVisiveis)
                        + " outro(s) imposto(s) com alerta");
                restantes.setForeground(new Color(100, 116, 139));
                restantes.setFont(new Font("SansSerif", Font.PLAIN, 11));
                painel.add(restantes);
            }
        }
        return painel;
    }
}
