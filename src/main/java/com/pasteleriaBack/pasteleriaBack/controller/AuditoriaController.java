package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.Auditoria;
import com.pasteleriaBack.pasteleriaBack.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {
    @Autowired
    private AuditoriaService auditoriaService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Auditoria> crearAuditoria(@RequestBody Auditoria auditoria) {
        return ResponseEntity.ok(auditoriaService.crearAuditoria(auditoria));
    }

    // Otros métodos


}