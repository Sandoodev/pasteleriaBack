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
    //autor y emp_dni
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
        if (!RolEmpleadoENUM.Administrador.equals(administrador.getEmpRol())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para realizar este cambio.");
        }

        // Validar el nuevo límite
        if (nuevoLimite < 1) {
            return ResponseEntity.badRequest().body("El límite de jornada laboral no puede ser menor a 1 hora.");
        }

        // Obtener el horario de apertura y cierre
        List<HorarioAperturaCierre> horarios = horarioRepository.findAll();
        if (horarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No hay horarios de apertura y cierre definidos.");
        }

        // Suponiendo que solo hay un horario que contiene tanto la mañana como la tarde
        HorarioAperturaCierre horario = horarios.get(0);

        // Obtener las horas de apertura y cierre
        LocalTime horaAperturaManana = horario.getHac_manana_apertura().toLocalTime();
        LocalTime horaCierreManana = horario.getHac_manana_cierre().toLocalTime();
        LocalTime horaAperturaTarde = horario.getHac_tarde_apertura().toLocalTime();
        LocalTime horaCierreTarde = horario.getHac_tarde_cierre().toLocalTime();

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

    public ResponseEntity<Empleado> createEmpleado(Empleado nuevoEmpleado, Integer dniAdministrador) {
        System.out.println(nuevoEmpleado.getEmp_dni());
        System.out.println(dniAdministrador);

        if (dniAdministrador == null) {
            return ResponseEntity.badRequest().body(null); // Manejar el caso donde el DNI es nulo
        }

        // Verificar que el DNI del nuevo empleado no sea nulo
        if (nuevoEmpleado.getEmp_dni() == null) {
            return ResponseEntity.badRequest().body(null); // Manejar el caso donde el DNI del empleado es nulo
        }

        // Verificar si el DNI ya existe
        if (empleadoRepository.existsById(nuevoEmpleado.getEmp_dni())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }

        // Convertir el rol de String a RolEmpleadoENUM
        String rolString = nuevoEmpleado.getEmpRol().name();
        RolEmpleadoENUM rol;
        try {
            rol = RolEmpleadoENUM.valueOf(rolString);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Manejar el caso donde el rol no es válido
        }

        // Verificar que el rol no sea nulo
        if (nuevoEmpleado.getEmpRol() == null) {
            System.out.println("Lo toma como nulo");
            return ResponseEntity.badRequest().body(null); // Manejar el caso donde el rol es nulo
        }

        // Asignar el rol convertido al nuevo empleado
        nuevoEmpleado.setEmpRol(rol);

        // Guardar el nuevo empleado
        Empleado savedEmpleado = empleadoRepository.save(nuevoEmpleado);

        // Obtener el administrador que está registrando al nuevo empleado
        Empleado administrador = empleadoRepository.findById(dniAdministrador)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        // Registrar la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Registro de empleado");
        auditoria.setAud_detalle("El administrador " + administrador.getEmp_apellidoNombre() + " registró al empleado: " + nuevoEmpleado.getEmp_apellidoNombre() + " con rol: " + nuevoEmpleado.getEmpRol());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(administrador);
        auditoriaRepository.save(auditoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpleado); // 201 Created
    }

    // REQUERIMIENTO 8: actualizar un cocinero existente
    public ResponseEntity<String> updateCocinero(Integer dni, Empleado updatedEmpleado, Integer dniAdministrador) {
        // Verificar si el cocinero existe
        if (!empleadoRepository.existsById(dni)) {
            return ResponseEntity.notFound().build();
        }

        // Obtener el cocinero existente
        Empleado existingEmpleado = empleadoRepository.findById(dni).orElseThrow(() -> new RuntimeException("Cocinero no encontrado"));

        // Actualizar los campos necesarios
        existingEmpleado.setEmp_apellidoNombre(updatedEmpleado.getEmp_apellidoNombre());
        existingEmpleado.setEmp_email(updatedEmpleado.getEmp_email());
        existingEmpleado.setEmp_nroCelular(updatedEmpleado.getEmp_nroCelular());
        existingEmpleado.setEmp_sueldo(updatedEmpleado.getEmp_sueldo());
        existingEmpleado.setEmp_porcentajeComisionPedido(updatedEmpleado.getEmp_porcentajeComisionPedido());
        existingEmpleado.setEmpRol(updatedEmpleado.getEmpRol());

        // Si se proporciona una nueva contraseña, actualizarla
        if (updatedEmpleado.getEmp_contraseña() != null) {
            existingEmpleado.setEmp_contraseña(updatedEmpleado.getEmp_contraseña());
        }

        // Guardar los cambios
        empleadoRepository.save(existingEmpleado);

        // Registrar la auditoría
        Empleado administrador = empleadoRepository.findById(dniAdministrador)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Actualización de cocinero");
        auditoria.setAud_detalle("El administrador " + administrador.getEmp_apellidoNombre() + " actualizó la información del cocinero: " + existingEmpleado.getEmp_apellidoNombre());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(administrador); // Guardar el DNI del administrador
        auditoriaRepository.save(auditoria);

        return ResponseEntity.ok("Cocinero actualizado correctamente.");
    }
}