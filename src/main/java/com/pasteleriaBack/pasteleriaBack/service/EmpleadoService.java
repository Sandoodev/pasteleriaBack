package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Auditoria;
import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.HorarioAperturaCierre;
import com.pasteleriaBack.pasteleriaBack.model.RolEmpleadoENUM;
import com.pasteleriaBack.pasteleriaBack.repository.AuditoriaRepository;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.HorarioAperturaCierreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private AuditoriaRepository auditoriaRepository; // Repositorio para auditorías

    @Autowired
    private HorarioAperturaCierreRepository horarioRepository; // Repositorio para horarios

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
    //REQUERIMIENTO 3: editar el límite de jornada laboral
    public ResponseEntity<String> editarLimiteJornadaLaboral(Integer dniAdministrador, Integer dniEmpleado, Double nuevoLimite) {
        // Obtener el empleado cuyo límite de jornada laboral se va a cambiar
        Empleado empleadoAModificar = empleadoRepository.findById(dniEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Obtener el administrador que está realizando el cambio
        Empleado administrador = empleadoRepository.findById(dniAdministrador)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        // Verificar si el administrador tiene el rol adecuado
        if (!RolEmpleadoENUM.Administrador.equals(administrador.getEmp_rol())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para realizar este cambio.");
        }

        // Validar el nuevo límite
        if (nuevoLimite < 1) {
            return ResponseEntity.badRequest().body("El límite de jornada laboral no puede ser menor a 1 hora.");
        }

        // Obtener todos los horarios de apertura y cierre
        List<HorarioAperturaCierre> horarios = horarioRepository.findAll();
        if (horarios.size() < 2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se requieren al menos dos horarios para determinar la mañana y la tarde.");
        }

        // Suponiendo que tienes exactamente dos horarios
        HorarioAperturaCierre horarioManana = horarios.get(0);
        HorarioAperturaCierre horarioTarde = horarios.get(1);

        // Obtener las horas de apertura y cierre
        LocalTime horaAperturaManana = horarioManana.getHac_horarioApertura().toLocalTime();
        LocalTime horaCierreManana = horarioManana.getHac_horarioCierre().toLocalTime();
        LocalTime horaAperturaTarde = horarioTarde.getHac_horarioApertura().toLocalTime();
        LocalTime horaCierreTarde = horarioTarde.getHac_horarioCierre().toLocalTime();

        // Calcular el tiempo de apertura y cierre para la mañana
        long horasManana = Duration.between(horaAperturaManana, horaCierreManana).toHours();
        // Calcular el tiempo de apertura y cierre para la tarde
        long horasTarde = Duration.between(horaAperturaTarde, horaCierreTarde).toHours();

        // Sumar los rangos de horas
        long horasTotales = horasManana + horasTarde;

        // Comparar el nuevo límite con la suma de los rangos de horas
        if (nuevoLimite > horasTotales) {
            return ResponseEntity.badRequest().body("El límite de jornada laboral no puede ser mayor que el tiempo total de apertura y cierre del negocio.");
        }

        // Actualizar el límite de jornada laboral
        empleadoAModificar.setEmp_jornadaLaboral(nuevoLimite.intValue()); // sabiendo que emp_jornadaLaboral es un Integer
        empleadoRepository.save(empleadoAModificar);

        // Registrar la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Actualización del límite de jornada laboral");
        auditoria.setAud_detalle("Nuevo límite: " + nuevoLimite + " establecido por el administrador: " + administrador.getEmp_apellidoNombre() + " para el empleado: " + empleadoAModificar.getEmp_apellidoNombre());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(administrador); // Guardar el DNI del administrador
        auditoriaRepository.save(auditoria);

        return ResponseEntity.ok("Límite de jornada laboral actualizado correctamente.");
    }
}