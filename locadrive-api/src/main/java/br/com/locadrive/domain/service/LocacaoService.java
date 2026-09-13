package br.com.locadrive.domain.service;

import br.com.locadrive.domain.model.Cliente;
import br.com.locadrive.domain.model.Locacao;
import br.com.locadrive.domain.model.Veiculo;
import br.com.locadrive.infrastructure.exception.RegraDeNegocioException;
import br.com.locadrive.rest.dto.LocacaoRequestDTO;
import br.com.locadrive.rest.dto.LocacaoResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class LocacaoService {

    @Inject
    ClienteService clienteService;

    @Inject
    VeiculoService veiculoService;

    public List<LocacaoResponseDTO> listarTodas() {
        return Locacao.<Locacao>listAll()
                .stream()
                .map(LocacaoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public LocacaoResponseDTO realizarLocacao(LocacaoRequestDTO dto) {
        Cliente cliente = clienteService.buscarPorIdEntity(dto.clienteId());
        Veiculo veiculo = veiculoService.buscarPorIdEntity(dto.veiculoId());

        if (Boolean.FALSE.equals(veiculo.disponivel)) {
            throw new RegraDeNegocioException("O veículo " + veiculo.modelo + " (Placa: " + veiculo.placa + ") não está disponível para locação.");
        }

        Locacao locacao = new Locacao();
        locacao.cliente = cliente;
        locacao.veiculo = veiculo;
        locacao.dataInicio = LocalDateTime.now();
        locacao.dataFimPrevista = LocalDateTime.now().plusDays(dto.dias());

        // Atribuição necessária para evitar o erro de NOT NULL na coluna valor_diaria_aplicado
        locacao.valorDiariaAplicado = veiculo.valorDiaria;

        if (veiculo.valorDiaria != null) {
            locacao.valorTotal = veiculo.valorDiaria.multiply(BigDecimal.valueOf(dto.dias()));
        }

        veiculo.disponivel = false;
        locacao.persist();

        return LocacaoResponseDTO.fromEntity(locacao);
    }
}