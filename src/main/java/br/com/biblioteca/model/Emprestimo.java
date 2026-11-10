package br.com.biblioteca.model;
import jakarta.persistence.*;
@Entity
@Table(name="emprestimos")
public class Emprestimo {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 public Long getId() { return id; }
 // Emprestimo possui a FK leitor_id (lado proprietário).
 @ManyToOne(optional=false, fetch=FetchType.LAZY)
 @JoinColumn(name="leitor_id", nullable=false) private Leitor leitor;
 @ManyToOne(optional=false, fetch=FetchType.LAZY)
 @JoinColumn(name="livro_id", nullable=false) private Livro livro;
 @Column(nullable=false) private java.time.LocalDate dataEmprestimo;
 @Column(nullable=false) private java.time.LocalDate dataPrevistaDevolucao;
 private java.time.LocalDate dataDevolucao;
 public Leitor getLeitor() { return leitor; }
 public void setLeitor(Leitor leitor) { this.leitor = leitor; }
 public Livro getLivro() { return livro; }
 public void setLivro(Livro livro) { this.livro = livro; }
 public java.time.LocalDate getDataEmprestimo() { return dataEmprestimo; }
 public void setDataEmprestimo(java.time.LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
 public java.time.LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
 public void setDataPrevistaDevolucao(java.time.LocalDate dataPrevistaDevolucao) { this.dataPrevistaDevolucao = dataPrevistaDevolucao; }
 public java.time.LocalDate getDataDevolucao() { return dataDevolucao; }
 public void setDataDevolucao(java.time.LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }
}
