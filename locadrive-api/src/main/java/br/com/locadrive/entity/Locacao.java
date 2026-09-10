package br.com.locadrive.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "locacoes")
public class Locacao extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente é obrigatório")
    public Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    @NotNull(message = "O veículo é obrigatório")
    public Veiculo veiculo;

    @Column(name = "data_inicio", nullable = false)
    public LocalDateTime dataInicio = LocalDateTime.now();

    @NotNull(message = "A data fim prevista é obrigatória")
    @Column(name = "data_fim_prevista", nullable = false)
    public LocalDateTime dataFimPrevista;

    @Column(name = "data_devolucao")
    public LocalDateTime dataDevolucao;

    @Column(name = "valor_diaria_aplicado", nullable = false, precision = 10, scale = 2)
    public BigDecimal valorDiariaAplicado;

    @Column(name = "valor_total", precision = 10, scale = 2)
    public BigDecimal valorTotal;

    @Column(name = "valor_multa", precision = 10, scale = 2)
    public BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    public String status = "EM_ANDAMENTO";
}