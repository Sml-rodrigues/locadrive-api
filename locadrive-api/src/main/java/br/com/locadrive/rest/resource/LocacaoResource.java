package br.com.locadrive.rest.resource;

import br.com.locadrive.domain.service.LocacaoService;
import br.com.locadrive.rest.dto.LocacaoRequestDTO;
import br.com.locadrive.rest.dto.LocacaoResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1/locacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})
public class LocacaoResource {

    @Inject
    LocacaoService locacaoService;

    @GET
    public List<LocacaoResponseDTO> listarTodas() {
        return locacaoService.listarTodas();
    }

    @POST
    public Response realizarLocacao(@Valid LocacaoRequestDTO dto) {
        LocacaoResponseDTO response = locacaoService.realizarLocacao(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}