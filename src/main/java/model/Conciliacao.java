package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Conciliacao {

    // Constante que identifica contas que precisam ser pagas.
    public static final String PAGAR = "PAGAR";

    // Constante que identifica contas que precisam ser recebidas.
    public static final String RECEBER = "RECEBER";

    // Data da movimentação.
    // Tipo: LocalDate. Função: armazenar a data da conciliação.
    private LocalDate data;

    // Descrição da movimentação presente no extrato bancário.
    // Tipo: String. Função: identificar o lançamento do banco.
    private String descricaoExtrato;

    // Valor registrado no extrato bancário.
    // Tipo: double. Função: armazenar o valor bancário.
    private double valorExtrato;

    // Descrição da movimentação registrada no sistema.
    // Tipo: String. Função: identificar o lançamento no ERP.
    private String descricaoSistema;

    // Valor registrado no sistema ERP.
    // Tipo: double. Função: armazenar o valor do sistema.
    private double valorSistema;

    // Diferença entre o valor do extrato e o valor do sistema.
    // Tipo: double. Função: identificar divergências financeiras.
    private double diferenca;

    // Situação atual da movimentação.
    // Tipo: String. Função: informar se está conciliada, pendente ou divergente.
    private String status;

    // Tipo da movimentação, como PAGAR ou RECEBER.
    // Tipo: String. Função: classificar a movimentação.
    private String tipo;

    // Lista estática que armazena todas as conciliações.
    // Tipo: ArrayList<Conciliacao>. Função: manter os dados disponíveis para a aplicação.
    private static final ArrayList<Conciliacao> movimentacoes =
            new ArrayList<>();

    public Conciliacao(
            LocalDate data,
            String descricaoExtrato,
            double valorExtrato,
            String descricaoSistema,
            double valorSistema,
            double diferenca,
            String status,
            String tipo
    ) {

        // Atribui os dados recebidos aos atributos do objeto.
        this.data = data;
        this.descricaoExtrato = descricaoExtrato;
        this.valorExtrato = valorExtrato;
        this.descricaoSistema = descricaoSistema;
        this.valorSistema = valorSistema;
        this.diferenca = diferenca;
        this.status = status;
        this.tipo = tipo;
    }

    // Retorna a data da movimentação.
    public LocalDate getData() {
        return data;
    }

    // Retorna a descrição encontrada no extrato.
    public String getDescricaoExtrato() {
        return descricaoExtrato;
    }

    // Retorna o valor do extrato bancário.
    public double getValorExtrato() {
        return valorExtrato;
    }

    // Retorna a descrição registrada no sistema.
    public String getDescricaoSistema() {
        return descricaoSistema;
    }

    // Retorna o valor registrado no sistema.
    public double getValorSistema() {
        return valorSistema;
    }

    // Retorna a diferença entre os valores.
    public double getDiferenca() {
        return diferenca;
    }

    // Retorna o status da movimentação.
    public String getStatus() {
        return status;
    }

    // Retorna o tipo da movimentação.
    public String getTipo() {
        return tipo;
    }

    public static ArrayList<Conciliacao> getMovimentacoes() {

        // Carrega os dados somente se a lista estiver vazia.
        if (movimentacoes.isEmpty()) {
            carregarDados();
        }

        // Retorna a lista de movimentações.
        return movimentacoes;
    }

    public static int getTotalConciliadas() {

        // Quantidade total de movimentações conciliadas.
        // Tipo: int. Função: contar os registros com status Conciliado.
        int total = 0;

        // Percorre todas as movimentações.
        for (Conciliacao c : getMovimentacoes()) {

            if ("Conciliado".equals(c.getStatus())) {

                // Soma uma movimentação ao total.
                total++;
            }
        }

        return total;
    }

    public static double getTotalDiferenca() {

        // Soma total das diferenças encontradas.
        // Tipo: double. Função: calcular o valor total das divergências.
        double total = 0;

        // Percorre todas as conciliações.
        for (Conciliacao c : getMovimentacoes()) {

            // Soma o valor absoluto da diferença.
            total += Math.abs(c.getDiferenca());
        }

        return total;
    }

    public static int getPendentes(String tipo) {

        // Quantidade de contas pendentes.
        // Tipo: int. Função: contar contas pendentes.
        int total = 0;

        // Percorre as contas cadastradas no sistema.
        for (Contas conta : Contas.getContas()) {

            // Verifica se o tipo e o status correspondem ao filtro.
            if (tipo.equals(conta.getTipo())
                    && "Pendente".equals(conta.getStatus())) {

                total++;
            }
        }

        return total;
    }

    public static int getAtrasados(String tipo) {

        // Quantidade de contas atrasadas.
        // Tipo: int. Função: contar contas vencidas ou atrasadas.
        int total = 0;

        // Percorre as contas cadastradas.
        for (Contas conta : Contas.getContas()) {

            // Verifica o tipo e se a conta está vencida ou atrasada.
            if (tipo.equals(conta.getTipo())
                    && ("Vencida".equals(conta.getStatus())
                    || "Atrasada".equals(conta.getStatus()))) {

                total++;
            }
        }

        return total;
    }

    public static ArrayList<Conciliacao> listarPorPeriodo(
            LocalDate inicio,
            LocalDate fim) {

        // Data inicial utilizada no filtro.
        // Tipo: LocalDate. Função: definir o início do período.

        // Data final utilizada no filtro.
        // Tipo: LocalDate. Função: definir o fim do período.

        // Lista que receberá somente as movimentações do período.
        // Tipo: ArrayList<Conciliacao>.
        ArrayList<Conciliacao> resultado = new ArrayList<>();

        // Percorre todas as movimentações cadastradas.
        for (Conciliacao c : getMovimentacoes()) {

            // Verifica se a data está dentro do intervalo escolhido.
            if (!c.getData().isBefore(inicio)
                    && !c.getData().isAfter(fim)) {

                resultado.add(c);
            }
        }

        // Organiza os resultados pela data.
        resultado.sort(
                Comparator.comparing(Conciliacao::getData)
        );

        return resultado;
    }

    private static void carregarDados() {

        // Limpa os dados antigos antes de carregar novamente.
        movimentacoes.clear();

        // Percorre todas as contas cadastradas.
        for (Contas conta : Contas.getContas()) {

            // Valor da conta registrado no sistema.
            // Tipo: double. Função: servir como valor de referência.
            double valorSistema = conta.getValor();

            // Valor inicialmente considerado no extrato.
            // Tipo: double. Função: iniciar a comparação com o valor do sistema.
            double valorExtrato = valorSistema;

            // Diferença inicial entre os valores.
            // Tipo: double. Função: armazenar a divergência encontrada.
            double diferenca = 0;

            // Status que será atribuído à movimentação.
            // Tipo: String.
            String status;

            switch (conta.getStatus()) {

                case "Paga":
                case "Recebida":

                    /*
                     * Algumas movimentações terão uma pequena
                     * diferença para demonstrar a conciliação.
                     */

                    // Simula uma diferença no valor do extrato
                    // para a conta de ID 11.
                    if (conta.getId() == 11) {
                        valorExtrato = 4600.00;
                    }

                    // Calcula a diferença entre extrato e sistema.
                    diferenca = valorExtrato - valorSistema;

                    // Define o status de acordo com a diferença.
                    if (diferenca == 0) {
                        status = "Conciliado";
                    } else {
                        status = "Divergente";
                    }

                    break;

                case "Pendente":

                    // Conta ainda não foi conciliada.
                    status = "Pendente";
                    break;

                case "Vencida":
                case "Atrasada":

                    // Conta está atrasada.
                    status = "Atrasada";
                    break;

                default:

                    // Mantém o status original da conta.
                    status = conta.getStatus();
                    break;
            }

            // Cria uma nova conciliação com os dados da conta.
            movimentacoes.add(
                    new Conciliacao(

                            // Data de vencimento da conta.
                            conta.getDataVencimento(),

                            // Junta entidade e descrição para mostrar
                            // uma identificação mais completa no extrato.
                            conta.getEntidade()
                                    + " - "
                                    + conta.getDescricao(),

                            // Valor encontrado no extrato.
                            valorExtrato,

                            // Descrição registrada no sistema.
                            conta.getDescricao(),

                            // Valor registrado no sistema.
                            valorSistema,

                            // Diferença calculada.
                            diferenca,

                            // Status calculado.
                            status,

                            // Tipo da conta.
                            conta.getTipo()
                    )
            );
        }

        // Ordena as movimentações pela data.
        movimentacoes.sort(
                Comparator.comparing(Conciliacao::getData)
        );
    }
}