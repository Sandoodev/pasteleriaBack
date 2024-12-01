package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoPorCocinero;
import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.RolEmpleadoENUM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    // Método para encontrar empleados por rol
    List<Empleado> findByEmpRol(RolEmpleadoENUM rol);
}

