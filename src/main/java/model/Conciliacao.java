
package model;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Conciliacao {

    // ==========================================
    // CONSTANTES
    // ==========================================

    public static final String ENTRADA = "ENTRADA";
    public static final String SAIDA = "SAIDA";
    public static final String TRANSFERENCIA = "TRANSFERENCIA";

    public static final String PENDENTE = "Pendente";
    public static final String EM_ANDAMENTO = "Em andamento";
    public static final String FINALIZADA = "Finalizada";

    // ==========================================
    // INSTITUIÇÕES
    // ==========================================

    public static class Instituicao {

        private String nome;
        private String cnpj;
        private String codigoCompe;
        private boolean ativa;
        private JTable tabelaContas;
        private DefaultTableModel modeloContas;

        public Instituicao(String nome, String cnpj, String codigoCompe) {
            this.nome = nome;
            this.cnpj = cnpj;
            this.codigoCompe = codigoCompe;
            this.ativa = true;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getCodigoCompe() {
            return codigoCompe;
        }

        public void setCodigoCompe(String codigoCompe) {
            this.codigoCompe = codigoCompe;
        }

        public boolean isAtiva() {
            return ativa;
        }

        public void setAtiva(boolean ativa) {
            this.ativa = ativa;
        }
    }

    // ==========================================
    // CONTAS BANCÁRIAS
    // ==========================================

    public static class ContaBancaria {

        private Instituicao instituicao;
        private String cnpjEmpresa;
        private String agencia;
        private String numeroConta;
        private String tipoConta;
        private double saldoInicial;

        public ContaBancaria(
                Instituicao instituicao,
                String cnpjEmpresa,
                String agencia,
                String numeroConta,
                String tipoConta,
                double saldoInicial) {

            this.instituicao = instituicao;
            this.cnpjEmpresa = cnpjEmpresa;
            this.agencia = agencia;
            this.numeroConta = numeroConta;
            this.tipoConta = tipoConta;
            this.saldoInicial = saldoInicial;
        }

        public Instituicao getInstituicao() {
            return instituicao;
        }

        public void setInstituicao(Instituicao instituicao) {
            this.instituicao = instituicao;
        }

        public String getCnpjEmpresa() {
            return cnpjEmpresa;
        }

        public void setCnpjEmpresa(String cnpjEmpresa) {
            this.cnpjEmpresa = cnpjEmpresa;
        }

        public String getAgencia() {
            return agencia;
        }

        public void setAgencia(String agencia) {
            this.agencia = agencia;
        }

        public String getNumeroConta() {
            return numeroConta;
        }

        public void setNumeroConta(String numeroConta) {
            this.numeroConta = numeroConta;
        }

        public String getTipoConta() {
            return tipoConta;
        }

        public void setTipoConta(String tipoConta) {
            this.tipoConta = tipoConta;
        }

        public double getSaldoInicial() {
            return saldoInicial;
        }

        public void setSaldoInicial(double saldoInicial) {
            this.saldoInicial = saldoInicial;
        }

        // Calcula o saldo considerando entradas, saídas e transferências.

        public double getSaldoAtual() {

            double saldo = saldoInicial;

            for (MovimentacaoBancaria movimentacao : movimentacoes) {

                if (movimentacao.getConta() == this) {

                    if (movimentacao.getTipo().equals(ENTRADA)) {
                        saldo += movimentacao.getValor();

                    } else if (movimentacao.getTipo().equals(SAIDA)
                            || movimentacao.getTipo().equals(TRANSFERENCIA)) {

                        saldo -= movimentacao.getValor();
                    }
                }

                if (movimentacao.getTipo().equals(TRANSFERENCIA)
                        && movimentacao.getContaDestino() == this) {

                    saldo += movimentacao.getValor();
                }
            }

            return arredondar(saldo);
        }

        // Lista as movimentações desta conta.

        public ArrayList<MovimentacaoBancaria> getMovimentacoes() {

            ArrayList<MovimentacaoBancaria> resultado = new ArrayList<>();

            for (MovimentacaoBancaria movimentacao : movimentacoes) {

                if (movimentacao.getConta() == this
                        || movimentacao.getContaDestino() == this) {

                    resultado.add(movimentacao);
                }
            }

            return resultado;
        }
    }

    // ==========================================
    // MOVIMENTAÇÕES BANCÁRIAS
    // ==========================================

    public static class MovimentacaoBancaria {

        private final int id;
        private ContaBancaria conta;
        private ContaBancaria contaDestino;
        private LocalDate data;
        private String descricao;
        private double valor;
        private String tipo;

        public MovimentacaoBancaria(
                int id,
                ContaBancaria conta,
                ContaBancaria contaDestino,
                LocalDate data,
                String descricao,
                double valor,
                String tipo) {

            this.id = id;
            this.conta = conta;
            this.contaDestino = contaDestino;
            this.data = data;
            this.descricao = descricao;
            this.valor = valor;
            this.tipo = tipo;
        }

        public int getId() {
            return id;
        }

        public ContaBancaria getConta() {
            return conta;
        }

        public void setConta(ContaBancaria conta) {
            this.conta = conta;
        }

        public ContaBancaria getContaDestino() {
            return contaDestino;
        }

        public void setContaDestino(ContaBancaria contaDestino) {
            this.contaDestino = contaDestino;
        }

        public LocalDate getData() {
            return data;
        }

        public void setData(LocalDate data) {
            this.data = data;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    }

    // ==========================================
    // ITENS DA CONCILIAÇÃO
    // ==========================================

    public static class ItemConciliacao {

        private Contas contaFluxo;
        private Contas.Baixa baixa;
        private MovimentacaoBancaria movimentacao;

        public ItemConciliacao(
                Contas contaFluxo,
                Contas.Baixa baixa,
                MovimentacaoBancaria movimentacao) {

            this.contaFluxo = contaFluxo;
            this.baixa = baixa;
            this.movimentacao = movimentacao;
        }

        public Contas getContaFluxo() {
            return contaFluxo;
        }

        public Contas.Baixa getBaixa() {
            return baixa;
        }

        public MovimentacaoBancaria getMovimentacao() {
            return movimentacao;
        }

        public boolean isAssociado() {
            return baixa != null && movimentacao != null;
        }
    }

    // ==========================================
    // REGISTRO DE CONCILIAÇÃO
    // ==========================================

    public static class RegistroConciliacao {

        private ContaBancaria conta;
        private LocalDate dataInicio;
        private LocalDate dataFim;
        private LocalDate dataFinalizacao;
        private String responsavel;
        private String status;

        private double saldoInicial;
        private double saldoFinalBanco;
        private double saldoFinalSistema;
        private double diferenca;

        private final ArrayList<ItemConciliacao> itens =
                new ArrayList<>();

        public RegistroConciliacao(
                ContaBancaria conta,
                LocalDate dataInicio,
                LocalDate dataFim,
                String responsavel,
                double saldoInicial) {

            this.conta = conta;
            this.dataInicio = dataInicio;
            this.dataFim = dataFim;
            this.responsavel = responsavel;
            this.saldoInicial = saldoInicial;
            this.status = PENDENTE;
        }

        public ContaBancaria getConta() {
            return conta;
        }

        public LocalDate getDataInicio() {
            return dataInicio;
        }

        public LocalDate getDataFim() {
            return dataFim;
        }

        public LocalDate getDataFinalizacao() {
            return dataFinalizacao;
        }

        public String getResponsavel() {
            return responsavel;
        }

        public String getStatus() {
            return status;
        }

        public double getSaldoInicial() {
            return saldoInicial;
        }

        public double getSaldoFinalBanco() {
            return saldoFinalBanco;
        }

        public double getSaldoFinalSistema() {
            return saldoFinalSistema;
        }

        public double getDiferenca() {
            return diferenca;
        }

        public ArrayList<ItemConciliacao> getItens() {
            return itens;
        }

        public void adicionarItem(ItemConciliacao item) {
            if (item != null && !itens.contains(item)) {
                itens.add(item);
                status = EM_ANDAMENTO;
            }
        }

        public void removerItem(ItemConciliacao item) {
            itens.remove(item);

            if (itens.isEmpty()) {
                status = PENDENTE;
            }
        }

        public void finalizar(
                double saldoFinalBanco,
                double saldoFinalSistema) {

            this.saldoFinalBanco = arredondar(saldoFinalBanco);
            this.saldoFinalSistema = arredondar(saldoFinalSistema);

            this.diferenca = arredondar(
                    this.saldoFinalBanco - this.saldoFinalSistema
            );

            this.dataFinalizacao = LocalDate.now();
            this.status = FINALIZADA;
        }
    }

    // ==========================================
    // LISTAS PRINCIPAIS
    // ==========================================

    private static final ArrayList<Instituicao> instituicoes =
            new ArrayList<>();

    private static final ArrayList<ContaBancaria> contas =
            new ArrayList<>();

    private static final ArrayList<MovimentacaoBancaria> movimentacoes =
            new ArrayList<>();

    private static final ArrayList<RegistroConciliacao> conciliacoes =
            new ArrayList<>();

    // ==========================================
    // INSTITUIÇÕES - OPERAÇÕES
    // ==========================================

    public static ArrayList<Instituicao> getInstituicoes() {
        return instituicoes;
    }

    public static void cadastrarInstituicao(Instituicao instituicao) {
        if (instituicao != null) {
            instituicoes.add(instituicao);
        }
    }

    public static void editarInstituicao(
            int indice,
            Instituicao instituicaoAtualizada) {

        if (indice >= 0 && indice < instituicoes.size()
                && instituicaoAtualizada != null) {

            Instituicao atual = instituicoes.get(indice);

            atual.setNome(instituicaoAtualizada.getNome());
            atual.setCnpj(instituicaoAtualizada.getCnpj());
            atual.setCodigoCompe(instituicaoAtualizada.getCodigoCompe());
        }
    }

    public static void alterarSituacaoInstituicao(
            int indice,
            boolean ativa) {

        if (indice >= 0 && indice < instituicoes.size()) {
            instituicoes.get(indice).setAtiva(ativa);
        }
    }

    // ==========================================
    // CONTAS BANCÁRIAS - OPERAÇÕES
    // ==========================================

    public static ArrayList<ContaBancaria> getContasBancarias() {
        return contas;
    }

    public static void cadastrarConta(ContaBancaria conta) {
        if (conta != null) {
            contas.add(conta);
        }
    }

    public static void editarConta(
            int indice,
            ContaBancaria contaAtualizada) {

        if (indice >= 0 && indice < contas.size()
                && contaAtualizada != null) {

            ContaBancaria atual = contas.get(indice);

            atual.setInstituicao(contaAtualizada.getInstituicao());
            atual.setCnpjEmpresa(contaAtualizada.getCnpjEmpresa());
            atual.setAgencia(contaAtualizada.getAgencia());
            atual.setNumeroConta(contaAtualizada.getNumeroConta());
            atual.setTipoConta(contaAtualizada.getTipoConta());
            atual.setSaldoInicial(contaAtualizada.getSaldoInicial());
        }
    }

    public static boolean excluirConta(int indice) {

        if (indice < 0 || indice >= contas.size()) {
            return false;
        }

        ContaBancaria conta = contas.get(indice);

        // Não permite excluir contas que possuem movimentações.

        if (!conta.getMovimentacoes().isEmpty()) {
            return false;
        }

        // Não permite excluir contas presentes em conciliações.

        for (RegistroConciliacao conciliacao : conciliacoes) {
            if (conciliacao.getConta() == conta) {
                return false;
            }
        }

        contas.remove(indice);
        return true;
    }

    // ==========================================
    // MOVIMENTAÇÕES - OPERAÇÕES
    // ==========================================

    public static ArrayList<MovimentacaoBancaria> getMovimentacoes() {
        return movimentacoes;
    }

    public static void cadastrarMovimentacao(
            MovimentacaoBancaria movimentacao) {

        if (movimentacao != null) {
            movimentacoes.add(movimentacao);
        }
    }

    public static void editarMovimentacao(
            int indice,
            MovimentacaoBancaria movimentacaoAtualizada) {

        if (indice >= 0 && indice < movimentacoes.size()
                && movimentacaoAtualizada != null) {

            MovimentacaoBancaria atual = movimentacoes.get(indice);

            atual.setConta(movimentacaoAtualizada.getConta());
            atual.setContaDestino(movimentacaoAtualizada.getContaDestino());
            atual.setData(movimentacaoAtualizada.getData());
            atual.setDescricao(movimentacaoAtualizada.getDescricao());
            atual.setValor(movimentacaoAtualizada.getValor());
            atual.setTipo(movimentacaoAtualizada.getTipo());
        }
    }

    public static void excluirMovimentacao(int indice) {

        if (indice >= 0 && indice < movimentacoes.size()) {
            movimentacoes.remove(indice);
        }
    }

    public static MovimentacaoBancaria buscarMovimentacaoPorId(int id) {

        for (MovimentacaoBancaria movimentacao : movimentacoes) {

            if (movimentacao.getId() == id) {
                return movimentacao;
            }
        }

        return null;
    }

    // ==========================================
    // CONCILIAÇÕES - OPERAÇÕES
    // ==========================================

    public static ArrayList<RegistroConciliacao> getConciliacoes() {
        return conciliacoes;
    }

    public static void cadastrarConciliacao(
            RegistroConciliacao conciliacao) {

        if (conciliacao != null) {
            conciliacoes.add(conciliacao);
        }
    }

    public static RegistroConciliacao buscarConciliacao(
            int indice) {

        if (indice >= 0 && indice < conciliacoes.size()) {
            return conciliacoes.get(indice);
        }

        return null;
    }

    // ==========================================
    // SALDO TOTAL
    // ==========================================

    public static double getSaldoTotal() {

        double total = 0;

        for (ContaBancaria conta : contas) {
            total += conta.getSaldoAtual();
        }

        return arredondar(total);
    }

    // ==========================================
    // UTILITÁRIOS
    // ==========================================

    private static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
