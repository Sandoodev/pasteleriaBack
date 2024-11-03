package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoDTO;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoCantidadDTO;
import com.pasteleriaBack.pasteleriaBack.model.*;
import com.pasteleriaBack.pasteleriaBack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

        // Asignar fecha y horario de envío/retiro
        Timestamp fechaEnvio = calcularFechaEnvio(nuevoPedido);
        nuevoPedido.setPedFechaDeEntrega(fechaEnvio);

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

//    //metodo para asignar un cocinero
//    private Empleado asignarCocinero(Pedido pedido) {
//        List<Empleado> cocineros = empleadoRepository.findByEmpRol(RolEmpleadoENUM.Cocinero);
//
//        if (cocineros.isEmpty()) {
//            throw new RuntimeException("No hay cocineros disponibles");
//        }
//
//        Empleado cocineroAsignado = null;
//        int tiempoMinimo = Integer.MAX_VALUE;
//
//        for (Empleado cocinero : cocineros) {
//            int tiempoAsignado = calcularTiempoAsignado(cocinero, pedido.getPedFechaDeEntrega());
//            if (tiempoAsignado < tiempoMinimo) {
//                tiempoMinimo = tiempoAsignado;
//                cocineroAsignado = cocinero;
//            } else if (tiempoAsignado == tiempoMinimo) {
//                // Si hay un empate, asignar aleatoriamente entre los cocineros con el mismo tiempo
//                if (new Random().nextBoolean()) {
//                    cocineroAsignado = cocinero;
//                }
//            }
//        }
//
//        return cocineroAsignado;
//    }

    private Empleado asignarCocinero(Pedido pedido) {
        List<Empleado> cocineros = empleadoRepository.findByEmpRol(RolEmpleadoENUM.Cocinero);
        System.out.println("Asignando cocinero para el pedido: " + pedido.getPed_id());

        if (cocineros.isEmpty()) {
            throw new RuntimeException("No hay cocineros disponibles");
        }

        Empleado cocineroAsignado = null;
        int tiempoMinimo = Integer.MAX_VALUE;

        // Calcular el tiempo de producción del nuevo pedido
        int tiempoProduccionNuevoPedido = calcularTiempoProduccion(pedido);

        for (Empleado cocinero : cocineros) {
            // Calcular el tiempo total asignado al cocinero
            int tiempoAsignado = calcularTiempoAsignado(cocinero);
            System.out.println("Tiempo asignado para el cocinero " + cocinero.getEmp_dni() + ": " + tiempoAsignado);

            // Aquí no verificamos si el cocinero puede aceptar el nuevo pedido en base a su jornada laboral
            // Solo buscamos el cocinero con menor carga de trabajo
            if (tiempoAsignado < tiempoMinimo) {
                tiempoMinimo = tiempoAsignado;
                cocineroAsignado = cocinero;
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

    // Este método calcula la fecha de envío considerando el tiempo de producción y la disponibilidad del cocinero.
    private Timestamp calcularFechaEnvio(Pedido pedido) {
        // Sabiendo que cada producto tiene un tiempo de producción asociado (en minutos)
        int tiempoProduccion = calcularTiempoProduccion(pedido);

        // Obtener la fecha actual y calcular la fecha de envío para el día siguiente
        Timestamp fechaEnvio = new Timestamp(System.currentTimeMillis() + 86400000); // 1 día en milisegundos

        // Obtener el cocinero asignado
        Empleado cocinero = pedido.getEmpleado();
        System.out.println("Cocineros llegaaa: " + cocinero.getEmp_dni());

        // Verificar la disponibilidad del cocinero
        while (!verificarDisponibilidad(cocinero, fechaEnvio, tiempoProduccion)) {
            // Si el cocinero no está disponible, avanzar al siguiente día
            fechaEnvio = new Timestamp(fechaEnvio.getTime() + 86400000); // Incrementar en 1 día
        }

        return fechaEnvio;
    }

    private boolean verificarDisponibilidad(Empleado cocinero, Timestamp fechaEnvio, int tiempoProduccion) {
        if (cocinero == null) {
            throw new RuntimeException("Cocinero no asignado");
        }
        // Implementar la lógica para verificar si el cocinero tiene capacidad para aceptar el nuevo pedido
        // Esto puede incluir consultar los pedidos existentes del cocinero y sumar el tiempo de producción
        int tiempoAsignado = calcularTiempoAsignado(cocinero); // Implementar este método
        return (tiempoAsignado + tiempoProduccion) <= cocinero.getEmp_jornadaLaboral(); // Comparar con la jornada laboral
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










