package br.com.biblioteca.api;
import br.com.biblioteca.api.Dados.*;
import br.com.biblioteca.service.BibliotecaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api")
public class BibliotecaController {
 private final BibliotecaService service;
 public BibliotecaController(BibliotecaService service) { this.service=service; }
 @PostMapping("/leitores") public ResponseEntity<LeitorResposta> leitor(@Valid @RequestBody NovoLeitor d) {
  var r=service.cadastrarLeitor(d); return ResponseEntity.created(URI.create("/api/leitores/"+r.id())).body(r);
 }
 @GetMapping("/leitores") public List<LeitorResposta> leitores() { return service.listarLeitores(); }
 @GetMapping("/leitores/{id}") public LeitorResposta leitor(@PathVariable Long id) { return service.consultarLeitor(id); }
 @GetMapping("/leitores/{id}/emprestimos") public List<EmprestimoResposta> historico(@PathVariable Long id) { return service.emprestimosDoLeitor(id); }
 @PostMapping("/categorias") @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
 public CategoriaResposta categoria(@Valid @RequestBody NovaCategoria d) { return service.cadastrarCategoria(d); }
 @GetMapping("/categorias") public List<CategoriaResposta> categorias() { return service.listarCategorias(); }
 @PostMapping("/livros") public ResponseEntity<LivroResposta> livro(@Valid @RequestBody NovoLivro d) {
  var r=service.cadastrarLivro(d); return ResponseEntity.created(URI.create("/api/livros/"+r.id())).body(r);
 }
 @GetMapping("/livros") public List<LivroResposta> livros() { return service.listarLivros(); }
 @GetMapping("/livros/{id}") public LivroResposta livro(@PathVariable Long id) { return service.consultarLivro(id); }
 @PostMapping("/emprestimos") @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
 public EmprestimoResposta emprestimo(@Valid @RequestBody NovoEmprestimo d) { return service.cadastrarEmprestimo(d); }
 @GetMapping("/emprestimos") public List<EmprestimoResposta> emprestimos() { return service.listarEmprestimos(); }
 @PatchMapping("/emprestimos/{id}/devolucao") public EmprestimoResposta devolver(@PathVariable Long id) { return service.devolver(id); }
}
