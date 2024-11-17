package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.exception.ResourceNotFoundException;
import com.pasteleriaBack.pasteleriaBack.model.*;
import com.pasteleriaBack.pasteleriaBack.repository.AuditoriaRepository;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.HorarioAperturaCierreRepository;
import com.pasteleriaBack.pasteleriaBack.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private AuditoriaRepository auditoriaRepository; // Repositorio para auditorías

    @Autowired
    private HorarioAperturaCierreRepository horarioRepository; // Repositorio para horarios
    @Autowired
    private PedidoRepository pedidoRepository;


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

    // REQUERIMIENTNO 10: eliminacion de un cocinero
    @Transactional
    public ResponseEntity<Void> deleteEmpleado(Integer dni, String motivo, Integer dniAutor) {
        // Verificar si el cocinero existe
        Empleado cocinero = empleadoRepository.findById(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Cocinero no encontrado"));

        // Obtener los pedidos asignados al cocinero
        List<Pedido> pedidosAsignados = pedidoRepository.findByEmpleado(cocinero);

        // Cambiar el estado del cocinero a "eliminado"
        cocinero.setEmp_estado(EstadoEmpleadoENUM.inactivo);
        empleadoRepository.save(cocinero); // Guardar el cambio en el estado

        // Obtener el empleado que realiza la eliminación
        Empleado autor = empleadoRepository.findById(dniAutor)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con DNI: " + dniAutor));

        // Registrar la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Eliminación lógica de cocinero");
        auditoria.setAud_detalle("Cocinero DNI: " + dni + " marcado como eliminado." +
                (motivo != null ? ", Motivo: " + motivo : ""));
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(autor); // Asigna el empleado autor
        auditoriaRepository.save(auditoria); // Guarda el registro de auditoría

        // Verifica si hay pedidos asignados
        if (pedidosAsignados.isEmpty()) {
            System.out.println("No hay pedidos asignados para el cocinero con DNI: " + dni);
            return ResponseEntity.noContent().build(); // Retorna respuesta sin contenido
        }

        // Reasigna los pedidos a otros cocineros
        for (Pedido pedido : pedidosAsignados) {
            try {
                Empleado nuevoCocinero = asignarCocinero();
                pedido.setEmpleado(nuevoCocinero);
                pedidoRepository.save(pedido); // Guarda el pedido actualizado
            } catch (RuntimeException e) {
                System.err.println("Error al reasignar el pedido ID: " + pedido.getPed_id() + " - " + e.getMessage());
            }
        }
        return ResponseEntity.noContent().build(); // Retornar respuesta sin contenido
    }

    private Empleado asignarCocinero() {
        List<Empleado> cocineros = empleadoRepository.findByEmpRol(RolEmpleadoENUM.Cocinero);

        // Filtrar solo los cocineros que están activos
        List<Empleado> cocinerosActivos = cocineros.stream()
                .filter(cocinero -> cocinero.getEmp_estado() == EstadoEmpleadoENUM.activo)
                .collect(Collectors.toList());

        if (cocineros.isEmpty()) {
            throw new RuntimeException("No hay cocineros disponibles");
        }

        Empleado cocineroAsignado = null;
        int tiempoMinimo = Integer.MAX_VALUE;

        for (Empleado cocinero : cocinerosActivos) {
            // Calcular el tiempo total asignado al cocinero
            int tiempoAsignado = calcularTiempoAsignado(cocinero);
            System.out.println("Tiempo asignado para el cocinero " + cocinero.getEmp_dni() + ": " + tiempoAsignado);

            // Aquí no verificamos si el cocinero puede aceptar el nuevo pedido en base a su jornada laboral
            // Solo buscamos el cocinero con menor carga de trabajo
            if (tiempoAsignado < tiempoMinimo) {
                tiempoMinimo = tiempoAsignado;
                System.out.println("El tiempo minimo es " + tiempoMinimo);
                cocineroAsignado = cocinero;
                System.out.println("El cocinero asignado es: " + cocineroAsignado.getEmp_apellidoNombre());
            }else if (tiempoAsignado == tiempoMinimo) {
                // Si hay un empate, asignar aleatoriamente entre los cocineros con el mismo tiempo
                if (new Random().nextBoolean()) {
                    cocineroAsignado = cocinero;
                }
            }
        }

        if (cocineroAsignado == null) {
            throw new RuntimeException("No hay cocineros disponibles para asignar el nuevo pedido");
        }

        return cocineroAsignado;
    }

    //calcula el tiempo total de produccion de todos los productos juntos del pedido
    private int calcularTiempoAsignado(Empleado cocinero) {
        // Obtener todos los pedidos asignados al cocinero
        System.out.println("Calculando tiempo asignado para el cocinero: " + cocinero.getEmp_dni());
        List<Pedido> pedidosEnPreparacion = pedidoRepository.findByEmpleadoAndPedEstado(cocinero, EstadoPedidoENUM.enPreparacion);

        int tiempoTotal = 0;
        for (Pedido pedido : pedidosEnPreparacion) {
            System.out.println("Hasta aqui tambien llega" + pedido.getPed_id());
            // Calcular el tiempo de producción de cada pedido
            int tiempoProduccion = calcularTiempoProduccion(pedido.getPed_id());
            tiempoTotal += tiempoProduccion;
        }

        return tiempoTotal; // Devuelve el tiempo total en minutos
    }

    //obtiene el tiempo total de produccion en minutos de todos los productos del pedido
    private int calcularTiempoProduccion(int pedidoId) {
        // Busca el pedido en la base de datos usando el ID
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));

        // Inicializa el tiempo total en minutos
        int tiempoTotal = 0;

        // Itera sobre los productos en el pedido
        for (PedidoProducto pedidoProducto : pedido.getPedidoProductos()) {
            // Obtiene el producto asociado al pedido
            Producto producto = pedidoProducto.getProducto();
            // Suma el tiempo de producción del producto multiplicado por la cantidad
            tiempoTotal += producto.getProd_tiempoDeProduccion() * pedidoProducto.getCantidad();
            System.out.println("producto: " + producto.getProd_tiempoDeProduccion() + " * cantidad: " + pedidoProducto.getCantidad());
        }
        // Devuelve el tiempo total en minutos
        System.out.println("EL TIEMPO TOTAL ES: " + tiempoTotal);
        return tiempoTotal;
    }
}