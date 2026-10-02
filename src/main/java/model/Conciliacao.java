package model;

public class Conciliacao {

    private String data;
    private String descricaoExtrato;
    private double valorExtrato;
    private String descricaoSistema;
    private double valorSistema;
    private double diferenca;
    private String status;

    public Conciliacao(
            String data,
            String descricaoExtrato,
            double valorExtrato,
            String descricaoSistema,
            double valorSistema,
            double diferenca,
            String status
    ) {

        this.data = data;
        this.descricaoExtrato = descricaoExtrato;
        this.valorExtrato = valorExtrato;
        this.descricaoSistema = descricaoSistema;
        this.valorSistema = valorSistema;
        this.diferenca = diferenca;
        this.status = status;
    }

    public String getData() {
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
}