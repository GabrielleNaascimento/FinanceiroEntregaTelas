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
import java.util.List;
import model.Fiscal;
import java.util.ArrayList;
import java.util.Comparator;

public class TelaFiscal extends JPanel {

    private static final long serialVersionUID = 1L;

    private DefaultTableModel tableModel;
    private JPanel listaAlertas;

    public TelaFiscal() {
        setLayout(new BorderLayout());
        add(criarAreaPrincipal(), BorderLayout.CENTER);
        carregarDados();
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

        painel.add(cardMetrica("Total Impostos", "R$ 42.680,00", new Color(34, 99, 212)));
        painel.add(cardMetrica("Pagos", "R$ 28.400,00", new Color(111, 199, 101)));
        painel.add(cardMetrica("Pendentes", "R$ 14.280,00", new Color(224, 78, 34)));

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

        String[] colunas = {"Imposto", "Competência", "Vencimento", "Base de Cálculo", "Alíquota", "Valor", "Status"};
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setShowGrid(false);

        // Estiliza coluna Status
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
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

        listaAlertas = new JPanel();
        listaAlertas.setLayout(new BoxLayout(listaAlertas, BoxLayout.Y_AXIS));
        listaAlertas.setOpaque(false);

        JScrollPane sp = new JScrollPane(listaAlertas);
        sp.setBorder(null);
        sp.getViewport().setBackground(Color.WHITE);
        painel.add(sp, BorderLayout.CENTER);

        return painel;
    }

    private void atualizarAlertas(List<Fiscal> lista) {
        listaAlertas.removeAll();

        // do vencimento mais antigo para o mais novo (vencidos primeiro)
        List<Fiscal> ordenada = new ArrayList<>(lista);
        ordenada.sort(Comparator.comparing(Fiscal::getDataVencimento));

        boolean temAlerta = false;
        for (Fiscal f : ordenada) {
            String prioridade = f.getPrioridade();
            if (prioridade == null) continue; // pago não alerta

            listaAlertas.add(criarCardAlerta(f, prioridade));
            listaAlertas.add(Box.createVerticalStrut(6));
            temAlerta = true;
        }

        if (!temAlerta) {
            listaAlertas.add(new JLabel("Nenhum imposto pendente."));
        }

        listaAlertas.revalidate();
        listaAlertas.repaint();
    }

    private JPanel criarCardAlerta(Fiscal f, String prioridade) {
        Color corBorda;
        Color corFundo;
        String corTexto;

        switch (prioridade) {
            case "ALTA":
                corBorda = new Color(239, 68, 68);
                corFundo = new Color(254, 242, 242);
                corTexto = "#961006";
                break;
            case "MÉDIA":
                corBorda = new Color(239, 151, 68);
                corFundo = new Color(254, 242, 242);
                corTexto = "#eb7b0c";
                break;
            default: // BAIXA
                corBorda = new Color(239, 228, 68);
                corFundo = new Color(254, 254, 242);
                corTexto = "#ffcc00";
                break;
        }

        long dias = f.getDiasParaVencimento();
        String titulo = f.getTipoImposto() + (dias < 0 ? " em Atraso" : "");
        String prazo;
        if (dias < 0) {
            prazo = "Vencido há " + (-dias) + " dia(s), em " + f.getVencimento() + ".";
        } else if (dias == 0) {
            prazo = "Vence hoje (" + f.getVencimento() + ").";
        } else {
            prazo = "Vence em " + dias + " dia(s), em " + f.getVencimento() + ".";
        }

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(corFundo);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, corBorda),
                new EmptyBorder(8, 8, 8, 8)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel msg = new JLabel("<html><b>" + titulo + "</b><br/>"
                + "<small style='color:gray;'>" + prazo + " Valor: R$ " + f.getValor() + "</small><br/>"
                + "<b style=\"color: " + corTexto + ";\">" + prioridade + "</b></html>");
        card.add(msg, BorderLayout.CENTER);

        return card;
    }

    public void carregarDados() {
        tableModel.setRowCount(0); // limpa antes de preencher
        List<Fiscal> lista = Fiscal.listarTodos();
        for (Fiscal f : lista) {
            tableModel.addRow(new Object[] {
                f.getTipoImposto(),
                f.getCompetencia(),
                f.getVencimento(),
                f.getBaseCalculo(),
                f.getAliquota(),
                f.getValor(),
                f.getStatus()
            });
        }
        atualizarAlertas(lista);
    }

}
