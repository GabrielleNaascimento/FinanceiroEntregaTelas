package controller;

import model.ResumoMensal;
import dao.DespesasDAO;
import model.Despesas;
import model.Usuario;
import view.TelaDashboard;

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

    public void carregarResumoMensal() {
        List<ResumoMensal> dados = new ArrayList<>();
        dados.add(new ResumoMensal("Maio",      120000, 90000));
        dados.add(new ResumoMensal("Junho",     135000, 98000));
        dados.add(new ResumoMensal("Julho",     128000, 105000));

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

    
           

    


