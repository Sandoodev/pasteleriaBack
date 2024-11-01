package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.RolEmpleadoENUM;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    // Método para encontrar empleados por rol
    List<Empleado> findByEmpRol(RolEmpleadoENUM rol);
}

