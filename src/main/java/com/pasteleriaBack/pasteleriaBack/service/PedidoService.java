package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoDTO;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoCantidadDTO;
import com.pasteleriaBack.pasteleriaBack.model.*;
import com.pasteleriaBack.pasteleriaBack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoProductoRepository pedidoProductoRepository;
    @Autowired
    private PedidoDomicilioRepository pedidoDomicilioRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private HorarioAperturaCierreRepository horarioAperturaCierreRepository;

    public Pedido savePedido(Pedido pedido) {
        // Lógica para calcular total, asignar cocinero, etc.
        return pedidoRepository.save(pedido);
    }

    // Método para obtener todos los pedidos
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    // Método para obtener un pedido por ID
    public ResponseEntity<Pedido> getPedidoById(Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Método para actualizar un pedido existente
    public ResponseEntity<Pedido> updatePedido(Integer id, Pedido updatedPedido) {//recibir como parametro los valores a setear, y persistir el objeto
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedPedido.setPed_id(id); // Asegurarse de que el ID se mantenga
        Pedido savedPedido = pedidoRepository.save(updatedPedido);
        return ResponseEntity.ok(savedPedido);
    }

    // Método para eliminar un pedido
    public ResponseEntity<Void> deletePedido(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Método para crear un nuevo pedido
    public ResponseEntity<Pedido> createPedido(PedidoDTO pedidoDTO) {
        // Verificar si el cliente existe
        Cliente cliente = clienteRepository.findById(pedidoDTO.getDni())
                .orElseGet(() -> {
                    // Si el cliente no existe, crear uno nuevo
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setCli_dni(pedidoDTO.getDni());
                    nuevoCliente.setCli_apellidoNombre(pedidoDTO.getCli_apellidoNombreDto());
                    nuevoCliente.setCli_email(pedidoDTO.getEmailDto());
                    nuevoCliente.setCli_numCelu(pedidoDTO.getCli_numCeluDto());
                    nuevoCliente.setCli_nroTelefonoFijo(pedidoDTO.getCli_nroTelefonoFijoDto());

                    // Guardar el nuevo cliente en la base de datos
                    return clienteRepository.save(nuevoCliente);
                });
        // Crear un nuevo pedido
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setPed_fechaDeCreacion(new Timestamp(System.currentTimeMillis()));
        nuevoPedido.setPedEstado(EstadoPedidoENUM.enPreparacion);
        nuevoPedido.setPed_entrega(pedidoDTO.getPed_entregaDto());
        nuevoPedido.setPed_descripcion(pedidoDTO.getPed_descripcionDto());
        nuevoPedido.setPorcentajeComisionPedidoActual(pedidoDTO.getPorcentajeComisionPedidoActualDto());

        // Asignar cocinero
        Empleado cocinero = asignarCocinero(nuevoPedido);
        nuevoPedido.setEmpleado(cocinero); // Se asigna en el campo empleado de Pedido

        // Calcular el tiempo total de producción del nuevo pedido
        int tiempoProduccionNuevoPedido = calcularTiempoProduccion(nuevoPedido);
        // Asignar fecha y horario de envío/retiro
        Date fechaEntrega = calcularFechaEntrega(tiempoProduccionNuevoPedido);
        nuevoPedido.setPedFechaDeEntrega(new Timestamp(fechaEntrega.getTime()));

        // Manejo de envío a domicilio
        if (EstadoEntregaENUM.envio.equals(pedidoDTO.getPed_entregaDto())) {
            PedidoDomicilio direccion = new PedidoDomicilio();
            direccion.setPed_barrio(pedidoDTO.getPed_barrioDto());
            direccion.setPed_calle(pedidoDTO.getPed_calleDto());
            direccion.setPed_numeroCasa(pedidoDTO.getPed_numeroCasaDto());
            direccion.setPed_ciudad(pedidoDTO.getPed_ciudadDto());
            // Establecer la relación entre Pedido y PedidoDomicilio
            direccion.setPedido(nuevoPedido); // Método de PedidoDomicilio

            // Guardar la dirección
            PedidoDomicilio direccionGuardada = pedidoDomicilioRepository.save(direccion);
            nuevoPedido.setPedidoDomicilio(direccionGuardada); // Establecer la relación en Pedido
        }

        // Guardar el pedido para obtener el ID generado
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        // Agregar productos al pedido
        for (ProductoCantidadDTO productoCantidad : pedidoDTO.getProductos()) {
            Producto producto = productoRepository.findById(productoCantidad.getProdId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Crear la clave compuesta para PedidoProducto
            PedidoProductoId id = new PedidoProductoId();
            id.setProdId(productoCantidad.getProdId());
            id.setPedId(pedidoGuardado.getPed_id());

            // Crear el objeto PedidoProducto
            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setId(id);
            pedidoProducto.setPedido(pedidoGuardado);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(productoCantidad.getCantidad());

            // Establecer los precios del producto
            pedidoProducto.setPrecioCosto(producto.getProd_precioCosto());
            pedidoProducto.setPrecioVenta(producto.getProd_precioVenta());

            // Guardar el pedidoProducto en el repositorio
            pedidoProductoRepository.save(pedidoProducto);
        }

        //Generar comprobante
        generarComprobante(pedidoGuardado);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado); // Retornar el pedido creado con estado 201
    }

    private Empleado asignarCocinero(Pedido pedido) {
        List<Empleado> cocineros = empleadoRepository.findByEmpRol(RolEmpleadoENUM.Cocinero);
        System.out.println("Asignando cocinero para el pedido: " + pedido.getPed_id());

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
        //List<Pedido> pedidos = pedidoRepository.findByEmpleadoAndPedFechaDeEntrega(cocinero, fechaEnvio);
        System.out.println("Calculando tiempo asignado para el cocinero: " + cocinero.getEmp_dni());
        List<Pedido> pedidosEnPreparacion = pedidoRepository.findByEmpleadoAndPedEstado(cocinero, EstadoPedidoENUM.enPreparacion);

        // Obtener todos los pedidos asignados al cocinero
        //List<Pedido> todosLosPedidos = pedidoRepository.findByEmpleado(cocinero);

        // Filtrar solo los pedidos que están en estado "en preparación"
        //List<Pedido> pedidosEnPreparacion = todosLosPedidos.stream()
               // .filter(pedido -> pedido.getPedEstado() == EstadoPedidoENUM.enPreparacion)
                //.collect(Collectors.toList());

        System.out.println("Calculando tiempo asignado para el cocinero2: " + cocinero.getEmp_dni());
        int tiempoTotal = 0;
        for (Pedido pedido : pedidosEnPreparacion) {
            System.out.println("Hasta aqui tambien llega" + pedido.getPed_id());
            // Calcular el tiempo de producción de cada pedido
            int tiempoProduccion = calcularTiempoProduccion(pedido); // Reutilizar el método que ya tienes
            tiempoTotal += tiempoProduccion;
        }

        return tiempoTotal; // Devuelve el tiempo total en minutos
    }

    //obtiene el tiempo totalde produccion en minutos de todos los productos del pedido
    private int calcularTiempoProduccion(Pedido pedido) {
        // Inicializa el tiempo total en minutos
        int tiempoTotal = 0;

        // Itera sobre los productos en el pedido
        for (PedidoProducto pedidoProducto : pedido.getPedidoProductos()) {
            // Obtiene el producto asociado al pedido
            Producto producto = pedidoProducto.getProducto();
            // Suma el tiempo de producción del producto multiplicado por la cantidad
            tiempoTotal += producto.getProd_tiempoDeProduccion() * pedidoProducto.getCantidad();
        }
        // Devuelve el tiempo total en minutos
        return tiempoTotal;
    }

    private Date calcularFechaEntrega(int tiempoProduccionNuevoPedido) {
        // Obtener el horario de apertura y cierre
        HorarioAperturaCierre horario = horarioAperturaCierreRepository.findFirstByOrderByHacIdAsc();
        Time aperturaManana = horario.getHac_manana_apertura();
        Time cierreManana = horario.getHac_manana_cierre();
        Time aperturaTarde = horario.getHac_tarde_apertura();
        Time cierreTarde = horario.getHac_tarde_cierre();

        // Calcular los minutos disponibles en un día
        int minutosDisponiblesPorDia = calcularMinutosDisponiblesPorDia(aperturaManana, cierreManana, aperturaTarde, cierreTarde);

        // Calcular la fecha de entrega comenzando desde el día siguiente
        Calendar entregaCalendar = Calendar.getInstance();
        entregaCalendar.add(Calendar.DAY_OF_YEAR, 1); // Sumar un día

        // Sumar el tiempo de producción al calendario
        while (tiempoProduccionNuevoPedido > 0) {
            // Si el tiempo de producción restante es mayor que los minutos disponibles en el día
            if (tiempoProduccionNuevoPedido >= minutosDisponiblesPorDia) {
                tiempoProduccionNuevoPedido -= minutosDisponiblesPorDia; // Restar los minutos del día
                // Avanzar al siguiente día
                entregaCalendar.add(Calendar.DAY_OF_YEAR, 1);
            } else {
                // Si el tiempo de producción restante es menor que los minutos disponibles en el día
                // Ajustar la hora de entrega dentro del horario de apertura
                entregaCalendar.set(Calendar.HOUR_OF_DAY, aperturaManana.getHours());
                entregaCalendar.set(Calendar.MINUTE, aperturaManana.getMinutes());

                // Sumar el tiempo de producción restante
                while (tiempoProduccionNuevoPedido > 0) {
                    // Verificar si estamos dentro del horario de apertura
                    if (isDentroDelHorario(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde)) {
                        // Calcular los minutos disponibles en el horario actual
                        int minutosDisponibles = calcularMinutosDisponiblesEnHorario(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde);
                        if (minutosDisponibles > 0) {
                            int minutosASumar = Math.min(tiempoProduccionNuevoPedido, minutosDisponibles);
                            entregaCalendar.add(Calendar.MINUTE, minutosASumar); // Sumar minutos
                            tiempoProduccionNuevoPedido -= minutosASumar; // Reducir el tiempo restante
                        }
                    } else {
                        // Si estamos fuera del horario, avanzar al siguiente horario de apertura
                        avanzarAlSiguienteHorario(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde);
                    }
                }
            }
        }

        return entregaCalendar.getTime();
    }

    //devuelve la cantidad de minutos total de la jornada diaria del negocio
    private int calcularMinutosDisponiblesPorDia(Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Calcular los minutos disponibles en un día
        int minutosManana = (cierreManana.getHours() * 60 + cierreManana.getMinutes()) - (aperturaManana.getHours() * 60 + aperturaManana.getMinutes());
        int minutosTarde = (cierreTarde.getHours() * 60 + cierreTarde.getMinutes()) - (aperturaTarde.getHours() * 60 + aperturaTarde.getMinutes());
        return minutosManana + minutosTarde;
    }

    private int calcularMinutosDisponiblesEnHorario(Calendar entregaCalendar, Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Calcular los minutos disponibles en el horario actual
        Calendar finHorario = (Calendar) entregaCalendar.clone();

        // Si estamos en la mañana
        if (entregaCalendar.after(aperturaManana) && entregaCalendar.before(cierreManana)) {
            finHorario.setTime(cierreManana);
        } else if (entregaCalendar.after(cierreManana) && entregaCalendar.before(aperturaTarde)) {
            // Si estamos entre el cierre de la mañana y la apertura de la tarde
            finHorario.setTime(aperturaTarde);
        }else {
            // Si estamos fuera del horario, no hay minutos disponibles
            return 0;
        }

        // Calcular los minutos disponibles desde el tiempo actual hasta el final del horario
        long minutosDisponibles = (finHorario.getTimeInMillis() - entregaCalendar.getTimeInMillis()) / (1000 * 60);
        return (int) minutosDisponibles;
    }

    //controla si esta dentro de las horas de atencion del local
    private boolean isDentroDelHorario(Calendar entregaCalendar, Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Obtener la hora actual en el calendario
        int hora = entregaCalendar.get(Calendar.HOUR_OF_DAY);
        int minuto = entregaCalendar.get(Calendar.MINUTE);

        // Comprobar si está dentro del horario de la mañana
        if (hora >= aperturaManana.getHours() && hora < cierreManana.getHours()) {
            return true;
        }

        // Comprobar si está dentro del horario de la tarde
        if (hora >= aperturaTarde.getHours() && hora < cierreTarde.getHours()) {
            return true;
        }

        return false;
    }

    private void avanzarAlSiguienteHorario(Calendar entregaCalendar, Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Avanzar al siguiente día
        entregaCalendar.add(Calendar.DAY_OF_YEAR, 1);
        entregaCalendar.set(Calendar.HOUR_OF_DAY, aperturaManana.getHours());
        entregaCalendar.set(Calendar.MINUTE, aperturaManana.getMinutes());
    }
    private void generarComprobante(Pedido pedido) {
        // Crear un StringBuilder para construir el comprobante
        StringBuilder comprobante = new StringBuilder();

        // Información del pedido
        comprobante.append("Comprobante de Pedido\n");
        comprobante.append("Fecha de Creación: ").append(pedido.getPed_fechaDeCreacion()).append("\n");
        comprobante.append("Fecha de Envío/Retiros: ").append(pedido.getPedFechaDeEntrega()).append("\n");
        comprobante.append("Descripción: ").append(pedido.getPed_descripcion()).append("\n");
        comprobante.append("Estado: ").append(pedido.getPedEstado()).append("\n");
        comprobante.append("Cocinero a Cargo: ").append(pedido.getEmpleado().getEmp_apellidoNombre()).append("\n");

        // Productos pedidos
        comprobante.append("Productos:\n");
        double total = 0.0; // Variable para calcular el total del pedido
        for (PedidoProducto pedidoProducto : pedido.getPedidoProductos()) {
            Producto producto = pedidoProducto.getProducto(); // Obtener el producto desde PedidoProducto
            int cantidad = pedidoProducto.getCantidad();
            double precioVenta = pedidoProducto.getPrecioVenta();
            double subtotal = precioVenta * cantidad;

            comprobante.append(" - ").append(producto.getProd_titulo())
                    .append(" x ").append(cantidad)
                    .append(" (").append(precioVenta).append(") - Subtotal: ").append(subtotal).append("\n");

            total += subtotal; // Sumar al total
        }
        // Mostrar el total del pedido
        comprobante.append("Total: ").append(total).append("\n");

        // Dirección de envío a domicilio (si aplica)
        if (pedido.getPedidoDomicilio() != null) {
            comprobante.append("Dirección de Envío:\n");
            comprobante.append(" - Barrio: ").append(pedido.getPedidoDomicilio().getPed_barrio()).append("\n");
            comprobante.append(" - Calle: ").append(pedido.getPedidoDomicilio().getPed_calle()).append("\n");
            comprobante.append(" - Número de Casa: ").append(pedido.getPedidoDomicilio().getPed_numeroCasa()).append("\n");
            comprobante.append(" - Ciudad: ").append(pedido.getPedidoDomicilio().getPed_ciudad()).append("\n");
        }

        // Mostrar el comprobante
        System.out.println(comprobante.toString());
    }
}










