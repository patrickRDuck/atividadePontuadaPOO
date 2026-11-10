# Biblioteca escolar

API REST em Java 17+ com Spring Boot, Spring Data JPA, Bean Validation e H2. Implementa os três desafios de relacionamentos e permite cadastrar e consultar leitores, livros, categorias e empréstimos, além de registrar devoluções.

## Executar

Pré-requisitos: JDK 17 ou superior e Maven 3.6.3 ou superior.

```sh
mvn spring-boot:run
```

A API fica em `http://localhost:8080/api`. O banco é criado automaticamente em `data/` e preservado entre execuções. Não exige instalação de banco externo. A aplicação inicia sem cadastros; execute os exemplos abaixo em ordem.

```sh
mvn test
mvn clean package
java -jar target/biblioteca-1.0.0.jar
```

## Relacionamentos e respostas às questões

- **Leitor ↔ Carteirinha (`@OneToOne`):** `leitores.carteirinha_id` é uma chave estrangeira obrigatória e única. Assim, cada leitor possui uma carteirinha e dois leitores não podem compartilhar a mesma carteirinha. O número da carteirinha também é único. O cadastro ocorre em uma única transação e `cascade = ALL` persiste a carteirinha junto ao leitor. `Carteirinha.leitor` é o lado inverso, com `mappedBy = "carteirinha"`. A API cria carteirinhas somente junto com seus leitores.
- **Leitor ↔ Empréstimo (`@OneToMany` / `@ManyToOne`):** `emprestimos.leitor_id` é a chave estrangeira obrigatória; Empréstimo é o proprietário. `Leitor.emprestimos` usa `mappedBy = "leitor"`. Cada linha da tabela de empréstimos referencia um leitor, permitindo vários registros para o mesmo leitor.
- **Livro ↔ Categoria (`@ManyToMany`):** Livro é o proprietário e configura `@JoinTable(name = "livro_categoria")`. A tabela intermediária contém `livro_id` e `categoria_id`, com unicidade do par. Categoria usa `mappedBy = "categorias"`. Não há remoção em cascata de categorias compartilhadas.
- **Empréstimo → Livro (`@ManyToOne`):** cada empréstimo identifica também o livro retirado, mantendo o histórico depois da devolução.

As entidades ficam em `model`, o acesso ao banco em `repository`, as regras transacionais em `service` e os endpoints/DTOs em `api`. DTOs evitam ciclos de serialização dos relacionamentos bidirecionais. Os testes executam `flush` e limpam o contexto JPA antes de verificar os relacionamentos persistidos.

## Endpoints

| Método | Caminho | Operação |
|---|---|---|
| POST | `/api/leitores` | Cadastrar leitor com carteirinha |
| GET | `/api/leitores` | Listar leitores |
| GET | `/api/leitores/{id}` | Consultar leitor e carteirinha |
| GET | `/api/leitores/{id}/emprestimos` | Consultar histórico do leitor |
| POST | `/api/categorias` | Cadastrar categoria |
| GET | `/api/categorias` | Listar categorias |
| POST | `/api/livros` | Cadastrar livro com categorias existentes |
| GET | `/api/livros` | Listar livros e suas categorias |
| GET | `/api/livros/{id}` | Consultar livro |
| POST | `/api/emprestimos` | Registrar empréstimo |
| GET | `/api/emprestimos` | Listar empréstimos |
| PATCH | `/api/emprestimos/{id}/devolucao` | Registrar devolução |

## Exemplo completo

Os IDs abaixo pressupõem um banco novo. Se já houver registros, utilize os IDs retornados pela API.

```sh
curl -i -X POST http://localhost:8080/api/leitores \
 -H 'Content-Type: application/json' \
 -d '{"nome":"Ana Silva","email":"ana@escola.com","carteirinha":{"numero":"BIB-001"}}'

curl -i -X POST http://localhost:8080/api/categorias \
 -H 'Content-Type: application/json' -d '{"nome":"Tecnologia"}'

curl -i -X POST http://localhost:8080/api/categorias \
 -H 'Content-Type: application/json' -d '{"nome":"Programação"}'

curl -i -X POST http://localhost:8080/api/livros \
 -H 'Content-Type: application/json' \
 -d '{"titulo":"Introdução ao Java","autor":"Maria Santos","isbn":"9780000000001","categoriaIds":[1,2]}'

curl -i -X POST http://localhost:8080/api/emprestimos \
 -H 'Content-Type: application/json' \
 -d '{"leitorId":1,"livroId":1,"dataPrevistaDevolucao":"2099-12-31"}'

curl http://localhost:8080/api/leitores/1/emprestimos

curl -i -X PATCH http://localhost:8080/api/emprestimos/1/devolucao
```

Datas usam `AAAA-MM-DD`. A data de emissão, a data do empréstimo e a devolução são atribuídas pelo servidor. O prazo não pode ser anterior ao dia atual. Cadastro retorna HTTP 201; consultas e devolução, 200; dados inválidos, 400; referência inexistente, 404; duplicidade ou conflito de empréstimo, 409. Os erros seguem `ProblemDetail`, com os campos inválidos quando aplicável.

Cada registro de Livro representa uma unidade emprestável. O sistema impede dois empréstimos ativos para o mesmo livro usando bloqueio pessimista na transação. Para vários exemplares da mesma edição, uma evolução seria criar a entidade Exemplar e transferir o empréstimo para ela.
