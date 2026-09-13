package br.com.locadrive.rest.resource;

import br.com.locadrive.rest.dto.ClienteRequestDTO;
import br.com.locadrive.rest.dto.LocacaoRequestDTO;
import br.com.locadrive.rest.dto.VeiculoRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class LocacaoResourceTest {

    @Test
    @DisplayName("Deve negar acesso ao listar locações sem token JWT (401 Unauthorized)")
    void deveRetornar401QuandoNaoAutenticado() {
        given()
                .when().get("/api/v1/locacoes")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Deve listar locações com sucesso quando autenticado com perfil USER")
    @TestSecurity(user = "atendente", roles = {"USER"})
    void deveListarLocacoesComSucesso() {
        given()
                .when().get("/api/v1/locacoes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Deve criar uma nova locação quando autenticado com perfil ADMIN")
    @TestSecurity(user = "gerente", roles = {"ADMIN"})
    void deveCriarLocacaoComSucesso() {
        var clienteDto = new ClienteRequestDTO(
                "Cliente Teste Locacao",
                "99988877700",
                "locador." + System.currentTimeMillis() + "@email.com",
                "11999998888"
        );
        Long clienteId = given()
                .contentType(ContentType.JSON)
                .body(clienteDto)
                .post("/api/v1/clientes")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        var veiculoDto = new VeiculoRequestDTO(
                "Civic",
                "Honda",
                "LOC" + (System.currentTimeMillis() % 10000),
                2023,
                new BigDecimal("250.00")
        );
        Long veiculoId = given()
                .contentType(ContentType.JSON)
                .body(veiculoDto)
                .post("/api/v1/veiculos")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        var locacaoDto = new LocacaoRequestDTO(clienteId, veiculoId, 5);

        given()
                .contentType(ContentType.JSON)
                .body(locacaoDto)
                .when().post("/api/v1/locacoes")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }
}