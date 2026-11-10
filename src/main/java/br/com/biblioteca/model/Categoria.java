package br.com.biblioteca.model;
import jakarta.persistence.*;
@Entity
@Table(name="categorias")
public class Categoria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 public Long getId() { return id; }
 @Column(nullable=false, unique=true) private String nome;
 @ManyToMany(mappedBy="categorias") private java.util.Set<Livro> livros = new java.util.HashSet<>();
 public String getNome() { return nome; }
 public void setNome(String nome) { this.nome = nome; }
 public java.util.Set<Livro> getLivros() { return livros; }
 public void setLivros(java.util.Set<Livro> livros) { this.livros = livros; }
}
