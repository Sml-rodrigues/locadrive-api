package br.com.locadrive.rest.resource;

import br.com.locadrive.domain.service.ClienteService;
import br.com.locadrive.rest.dto.ClienteRequestDTO;
import br.com.locadrive.rest.dto.ClienteResponseDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteService clienteService;

    @GET
    public List<ClienteResponseDTO> listarTodos() {
        return clienteService.listarTodos();
    }

    @POST
    public Response criar(@Valid ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.criar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}