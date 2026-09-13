package br.com.locadrive.rest.resource;

import br.com.locadrive.rest.dto.VeiculoRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class VeiculoResourceTest {

    @Test
    @DisplayName("Deve listar todos os veículos e retornar status 200 OK")
    void deveListarVeiculosComSucesso() {
        given()
                .when().get("/api/v1/veiculos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Deve cadastrar um novo veículo com sucesso e retornar 201 Created")
    void deveCriarVeiculoComSucesso() {
        var dto = new VeiculoRequestDTO(
                "Corolla",
                "Toyota",
                "ABC1D" + System.currentTimeMillis() % 10000,
                2024,
                new BigDecimal("220.00")
        );

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/api/v1/veiculos")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("modelo", is("Corolla"))
                .body("marca", is("Toyota"))
                .body("disponivel", is(true));
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request ao tentar cadastrar veículo com dados inválidos")
    void deveRetornarErroValidacaoQuandoDtoInvalido() {
        var dtoInvalido = new VeiculoRequestDTO(
                "",
                "Honda",
                "",
                1800,
                new BigDecimal("-50.00")
        );

        given()
                .contentType(ContentType.JSON)
                .body(dtoInvalido)
                .when().post("/api/v1/veiculos")
                .then()
                .statusCode(400)
                .body("parameterViolations", notNullValue());
    }
}