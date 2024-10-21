package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.HorarioAperturaCierre;
import com.pasteleriaBack.pasteleriaBack.service.HorarioAperturaCierreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/horarios-apertura-cierre")
public class HorarioAperturaCierreController {
    @Autowired
    private HorarioAperturaCierreService horarioAperturaCierreService;

    @PostMapping
    public ResponseEntity<HorarioAperturaCierre> crearHorarioAperturaCierre(@RequestBody HorarioAperturaCierre horarioAperturaCierre) {
        return ResponseEntity.ok(horarioAperturaCierreService.crearHorarioAperturaCierre(horarioAperturaCierre));
    }

    // Otros métodos según los requerimientos
}
