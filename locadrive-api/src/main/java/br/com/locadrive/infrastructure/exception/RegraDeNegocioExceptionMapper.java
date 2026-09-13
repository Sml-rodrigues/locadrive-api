package br.com.locadrive.infrastructure.exception;

import br.com.locadrive.rest.dto.ErroRespostaDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public class RegraDeNegocioExceptionMapper implements ExceptionMapper<RegraDeNegocioException> {

    @Override
    public Response toResponse(RegraDeNegocioException exception) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(erro)
                .build();
    }


}