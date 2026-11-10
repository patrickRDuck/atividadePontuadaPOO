package br.com.biblioteca.repository;
import br.com.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> { java.util.List<Emprestimo> findByLeitorIdOrderByIdDesc(Long leitorId);
 boolean existsByLivroIdAndDataDevolucaoIsNull(Long livroId); }
