package br.com.biblioteca.repository;
import br.com.biblioteca.model.Carteirinha;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CarteirinhaRepository extends JpaRepository<Carteirinha, Long> { boolean existsByNumero(String numero); }
