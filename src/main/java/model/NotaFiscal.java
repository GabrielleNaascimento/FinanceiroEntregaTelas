package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Nota fiscal simples, só pra controle interno (não é uma NF-e de verdade).
 * Uma nota é gerada automaticamente a cada baixa (pagamento ou recebimento) e também dá pra emitir uma na mão.
 * Nada é apagado: se a conta a receber for estornada, as notas dela ficam com status "Estornada".
 */
public class NotaFiscal {

    // pagamento (conta a pagar) gera nota de entrada; recebimento (conta a receber) gera nota de saída
    public static final String ENTRADA = "Entrada";
    public static final String SAIDA = "Saída";

    public static final String EMITIDA = "Emitida";
    public static final String ESTORNADA = "Estornada";

    // dados fixos da empresa por enquanto (exemplo)
    public static final String EMPRESA_NOME = "Empresa Exemplo Ltda";
    public static final String EMPRESA_CNPJ = "00.000.000/0001-00";

    private static final ArrayList<NotaFiscal> notas = new ArrayList<>();

    private final int numero;
    private final String serie;
    private final LocalDate dataEmissao;
    private final String tipo;
    private final String entidade;
    private final String descricao;
    private final double valorBase;
    private final double juros;
    private final double multa;
    private final double desconto;
    private final double tarifa;
    private final double valorTotal;
    private final String contaFinanceira;
    private final String categoria;
    private final String observacao;
    // id da conta que originou a nota (0 = nota emitida manualmente, sem conta)
    private final int idConta;
    private String status = EMITIDA;

    private NotaFiscal(int numero, LocalDate dataEmissao, String tipo, String entidade, String descricao,
                       double valorBase, double juros, double multa, double desconto, double tarifa,
                       double valorTotal, String contaFinanceira, String categoria, String observacao,
                       int idConta) {
        this.numero = numero;
        this.serie = "1";
        this.dataEmissao = dataEmissao;
        this.tipo = tipo;
        this.entidade = entidade;
        this.descricao = descricao;
        this.valorBase = valorBase;
        this.juros = juros;
        this.multa = multa;
        this.desconto = desconto;
        this.tarifa = tarifa;
        this.valorTotal = valorTotal;
        this.contaFinanceira = contaFinanceira;
        this.categoria = categoria;
        this.observacao = observacao;
        this.idConta = idConta;
    }

    public int getNumero() {
        return numero;
    }

    public String getNumeroFormatado() {
        return String.format("%06d", numero);
    }

    public String getSerie() {
        return serie;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getDescricao() {
        return descricao;
    }

    /** Valor da conta que foi quitado nessa baixa, sem juros, multa, etc. */
    public double getValorBase() {
        return valorBase;
    }

    public double getJuros() {
        return juros;
    }

    public double getMulta() {
        return multa;
    }

    public double getDesconto() {
        return desconto;
    }

    public double getTarifa() {
        return tarifa;
    }

    /** O que realmente foi pago ou recebido. */
    public double getValorTotal() {
        return valorTotal;
    }

    public String getContaFinanceira() {
        return contaFinanceira;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getObservacao() {
        return observacao;
    }

    public int getIdConta() {
        return idConta;
    }

    public String getStatus() {
        return status;
    }

    public boolean isEntrada() {
        return tipo.equals(ENTRADA);
    }

    public boolean isManual() {
        return idConta == 0;
    }

    // ===================== EMISSÃO =====================

    /** Gera a nota de uma baixa. valorBase é a parte da conta que essa baixa quitou. */
    static NotaFiscal emitirDaBaixa(Contas conta, Contas.Baixa b, double valorBase) {
        String tipo = conta.getTipo().equals(Contas.PAGAR) ? ENTRADA : SAIDA;

        NotaFiscal nota = new NotaFiscal(notas.size() + 1, b.getData(), tipo, conta.getEntidade(),
                conta.getDescricao(), valorBase, b.getJuros(), b.getMulta(), b.getDesconto(), b.getTarifa(),
                b.getValorPago(), b.getContaFinanceira(), b.getCategoria(), b.getHistorico(), conta.getId());
        notas.add(nota);
        return nota;
    }

    /** Nota emitida na mão, sem ligação com nenhuma conta (não mexe no saldo). */
    public static NotaFiscal emitirManual(String tipo, String entidade, String descricao, double valor,
                                          LocalDate data, String observacao) {
        NotaFiscal nota = new NotaFiscal(notas.size() + 1, data, tipo, entidade, descricao,
                valor, 0, 0, 0, 0, valor, "", "", observacao, 0);
        notas.add(nota);
        return nota;
    }

    /** Quando uma conta a receber é estornada, as notas das baixas dela passam a Estornada. */
    static void marcarEstornadas(int idConta) {
        for (NotaFiscal n : notas) {
            if (n.idConta == idConta) {
                n.status = ESTORNADA;
            }
        }
    }

    // ===================== CONSULTA =====================

    /** Todas as notas, da mais antiga pra mais nova. */
    public static ArrayList<NotaFiscal> getNotas() {
        // garante que as contas de exemplo (e as notas delas) já foram carregadas
        Contas.getContas();
        return notas;
    }

    /** Última nota emitida (a que acabou de ser gerada por uma baixa). */
    public static NotaFiscal getUltima() {
        ArrayList<NotaFiscal> todas = getNotas();
        return todas.isEmpty() ? null : todas.get(todas.size() - 1);
    }
}