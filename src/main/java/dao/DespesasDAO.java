package dao;

import model.Despesas;
import java.util.ArrayList;
import java.util.List;

public class DespesasDAO {


  public List<Despesas> listarDespesas() {
    List<Despesas> listaDeDespesas = new ArrayList<Despesas>();
        // criando objetos de despesas e adicionado pra lista, integração com bd depois.
    
    Despesas d1 = new Despesas();
        d1.setId(1);
        d1.setCategoria("Folha de Pagamento");
        d1.setValor(5000);
    Despesas d2 = new Despesas();
        d2.setId(2);
        d2.setCategoria("Fornecedores");
        d2.setValor(2000);
    Despesas d3 = new Despesas();
        d3.setId(3);
        d3.setCategoria("Impostos");
        d3.setValor(1000);
    Despesas d4 = new Despesas();
        d4.setId(4);
        d4.setCategoria("Operacional");
        d4.setValor(5500);
    Despesas d5 = new Despesas();
        d5.setId(5);
        d5.setCategoria("Outros");
        d5.setValor(800);
    listaDeDespesas.add(d1);
    listaDeDespesas.add(d2);
    listaDeDespesas.add(d3);
    listaDeDespesas.add(d4);
    listaDeDespesas.add(d5);
    
    return listaDeDespesas;
  }
    

}