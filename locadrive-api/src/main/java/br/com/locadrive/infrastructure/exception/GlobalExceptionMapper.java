package br.com.locadrive.infrastructure.exception;

import br.com.locadrive.rest.dto.ErroRespostaDTO;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ConstraintViolationException cve) {
            List<String> mensagens = cve.getConstraintViolations()
                    .stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "status", 400,
                            "erros", mensagens,
                            "timestamp", java.time.LocalDateTime.now()
                    ))
                    .build();
        }

        if (exception instanceof EntidadeNaoEncontradaException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErroRespostaDTO.of(404, exception.getMessage()))
                    .build();
        }

        if (exception instanceof RegraDeNegocioException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErroRespostaDTO.of(400, exception.getMessage()))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErroRespostaDTO.of(500, "Ocorreu um erro interno inesperado no servidor."))
                .build();
    }
}