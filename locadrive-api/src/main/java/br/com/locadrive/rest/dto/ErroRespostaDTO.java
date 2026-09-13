package br.com.locadrive.rest.dto;

import java.time.LocalDateTime;

public record ErroRespostaDTO(
        int status,
        String mensagem,
        LocalDateTime timestamp
) {
    public static ErroRespostaDTO of(int status, String mensagem) {
        return new ErroRespostaDTO(status, mensagem, LocalDateTime.now());
    }
}