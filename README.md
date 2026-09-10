# # 🚗 LocaDrive API 2.0

![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-blue?style=for-the-badge&logo=quarkus)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=for-the-badge&logo=postgresql)
![License MIT](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

> API RESTful de alta performance para gerenciamento e automação de locação de veículos.

---

## 📌 Sobre o Projeto

O **LocaDrive API 2.0** é uma solução moderna para controle operacional de locadoras de veículos. A aplicação gerencia todo o ecossistema do negócio, desde o cadastro e disponibilidade da frota até o controle de clientes, reservas e autenticação segura baseada em funções (*Role-Based Access Control* - RBAC).

A API foi projetada focando em alta disponibilidade, baixo consumo de memória (cloud-native) e execução otimizada através do ecossistema **Quarkus**.

---

## 🚀 Tecnologias e Ferramentas

| Categoria | Tecnologia |
| :--- | :--- |
| **Linguagem** | Java 21 |
| **Framework** | Quarkus 3.x |
| **Persistência / ORM** | Hibernate ORM com Panache |
| **Banco de Dados** | PostgreSQL |
| **Segurança** | SmallRye JWT (JSON Web Tokens) |
| **Migração / Scripts** | Hibernate ORM DDL Auto / Import SQL |
| **Containers** | Docker & Docker Compose |
| **Gerenciamento de Dependências** | Apache Maven |

---

## 🔒 Requisitos de Segurança & Boas Práticas

A segurança da aplicação foi estruturada seguindo rigorosos padrões de mercado para ambientes corporativos:

* **Proteção de Credenciais:** Nenhuma chave privada (`.pem`), arquivo de configuração de ambiente (`.env`) ou credencial de banco de dados é versionada no repositório.
* **Autenticação Stateless (JWT):** Utilização de par de chaves assimétricas (RSA) `privateKey.pem` e `publicKey.pem` para emissão e validação de tokens JWT.
* **Controle de Acesso (RBAC):** Restrição de endpoints por perfis de usuário (`@RolesAllowed`) via anotações nativas do Quarkus Security.
* **Isolamento por Variáveis de Ambiente:** Parâmetros sensíveis e strings de conexão são injetados dinamicamente via `application.properties` utilizando o padrão do MicroProfile Config.

---

## 🛠️ Arquitetura e Funcionalidades Principais

* **`AuthResource`**: Gerenciamento de login e emissão de tokens de acesso JWT.
* **`ClienteResource`**: CRUD completo de clientes com validação de dados cadastrais.
* **`VeiculoResource`**: Controle de frota, incluindo status de disponibilidade e categoria dos veículos.
* **`LocacaoResource`**: Processamento de reservas, cálculo de diárias e encerramento de locações.

---

## 📋 Pré-requisitos para Execução

Antes de começar, garanta que possui as seguintes ferramentas instaladas na sua máquina:

* [JDK 21](https://adoptium.net/)
* [Apache Maven 3.9+](https://maven.apache.org/)
* [PostgreSQL 15+](https://www.postgresql.org/) ou [Docker Desktop / Open Source Engine](https://www.docker.com/)
* Chaves RSA de segurança para o JWT geradas na pasta de *resources* (`privateKey.pem` e `publicKey.pem`).

---

## ⚙️ Configuração e Execução Local

### 1. Clocar o Repositório
```bash
git clone [https://github.com/Sml-rodrigues/locadrive-api.git](https://github.com/Sml-rodrigues/locadrive-api.git)
cd locadrive-api/locadrive-api