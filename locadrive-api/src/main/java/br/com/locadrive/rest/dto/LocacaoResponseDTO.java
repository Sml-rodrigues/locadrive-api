package br.com.locadrive.rest.dto;

import br.com.locadrive.domain.model.Locacao;
import br.com.locadrive.rest.dto.ClienteResponseDTO;
import br.com.locadrive.rest.dto.VeiculoResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LocacaoResponseDTO(
        Long id,
        ClienteResponseDTO cliente,
        VeiculoResponseDTO veiculo,
        LocalDateTime dataInicio,
        LocalDateTime dataFimPrevista,
        BigDecimal valorTotal
) {
    public static LocacaoResponseDTO fromEntity(Locacao entity) {
        return new LocacaoResponseDTO(
                entity.id,
                entity.cliente != null ? ClienteResponseDTO.fromEntity(entity.cliente) : null,
                entity.veiculo != null ? VeiculoResponseDTO.fromEntity(entity.veiculo) : null,
                entity.dataInicio,
                entity.dataFimPrevista,
                entity.valorTotal
        );
    }
}