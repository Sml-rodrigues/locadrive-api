package br.com.locadrive.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record VeiculoRequestDTO(
        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotBlank(message = "A placa é obrigatória")
        String placa,

        @NotNull(message = "O ano de fabricação é obrigatório")
        Integer anoFabricacao,

        @NotNull(message = "O valor da diária é obrigatório")
        @Positive(message = "O valor da diária deve ser maior que zero")
        BigDecimal valorDiaria
) {}