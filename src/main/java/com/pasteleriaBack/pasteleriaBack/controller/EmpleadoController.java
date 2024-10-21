package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.Empleado;
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
}

