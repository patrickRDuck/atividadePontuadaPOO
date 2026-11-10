package br.com.biblioteca.service;
import br.com.biblioteca.model.*;
import br.com.biblioteca.repository.*;
import br.com.biblioteca.api.Dados.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Transactional(readOnly=true)
public class BibliotecaService {
 private final LeitorRepository leitores;
 private final CarteirinhaRepository carteirinhas;
 private final CategoriaRepository categorias;
 private final LivroRepository livros;
 private final EmprestimoRepository emprestimos;
 public BibliotecaService(LeitorRepository l, CarteirinhaRepository c, CategoriaRepository ca, LivroRepository li, EmprestimoRepository e) {
  leitores=l; carteirinhas=c; categorias=ca; livros=li; emprestimos=e;
 }
 private <T> T exigir(Optional<T> valor, String tipo) {
  return valor.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, tipo+" não encontrado(a)."));
 }
 private void conflito(boolean existe, String mensagem) {
  if(existe) throw new ResponseStatusException(HttpStatus.CONFLICT, mensagem);
 }
 private LeitorResposta resposta(Leitor l) {
  var c=l.getCarteirinha();
  return new LeitorResposta(l.getId(),l.getNome(),l.getEmail(),new CarteirinhaResposta(c.getId(),c.getNumero(),c.getDataEmissao()));
 }
 private CategoriaResposta resposta(Categoria c) { return new CategoriaResposta(c.getId(),c.getNome()); }
 private LivroResposta resposta(Livro l) {
  return new LivroResposta(l.getId(),l.getTitulo(),l.getAutor(),l.getIsbn(),l.getCategorias().stream().map(this::resposta).collect(Collectors.toSet()));
 }
 private EmprestimoResposta resposta(Emprestimo e) {
  return new EmprestimoResposta(e.getId(),e.getLeitor().getId(),e.getLivro().getId(),e.getDataEmprestimo(),e.getDataPrevistaDevolucao(),e.getDataDevolucao());
 }
 @Transactional public LeitorResposta cadastrarLeitor(NovoLeitor d) {
  String email=d.email().strip().toLowerCase(Locale.ROOT);
  conflito(leitores.existsByEmail(email),"E-mail já cadastrado.");
  conflito(carteirinhas.existsByNumero(d.carteirinha().numero().strip()),"Número de carteirinha já cadastrado.");
  var l=new Leitor(); l.setNome(d.nome().strip()); l.setEmail(email);
  var c=new Carteirinha(); c.setNumero(d.carteirinha().numero().strip()); c.setDataEmissao(LocalDate.now()); c.setLeitor(l);
  l.setCarteirinha(c); return resposta(leitores.save(l));
 }
 public List<LeitorResposta> listarLeitores() { return leitores.findAll().stream().map(this::resposta).toList(); }
 public LeitorResposta consultarLeitor(Long id) { return resposta(exigir(leitores.findById(id),"Leitor")); }
 @Transactional public CategoriaResposta cadastrarCategoria(NovaCategoria d) {
  conflito(categorias.existsByNome(d.nome().strip()),"Categoria já cadastrada.");
  var c=new Categoria(); c.setNome(d.nome().strip()); return resposta(categorias.save(c));
 }
 public List<CategoriaResposta> listarCategorias() { return categorias.findAll().stream().map(this::resposta).toList(); }
 @Transactional public LivroResposta cadastrarLivro(NovoLivro d) {
  conflito(livros.existsByIsbn(d.isbn().strip()),"ISBN já cadastrado.");
  var l=new Livro(); l.setTitulo(d.titulo().strip()); l.setAutor(d.autor().strip()); l.setIsbn(d.isbn().strip());
  for(Long id:d.categoriaIds()) {
   var c=exigir(categorias.findById(id),"Categoria"); l.getCategorias().add(c); c.getLivros().add(l);
  }
  return resposta(livros.save(l));
 }
 public List<LivroResposta> listarLivros() { return livros.findAll().stream().map(this::resposta).toList(); }
 public LivroResposta consultarLivro(Long id) { return resposta(exigir(livros.findById(id),"Livro")); }
 @Transactional public EmprestimoResposta cadastrarEmprestimo(NovoEmprestimo d) {
  var l=exigir(leitores.findById(d.leitorId()),"Leitor");
  var livro=exigir(livros.buscarComBloqueio(d.livroId()),"Livro");
  conflito(emprestimos.existsByLivroIdAndDataDevolucaoIsNull(livro.getId()),"Livro já está emprestado.");
  var e=new Emprestimo(); e.setLeitor(l); e.setLivro(livro); e.setDataEmprestimo(LocalDate.now());
  e.setDataPrevistaDevolucao(d.dataPrevistaDevolucao()); l.getEmprestimos().add(e);
  return resposta(emprestimos.save(e));
 }
 public List<EmprestimoResposta> listarEmprestimos() { return emprestimos.findAll().stream().map(this::resposta).toList(); }
 public List<EmprestimoResposta> emprestimosDoLeitor(Long id) {
  exigir(leitores.findById(id),"Leitor"); return emprestimos.findByLeitorIdOrderByIdDesc(id).stream().map(this::resposta).toList();
 }
 @Transactional public EmprestimoResposta devolver(Long id) {
  var e=exigir(emprestimos.findById(id),"Empréstimo");
  exigir(livros.buscarComBloqueio(e.getLivro().getId()),"Livro");
  conflito(e.getDataDevolucao()!=null,"Empréstimo já devolvido.");
  e.setDataDevolucao(LocalDate.now()); return resposta(e);
 }
}
