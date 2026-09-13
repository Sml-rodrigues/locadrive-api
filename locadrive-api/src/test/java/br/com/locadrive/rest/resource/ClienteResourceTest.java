package br.com.locadrive.rest.resource;

import br.com.locadrive.rest.dto.ClienteRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ClienteResourceTest {

    @Test
    @DisplayName("Deve listar clientes e retornar status 200 OK")
    void deveListarClientesComSucesso() {
        given()
                .when().get("/api/v1/clientes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Deve cadastrar um novo cliente com sucesso e retornar 201 Created")
    void deveCriarClienteComSucesso() {
        var dto = new ClienteRequestDTO(
                "Carlos Silva",
                "12345678900", // CPF válido primeiro
                "carlos." + System.currentTimeMillis() + "@email.com", // Email em seguida
                "12987654321" // CNH
        );

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/api/v1/clientes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", is("Carlos Silva"));
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request para cliente com dados inválidos")
    void deveRetornarErroValidacaoQuandoDtoInvalido() {
        var dtoInvalido = new ClienteRequestDTO("", "email-invalido", "", "");

        given()
                .contentType(ContentType.JSON)
                .body(dtoInvalido)
                .when().post("/api/v1/clientes")
                .then()
                .statusCode(400)
                .body("parameterViolations", notNullValue());
    }
}