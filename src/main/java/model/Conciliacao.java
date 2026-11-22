
package model;

import java.util.ArrayList;

public class Conciliacao {

    private String codigoBanco;
    private String agencia;
    private String numeroConta;
    private double saldo;

    private static final ArrayList<Conciliacao> bancos =
            new ArrayList<>();

    public Conciliacao(
            String codigoBanco,
            String agencia,
            String numeroConta,
            double saldo) {

        this.codigoBanco = codigoBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
    }

    // Getters

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public double getSaldo() {
        return saldo;
    }

    // Setters

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Listar bancos

    public static ArrayList<Conciliacao> getBancos() {
        return bancos;
    }

    // Cadastrar banco

    public static void cadastrarBanco(Conciliacao banco) {
        bancos.add(banco);
    }

    // Editar banco

    public static void editarBanco(
            int indice,
            Conciliacao bancoAtualizado) {

        if (indice >= 0 && indice < bancos.size()) {
            bancos.set(indice, bancoAtualizado);
        }
    }

    // Excluir banco

    public static void excluirBanco(int indice) {

        if (indice >= 0 && indice < bancos.size()) {
            bancos.remove(indice);
        }
    }

    // Calcular saldo total

    public static double getSaldoTotal() {

        double total = 0;

        for (Conciliacao banco : bancos) {
            total += banco.getSaldo();
        }

        return total;
    }
}
