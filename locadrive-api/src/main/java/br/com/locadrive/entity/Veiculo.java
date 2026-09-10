package br.com.locadrive.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "veiculos")
public class Veiculo extends PanacheEntity {

    @NotBlank(message = "A placa é obrigatória")
    @Column(nullable = false, unique = true, length = 10)
    public String placa;

    @NotBlank(message = "A marca é obrigatória")
    @Column(nullable = false, length = 50)
    public String marca;

    @NotBlank(message = "O modelo é obrigatório")
    @Column(nullable = false, length = 50)
    public String modelo;

    @NotNull(message = "O ano de fabricação é obrigatório")
    @Column(name = "ano_fabricacao", nullable = false)
    public Integer anoFabricacao;

    @NotNull(message = "O valor da diária é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor da diária deve ser maior que zero")
    @Column(name = "valor_diaria", nullable = false, precision = 10, scale = 2)
    public BigDecimal valorDiaria;

    @NotBlank(message = "O status é obrigatório")
    @Column(nullable = false, length = 20)
    public String status = "DISPONIVEL";

    // Busca rápida por placa (Panache Active Record)
    public static Veiculo findByPlaca(String placa) {
        return find("placa", placa).firstResult();
    }
}