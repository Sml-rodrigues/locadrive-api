package br.com.locadrive.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente extends PanacheEntity {

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false, length = 100)
    public String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "CPF em formato inválido")
    @Column(nullable = false, unique = true, length = 14)
    public String cpf;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail em formato inválido")
    @Column(nullable = false, unique = true, length = 100)
    public String email;

    @Column(length = 20)
    public String telefone;

    @Column(name = "data_cadastro")
    public LocalDateTime dataCadastro = LocalDateTime.now();

    // Método Utilitário/Busca no padrão Panache
    public static Cliente findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
}