package br.com.locadrive.domain.service;

import br.com.locadrive.domain.model.Veiculo;
import br.com.locadrive.infrastructure.exception.EntidadeNaoEncontradaException;
import br.com.locadrive.infrastructure.exception.RegraDeNegocioException;
import br.com.locadrive.rest.dto.VeiculoRequestDTO;
import br.com.locadrive.rest.dto.VeiculoResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class VeiculoService {

    public List<VeiculoResponseDTO> listarTodos() {
        return Veiculo.<Veiculo>listAll()
                .stream()
                .map(VeiculoResponseDTO::fromEntity)
                .toList();
    }

    public Veiculo buscarPorIdEntity(Long id) {
        Veiculo veiculo = Veiculo.findById(id);
        if (veiculo == null) {
            throw new EntidadeNaoEncontradaException("Veículo não encontrado com o ID: " + id);
        }
        return veiculo;
    }

    @Transactional
    public VeiculoResponseDTO criar(VeiculoRequestDTO dto) {
        if (Veiculo.find("placa", dto.placa()).firstResult() != null) {
            throw new RegraDeNegocioException("Já existe um veículo cadastrado com a placa: " + dto.placa());
        }

        Veiculo veiculo = new Veiculo();
        veiculo.modelo = dto.modelo();
        veiculo.marca = dto.marca();
        veiculo.placa = dto.placa();
        veiculo.anoFabricacao = dto.anoFabricacao();
        veiculo.valorDiaria = dto.valorDiaria();
        veiculo.disponivel = true;

        veiculo.persist();
        return VeiculoResponseDTO.fromEntity(veiculo);
    }
}
