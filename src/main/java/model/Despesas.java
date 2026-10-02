package model;

public class Despesas {
    private int id;
    private String categoria;
    private double valor;



    public Despesas() {

    }
    public Despesas(int id, String categoria, double valor) {
        this.id = id;
        this.categoria = categoria;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
