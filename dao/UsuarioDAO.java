package dao;

import model.Usuario;
import util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario identificarUsuario(String login) throws SQLException {
    Connection c = null;
    PreparedStatement p = null;
    ResultSet r = null;
    try {
      c = Conexao.abrir();
      p = c.prepareStatement("SELECT * FROM usuario WHERE login=?");
      p.setString(1, login.trim());
      r = p.executeQuery();
      if (r.next()) {
        Usuario u = mapear(r);
        return u;
      }
      return null;
    } finally {
      Conexao.fechar(c, p, r);
    }
  }

    private Usuario mapear(ResultSet r) throws SQLException {
    Usuario u = new Usuario();
    u.setNomeUsuario(r.getString("nomeUsuario"));
    u.setLogin(r.getString("login"));
    u.setSessaoUsuario(r.getString("sessaoUsuario"));
    return u;
  }


}
