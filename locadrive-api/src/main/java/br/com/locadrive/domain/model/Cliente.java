package br.com.locadrive.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends PanacheEntity {

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