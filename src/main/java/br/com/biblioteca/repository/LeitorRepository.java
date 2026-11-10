package br.com.biblioteca.repository;
import br.com.biblioteca.model.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LeitorRepository extends JpaRepository<Leitor, Long> { boolean existsByEmail(String email); }
