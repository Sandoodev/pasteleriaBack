package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.RolEmpleadoENUM; // Asegúrate de importar el enum
import com.pasteleriaBack.pasteleriaBack.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @CrossOrigin
    @GetMapping
    public List<Empleado> getAllEmpleados() {
        return empleadoService.getAllEmpleados();
    }

    @CrossOrigin
    @GetMapping("/{dni}")
    public ResponseEntity<Empleado> getEmpleadoByDni(@PathVariable Integer dni) {
        return empleadoService.getEmpleadoByDni(dni);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Empleado> createEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.crearEmpleado(empleado);
    }

    @CrossOrigin
    @PutMapping("/{dni}")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable Integer dni, @RequestBody Empleado updatedEmpleado) {
        return empleadoService.updateEmpleado(dni, updatedEmpleado);
    }

    @CrossOrigin
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Integer dni) {
        return empleadoService.deleteEmpleado(dni);
    }


    // Otros métodos
    // Método para REQUERIMIENTO 3: cambio de horario de jornada por parte del administrador al cocinero, además del registro en auditoría
    @CrossOrigin
    @PutMapping("/{dni}/limiteJornada")
    public ResponseEntity<String> editarLimiteJornada(
            @PathVariable Integer dni,
            @RequestParam Integer dniAdministrador,
            @RequestParam Double nuevoLimite) {
        return empleadoService.editarLimiteJornadaLaboral(dniAdministrador, dni, nuevoLimite);
    }

    // REQUERIMIENTO 7: crear un nuevo empleado (cocinero o administrador)
    @CrossOrigin
    @PostMapping("/createEmpleado")
    public ResponseEntity<Empleado> createEmpleado(
            @RequestBody Empleado nuevoEmpleado,
            @RequestParam Integer dniAdministrador) {
        return empleadoService.createEmpleado(nuevoEmpleado, dniAdministrador);
    }

    // REQUERIMIENTO 8: ACTUALIZANDO COCINERO POR EL ADMIN
    @CrossOrigin
    @PutMapping("/updateCocinero/{dni}")
    public ResponseEntity<String> updateCocinero(
            @PathVariable Integer dni,
            @RequestBody Empleado updatedEmpleado,
            @RequestParam Integer dniAdministrador) {
        return empleadoService.updateCocinero(dni, updatedEmpleado, dniAdministrador);
    }
}