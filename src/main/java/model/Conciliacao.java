package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Conciliacao {

    public static final String PAGAR = "PAGAR";
    public static final String RECEBER = "RECEBER";

    private LocalDate data;
    private String descricaoExtrato;
    private double valorExtrato;
    private String descricaoSistema;
    private double valorSistema;
    private double diferenca;
    private String status;
    private String tipo;

    private static final ArrayList<Conciliacao> movimentacoes =
            new ArrayList<>();

    public Conciliacao(
            LocalDate data,
            String descricaoExtrato,
            double valorExtrato,
            String descricaoSistema,
            double valorSistema,
            double diferenca,
            String status,
            String tipo
    ) {
        this.data = data;
        this.descricaoExtrato = descricaoExtrato;
        this.valorExtrato = valorExtrato;
        this.descricaoSistema = descricaoSistema;
        this.valorSistema = valorSistema;
        this.diferenca = diferenca;
        this.status = status;
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricaoExtrato() {
        return descricaoExtrato;
    }

    public double getValorExtrato() {
        return valorExtrato;
    }

    public String getDescricaoSistema() {
        return descricaoSistema;
    }

    public double getValorSistema() {
        return valorSistema;
    }

    public double getDiferenca() {
        return diferenca;
    }

    public String getStatus() {
        return status;
    }

    public String getTipo() {
        return tipo;
    }

    public static ArrayList<Conciliacao> getMovimentacoes() {

        if (movimentacoes.isEmpty()) {
            carregarDados();
        }

        return movimentacoes;
    }

    public static int getTotalConciliadas() {
    int total = 0;

    for (Conciliacao c : getMovimentacoes()) {
        if ("Conciliado".equals(c.getStatus())) {
            total++;
        }
    }

    return total;
}

public static double getTotalDiferenca() {
    double total = 0;

    for (Conciliacao c : getMovimentacoes()) {
        total += Math.abs(c.getDiferenca());
    }

    return total;
}

public static int getPendentes(String tipo) {
    int total = 0;

    for (Conciliacao c : getMovimentacoes()) {
        if (tipo.equals(c.getTipo())
                && "Pendente".equals(c.getStatus())) {
            total++;
        }
    }

    return total;
}

public static int getAtrasados(String tipo) {
    int total = 0;

    for (Contas conta : Contas.getContas()) {
        if (tipo.equals(conta.getTipo())
                && "Atrasada".equals(conta.getStatus())) {
            total++;
        }

        if (PAGAR.equals(tipo)
                && "Vencida".equals(conta.getStatus())) {
            total++;
        }
    }

    return total;
}

public static ArrayList<Conciliacao> listarPorPeriodo(
        LocalDate inicio,
        LocalDate fim) {

    ArrayList<Conciliacao> resultado = new ArrayList<>();

    for (Conciliacao c : getMovimentacoes()) {
        if (!c.getData().isBefore(inicio)
                && !c.getData().isAfter(fim)) {

            resultado.add(c);
        }
    }

    resultado.sort(
            Comparator.comparing(Conciliacao::getData)
    );

    return resultado;
}

    private static void carregarDados() {

        movimentacoes.add(new Conciliacao(
                LocalDate.of(2026, 10, 10),
                "Recebimento Tech Solutions",
                15000.00,
                "Recebimento Tech Solutions",
                15000.00,
                0.00,
                "Conciliado",
                RECEBER
        ));

        movimentacoes.add(new Conciliacao(
                LocalDate.of(2026, 10, 11),
                "Pgto AWS Cloud Server",
                4850.00,
                "Pgto AWS Cloud Server",
                4800.00,
                50.00,
                "Divergente",
                PAGAR
        ));

        movimentacoes.add(new Conciliacao(
                LocalDate.of(2026, 10, 12),
                "Recebimento Metalúrgica",
                28450.00,
                "Recebimento Metalúrgica",
                28450.00,
                0.00,
                "Conciliado",
                RECEBER
        ));

        movimentacoes.add(new Conciliacao(
                LocalDate.of(2026, 10, 14),
                "Pgto Aluguel Comercial",
                12000.00,
                "Pgto Aluguel Comercial",
                11900.00,
                100.00,
                "Divergente",
                PAGAR
        ));

        movimentacoes.add(new Conciliacao(
                LocalDate.of(2026, 10, 15),
                "Pgto Folha de Colaboradores",
                45200.00,
                "Pgto Folha de Colaboradores",
                45200.00,
                0.00,
                "Conciliado",
                PAGAR
        ));

        movimentacoes.add(new Conciliacao(
                LocalDate.of(2026, 10, 16),
                "Recebimento Vânia Vida",
                8900.00,
                "Recebimento Vânia Vida",
                9000.00,
                -100.00,
                "Divergente",
                RECEBER
        ));

        movimentacoes.sort(
                Comparator.comparing(Conciliacao::getData)
        );
    }
}