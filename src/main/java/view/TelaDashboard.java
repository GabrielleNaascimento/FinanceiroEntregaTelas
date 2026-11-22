package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.DashboardController;
import model.Despesas;
import model.ResumoMensal;
import model.Usuario;

public class TelaDashboard extends JFrame {

    // =====================================================================
    // 1. DECLARANDO CONSTANTES
    // =====================================================================

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR"); // define o padrão de moeda e data para o Brasil. Usado em formatarValor() e formatarVariacao()
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(PT_BR); // formata valores monetários para o padrão brasileiro, com R$ e vírgula
    private static final long serialVersionUID = 1L;

    private static final Color VERDE    = new Color(16, 185, 129);
    private static final Color VERMELHO = new Color(239, 68, 68);


    // =====================================================================
    // 2. DECLARANDO CAMPOS (componentes que precisam ser acessados dps de criados.)
    // =====================================================================
    private double saldoAtual;
    private double fluxoAtual;
    private double fluxoAnterior;
    private ResumoMensal resumoAtual;
    private ResumoMensal resumoAnterior;

    private JPanel painelConteudo; // painel central, onde o dashboard e as outras telas serão trocadas
    private JPanel painelCards; // painel dos cards de cima. Usado para atualizar os cards sem precisar recriar a tela toda
    private JPanel telaDashboard; // painel do dashboard, que é criado apenas uma vez, e depois é mostrado ou escondido conforme a tela
    private JButton btnDashboard; // botão do menu lateral. Só existe pra poder ser 'selecionado' (branco) quando o dashboard estiver visível
    private JButton botaoSelecionado; // e este campo guarda qual botão do menu lateral está selecionado (branco) no momento

    private JLabel lblUser; // label do canto superior direito, que mostra o nome do usuário logado e a sessão

    private List<Despesas> despesas = new ArrayList<>(); // Lista de despesas. Usada para atualizar o gráfico de pizza sem precisar recriar a tela toda
   
    private PainelGraficoPizza painelGraficoPizza; // painel do gráfico de pizza, criado uma vez.
    private GraficoLinhaDashboard painelGraficoLinha; // painel do gráfico de linhas, criado uma vez.

    private Runnable aoAtualizar;


    // =====================================================================
    // 3. CONSTRUTOR (onde a tela é criada e os componentes são adicionados)
    // =====================================================================

    public TelaDashboard() { 
        setTitle("ERP Financeiro - Módulo Financeiro");
        setSize(1280, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(criarSidebar(), BorderLayout.WEST);
        painelConteudo = new JPanel(new BorderLayout());
        add(painelConteudo, BorderLayout.CENTER);
        telaDashboard = criarAreaPrincipal(); 
        mostrarTela(telaDashboard);
    }


    // =====================================================================
    // 4. MÉTODOS PUBLICOS (que podem ser chamados de fora da classe --> controller)
    // =====================================================================

    public void setAoAtualizar(Runnable r) { 
        this.aoAtualizar = r; 
    }

    public void atualizarTela() {
    if (aoAtualizar != null) aoAtualizar.run();
    else preencherCards();
    }
    
    public void carregarResumoMensal(List<ResumoMensal> dados) {
        painelGraficoLinha.atualizarDados(dados);
    }

    public void carregarDespesas(List<Despesas> despesas) {
        this.despesas = despesas;
        painelGraficoPizza.atualizarDespesas(despesas);
    }

    public void identificarUsuario(String nome, String sessao) {
        this.lblUser.setText(nome + " - " + sessao);

    }

    public void carregarSaldo(double saldo) {
        this.saldoAtual = saldo;
        preencherCards();
    }

    public void carregarMetricas(ResumoMensal atual, ResumoMensal anterior) {
        this.resumoAtual = atual;
        this.resumoAnterior = anterior;
        preencherCards();
    }


    // =====================================================================
    // 5. NAVEGAÇÃO (trocar de tela e selecionar o botão do menu)
    // =====================================================================

    private void mostrarTela(JPanel tela) { 
        painelConteudo.removeAll();
        painelConteudo.add(tela, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    // troca qual botão do menu fica "selecionado" (branco)
    private void selecionarBotao(JButton btn) {
        // o que estava selecionado volta pro visual normal, igual ao do criarBotaoMenu
        botaoSelecionado.setOpaque(false);
        botaoSelecionado.setContentAreaFilled(false);
        botaoSelecionado.setBorderPainted(false);
        botaoSelecionado.setForeground(new Color(203, 213, 225));
        botaoSelecionado.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // o clicado ganha o visual do dashboard (branco, texto azul, negrito)
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(27, 54, 93));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));

        botaoSelecionado = btn;
    }


