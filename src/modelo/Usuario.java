package modelo;

public class Usuario {
    int id;
     String nome;
     String email;
     String senha;
     String tipo; // "HOSPEDE" ou "ADMIN"

    public Usuario(int id, String nome, String email, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getTipo() { return tipo; }
}