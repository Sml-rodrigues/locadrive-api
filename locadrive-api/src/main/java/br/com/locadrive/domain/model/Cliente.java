package br.com.locadrive.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cliente_seq_gen")
    @SequenceGenerator(name = "tb_cliente_seq_gen", sequenceName = "tb_cliente_seq", allocationSize = 1)
    public Long id;

    @Column(nullable = false, length = 250)
    public String nome;

    @Column(nullable = false, unique = true, length = 11)
    public String cpf;

    @Column(nullable = false, unique = true, length = 250)
    public String email;

    @Column(nullable = false, length = 20)
    public String cnh;

    @Column(length = 20)
    public String telefone;

    public static Cliente findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
}