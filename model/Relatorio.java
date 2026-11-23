<<<<<<< Updated upstream:model/Relatorio.java
package model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

    // Nota para a pessoa de integracao ou de banco: A tela de Relatorios tem um 
    // seletor que muda o que aparece na tela. O valor que se origina de um mes
    // especifico teria que ser puxado do banco de dados. Eu vou usar YearMonth
    // para definir os meses com alguns ifs. Para o java nao dar problema, vou
    // deixar os valores em 0, mas o codigo esta preparado para voces 
    // substituirem por consultas no banco de dados. 
public class Relatorio {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate filtroInicio;
    private LocalDate filtroFim;

    private double receitaTotal;
    private double despesaTotal;
    private double lucroTotal;
    private double margemLucro;

    private List<ResumoMensal> resumoMensal;
    private List<CategoriaDespesa> despesasPorCategoria;

    public Relatorio() {

        receitaTotal = 0;
        despesaTotal = 0;
        lucroTotal = 0;
        margemLucro = 0;

        resumoMensal = new ArrayList<>();
        despesasPorCategoria = new ArrayList<>();

        definirPeriodoInicial();
        carregarDados();
    }

    public void setPeriodo(LocalDate inicio, LocalDate fim) {

        filtroInicio = inicio;
        filtroFim = fim;

        carregarDados();
    }

    public void atualizar() {

        carregarDados();
    }

    private void definirPeriodoInicial() {

        LocalDate menor = LocalDate.now();
        LocalDate maior = LocalDate.now();

        ArrayList<Contas> todas = Contas.getContas();

        if (!todas.isEmpty()) {

            menor = todas.get(0).getDataVencimento();
            maior = menor;

            for (Contas conta : todas) {

                if (conta.getDataVencimento().isBefore(menor)) {
                    menor = conta.getDataVencimento();
                }

                if (conta.getDataVencimento().isAfter(maior)) {
                    maior = conta.getDataVencimento();
                }
            }
        }

        filtroInicio = menor.withDayOfMonth(1);
        filtroFim = maior.withDayOfMonth(maior.lengthOfMonth());
    }

    private void carregarDados() {

        resumoMensal.clear();
        despesasPorCategoria.clear();

        YearMonth mesInicial = YearMonth.from(filtroInicio);
        YearMonth mesFinal = YearMonth.from(filtroFim);

        for (YearMonth mesReferencia = mesInicial;
             !mesReferencia.isAfter(mesFinal);
             mesReferencia = mesReferencia.plusMonths(1)) {

            double receita = 0;
            double despesa = 0;

            for (Contas conta : Contas.getContas()) {

                LocalDate data = conta.getDataVencimento();

                if (data.isBefore(filtroInicio) || data.isAfter(filtroFim)) {
                    continue;
                }

                if (YearMonth.from(data).equals(mesReferencia)) {

                    if (conta.getTipo().equals(Contas.RECEBER)) {
                        receita += conta.getValor();
                    }

                    if (conta.getTipo().equals(Contas.PAGAR)) {
                        despesa += conta.getValor();
                    }
                }
            }

            double lucro = receita - despesa;
            double margem = 0;

            if (receita > 0) {
                margem = (lucro / receita) * 100;
            }

            resumoMensal.add(new ResumoMensal(
                    mesReferencia,
                    receita,
                    despesa,
                    lucro,
                    margem
            ));
        }

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
        margemLucro = 0;

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
        return FORMATO_DATA.format(filtroInicio) + " a " + FORMATO_DATA.format(filtroFim);
    }

    public LocalDate getFiltroInicio() {
        return filtroInicio;
    }

