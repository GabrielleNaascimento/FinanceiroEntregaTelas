package controller;

import javax.swing.SwingUtilities;

import model.Usuario;
import view.TelaDashboard;

public class DashboardController {

    private TelaDashboard tela;
    private Usuario usuariologado;

    public DashboardController(TelaDashboard tela, Usuario usuariologado) {
        this.tela = tela;
        this.usuariologado = usuariologado;
    }

    public void identificarUsuario() {
        String nome = usuariologado.getNomeUsuario();
        String sessao = usuariologado.getSessaoUsuario();
        tela.identificarUsuario(nome, sessao);

     
    }

    /* 
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        TelaDashboard tela = new TelaDashboard();
        
        // teste de usuário logado 

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario("Jefferson");
        usuario.setSessaoUsuario("Administrador");

        DashboardController controller = new DashboardController(tela, usuario);
        controller.identificarUsuario(); 

        tela.setVisible(true);
       
    });
 
    }    */



}
