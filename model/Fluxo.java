package model;

public class Fluxo {

    private String data;
    private String descricao;
    private String tipo;
    private String categoria;
    private double valor;
    private double saldo;

    public Fluxo(String data, String descricao, String tipo,
                        String categoria, double valor, double saldo) {

        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.saldo = saldo;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValor() {
        return valor;
    }

    public double getSaldo() {
        return saldo;
    }
}