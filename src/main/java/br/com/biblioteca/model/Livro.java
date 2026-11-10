package br.com.biblioteca.model;
import jakarta.persistence.*;
@Entity
@Table(name="livros")
public class Livro {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 public Long getId() { return id; }
 @Column(nullable=false) private String titulo;
 @Column(nullable=false) private String autor;
 @Column(nullable=false, unique=true) private String isbn;
 // Livro gerencia as associações; categorias podem ser compartilhadas.
 @ManyToMany
 @JoinTable(name="livro_categoria", joinColumns=@JoinColumn(name="livro_id"), inverseJoinColumns=@JoinColumn(name="categoria_id"), uniqueConstraints=@UniqueConstraint(columnNames={"livro_id", "categoria_id"}))
 private java.util.Set<Categoria> categorias = new java.util.HashSet<>();
 public String getTitulo() { return titulo; }
 public void setTitulo(String titulo) { this.titulo = titulo; }
 public String getAutor() { return autor; }
 public void setAutor(String autor) { this.autor = autor; }
 public String getIsbn() { return isbn; }
 public void setIsbn(String isbn) { this.isbn = isbn; }
 public java.util.Set<Categoria> getCategorias() { return categorias; }
 public void setCategorias(java.util.Set<Categoria> categorias) { this.categorias = categorias; }
}
