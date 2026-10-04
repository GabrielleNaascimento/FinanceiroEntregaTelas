package controller;

import model.ResumoMensal;
import dao.DespesasDAO;
import model.Despesas;
import model.Usuario;
import view.TelaDashboard;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    private TelaDashboard tela;
    private Usuario usuariologado;
    private DespesasDAO despesasDAO;

    public DashboardController(TelaDashboard tela, Usuario usuariologado) {
        this.tela = tela;
        this.usuariologado = usuariologado;
        this.despesasDAO = new DespesasDAO();
    }

    public void identificarUsuario() {
        String nome = usuariologado.getNomeUsuario();
        String sessao = usuariologado.getSessaoUsuario();
        tela.identificarUsuario(nome, sessao);

     
    }

    
    private int gerarNumero(int minimo, int maximo) {
    return new Random().nextInt(maximo - minimo + 1) + minimo;
}
    public void carregarResumoMensal() {

    List<ResumoMensal> dados = new ArrayList<>();

    dados.add(new ResumoMensal(
        "Maio",
        gerarNumero(90000, 110000),
        gerarNumero(90000, 130000)
    ));

    dados.add(new ResumoMensal(
        "Junho",
        gerarNumero(85000, 120000),
        gerarNumero(75000, 120000)
    ));

    dados.add(new ResumoMensal(
        "Julho",
        gerarNumero(100000, 130000),
        gerarNumero(85000, 110000)
    ));

    dados.add(new ResumoMensal(
        "Agosto",
        gerarNumero(130000, 140000),
        gerarNumero(85000, 110000)
    ));

    dados.add(new ResumoMensal(
        "Setembro",
        gerarNumero(130000, 140000),
        gerarNumero(85000, 110000)
    ));

    dados.add(new ResumoMensal(
        "Outubro",
        gerarNumero(130000, 140000),
        gerarNumero(85000, 110000)
    ));

    tela.carregarResumoMensal(dados);
}
    
   

    public List<Despesas> listarDespesas() {
        return despesasDAO.listarDespesas();
    }
    public void carregarDespesas() {
    List<Despesas> despesas = despesasDAO.listarDespesas();

    tela.carregarDespesas(despesas);
}

    }

    
           

    


