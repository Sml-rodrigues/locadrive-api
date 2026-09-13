package br.com.locadrive.rest.dto;

import br.com.locadrive.domain.model.Locacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LocacaoResponseDTO(
        Long id,
        ClienteResponseDTO cliente,
        VeiculoResponseDTO veiculo,
        LocalDateTime dataInicio,
        LocalDateTime dataFimPrevista,
        LocalDateTime dataDevolucao,
        BigDecimal valorDiariaAplicado,
        BigDecimal valorTotal,
        BigDecimal valorMulta,
        String status
) {
    public static LocacaoResponseDTO fromEntity(Locacao locacao) {
        return new LocacaoResponseDTO(
                locacao.id,
                locacao.cliente != null ? ClienteResponseDTO.fromEntity(locacao.cliente) : null,
                locacao.veiculo != null ? VeiculoResponseDTO.fromEntity(locacao.veiculo) : null,
                locacao.dataInicio,
                locacao.dataFimPrevista,
                locacao.dataDevolucao,
                locacao.valorDiariaAplicado,
                locacao.valorTotal,
                locacao.valorMulta,
                locacao.status
        );
    }
}