package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Fiscal {

    public int id;
    public String tipoImposto;
    public String competencia;
    public String vencimento;
    public double baseCalculo;
    public double aliquota;
    public double valor;
    public String status;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean pago;
    

    public Fiscal(int id, String tipoImposto, String competencia, String vencimento, double baseCalculo, double aliquota) {
        this.id = id;
        this.tipoImposto = tipoImposto;
        this.competencia = competencia;
        this.vencimento = vencimento;
        this.baseCalculo = baseCalculo;   
        this.aliquota = aliquota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoImposto() {
        return tipoImposto;
    }

    public void setTipoImposto(String tipoImposto) {
        this.tipoImposto = tipoImposto;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public double getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(int baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public double getAliquota() {
        return aliquota;
    }

    public void setAliquota(int aliquota) {
        this.aliquota = aliquota;
    }

    public double getValor() {
        return baseCalculo * aliquota / 100;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public LocalDate getDataVencimento() {
        return LocalDate.parse(vencimento, FORMATO);
    }

    public long getDiasParaVencimento() {
        return ChronoUnit.DAYS.between(LocalDate.now(), getDataVencimento());
    }

    public String getPrioridade() {
        if (pago) return null;
        LocalDate hoje = LocalDate.now();
        LocalDate venc = getDataVencimento();
        if (venc.isBefore(hoje)) return "ALTA";
        if (!venc.isAfter(hoje.plusMonths(1))) return "MÉDIA";
        return "BAIXA";
    }

    public String getStatus() {
        if (pago) return "Pago";
        return getDataVencimento().isBefore(LocalDate.now()) ? "Vencido" : "Pendente";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    public static List<Fiscal> listarTodos() {
        List<Fiscal> lista = new ArrayList<>();

        Fiscal f1 = new Fiscal(1, "IRPJ", "09/2026", "25/10/2026", 156300, 15);
        f1.setPago(true);
        Fiscal f2 = new Fiscal(2, "CSLL", "09/2026", "25/10/2026", 156300, 9);
        f2.setPago(true);
        Fiscal f3 = new Fiscal(3, "PIS", "09/2026", "25/10/2026", 156300, 0.65);
        f3.setPago(true);
        Fiscal f4 = new Fiscal(4, "COFINS", "09/2026", "25/10/2026", 156300, 3);
        f4.setPago(true);
        Fiscal f5 = new Fiscal(5, "ISS", "09/2026", "10/10/2026", 85000, 5);
        f5.setPago(true);
        Fiscal f6 = new Fiscal(6, "ICMS", "09/2026", "20/10/2026", 71300, 12);
        Fiscal f7 = new Fiscal(7, "IRRF (S/ Serviços)", "09/2026", "20/10/2026", 12000, 1.5);
        Fiscal f8 = new Fiscal(8, "GPS (INSS Patronal)", "08/2026", "20/09/2026", 45200, 20);

        lista.add(f1);
        lista.add(f2);
        lista.add(f3);
        lista.add(f4);
        lista.add(f5);
        lista.add(f6);
        lista.add(f7);
        lista.add(f8);
        return lista;
    }
}
