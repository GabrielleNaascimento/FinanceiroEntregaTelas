package model;

import java.util.ArrayList;
import java.util.List;

public class Relatorio {

    private String periodo;

    private double receitaTotal;
    private double despesaTotal;
    private double lucroTotal;
    private double margemLucro;

    private List<ResumoMensal> resumoMensal;
    private List<CategoriaDespesa> despesasPorCategoria;

    public Relatorio() {

        periodo = "Último Semestre";

        receitaTotal = 0;
        despesaTotal = 0;
        lucroTotal = 0;
        margemLucro = 0;

        resumoMensal = new ArrayList<>();
        despesasPorCategoria = new ArrayList<>();

        carregarDados();
    }
    private void carregarDados() {

        resumoMensal.add(new ResumoMensal(
                "Junho 2025",
                120000,
                80000,
                40000,
                33.3
        ));

        resumoMensal.add(new ResumoMensal(
                "Julho 2025",
                145000,
                90000,
                55000,
                37.9
        ));

        resumoMensal.add(new ResumoMensal(
                "Agosto 2025",
                130000,
                85000,
                45000,
                34.6
        ));

        resumoMensal.add(new ResumoMensal(
                "Setembro 2025",
                160000,
                110000,
                50000,
                31.2
        ));

        resumoMensal.add(new ResumoMensal(
                "Outubro 2025",
                156300,
                98420,
                57880,
                37.0
        ));

        resumoMensal.add(new ResumoMensal(
                "Novembro 2025",
                170000,
                105000,
                65000,
                38.2
        ));

        despesasPorCategoria.add(
                new CategoriaDespesa("Folha de Pagamento", 35)
        );

        despesasPorCategoria.add(
                new CategoriaDespesa("Fornecedores", 25)
        );

        despesasPorCategoria.add(
                new CategoriaDespesa("Impostos", 20)
        );

        despesasPorCategoria.add(
                new CategoriaDespesa("Operacional", 12)
        );

        despesasPorCategoria.add(
                new CategoriaDespesa("Outros", 8)
        );

        calcularTotais();
    }

    private void calcularTotais() {

        receitaTotal = 0;
        despesaTotal = 0;

        for (ResumoMensal resumo : resumoMensal) {

            receitaTotal += resumo.getReceita();
            despesaTotal += resumo.getDespesa();
        }

        lucroTotal = receitaTotal - despesaTotal;

        if (receitaTotal > 0) {
            margemLucro = (lucroTotal / receitaTotal) * 100;
        }
    }

    public String getPeriodo() {
        return periodo;
    }

    public double getReceitaTotal() {
        return receitaTotal;
    }

    public double getDespesaTotal() {
        return despesaTotal;
    }

    public double getLucroTotal() {
        return lucroTotal;
    }

    public double getMargemLucro() {
        return margemLucro;
    }

    public List<ResumoMensal> getResumoMensal() {
        return resumoMensal;
    }

    public List<CategoriaDespesa> getDespesasPorCategoria() {
        return despesasPorCategoria;
    }
    public static class ResumoMensal {

        private String mes;
        private double receita;
        private double despesa;
        private double lucro;
        private double margem;

        public ResumoMensal(
                String mes,
                double receita,
                double despesa,
                double lucro,
                double margem) {

            this.mes = mes;
            this.receita = receita;
            this.despesa = despesa;
            this.lucro = lucro;
            this.margem = margem;
        }

        public String getMes() {
            return mes;
        }

        public double getReceita() {
            return receita;
        }

        public double getDespesa() {
            return despesa;
        }

        public double getLucro() {
            return lucro;
        }

        public double getMargem() {
            return margem;
        }
    }
    public static class CategoriaDespesa {

        private String categoria;
        private double percentual;

        public CategoriaDespesa(String categoria, double percentual) {
            this.categoria = categoria;
            this.percentual = percentual;
        }

        public String getCategoria() {
            return categoria;
        }

        public double getPercentual() {
            return percentual;
        }
    }
}