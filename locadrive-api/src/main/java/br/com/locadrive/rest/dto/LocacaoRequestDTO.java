package br.com.locadrive.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LocacaoRequestDTO(
        @NotNull(message = "O ID do cliente é obrigatório")
        Long clienteId,

        @NotNull(message = "O ID do veículo é obrigatório")
        Long veiculoId,

        @NotNull(message = "A quantidade de dias é obrigatória")
        @Positive(message = "A quantidade de dias deve ser maior que zero")
        Integer dias
) {}