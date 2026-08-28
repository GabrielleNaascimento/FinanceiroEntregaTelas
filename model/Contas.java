package model;

public class Contas {

    private String entidade;
    private String descricao;
    private String vencimento;
    private double valor;
    private String tipo;
    private String status;

    public Contas(String entidade, String descricao, String vencimento,
                  double valor, String tipo, String status) {
        this.entidade = entidade;
        this.descricao = descricao;
        this.vencimento = vencimento;
        this.valor = valor;
        this.tipo = tipo;
        this.status = status;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getVencimento() {
        return vencimento;
    }

    public double getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus() {
        return status;
    }
}