package br.com.locadrive.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100)
    public String nome;

    @Column(nullable = false, unique = true, length = 14)
    public String cpf;

    @Column(nullable = false, unique = true, length = 100)
    public String email;

    @Column(nullable = false, length = 20)
    public String cnh;

    public static Cliente findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
}