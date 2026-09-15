package model;

import java.util.ArrayList;

public class Contas {

    private String entidade; // empresa ou fornecedor
    private String descricao; // descrição da conta
    private String vencimento; // vencimento da conta
    private double valor; // valor da conta
    private String tipo; // tipo dela, pagar/receber
    private String status; // pendente, vencida e paga

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

    public static ArrayList<Contas> getContas() {

        ArrayList<Contas> contas = new ArrayList<>();

        contas.add(new Contas(
                "Amazon",
                "Locação Servidores",
                "15/08/2026",
                4500.00,
                "PAGAR",
                "Vencida"
        ));

        contas.add(new Contas(
                "Microsoft",
                "Licenças Office",
                "20/09/2026",
                3200.00,
                "PAGAR",
                "Pendente"
        ));

        contas.add(new Contas(
                "Xavier",
                "Assessoria Jurídica",
                "05/08/2026",
                8500.00,
                "PAGAR",
                "Paga"
        ));

        contas.add(new Contas(
                "Imobiliária",
                "Aluguel Sala",
                "10/10/2026",
                12000.00,
                "PAGAR",
                "Pendente"
        ));

        contas.add(new Contas(
                "Tech Solutions",
                "Impl. ERP",
                "01/08/2026",
                15000.00,
                "RECEBER",
                "Recebida"
        ));

        contas.add(new Contas(
                "Metalúrgica",
                "Consultoria",
                "12/08/2026",
                8540.00,
                "RECEBER",
                "Atrasada"
        ));

        contas.add(new Contas(
                "Hospital",
                "Manut. Hardware",
                "25/08/2026",
                3800.00,
                "RECEBER",
                "Pendente"
        ));

        contas.add(new Contas(
                "Banco Nacional",
                "Suporte TI",
                "18/08/2026",
                4500.00,
                "RECEBER",
                "Recebida"
        ));

        return contas;
    }
}

