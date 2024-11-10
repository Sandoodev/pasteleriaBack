package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.HorarioAperturaCierre;
import com.pasteleriaBack.pasteleriaBack.service.HorarioAperturaCierreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;

@RestController
@RequestMapping("/api/horarios")
public class HorarioAperturaCierreController {
    @Autowired
    private HorarioAperturaCierreService horarioAperturaCierreService;

    @CrossOrigin
    @PutMapping("/editar")
    public ResponseEntity<HorarioAperturaCierre> editarHorario(
            @RequestParam Integer autorDni, // DNI del autor que realiza el cambio
            @RequestParam Time aperturaManana, // Nuevo horario de apertura de la mañana
            @RequestParam Time cierreManana, // Nuevo horario de cierre de la mañana
            @RequestParam Time aperturaTarde, // Nuevo horario de apertura de la tarde
            @RequestParam Time cierreTarde) { // Nuevo horario de cierre de la tarde

        // Llama al servicio para editar el horario
        HorarioAperturaCierre horarioActualizado = horarioAperturaCierreService.editarHorarioAperturaCierre(
                autorDni, aperturaManana, cierreManana, aperturaTarde, cierreTarde);

        // Retorna la respuesta con el horario actualizado
        return ResponseEntity.ok(horarioActualizado);
    }
}