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
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class LocacaoResourceTest {

    private String gerarCpfUnico() {
        return String.format("%011d", Math.abs(new Random().nextLong() % 100000000000L));
    }

    private String gerarPlacaUnica() {
        return "LOC" + (System.currentTimeMillis() % 100000);
    }

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
                gerarCpfUnico(),
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
                gerarPlacaUnica(),
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

    @Test
    @DisplayName("Deve finalizar uma locação com sucesso ao registrar devolução")
    @TestSecurity(user = "atendente", roles = {"USER"})
    void deveFinalizarDevolucaoComSucesso() {
        var clienteDto = new ClienteRequestDTO(
                "Cliente Devolucao",
                gerarCpfUnico(),
                "devolucao." + System.currentTimeMillis() + "@email.com",
                "16999990000"
        );
        Long clienteId = given()
                .contentType(ContentType.JSON)
                .body(clienteDto)
                .post("/api/v1/clientes")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        var veiculoDto = new VeiculoRequestDTO(
                "Corolla",
                "Toyota",
                gerarPlacaUnica(),
                2024,
                new BigDecimal("200.00")
        );
        Long veiculoId = given()
                .contentType(ContentType.JSON)
                .body(veiculoDto)
                .post("/api/v1/veiculos")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        var locacaoDto = new LocacaoRequestDTO(clienteId, veiculoId, 3);
        Long locacaoId = given()
                .contentType(ContentType.JSON)
                .body(locacaoDto)
                .post("/api/v1/locacoes")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .contentType(ContentType.JSON)
                .when().post("/api/v1/locacoes/" + locacaoId + "/devolucao")
                .then()
                .statusCode(200)
                .body("status", equalTo("CONCLUIDA"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar alugar um veículo já alugado")
    @TestSecurity(user = "gerente", roles = {"ADMIN"})
    void deveRetornar400AoAlugarVeiculoIndisponivel() {
        var clienteDto = new ClienteRequestDTO("Cliente Teste 1", gerarCpfUnico(), "c1." + System.currentTimeMillis() + "@email.com", "16999990001");
        Long clienteId = given().contentType(ContentType.JSON).body(clienteDto).post("/api/v1/clientes").then().extract().jsonPath().getLong("id");

        var veiculoDto = new VeiculoRequestDTO("Fit", "Honda", gerarPlacaUnica(), 2022, new BigDecimal("180.00"));
        Long veiculoId = given().contentType(ContentType.JSON).body(veiculoDto).post("/api/v1/veiculos").then().extract().jsonPath().getLong("id");

        var locacaoDto = new LocacaoRequestDTO(clienteId, veiculoId, 2);
        given().contentType(ContentType.JSON).body(locacaoDto).post("/api/v1/locacoes").then().statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(locacaoDto)
                .when().post("/api/v1/locacoes")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("mensagem", containsString("não está disponível para locação"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar devolver uma locação já concluída")
    @TestSecurity(user = "atendente", roles = {"USER"})
    void deveRetornar400AoDevolverLocacaoJaConcluida() {
        var clienteDto = new ClienteRequestDTO("Cliente Teste 2", gerarCpfUnico(), "c2." + System.currentTimeMillis() + "@email.com", "16999990002");
        Long clienteId = given().contentType(ContentType.JSON).body(clienteDto).post("/api/v1/clientes").then().extract().jsonPath().getLong("id");

        var veiculoDto = new VeiculoRequestDTO("Yaris", "Toyota", gerarPlacaUnica(), 2023, new BigDecimal("190.00"));
        Long veiculoId = given().contentType(ContentType.JSON).body(veiculoDto).post("/api/v1/veiculos").then().extract().jsonPath().getLong("id");

        var locacaoDto = new LocacaoRequestDTO(clienteId, veiculoId, 3);
        Long locacaoId = given().contentType(ContentType.JSON).body(locacaoDto).post("/api/v1/locacoes").then().extract().jsonPath().getLong("id");
        given().contentType(ContentType.JSON).post("/api/v1/locacoes/" + locacaoId + "/devolucao").then().statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .when().post("/api/v1/locacoes/" + locacaoId + "/devolucao")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("mensagem", equalTo("Esta locação já foi finalizada."));
    }
}