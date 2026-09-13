package br.com.locadrive.rest.resource;

import br.com.locadrive.domain.service.VeiculoService;
import br.com.locadrive.rest.dto.VeiculoRequestDTO;
import br.com.locadrive.rest.dto.VeiculoResponseDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1/veiculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VeiculoResource {

    @Inject
    VeiculoService veiculoService;

    @GET
    public List<VeiculoResponseDTO> listarTodos() {
        return veiculoService.listarTodos();
    }

    @POST
    public Response criar(@Valid VeiculoRequestDTO dto) {
        VeiculoResponseDTO response = veiculoService.criar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}