package com.clinica.clinica_backend.service;

import com.clinica.clinica_backend.model.Medico;
import com.clinica.clinica_backend.repository.MedicoRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicoService {
    private final MedicoRepository repo;

    public MedicoService(MedicoRepository repo) {
        this.repo = repo;
    }

    public List<Medico> listarTodos() {
        return repo.findAll();
    }

    public Medico buscarPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    }

    public Medico buscarPorCrm(String crm) {
        return repo.findByCrm(crm)
                .orElseThrow(() -> new RuntimeException("Médico com CRM " + crm + " não encontrado"));
    }

    public boolean existePorCrm(String crm) {
        return repo.findByCrm(crm).isPresent();
    }

    public Medico salvar(Medico medico) {
        if (medico.getId() == null && existePorCrm(medico.getCrm())) {
            throw new RuntimeException("Já existe um médico com este CRM");
        }
        return repo.save(medico);
    }

    public void deletar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Médico não encontrado");
        }
        repo.deleteById(id);
    }
}