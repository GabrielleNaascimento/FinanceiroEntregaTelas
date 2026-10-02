package controller;

import javax.swing.SwingUtilities;
import dao.DespesasDAO;
import model.Despesas;
import model.Usuario;
import view.TelaDashboard;
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

   

    public List<Despesas> listarDespesas() {
        return despesasDAO.listarDespesas();
    }
    public void carregarDespesas() {
    List<Despesas> despesas = despesasDAO.listarDespesas();

    tela.carregarDespesas(despesas);
}

    
    }

    
           

    


