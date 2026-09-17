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
                .map(loc -> {
                    if (loc.veiculo != null) {
                        loc.valorDiariaAplicado = loc.veiculo.valorDiaria;
                    }
                    return LocacaoResponseDTO.fromEntity(loc);
                })
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
        locacao.valorDiariaAplicado = veiculo.valorDiaria;

        if (veiculo.valorDiaria != null) {
            locacao.valorTotal = veiculo.valorDiaria.multiply(BigDecimal.valueOf(dto.dias()));
        }

        veiculo.disponivel = false;
        veiculo.status = "INDISPONIVEL";

        locacao.persist();

        return LocacaoResponseDTO.fromEntity(locacao);
    }

    @Transactional
    public LocacaoResponseDTO finalizarDevolucao(Long id) {
        Locacao locacao = Locacao.<Locacao>findByIdOptional(id)
                .orElseThrow(() -> new RegraDeNegocioException("Locação não encontrada com o ID: " + id));

        if ("CONCLUIDA".equals(locacao.status)) {
            throw new RegraDeNegocioException("Esta locação já foi finalizada.");
        }

        LocalDateTime dataDevolucaoReal = LocalDateTime.now();
        locacao.dataDevolucao = dataDevolucaoReal;

        BigDecimal valorMulta = BigDecimal.ZERO;

        if (locacao.veiculo != null) {
            locacao.valorDiariaAplicado = locacao.veiculo.valorDiaria;
        }

        if (dataDevolucaoReal.isAfter(locacao.dataFimPrevista)) {
            long diasAtraso = java.time.Duration.between(locacao.dataFimPrevista, dataDevolucaoReal).toDays();
            if (diasAtraso == 0) {
                diasAtraso = 1;
            }

            BigDecimal diaria = (locacao.veiculo != null && locacao.veiculo.valorDiaria != null)
                    ? locacao.veiculo.valorDiaria
                    : BigDecimal.ZERO;

            BigDecimal valorDiariaComMulta = diaria.multiply(new BigDecimal("1.20"));
            valorMulta = valorDiariaComMulta.multiply(BigDecimal.valueOf(diasAtraso));
        }

        locacao.valorMulta = valorMulta;
        BigDecimal valorBase = locacao.valorTotal != null ? locacao.valorTotal : BigDecimal.ZERO;
        locacao.valorTotal = valorBase.add(valorMulta);
        locacao.status = "CONCLUIDA";

        if (locacao.veiculo != null) {
            locacao.veiculo.disponivel = true;
            locacao.veiculo.status = "DISPONIVEL";
        }

        locacao.persist();

        return LocacaoResponseDTO.fromEntity(locacao);
    }
}