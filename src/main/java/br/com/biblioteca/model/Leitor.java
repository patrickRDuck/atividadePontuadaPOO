package br.com.biblioteca.model;
import jakarta.persistence.*;
@Entity
@Table(name="leitores")
public class Leitor {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 public Long getId() { return id; }
 @Column(nullable=false) private String nome;
 @Column(nullable=false, unique=true) private String email;
 // Leitor possui a FK; UNIQUE impede compartilhar a carteirinha.
 @OneToOne(optional=false, cascade=CascadeType.ALL, orphanRemoval=true)
 @JoinColumn(name="carteirinha_id", nullable=false, unique=true)
 private Carteirinha carteirinha;
 @OneToMany(mappedBy="leitor") private java.util.List<Emprestimo> emprestimos = new java.util.ArrayList<>();
 public String getNome() { return nome; }
 public void setNome(String nome) { this.nome = nome; }
 public String getEmail() { return email; }
 public void setEmail(String email) { this.email = email; }
 public Carteirinha getCarteirinha() { return carteirinha; }
 public void setCarteirinha(Carteirinha carteirinha) { this.carteirinha = carteirinha; }
 public java.util.List<Emprestimo> getEmprestimos() { return emprestimos; }
 public void setEmprestimos(java.util.List<Emprestimo> emprestimos) { this.emprestimos = emprestimos; }
}
