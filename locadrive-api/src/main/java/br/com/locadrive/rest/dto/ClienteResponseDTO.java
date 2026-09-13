package br.com.locadrive.rest.dto;

import br.com.locadrive.domain.model.Cliente;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String cnh
) {
    public static ClienteResponseDTO fromEntity(Cliente entity) {
        return new ClienteResponseDTO(
                entity.id,
                entity.nome,
                entity.cpf,
                entity.email,
                entity.cnh
        );
    }
}