package com.clinica.clinica_backend.service;

import com.clinica.clinica_backend.model.Funcionario;
import com.clinica.clinica_backend.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FuncionarioService {
    private final FuncionarioRepository repo;

    public FuncionarioService(FuncionarioRepository repo) {
        this.repo = repo;
    }

    public List<Funcionario> listarTodos() {
        return repo.findAll();
    }

    public Funcionario buscarPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public Funcionario buscarPorCpf(String cpf) {
        return repo.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Funcionário com CPF " + cpf + " não encontrado"));
    }

    public boolean existePorCpf(String cpf) {
        return repo.existsByCpf(cpf);
    }

    public List<Funcionario> buscarPorCargo(String cargo) {
        return repo.findByCargo(cargo);
    }

    public Funcionario salvar(Funcionario funcionario) {

        if (funcionario.getId() != null) {
            throw new RuntimeException("Não é permitido enviar ID ao criar um novo funcionário");
        }
        if (existePorCpf(funcionario.getCpf())) {
            throw new RuntimeException("Já existe um funcionário com este CPF");
        }
        return repo.save(funcionario);
    }

    public Funcionario atualizar(Integer id, Funcionario funcionario) {
        Funcionario existente = buscarPorId(id);

        repo.findByCpf(funcionario.getCpf()).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new RuntimeException("Já existe outro funcionário com este CPF");
            }
        });

        existente.setNome(funcionario.getNome());
        existente.setCpf(funcionario.getCpf());
        existente.setTelefone(funcionario.getTelefone());
        existente.setCargo(funcionario.getCargo());
        existente.setSalario(funcionario.getSalario());

        return repo.save(existente);
    }

    public void deletar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado");
        }
        repo.deleteById(id);
    }
}