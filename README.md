# 🚗 LocaDrive API 2.0

![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-blue?style=for-the-badge&logo=quarkus)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=for-the-badge&logo=postgresql)
![License MIT](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

> API RESTful de alta performance e padronização Enterprise para gerenciamento e automação de locação de veículos.

---

## 📌 Sobre o Projeto

O **LocaDrive API 2.0** é uma solução para controle operacional de locadoras de veículos. A aplicação gerencia todo o ecossistema do negócio: cadastro e disponibilidade da frota, controle de clientes, reservas, cálculo dinâmico de diárias/multas e autenticação segura baseada em funções (*Role-Based Access Control* - RBAC).

A arquitetura utiliza o ecossistema **Quarkus 3.x** focado em baixo consumo de memória, execução em containers e testes automatizados orientados a integração.

---

## 🚀 Tecnologias e Ferramentas

| Categoria | Tecnologia / Ferramenta |
| :--- | :--- |
| **Linguagem** | Java 21 (LTS) |
| **Framework** | Quarkus 3.x |
| **Persistência / ORM** | Hibernate ORM com Panache (Active Record) |
| **Banco de Dados** | PostgreSQL (com Quarkus DevServices) |
| **Migração de Banco** | Flyway |
| **Segurança** | SmallRye JWT (Assinatura RSA de 2048 bits) |
| **Testes Automatizados** | JUnit 5, REST Assured, Quarkus Security Test |
| **Build & Dependências** | Apache Maven |

---

## 📂 Arquitetura do Projeto & Detalhamento Técnico

```text
locadrive-api/
├── src/main/resources/
│   ├── db/migration/
│   │   ├── V1.0.0__create_tables.sql
│   │   └── V1.0.1__insert_initial_data.sql
│   ├── application.properties
│   ├── privateKey.pem
│   └── publicKey.pem
├── src/main/java/br/com/locadrive/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Cliente.java
│   │   │   ├── Veiculo.java
│   │   │   └── Locacao.java
│   │   └── service/
│   │       ├── ClienteService.java
│   │       ├── VeiculoService.java
│   │       └── LocacaoService.java
│   └── rest/
│       ├── dto/
│       │   ├── ClienteRequestDTO.java
│       │   ├── VeiculoRequestDTO.java
│       │   ├── LocacaoRequestDTO.java
│       │   └── LocacaoResponseDTO.java
│       └── resource/
│           ├── ClienteResource.java
│           ├── VeiculoResource.java
│           └── LocacaoResource.java
└── src/test/java/br/com/locadrive/rest/resource/
    └── LocacaoResourceTest.java
```

## 🔍 Explicação Detalhada dos Arquivos e Regras de Negócio

---

### 1. Banco de Dados e Migrações (`db/migration/`)

* **`V1.0.0__create_tables.sql`**: Define o DDL inicial do PostgreSQL.
    * `CREATE SEQUENCE tb_cliente_seq START WITH 1 INCREMENT BY 1;`: Garante controle explícito de sequências para chaves primárias.
    * `CONSTRAINT fk_locacao_cliente FOREIGN KEY`: Mapeia a integridade referencial entre locações, clientes e veículos.
* **`V1.0.1__insert_initial_data.sql`**: Popula a base inicial de testes mantendo a sincronia dos ponteiros de sequência via `nextval()`.

---

### 2. Configurações Globais (`application.properties`)

* `quarkus.datasource.devservices.enabled=true`: Ativa a criação automática de containers PostgreSQL em tempo de execução para dev/test.
* `quarkus.flyway.migrate-at-start=true`: Garante que o Flyway execute todos os scripts de migração na inicialização da aplicação.
* `smallrye.jwt.sign.key.location` e `mp.jwt.verify.publickey.location`: Define o par de chaves RSA assimétricas de 2048-bit para emissão e validação dos tokens JWT.

---

### 3. Modelo de Domínio (Entidades JPA)

* **`Cliente.java`**: Representa o cliente no sistema.
    * `@SequenceGenerator`: Sincroniza a geração de IDs com a sequence gerenciada pelo Flyway no PostgreSQL.
    * `findByCpf(String cpf)`: Método utilitário usando Active Record do Panache para buscas diretas.
* **`Veiculo.java`**: Gerencia a frota disponível.
    * `public Boolean disponivel`: Flag de controle utilizada na validação de novas reservas.
    * `public BigDecimal valorDiaria`: Valor base da diária utilizado para cálculo do contrato.
* **`Locacao.java`**: Entidade central de contratos de locação.
    * `@Transient public BigDecimal valorDiariaAplicado`: Mantém em memória o valor estático da diária aplicada ao contrato sem violar a estrutura do esquema do banco.
    * `@Transient public BigDecimal valorMulta`: Armazena o cálculo de penalidades em caso de devolução em atraso.

---

### 4. Camada de Serviço e Negócio (`LocacaoService.java`)

Contém as validações e cálculos do ciclo de vida da locação:

* **Validação de Disponibilidade**: Verifica se o veículo solicitado não está marcado como `INDISPONIVEL` antes de efetivar o registro.
* **Cálculo de Atraso e Multa**:
    * Ao finalizar a locação (`finalizarDevolucao`), o sistema compara a `dataDevolucao` real com a `dataFimPrevista`.
    * Em caso de atraso, aplica taxa de **20% de acréscimo** sobre a diária base por cada dia excedido:
      $$\text{Valor da Multa} = (\text{Valor Diária} \times 1.20) \times \text{Dias de Atraso}$$

---

### 5. Suíte de Testes de Integração (`LocacaoResourceTest.java`)

* `@QuarkusTest`: Inicializa o contexto do Quarkus e sobe o container PostgreSQL via DevServices.
* `@TestSecurity(user = "gerente", roles = {"ADMIN"})`: Simula o contexto de autenticação JWT injetando permissões sem depender da chamada externa ao serviço de autenticação.
* `gerarCpfUnico()` / `gerarPlacaUnica()`: Geradores auxiliares de dados para garantir o isolamento e evitar erros de violação de chave única (`UNIQUE constraint`) durante a execução contínua da suíte de testes.

---

## 🛠️ Como Executar a Aplicação

### Pré-requisitos
* **Java 21 JDK**
* **Docker Desktop / Docker Engine** (necessário para subir a base de dados via DevServices automaticamente)

### Modo Desenvolvimento
```bash
./mvnw quarkus:dev

```

---

A API estará acessível em `http://localhost:8080`.  
A documentação Swagger UI estará disponível em `http://localhost:8080/q/swagger-ui`.

### Execução da Suíte de Testes
```bash
./mvnw clean test

```
### Build para Produção
```bash
./mvnw clean package
```


