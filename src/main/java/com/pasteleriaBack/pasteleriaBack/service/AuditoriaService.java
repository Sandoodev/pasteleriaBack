package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Auditoria;
import com.pasteleriaBack.pasteleriaBack.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {
    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public Auditoria crearAuditoria(Auditoria auditoria) {
        // Lógica para crear auditoría
        return auditoriaRepository.save(auditoria);
    }

    public List<Auditoria> listarAuditorias() {
        return auditoriaRepository.findAll();
    }

    // Otros métodos según los requerimientos
}