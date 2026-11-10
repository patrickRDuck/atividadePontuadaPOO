package br.com.biblioteca.repository;
import br.com.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LivroRepository extends JpaRepository<Livro, Long> { boolean existsByIsbn(String isbn);
 @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
 @org.springframework.data.jpa.repository.Query("select l from Livro l where l.id = :id")
 java.util.Optional<Livro> buscarComBloqueio(@org.springframework.data.repository.query.Param("id") Long id); }
