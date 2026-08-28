package model;

public class Dashboard {

    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private String idioma;
    private String quantidade;
    private String preco;
  

    public Dashboard() {
    }

    public Dashboard(String titulo, String autor, String genero, String idioma, String quantidade, String preco ) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.idioma = idioma;
        this.quantidade = quantidade;
        this.preco = preco;

    }

    public Dashboard(int id, String titulo, String autor, String genero, String idioma, String quantidade, String preco) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.idioma = idioma;
        this.quantidade = quantidade;
        this.preco = preco;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
 
    
}

