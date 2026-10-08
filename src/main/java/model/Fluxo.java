package model;

import java.time.LocalDate;

public class Fluxo {

    private LocalDate data;
    private String descricao;
    private String tipo;
    private String categoria;
    private double valor;
    private double saldo;

    public Fluxo(
            LocalDate data,
            String descricao,
            String tipo,
            String categoria,
            double valor,
            double saldo) {

        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.saldo = saldo;
    }

    public LocalDate getData() {
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

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
