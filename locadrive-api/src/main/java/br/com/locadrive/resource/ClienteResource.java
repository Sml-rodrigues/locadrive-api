package br.com.locadrive.resource;

import br.com.locadrive.entity.Cliente;
import jakarta.annotation.security.RolesAllowed;
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

public class ClienteResource {

    // 1. LISTAR TODOS OS CLIENTES
    @GET
    public List<Cliente> listarTodos() {
        return Cliente.listAll();
    }

    // 2. BUSCAR CLIENTE POR ‘ID’
    @GET
    @Path("/{id}")
    public Cliente buscarPorId(@PathParam("id") Long id) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            throw new NotFoundException("Cliente não encontrado.");
        }
        return cliente;
    }

    // 3. CADASTRAR UM NOVO CLIENTE
    @POST
    @Transactional
    public Response criar(@Valid Cliente cliente) {
        cliente.persist();
        return Response.status(Response.Status.CREATED).entity(cliente).build();
    }
}