    public LocalDate getFiltroFim() {
        return filtroFim;
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

        private YearMonth mes;
        private double receita;
        private double despesa;
        private double lucro;
        private double margem;

        public ResumoMensal(
                YearMonth mes,
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

        public YearMonth getMes() {
            return mes;
        }

        public String getMesFormatado() {
            return mes.format(
                    DateTimeFormatter.ofPattern(
                            "MMM/yy",
                            new Locale("pt", "BR")
                    )
            );
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
=======
package model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

    // Nota para a pessoa de integracao ou de banco: A tela de Relatorios tem um 
    // seletor que muda o que aparece na tela. O valor que se origina de um mes
    // especifico teria que ser puxado do banco de dados. Eu vou usar YearMonth
    // para definir os meses com alguns ifs. Para o java nao dar problema, vou
    // deixar os valores em 0, mas o codigo esta preparado para voces 
    // substituirem por consultas no banco de dados. 
public class Relatorio {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate filtroInicio;
    private LocalDate filtroFim;

    private double receitaTotal;
    private double despesaTotal;
    private double lucroTotal;
    private double margemLucro;

    private List<ResumoMensal> resumoMensal;
    private List<CategoriaDespesa> despesasPorCategoria;

    public Relatorio() {

        receitaTotal = 0;
        despesaTotal = 0;
        lucroTotal = 0;
        margemLucro = 0;

        resumoMensal = new ArrayList<>();
        despesasPorCategoria = new ArrayList<>();

        definirPeriodoInicial();
        carregarDados();
    }

    public void setPeriodo(LocalDate inicio, LocalDate fim) {

        filtroInicio = inicio;
        filtroFim = fim;

        carregarDados();
    }

    public void atualizar() {

        carregarDados();
    }

    private void definirPeriodoInicial() {

        LocalDate menor = LocalDate.now();
        LocalDate maior = LocalDate.now();

        ArrayList<Contas> todas = Contas.getContas();

        if (!todas.isEmpty()) {

            menor = todas.get(0).getDataVencimento();
            maior = menor;

            for (Contas conta : todas) {

                if (conta.getDataVencimento().isBefore(menor)) {
                    menor = conta.getDataVencimento();
                }

                if (conta.getDataVencimento().isAfter(maior)) {
                    maior = conta.getDataVencimento();
                }
            }
        }

        filtroInicio = menor.withDayOfMonth(1);
        filtroFim = maior.withDayOfMonth(maior.lengthOfMonth());
    }

    private void carregarDados() {

        resumoMensal.clear();
        despesasPorCategoria.clear();

        YearMonth mesInicial = YearMonth.from(filtroInicio);
        YearMonth mesFinal = YearMonth.from(filtroFim);

        for (YearMonth mesReferencia = mesInicial;
             !mesReferencia.isAfter(mesFinal);
             mesReferencia = mesReferencia.plusMonths(1)) {

            double receita = 0;
            double despesa = 0;

            for (Contas conta : Contas.getContas()) {

                LocalDate data = conta.getDataVencimento();

                if (data.isBefore(filtroInicio) || data.isAfter(filtroFim)) {
                    continue;
                }

                if (YearMonth.from(data).equals(mesReferencia)) {

                    if (conta.getTipo().equals(Contas.RECEBER)) {
                        receita += conta.getValor();
                    }

                    if (conta.getTipo().equals(Contas.PAGAR)) {
                        despesa += conta.getValor();
                    }
                }
            }

            double lucro = receita - despesa;
            double margem = 0;

            if (receita > 0) {
                margem = (lucro / receita) * 100;
            }

            resumoMensal.add(new ResumoMensal(
                    mesReferencia,
                    receita,
                    despesa,
                    lucro,
                    margem
            ));
        }

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
        margemLucro = 0;

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
        return FORMATO_DATA.format(filtroInicio) + " a " + FORMATO_DATA.format(filtroFim);
    }

    public LocalDate getFiltroInicio() {
        return filtroInicio;
    }

    public LocalDate getFiltroFim() {
        return filtroFim;
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

        private YearMonth mes;
        private double receita;
        private double despesa;
        private double lucro;
        private double margem;

        public ResumoMensal(
                YearMonth mes,
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

        public YearMonth getMes() {
            return mes;
        }

        public String getMesFormatado() {
            return mes.format(
                    DateTimeFormatter.ofPattern(
                            "MMM/yy",
                            new Locale("pt", "BR")
                    )
            );
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
>>>>>>> Stashed changes:src/main/java/model/Relatorio.java
}