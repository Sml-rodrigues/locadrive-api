package br.com.locadrive.resource;

import br.com.locadrive.entity.Veiculo;
import jakarta.annotation.security.RolesAllowed;
import br.com.locadrive.entity.Cliente;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"}) // Exige autenticação

public class VeiculoResource {

    @GET
    public List<Veiculo> listarTodos() {
        return Veiculo.listAll();
    }

    @GET
    @Path("/{id}")
    public Veiculo buscarPorId(@PathParam("id") Long id) {
        Veiculo veiculo = Veiculo.findById(id);
        if (veiculo == null) {
            throw new NotFoundException("Veículo não encontrado.");
        }
        return veiculo;
    }

    @POST
    @Transactional
    public Response criar(@Valid Veiculo veiculo) {
        veiculo.persist();
        return Response.status(Response.Status.CREATED).entity(veiculo).build();
    }
}