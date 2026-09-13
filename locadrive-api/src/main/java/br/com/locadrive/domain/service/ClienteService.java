package br.com.locadrive.domain.service;

import br.com.locadrive.domain.model.Cliente;
import br.com.locadrive.infrastructure.exception.EntidadeNaoEncontradaException;
import br.com.locadrive.infrastructure.exception.RegraDeNegocioException;
import br.com.locadrive.rest.dto.ClienteRequestDTO;
import br.com.locadrive.rest.dto.ClienteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ClienteService {

    public List<ClienteResponseDTO> listarTodos() {
        return Cliente.<Cliente>listAll()
                .stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    public Cliente buscarPorIdEntity(Long id) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado com o ID: " + id);
        }
        return cliente;
    }

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        if (Cliente.findByCpf(dto.cpf()) != null) {
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CPF: " + dto.cpf());
        }

        Cliente cliente = new Cliente();
        cliente.nome = dto.nome();
        cliente.cpf = dto.cpf();
        cliente.email = dto.email();
        cliente.cnh = dto.cnh();

        cliente.persist();
        return ClienteResponseDTO.fromEntity(cliente);
    }
}