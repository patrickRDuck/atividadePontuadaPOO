package br.com.biblioteca.api;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
@RestControllerAdvice
public class TratamentoErros {
 @ExceptionHandler(ResponseStatusException.class)
 public ProblemDetail negocio(ResponseStatusException e) { return ProblemDetail.forStatusAndDetail(e.getStatusCode(),e.getReason()); }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ProblemDetail validacao(MethodArgumentNotValidException e) {
  var p=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Verifique os campos informados.");
  p.setProperty("erros",e.getBindingResult().getFieldErrors().stream().map(f -> f.getField()+": "+f.getDefaultMessage()).toList()); return p;
 }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ProblemDetail integridade() { return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,"Registro duplicado ou relacionamento inválido."); }
 @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
 public ProblemDetail formato() { return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"JSON, data ou identificador inválido."); }
}