    // =====================================================================
    // 6. CONSTRUÇÃO DA UI - MENU LATERAL
    // =====================================================================

    // menu lateral
    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(27, 54, 93)); // ao se colocar o nome do 'painel' com setBackground(new Color) é possível colocar cores
        sidebar.setPreferredSize(new Dimension(220, 0)); // e o mesmo vale pros demais sets
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // serve pra organizar as caixas 'empilhadas' para o eixo Y
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Logo
        JLabel lblLogo = new JLabel("<html><b>ERP Finance</b><br/><small style='color:#A0AEC0;'>MÓDULO FINANCEIRO</small></html>"); // da pra colocar conf html ao se criar labels
        lblLogo.setForeground(Color.WHITE); //seta as cores das coisas do primeiro plano( tipo texto) para branco
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 15)); //familia da fonte, o .PLAIN serve pra nn deixar o texto ficar negrito ou italico se houver algo q o altere
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT); //alinha o texto pra esquerda
        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30)); // cria um espaçamento invisivel


        // aqui é o botao selecionado, que no caso dessa página é o dashboard
        btnDashboard = new JButton("Dashboard"); // sem o "JButton" na frente, pra usar o campo e não uma variável local
        btnDashboard.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(SwingConstants.LEFT);
        btnDashboard.setBackground(Color.WHITE);
        btnDashboard.setForeground(new Color(27, 54, 93));
        btnDashboard.setFont(new Font("SansSerif", Font.BOLD, 12));
        botaoSelecionado = btnDashboard; // começa como o selecionado

        btnDashboard.addActionListener(e -> {
            atualizarTela();
            mostrarTela(telaDashboard);
            selecionarBotao(btnDashboard);
             
        });

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));

        //os demais botoes serao criados por metodo auxiliar

        JButton btnContasPagar = criarBotaoMenu("Contas a Pagar");

        btnContasPagar.addActionListener(e -> {
            mostrarTela(new TelaContasPagar());
            selecionarBotao(btnContasPagar);
        });

        sidebar.add(btnContasPagar);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnContasReceber = criarBotaoMenu("Contas a Receber");

        btnContasReceber.addActionListener(e -> {
            mostrarTela(new TelaContasReceber());
            selecionarBotao(btnContasReceber);
        });

        sidebar.add(btnContasReceber);
        sidebar.add(Box.createVerticalStrut(5));


        JButton btnFluxoCaixa = criarBotaoMenu("Fluxo de Caixa");

        btnFluxoCaixa.addActionListener(e -> {
            mostrarTela(new TelaFluxo());
            selecionarBotao(btnFluxoCaixa);
        });

        sidebar.add(btnFluxoCaixa);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnConciliacao = criarBotaoMenu("Conciliação Bancária");

        btnConciliacao.addActionListener(e -> {
            mostrarTela(new TelaConciliacaoBancaria());
            selecionarBotao(btnConciliacao);
        });

        sidebar.add(btnConciliacao);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnFiscal = criarBotaoMenu("Fiscal");

        btnFiscal.addActionListener(e -> {
            mostrarTela(new TelaFiscal());
            selecionarBotao(btnFiscal);
        });

        sidebar.add(btnFiscal);
        sidebar.add(Box.createVerticalStrut(5));

        JButton btnRelatorios = criarBotaoMenu("Relatórios");

        btnRelatorios.addActionListener(e -> {
            mostrarTela(new TelaRelatorios());
            selecionarBotao(btnRelatorios);
        });

        sidebar.add(btnRelatorios);
        sidebar.add(Box.createVerticalStrut(5));

        return sidebar;


    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(new Color(203, 213, 225));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }


    // =====================================================================
    // 7. CONSTRUÇÃO DA UI - ÁREA PRINCIPAL DO DASHBOARD
    // =====================================================================

    // painel central
    private JPanel criarAreaPrincipal() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(new Color(245, 247, 250));

        // cabeçalho ou header pros intimos
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false); //deixa transparente
        header.setBorder(new EmptyBorder(15, 25, 15, 25));



        lblUser = new JLabel("Não logado");

        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.add(lblUser, BorderLayout.EAST);
        area.add(header, BorderLayout.NORTH);

        // Painel Interno Vertical
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);
        conteudo.setBorder(new EmptyBorder(10, 25, 25, 25));

        JLabel lblTitulo = new JLabel("<html><h2 style='margin:0;'>Módulo Financeiro</h2><span style='color:gray;'>Visão geral financeira da organização</span></html>");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(lblTitulo);
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' de card
        conteudo.add(criarCards());
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' dos graficos e da despesa
        conteudo.add(criarGraficos());
        conteudo.add(Box.createVerticalStrut(15));

        // 'adesivos' das tabelas e o alerta
        conteudo.add(criarTabelaEAlertas());

        area.add(conteudo, BorderLayout.CENTER);
        return area;
    }

    // ---------- cards ----------

    // cards de cima
    private JPanel criarCards() {
        painelCards = new JPanel(new GridLayout(1, 4, 15, 0));
        painelCards.setOpaque(false);
        painelCards.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        preencherCards();
        return painelCards;
    }

    private void preencherCards() {
    painelCards.removeAll();
    
    painelCards.add(cardMetrica("Saldo atual", formatarValor(saldoAtual), "aaa", VERDE));

   if (resumoAtual != null && resumoAnterior != null) {
    double receitaAtual = resumoAtual.getReceita();
    double receitaAnterior = resumoAnterior.getReceita();
    double despesaAtual = resumoAtual.getDespesa();
    double despesaAnterior = resumoAnterior.getDespesa();

        painelCards.add(cardMetrica("Receitas", formatarValor(receitaAtual), formatarVariacao(receitaAtual, receitaAnterior), corVariacao(receitaAtual, receitaAnterior, true)));
        painelCards.add(cardMetrica("Despesas", formatarValor(despesaAtual), formatarVariacao(despesaAtual, despesaAnterior), corVariacao(despesaAtual, despesaAnterior, false)));
        painelCards.add(cardMetrica("Fluxo de Caixa", formatarValor(fluxoAtual), formatarVariacao(fluxoAtual, fluxoAnterior), corVariacao(fluxoAtual, fluxoAnterior, true)));
    } else {
        // ainda sem dados suficientes
        painelCards.add(cardMetrica("Receitas", "-", "sem dados", Color.GRAY));
        painelCards.add(cardMetrica("Despesas", "-", "sem dados", Color.GRAY));
        painelCards.add(cardMetrica("Fluxo de Caixa", "-", "sem dados", Color.GRAY));
    }

    painelCards.revalidate();
    painelCards.repaint();
}

    // metoxo auxiliar que vai criar os cards superiores
    private JPanel cardMetrica(String titulo, String valor, String variacao, Color corVariacao) {
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

        JLabel var = new JLabel(variacao);
        var.setFont(new Font("SansSerif", Font.BOLD, 11));
        var.setForeground(corVariacao);

        card.add(t);
        card.add(v);
        card.add(var);
        return card;
    }

    // ---------- gráficos ----------

    // graficos do SWING (Graphics2D)
    private JPanel criarGraficos() {
        JPanel painel = new JPanel(new GridBagLayout());

        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // preenche todo o espaço vertical e horizontal
        gbc.weighty = 1.0; // expande o grafico ate ocupar 100% da altura

        // grafico de linhas (despesas x receitas)
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        painelGraficoLinha = new GraficoLinhaDashboard();
        painel.add(painelGraficoLinha, gbc);

        // chama um metodo que vai desenhar as linhas
            
        // grafico de pizza
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelGraficoPizza = new PainelGraficoPizza(despesas);
        painel.add(painelGraficoPizza, gbc);

        return painel;
    }

    // ---------- tabela e alertas ----------

    // tabela e alertas
    private JPanel criarTabelaEAlertas() {
       /* */ JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

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

        String[] colunas = {"Descrição", "Fornecedor", "Vencimento", "Valor", "Status"};
        Object[][] dados = {
                {"Servidor AWS Cloud", "Amazon WS", "15/10/2025", "R$ 8.450,00", "Pendente"},
                {"Licenças ERP Office", "Microsoft", "18/10/2025", "R$ 3.200,00", "Pendente"},
                {"Consultoria Contábil", "Lopes Adv.", "20/10/2025", "R$ 5.000,00", "Paga"},
                {"Aluguel Escritório", "Imob. Central", "22/10/2025", "R$ 12.000,00", "Pendente"},
                {"Energia Elétrica", "Copel S/A", "25/10/2025", "R$ 1.850,00", "Vencida"}
        };

        DefaultTableModel model = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);

        // Estiliza coluna Status
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isSel, hasFocus, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String val = (String) v;
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

    private JPanel criarPainelAlertas() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Alertas Críticos");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(254, 242, 242));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(239, 68, 68)),
                new EmptyBorder(8, 8, 8, 8)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel msg = new JLabel("<html><b>Boleto Copel Vencido</b><br/><small style='color:gray;'>Atraso de 3 dias no pagamento.</small></html>");
        card.add(msg, BorderLayout.CENTER);

        painel.add(title);
        painel.add(Box.createVerticalStrut(12));
        painel.add(card);

        return painel;
    }


    // =====================================================================
    // 8. FORMATAÇÃO E APOIO VISUAL
    // =====================================================================

    private String formatarValor(double valor) {
        return FORMATO_MOEDA.format(valor);
    }

    private String formatarVariacao(double atual, double anterior) {
        if (anterior == 0) {
            return "sem base de comparação"; // evita divisão por zero
        }  
         double variacao = (atual - anterior) / anterior * 100;
         return String.format(PT_BR, "%+.1f%% vs. mês anterior", variacao);
    }

    // se altaEhBoa for true, então coloca a cor verde, false vermelho, se for zero retorna cinza.
    private Color corVariacao(double atual, double anterior, boolean altaEhBoa) { 
        if (anterior == 0) {
            return Color.GRAY; // evita divisão por zero
        }
        boolean subiu = atual >= anterior;
        return (subiu == altaEhBoa) ? VERDE : VERMELHO;

        }


    // =====================================================================
    // 9. CLASSE INTERNA - GRÁFICO DE PIZZA
    // =====================================================================

    // classe para desenhar o grafico de pizza (só SWING)
    private static class PainelGraficoPizza extends JPanel {
        private List<Despesas> despesas;

        public void atualizarDespesas(List<Despesas> despesas) {
            this.despesas = despesas;
            repaint();
        }

        public PainelGraficoPizza(List<Despesas> despesas) {
            this.despesas = despesas;

            for (Despesas d : despesas) {

                System.out.println(d.getCategoria());
                System.out.println(d.getValor());

            }

            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    new EmptyBorder(12, 12, 12, 12)
            ));
        }

        // Método responsável por desenhar uma fatia
        private void desenharFatia(
                Graphics2D g2,
                int x,
                int y,
                int size,
                double valor,
                double total,
                int inicio,
                Color cor
        ) {
            int angulo = (int) Math.round(valor / total * 360);

            g2.setColor(cor);
            g2.fillArc(x, y, size, size, inicio, angulo);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(Color.BLACK);
            g2.drawString("Despesas por Categoria", 12, 22);

            int size = Math.min(getWidth(), getHeight()) - 70;
            int x = 20;
            int y = 45;


            // Valores das categorias
            double folhaPagamento = 0;
            double fornecedores = 0;
            double impostos = 0;
            double operacional = 0;
            double outros = 0;


            // Percorre todas as despesas
            for (Despesas d : despesas) {

                if (d.getCategoria().equals("Folha de Pagamento")) {

                    folhaPagamento += d.getValor();

                } else if (d.getCategoria().equals("Fornecedores")) {

                    fornecedores += d.getValor();

                } else if (d.getCategoria().equals("Impostos")) {

                    impostos += d.getValor();

                } else if (d.getCategoria().equals("Operacional")) {

                    operacional += d.getValor();

                } else {

                    outros += d.getValor();
                }
            }


            // Soma total
            double total =
                    folhaPagamento
                            + fornecedores
                            + impostos
                            + operacional
                            + outros;


            // Evita divisão por zero
            if (total == 0) {
                return;
            }


            int inicio = 0;


            // Folha de Pagamento
            desenharFatia(
                    g2, x, y, size,
                    folhaPagamento,
                    total,
                    inicio,
                    new Color(37, 99, 235)
            );

            inicio += Math.round(
                    folhaPagamento / total * 360
            );


            // Fornecedores
            desenharFatia(
                    g2, x, y, size,
                    fornecedores,
                    total,
                    inicio,
                    new Color(16, 185, 129)
            );

            inicio += Math.round(
                    fornecedores / total * 360
            );


            // Impostos
            desenharFatia(
                    g2, x, y, size,
                    impostos,
                    total,
                    inicio,
                    new Color(245, 158, 11)
            );

            inicio += Math.round(
                    impostos / total * 360
            );


            // Operacional
            desenharFatia(
                    g2, x, y, size,
                    operacional,
                    total,
                    inicio,
                    new Color(239, 68, 68)
            );

            inicio += Math.round(
                    operacional / total * 360
            );


            // Outros
            desenharFatia(
                    g2, x, y, size,
                    outros,
                    total,
                    inicio,
                    new Color(168, 85, 247)
            );
        }
    }


    // =====================================================================
    // 10. MAIN (teste)
    // =====================================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            TelaDashboard tela = new TelaDashboard();

            Usuario usuario = new Usuario();

            // teste de usuario logado, administrador.

            usuario.setNomeUsuario("Jefferson");
            usuario.setSessaoUsuario("Administrador");


            DashboardController controller =
                    new DashboardController(tela, usuario);

            controller.identificarUsuario();
            controller.carregarDespesas();
            controller.carregarResumoMensal();
            controller.carregarSaldoAtual();
            tela.setVisible(true);
        });
    }
}
