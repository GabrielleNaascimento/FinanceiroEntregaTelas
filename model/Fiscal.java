package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Modelo dos impostos exibidos na tela Fiscal.
 * A lista fica neste modelo para Fiscal e Contas a Pagar compartilharem os mesmos dados.
 */
public class Fiscal {

    private int id;
    private String nome;
    private String competencia;
    private LocalDate vencimento;
    private double baseCalculo;
    private double aliquota;
    private double valor;
    private String status;

    private static final ArrayList<Fiscal> impostos = new ArrayList<>();

    public Fiscal(int id, String nome, String competencia, LocalDate vencimento,
                  double baseCalculo, double aliquota, double valor, String status) {
        this.id = id;
        this.nome = nome;
        this.competencia = competencia;
        this.vencimento = vencimento;
        this.baseCalculo = baseCalculo;
        this.aliquota = aliquota;
        this.valor = valor;
        this.status = status;
    }

    /** Registros de exemplo da tela Fiscal; são criados uma única vez. */
    public static ArrayList<Fiscal> listarImpostos() {
        if (impostos.isEmpty()) {
            impostos.add(new Fiscal(101, "IRPJ", "09/2026", LocalDate.of(2026, 10, 25), 156300.00, 15.0, 23445.00, "Pago"));
            impostos.add(new Fiscal(102, "CSLL", "09/2026", LocalDate.of(2026, 10, 25), 156300.00, 9.0, 14067.00, "Pago"));
            impostos.add(new Fiscal(103, "PIS", "09/2026", LocalDate.of(2026, 10, 25), 156300.00, 0.65, 1015.95, "Pago"));
            impostos.add(new Fiscal(104, "COFINS", "09/2026", LocalDate.of(2026, 10, 25), 156300.00, 3.0, 4689.00, "Pago"));
            impostos.add(new Fiscal(105, "ISS", "09/2026", LocalDate.of(2026, 10, 10), 85000.00, 5.0, 4250.00, "Pago"));
            impostos.add(new Fiscal(106, "ICMS", "09/2026", LocalDate.of(2026, 10, 20), 71300.00, 12.0, 8556.00, "Pendente"));
            impostos.add(new Fiscal(107, "IRRF (S/Serviços)", "09/2026", LocalDate.of(2026, 10, 20), 12000.00, 1.5, 180.00, "Pendente"));
            impostos.add(new Fiscal(108, "GPS (INSS Patronal)", "08/2026", LocalDate.of(2026, 9, 20), 45200.00, 20.0, 9040.00, "Vencido"));
        }
        return impostos;
    }

    public static void atualizarStatus(int id, String status) {
        for (Fiscal imposto : listarImpostos()) {
            if (imposto.getId() == id) {
                imposto.setStatus("Pago".equalsIgnoreCase(status) ? "Pago" : status);
                return;
            }
        }
    }

    /** Status calculado pelo vencimento; pagamento tem prioridade. */
    public String getStatusAtual() {
        if ("Pago".equalsIgnoreCase(status)) return "Pago";
        return vencimento.isBefore(LocalDate.now()) ? "Vencido" : "Pendente";
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCompetencia() { return competencia; }
    public LocalDate getVencimento() { return vencimento; }
    public double getBaseCalculo() { return baseCalculo; }
    public double getAliquota() { return aliquota; }
    public double getValor() { return valor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
