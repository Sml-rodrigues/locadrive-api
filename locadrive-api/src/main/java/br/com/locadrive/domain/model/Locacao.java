package br.com.locadrive.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "locacoes")
public class Locacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente é obrigatório")
    public Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    @NotNull(message = "O veículo é obrigatório")
    public Veiculo veiculo;

    @NotNull(message = "A data de locação é obrigatória")
    @Column(name = "data_locacao", nullable = false)
    public LocalDateTime dataInicio = LocalDateTime.now();

    @NotNull(message = "A data fim prevista é obrigatória")
    @Column(name = "data_fim_prevista", nullable = false)
    public LocalDateTime dataFimPrevista;

    @Column(name = "data_devolucao_real")
    public LocalDateTime dataDevolucao;

    @Transient
    public BigDecimal valorDiariaAplicado;

    @Column(name = "valor_total", precision = 10, scale = 2)
    public BigDecimal valorTotal;

    @Transient
    public BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    public String status = "EM_ANDAMENTO";
}