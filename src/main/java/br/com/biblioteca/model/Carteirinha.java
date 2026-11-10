package br.com.biblioteca.model;
import jakarta.persistence.*;
@Entity
@Table(name="carteirinhas")
public class Carteirinha {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 public Long getId() { return id; }
 @Column(nullable=false, unique=true) private String numero;
 @Column(nullable=false) private java.time.LocalDate dataEmissao;
 @OneToOne(mappedBy="carteirinha") private Leitor leitor;
 public String getNumero() { return numero; }
 public void setNumero(String numero) { this.numero = numero; }
 public java.time.LocalDate getDataEmissao() { return dataEmissao; }
 public void setDataEmissao(java.time.LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
 public Leitor getLeitor() { return leitor; }
 public void setLeitor(Leitor leitor) { this.leitor = leitor; }
}
