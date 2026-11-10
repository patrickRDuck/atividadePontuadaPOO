package br.com.biblioteca.api;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
public final class Dados {
 private Dados() {}
 public record NovaCarteirinha(@NotBlank @Size(max=255) String numero) {}
 public record NovoLeitor(@NotBlank @Size(max=255) String nome, @NotBlank @Email @Size(max=255) String email,
   @NotNull @jakarta.validation.Valid NovaCarteirinha carteirinha) {}
 public record NovaCategoria(@NotBlank @Size(max=255) String nome) {}
 public record NovoLivro(@NotBlank @Size(max=255) String titulo, @NotBlank @Size(max=255) String autor,
   @NotBlank @Size(max=255) String isbn, @NotEmpty Set<@NotNull @Positive Long> categoriaIds) {}
 public record NovoEmprestimo(@NotNull @Positive Long leitorId, @NotNull @Positive Long livroId,
   @NotNull @FutureOrPresent LocalDate dataPrevistaDevolucao) {}
 public record CarteirinhaResposta(Long id, String numero, LocalDate dataEmissao) {}
 public record LeitorResposta(Long id, String nome, String email, CarteirinhaResposta carteirinha) {}
 public record CategoriaResposta(Long id, String nome) {}
 public record LivroResposta(Long id, String titulo, String autor, String isbn, Set<CategoriaResposta> categorias) {}
 public record EmprestimoResposta(Long id, Long leitorId, Long livroId, LocalDate dataEmprestimo,
   LocalDate dataPrevistaDevolucao, LocalDate dataDevolucao) {}
}
