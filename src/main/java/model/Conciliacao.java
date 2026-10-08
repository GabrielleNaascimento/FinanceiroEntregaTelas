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

        for (Contas conta : Contas.getContas()) {

            if (tipo.equals(conta.getTipo())
                    && "Pendente".equals(conta.getStatus())) {

                total++;
            }
        }

        return total;
    }

    public static int getAtrasados(String tipo) {
        int total = 0;

        for (Contas conta : Contas.getContas()) {

            if (tipo.equals(conta.getTipo())
                    && ("Vencida".equals(conta.getStatus())
                    || "Atrasada".equals(conta.getStatus()))) {

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

        movimentacoes.clear();

        for (Contas conta : Contas.getContas()) {

            double valorSistema = conta.getValor();
            double valorExtrato = valorSistema;
            double diferenca = 0;

            String status;

            switch (conta.getStatus()) {

                case "Paga":
                case "Recebida":

                    /*
                     * Algumas movimentações terão uma pequena
                     * diferença para demonstrar a conciliação.
                     */

                    if (conta.getId() == 11) {
                        valorExtrato = 4600.00;
                    }

                    diferenca = valorExtrato - valorSistema;

                    if (diferenca == 0) {
                        status = "Conciliado";
                    } else {
                        status = "Divergente";
                    }

                    break;

                case "Pendente":
                    status = "Pendente";
                    break;

                case "Vencida":
                case "Atrasada":
                    status = "Atrasada";
                    break;

                default:
                    status = conta.getStatus();
                    break;
            }

            movimentacoes.add(
                    new Conciliacao(
                            conta.getDataVencimento(),

                            conta.getEntidade()
                                    + " - "
                                    + conta.getDescricao(),

                            valorExtrato,

                            conta.getDescricao(),

                            valorSistema,

                            diferenca,

                            status,

                            conta.getTipo()
                    )
            );
        }

        movimentacoes.sort(
                Comparator.comparing(Conciliacao::getData)
        );
    }
}