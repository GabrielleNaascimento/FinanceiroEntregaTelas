package model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class Contas {

    public static final String PAGAR = "PAGAR";
    public static final String RECEBER = "RECEBER";
    public static final String PARCIAL = "Parcial";
    public static final String CANCELADA = "Cancelada";
    public static final String ESTORNADA = "Estornada";

    private int id;
    private String entidade;
    private String descricao;
    private double valor;
    private String tipo;
    private String status;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;

    // quanto do valor original já foi quitado (sem juros, multa, desconto...)
    private double valorBaixado;
    // dinheiro que realmente saiu (pagar) ou entrou (receber) nas baixas dessa conta
    private double valorPago;
    // histórico das baixas feitas nessa conta
    private final ArrayList<Baixa> baixas = new ArrayList<>();
    // data em que a conta foi cancelada (pagar) ou estornada (receber); null se ainda está ativa
    private LocalDate dataEncerramento;

    static final ArrayList<Contas> contas = new ArrayList<>();
    // pra lista de exemplo ser carregada só uma vez, mesmo que a lista fique vazia
    private static boolean carregado = false;
    private static boolean impostosExemploCarregados = false;

    public Contas(int id, String entidade, String descricao, double valor, String tipo,
                  String status, LocalDate dataInicio, LocalDate dataVencimento) {
        this.id = id;
        this.entidade = entidade;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;

        // conta que já nasce quitada (dados de exemplo) conta como paga/recebida por inteiro
        if (status.equals(statusConcluido(tipo))) {
            this.valorBaixado = valor;
            this.valorPago = valor;
        }
    }

    // ===================== BAIXA =====================
    // guarda os dados de uma baixa (o que o usuário preencheu na tela "Baixa de lançamentos")

    public static class Baixa {
        private final LocalDate data;
        private final String contaFinanceira;
        private final String categoria;
        private final String historico;
        private final double juros;
        private final double desconto;
        private final double multa;
        private final double tarifa;
        private final double valorPago;

        public Baixa(LocalDate data, String contaFinanceira, String categoria, String historico,
                     double juros, double desconto, double multa, double tarifa, double valorPago) {
            this.data = data;
            this.contaFinanceira = contaFinanceira;
            this.categoria = categoria;
            this.historico = historico;
            this.juros = juros;
            this.desconto = desconto;
            this.multa = multa;
            this.tarifa = tarifa;
            this.valorPago = valorPago;
        }

        public LocalDate getData() {
            return data;
        }

        public String getContaFinanceira() {
            return contaFinanceira;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getHistorico() {
            return historico;
        }

        public double getJuros() {
            return juros;
        }

        public double getDesconto() {
            return desconto;
        }

        public double getMulta() {
            return multa;
        }

        public double getTarifa() {
            return tarifa;
        }

        public double getValorPago() {
            return valorPago;
        }
    }

    public int getId() {
        return id;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public double getValorPago() {
        return valorPago;
    }

    /** Quanto ainda falta pagar/receber do valor original. */
    public double getValorEmAberto() {
        return arredondar(valor - valorBaixado);
    }

    public ArrayList<Baixa> getBaixas() {
        return baixas;
    }

    public LocalDate getDataEncerramento() {
        return dataEncerramento;
    }

    /**
     * Valor que a conta tinha quando foi cancelada/estornada (0 se ela ainda está ativa).
     * - Cancelada (pagar): só o que faltava pagar, porque o que já foi pago continua no total pago.
     * - Estornada (receber): o valor inteiro, porque o que já tinha sido recebido foi devolvido.
     * Assim cada real da conta aparece em um lugar só (a pagar, pago ou cancelado).
     */
    public double getValorEncerrado() {
        if (!status.equals(statusEncerrado(tipo))) {
            return 0;
        }
        return tipo.equals(PAGAR) ? getValorEmAberto() : valor;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private static String statusConcluido(String tipo) {
        return tipo.equals(PAGAR) ? "Paga" : "Recebida";
    }

    // conta a pagar é cancelada, conta a receber é estornada
    private static String statusEncerrado(String tipo) {
        return tipo.equals(PAGAR) ? CANCELADA : ESTORNADA;
    }

    private static double arredondar(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private static String moeda(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(v);
    }

    /**
     * Encargos líquidos da baixa: juros + multa - desconto.
     * A tarifa soma na conta a pagar (o banco cobra de quem paga) e subtrai na conta a receber
     * (o banco desconta de quem recebe).
     */
    public double calcularEncargos(double juros, double multa, double desconto, double tarifa) {
        double sinalTarifa = tipo.equals(PAGAR) ? 1 : -1;
        return arredondar(juros + multa + sinalTarifa * tarifa - desconto);
    }

    /** Valor que quita a conta hoje: o que está em aberto mais os encargos. */
    public double calcularDevido(double juros, double multa, double desconto, double tarifa) {
        return arredondar(getValorEmAberto() + calcularEncargos(juros, multa, desconto, tarifa));
    }

    // ===================== LISTAGENS E TOTAIS =====================

    // lista as contas de um tipo, separando concluídas Paga/Recebida das em aberto (inclui as parciais)
    // as canceladas/estornadas não entram em nenhuma das duas, ficam em listarEncerradas
    public static ArrayList<Contas> listar(String tipo, boolean concluidas) {
        ArrayList<Contas> resultado = new ArrayList<>();
        String concluido = statusConcluido(tipo);
        String encerrado = statusEncerrado(tipo);

        for (Contas c : getContas()) {
            if (c.getTipo().equals(tipo) && !c.getStatus().equals(encerrado)
                    && c.getStatus().equals(concluido) == concluidas) {
                resultado.add(c);
            }
        }

        resultado.sort(Comparator.comparing(Contas::getDataVencimento));
        return resultado;
    }

    // contas canceladas (pagar) ou estornadas (receber): continuam registradas, só saem dos totais
    public static ArrayList<Contas> listarEncerradas(String tipo) {
        ArrayList<Contas> resultado = new ArrayList<>();
        String encerrado = statusEncerrado(tipo);

        for (Contas c : getContas()) {
            if (c.getTipo().equals(tipo) && c.getStatus().equals(encerrado)) {
                resultado.add(c);
            }
        }

        resultado.sort(Comparator.comparing(Contas::getDataVencimento));
        return resultado;
    }

    // em aberto: soma o que falta; concluídas: soma o que realmente foi pago/recebido
    public static double somar(String tipo, boolean concluidas) {
        double total = 0;
        for (Contas c : listar(tipo, concluidas)) {
            total += concluidas ? c.getValorPago() : c.getValorEmAberto();
        }
        return arredondar(total);
    }

    // tudo que já saiu/entrou de verdade, incluindo as baixas parciais das contas ainda abertas
    private static double somarPagamentos(String tipo) {
        double total = 0;
        for (Contas c : getContas()) {
            if (c.getTipo().equals(tipo)) {
                total += c.getValorPago();
            }
        }
        return arredondar(total);
    }

    /** Saldo atual = tudo que já foi recebido menos tudo que já foi pago. */
    public static double getSaldoAtual() {
        return arredondar(getTotalRecebido() - getTotalPago());
    }

    public static double getTotalPagar() {
        return somar(PAGAR, false);
    }

    public static double getTotalPago() {
        return somarPagamentos(PAGAR);
    }

    public static double getTotalReceber() {
        return somar(RECEBER, false);
    }

    public static double getTotalRecebido() {
        return somarPagamentos(RECEBER);
    }

    public static Contas buscarPorId(int id) {
        for (Contas c : getContas()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    // ===================== BAIXAR / EXCLUIR =====================

    /**
     * Registra uma baixa (total ou parcial) na conta.
     * Se o valor pago for igual ao devido, a conta fica Paga/Recebida; se for menor, fica Parcial
     * e o que sobrou continua em aberto.
     * Retorna null se deu certo, ou o texto do motivo se a baixa não pôde ser feita.
     */
    public static String baixar(int id, Baixa b) {
        Contas c = buscarPorId(id);
        if (c == null) {
            return "Conta não encontrada.";
        }
        if (c.status.equals(statusEncerrado(c.tipo))) {
            return "Esta conta já foi " + (c.tipo.equals(PAGAR) ? "cancelada." : "estornada.");
        }
        if (c.getValorEmAberto() <= 0) {
            return "Esta conta já foi quitada.";
        }
        if (b.getJuros() < 0 || b.getDesconto() < 0 || b.getMulta() < 0 || b.getTarifa() < 0) {
            return "Juros, desconto, multa e tarifa não podem ser negativos.";
        }

        double encargos = c.calcularEncargos(b.getJuros(), b.getMulta(), b.getDesconto(), b.getTarifa());
        double devido = c.calcularDevido(b.getJuros(), b.getMulta(), b.getDesconto(), b.getTarifa());
        double pago = arredondar(b.getValorPago());

        if (devido <= 0) {
            return "O desconto não pode ser maior que o valor devido.";
        }
        if (pago <= 0) {
            return "Informe um valor maior que zero.";
        }
        if (pago > devido) {
            return "O valor informado não pode ser maior que o valor devido (" + moeda(devido) + ").";
        }

        // a parte do valor pago que realmente abate a conta (o resto são encargos)
        double principal = arredondar(pago - encargos);
        if (principal <= 0) {
            return "O valor informado não cobre os encargos (juros, multa e tarifa).";
        }

        // pra nao ficar saldo negativo
        if (c.tipo.equals(PAGAR) && pago > getSaldoAtual()) {
            return "Saldo insuficiente para esta baixa. Saldo atual: " + moeda(getSaldoAtual()) + ".";
        }

        c.valorPago = arredondar(c.valorPago + pago);

        if (pago >= devido) {
            c.valorBaixado = c.valor;
            c.status = statusConcluido(c.tipo);
        } else {
            c.valorBaixado = arredondar(c.valorBaixado + principal);
            c.status = PARCIAL;
        }

        c.baixas.add(b);

        // toda baixa gera uma nota fiscal
        NotaFiscal.emitirDaBaixa(c, b, principal);
        return null;
    }

    /** Baixa total simples (sem encargos), mantida pra quem ainda chama o método antigo. */
    public static boolean marcarComoConcluida(int id) {
        Contas c = buscarPorId(id);
        if (c == null) {
            return false;
        }
        Baixa b = new Baixa(LocalDate.now(), "Caixa", "", "Liquidação de conta",
                0, 0, 0, 0, c.getValorEmAberto());
        return baixar(id, b) == null;
    }

    /**
     * Nada é apagado: a conta a pagar vira Cancelada e a conta a receber vira Estornada.
     * - Cancelar: o que falta pagar deixa de existir; o que já foi pago continua no saldo (o dinheiro saiu).
     * - Estornar: o que já foi recebido é devolvido, então sai do saldo (as baixas ficam guardadas no histórico).
     * Retorna null se deu certo, ou o texto do motivo se não foi possível.
     */
    public static String encerrar(int id) {
        Contas c = buscarPorId(id);
        if (c == null) {
            return "Conta não encontrada.";
        }

        String acao = c.tipo.equals(PAGAR) ? "cancelada" : "estornada";

        if (c.status.equals(statusEncerrado(c.tipo))) {
            return "Esta conta já foi " + acao + ".";
        }
        if (c.status.equals(statusConcluido(c.tipo))) {
            return "Uma conta já quitada não pode ser " + acao + " por aqui.";
        }

        if (c.tipo.equals(RECEBER) && c.valorPago > 0) {
            // devolver o que ja foi recebido nao pode deixar o saldo negativo
            if (c.valorPago > getSaldoAtual()) {
                return "Saldo insuficiente para estornar os " + moeda(c.valorPago)
                        + " já recebidos. Saldo atual: " + moeda(getSaldoAtual()) + ".";
            }
            c.valorPago = 0;
            NotaFiscal.marcarEstornadas(c.id);
        }

        c.status = statusEncerrado(c.tipo);
        c.dataEncerramento = LocalDate.now();
        return null;
    }

    /** Registra um imposto fiscal na lista compartilhada de contas a pagar. */
    public static Contas registrarImpostoFiscal(String nomeImposto, double valor, LocalDate vencimento) {
        if (nomeImposto == null || nomeImposto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do imposto.");
        }
        if (valor <= 0 || vencimento == null) {
            throw new IllegalArgumentException("Valor e vencimento do imposto devem ser válidos.");
        }

        String descricao = "IMPOSTO FISCAL - " + nomeImposto.trim();
        for (Contas existente : getContas()) {
            if (existente.getTipo().equals(PAGAR) && existente.getDescricao().equals(descricao)) {
                return existente; // evita duplicar quando a tela Fiscal for aberta novamente
            }
        }

        int novoId = 1;
        for (Contas existente : getContas()) {
            novoId = Math.max(novoId, existente.getId() + 1);
        }
        Contas imposto = new Contas(novoId, "Governo", descricao, valor, PAGAR, "Pendente",
                LocalDate.now(), vencimento);
        contas.add(imposto);
        return imposto;
    }

    public static ArrayList<Contas> getContas() {
        if (!carregado) {
            carregado = true;

            contas.add(new Contas(1, "Amazon", "Locação Servidores", 4500.00, PAGAR, "Vencida",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 15)));

            contas.add(new Contas(2, "Microsoft", "Licenças Office", 3200.00, PAGAR, "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 20)));

            contas.add(new Contas(3, "Xavier", "Assessoria Jurídica", 8500.00, PAGAR, "Paga",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5)));

            contas.add(new Contas(4, "Imobiliária", "Aluguel Sala", 12000.00, PAGAR, "Pendente",
                    LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 10)));

            contas.add(new Contas(5, "Adobe", "Licenças Creative Cloud", 1850.00, PAGAR, "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 25)));

            contas.add(new Contas(6, "Dell", "Equipamentos de TI", 6800.00, PAGAR, "Pendente",
                    LocalDate.of(2026, 9, 5), LocalDate.of(2026, 9, 28)));

            contas.add(new Contas(7, "Google", "Serviços Cloud", 2750.00, PAGAR, "Pendente",
                    LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 30)));

            contas.add(new Contas(8, "Tech Solutions", "Impl. ERP", 15000.00, RECEBER, "Recebida",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 1)));

            contas.add(new Contas(9, "Metalúrgica", "Consultoria", 8540.00, RECEBER, "Atrasada",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 12)));

            contas.add(new Contas(10, "Hospital", "Manut. Hardware", 3800.00, RECEBER, "Pendente",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 25)));

            contas.add(new Contas(11, "Banco Nacional", "Suporte TI", 4500.00, RECEBER, "Recebida",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 18)));

            contas.add(new Contas(12, "Grupo Alpha", "Desenvolvimento Sistema", 12500.00, RECEBER, "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 22)));

            contas.add(new Contas(13, "Comercial Silva", "Suporte Mensal", 6200.00, RECEBER, "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 27)));

            contas.add(new Contas(14, "Construtora Nova", "Consultoria Técnica", 9800.00, RECEBER, "Pendente",
                    LocalDate.of(2026, 9, 5), LocalDate.of(2026, 10, 5)));

            // as contas de exemplo que ja nascem quitadas tambem tem a baixa e a nota fiscal registradas
            for (Contas c : contas) {
                if (c.valorPago > 0) {
                    Baixa b = new Baixa(c.dataVencimento, "Caixa", "", "Liquidação de conta",
                            0, 0, 0, 0, c.valorPago);
                    c.baixas.add(b);
                    NotaFiscal.emitirDaBaixa(c, b, c.valor);
                }
            }
        }

        // Impostos são incluídos na lista compartilhada no primeiro acesso, antes de qualquer tela depender deles.
        if (!impostosExemploCarregados) {
            impostosExemploCarregados = true;
            registrarImpostosDeExemplo();
        }

        return contas;
    }


    private static void registrarImpostosDeExemplo() {
        // Cenário demonstrativo misto: 3 pagos, 3 pendentes e 2 vencidos.
        // Pagamentos de exemplo são lançados como baixas para manter os totais e o histórico coerentes.
        Contas irpj = registrarImpostoFiscal("IRPJ - 10/2026", 23445.00, LocalDate.of(2026, 12, 30));
        Contas csll = registrarImpostoFiscal("CSLL - 10/2026", 14067.00, LocalDate.of(2026, 12, 30));
        registrarImpostoFiscal("PIS - 10/2026", 1015.95, LocalDate.of(2026, 11, 25));
        registrarImpostoFiscal("COFINS - 10/2026", 4689.00, LocalDate.of(2026, 11, 25));
        registrarImpostoFiscal("ISS - 10/2026", 4250.00, LocalDate.of(2026, 11, 10));
        Contas icms = registrarImpostoFiscal("ICMS - 10/2026", 8556.00, LocalDate.of(2026, 11, 30));
        registrarImpostoFiscal("IRRF Serviços - 10/2026", 180.00, LocalDate.of(2026, 11, 20));
        registrarImpostoFiscal("INSS Patronal - 10/2026", 9040.00, LocalDate.of(2026, 12, 20));

        registrarPagamentoDeExemplo(irpj, LocalDate.of(2026, 11, 15));
        registrarPagamentoDeExemplo(csll, LocalDate.of(2026, 11, 18));
        registrarPagamentoDeExemplo(icms, LocalDate.of(2026, 11, 20));
    }


    private static void registrarPagamentoDeExemplo(Contas conta, LocalDate dataPagamento) {
        if (conta == null || conta.valorPago > 0 || !PAGAR.equals(conta.tipo)) return;
        conta.valorBaixado = conta.valor;
        conta.valorPago = conta.valor;
        conta.status = "Paga";
        Baixa baixa = new Baixa(dataPagamento, "Caixa", "Impostos", "Pagamento de imposto (demonstrativo)",
                0, 0, 0, 0, conta.valor);
        conta.baixas.add(baixa);
        NotaFiscal.emitirDaBaixa(conta, baixa, conta.valor);
    }
}
