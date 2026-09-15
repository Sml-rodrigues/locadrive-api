package br.com.locadrive.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_veiculo")
public class Veiculo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

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

    @Column(nullable = false, length = 20)
    public String status = "DISPONIVEL";

    @Column(nullable = false)
    public Boolean disponivel = true;
}