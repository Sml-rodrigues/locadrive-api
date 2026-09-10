package br.com.locadrive.resource;

import br.com.locadrive.entity.Cliente;
import br.com.locadrive.entity.Locacao;
import br.com.locadrive.entity.Veiculo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/locacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class LocacaoResource {

    @GET
    public List<Locacao> listarTodas() {
        return Locacao.listAll();
    }

    @POST
    @Transactional
    public Response abrirLocacao(DTOAbrirLocacao dto) {
        Cliente cliente = Cliente.findById(dto.clienteId);
        if (cliente == null) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        Veiculo veiculo = Veiculo.findById(dto.veiculoId);
        if (veiculo == null) {
            throw new NotFoundException("Veículo não encontrado.");
        }

        if (!"DISPONIVEL".equalsIgnoreCase(veiculo.status)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O veículo selecionado não está disponível para locação.")
                    .build();
        }

        Locacao locacao = new Locacao();
        locacao.cliente = cliente;
        locacao.veiculo = veiculo;
        locacao.dataInicio = LocalDateTime.now();
        locacao.dataFimPrevista = dto.dataFimPrevista;
        locacao.valorDiariaAplicado = veiculo.valorDiaria;
        locacao.status = "EM_ANDAMENTO";

        veiculo.status = "ALUGADO";

        locacao.persist();

        return Response.status(Response.Status.CREATED).entity(locacao).build();
    }

    public static class DTOAbrirLocacao {
        public Long clienteId;
        public Long veiculoId;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        public LocalDateTime dataFimPrevista;
    }
}