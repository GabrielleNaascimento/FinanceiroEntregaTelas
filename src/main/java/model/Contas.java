package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Contas {

    private int id;
    private String entidade;
    private String descricao;
    private double valor;
    private String tipo;
    private String status;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;

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
