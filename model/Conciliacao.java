package model;

public class Conciliacao {

    private String data;
    private String descricaoExtrato;
    private String valorExtrato;
    private String valorSistema;
    private double diferenca;
    private String status;

    public Conciliacao(
            String data,
            String descricaoExtrato,
            String valorExtrato,
            String valorSistema,
            double diferenca,
            String status
    ) {

        this.data = data;
        this.descricaoExtrato = descricaoExtrato;
        this.valorExtrato = valorExtrato;
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

    public String getValorExtrato() {
        return valorExtrato;
    }

    public String getValorSistema() {
        return valorSistema;
    }

    public double getDiferenca() {
        return diferenca;
    }

    public String getStatus() {
        return status;
    }
}