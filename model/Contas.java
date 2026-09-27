package model;

<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> 89783680b11676145fd107f108f2b48da3e26b6b
import java.util.ArrayList;

public class Contas {

<<<<<<< HEAD
    private int id;
    private String entidade;
    private String descricao;
    private double valor;
    private String tipo;
    private String status;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;
=======
    private String entidade; // empresa ou fornecedor
    private String descricao; // descrição da conta
    private String vencimento; // vencimento da conta
    private double valor; // valor da conta
    private String tipo; // tipo dela, pagar/receber
    private String status; // pendente, vencida e paga
>>>>>>> 89783680b11676145fd107f108f2b48da3e26b6b

    private static final ArrayList<Contas> contas = new ArrayList<>();

    public Contas(int id, String entidade, String descricao, double valor, String tipo,
                  String status, LocalDate dataInicio, LocalDate dataVencimento) {
        this.id = id;
        this.entidade = entidade;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
    }

    public int getId() {
        return id;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getDescricao() {
        return descricao;
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

<<<<<<< HEAD
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ArrayList<Contas> getContas() {
        if (contas.isEmpty()) {
            contas.add(new Contas(1, "Amazon", "Locação Servidores", 4500.00, "PAGAR", "Vencida",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 15)));

            contas.add(new Contas(2, "Microsoft", "Licenças Office", 3200.00, "PAGAR", "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 20)));

            contas.add(new Contas(3, "Xavier", "Assessoria Jurídica", 8500.00, "PAGAR", "Paga",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5)));

            contas.add(new Contas(4, "Imobiliária", "Aluguel Sala", 12000.00, "PAGAR", "Pendente",
                    LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 10)));

            contas.add(new Contas(5, "Adobe", "Licenças Creative Cloud", 1850.00, "PAGAR", "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 25)));

            contas.add(new Contas(6, "Dell", "Equipamentos de TI", 6800.00, "PAGAR", "Pendente",
                    LocalDate.of(2026, 9, 5), LocalDate.of(2026, 9, 28)));

            contas.add(new Contas(7, "Google", "Serviços Cloud", 2750.00, "PAGAR", "Pendente",
                    LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 30)));

            contas.add(new Contas(8, "Tech Solutions", "Impl. ERP", 15000.00, "RECEBER", "Recebida",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 1)));

            contas.add(new Contas(9, "Metalúrgica", "Consultoria", 8540.00, "RECEBER", "Atrasada",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 12)));

            contas.add(new Contas(10, "Hospital", "Manut. Hardware", 3800.00, "RECEBER", "Pendente",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 25)));

            contas.add(new Contas(11, "Banco Nacional", "Suporte TI", 4500.00, "RECEBER", "Recebida",
                    LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 18)));

            contas.add(new Contas(12, "Grupo Alpha", "Desenvolvimento Sistema", 12500.00, "RECEBER", "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 22)));

            contas.add(new Contas(13, "Comercial Silva", "Suporte Mensal", 6200.00, "RECEBER", "Pendente",
                    LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 27)));

            contas.add(new Contas(14, "Construtora Nova", "Consultoria Técnica", 9800.00, "RECEBER", "Pendente",
                    LocalDate.of(2026, 9, 5), LocalDate.of(2026, 10, 5)));
        }

        return contas;
    }
}
=======
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

>>>>>>> 89783680b11676145fd107f108f2b48da3e26b6b
