package br.com.locadrive.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_veiculo")
public class Veiculo extends PanacheEntity {

    @Column(nullable = false, length = 100)
    public String modelo;

    @Column(nullable = false, length = 50)
    public String marca;

    @Column(nullable = false, unique = true, length = 10)
    public String placa;

    @Column(name = "ano_fabricacao", nullable = false)
    public Integer anoFabricacao;

    @Column(name = "valor_diaria", nullable = false, precision = 10, scale = 2)
    public BigDecimal valorDiaria;

    @Column(nullable = false)
    public Boolean disponivel = true;
}