package model;

public class ResumoMensal {
    private String mes;
    private double receita;
    private double despesa;

    public ResumoMensal(String mes, double receita, double despesa) {
        this.mes = mes;
        this.receita = receita;
        this.despesa = despesa;
    }

    public String getMes() {
        return mes;
    }
    public Double getReceita() {
        return receita;
    }
    public Double getDespesa() {
        return despesa;
    }
    
}
