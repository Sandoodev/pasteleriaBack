package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    // Método para crear un nuevo empleado
    public ResponseEntity<Empleado> crearEmpleado(Empleado empleado) {
        // Aquí puedes agregar lógica adicional si es necesario
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpleado);
    }

    // Método para obtener todos los empleados
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    // Método para obtener un empleado por DNI
    public ResponseEntity<Empleado> getEmpleadoByDni(Integer dni) {
        Optional<Empleado> empleado = empleadoRepository.findById(dni);
        return empleado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Método para actualizar un empleado existente
    public ResponseEntity<Empleado> updateEmpleado(Integer dni, Empleado updatedEmpleado) {
        if (!empleadoRepository.existsById(dni)) {
            return ResponseEntity.notFound().build();
        }
        updatedEmpleado.setEmp_dni(dni); // Asegurarse de que el DNI se mantenga
        Empleado savedEmpleado = empleadoRepository.save(updatedEmpleado);
        return ResponseEntity.ok(savedEmpleado);
    }

    // Método para eliminar un empleado
    public ResponseEntity<Void> deleteEmpleado(Integer dni) {
        if (!empleadoRepository.existsById(dni)) {
            return ResponseEntity.notFound().build();
        }
        empleadoRepository.deleteById(dni);
        return ResponseEntity.noContent().build();
    }

    // Otros métodos según los requerimientos
}