package br.com.locadrive.rest.dto;

import br.com.locadrive.domain.model.Veiculo;
import java.math.BigDecimal;

public record VeiculoResponseDTO(
        Long id,
        String modelo,
        String marca,
        String placa,
        Integer anoFabricacao,
        BigDecimal valorDiaria,
        Boolean disponivel
) {
    public static VeiculoResponseDTO fromEntity(Veiculo entity) {
        return new VeiculoResponseDTO(
                entity.id,
                entity.modelo,
                entity.marca,
                entity.placa,
                entity.anoFabricacao,
                entity.valorDiaria,
                entity.disponivel
        );
    }
}