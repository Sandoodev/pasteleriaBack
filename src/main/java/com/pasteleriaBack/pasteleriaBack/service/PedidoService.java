package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoDTO;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoCantidadDTO;
import com.pasteleriaBack.pasteleriaBack.dto.UpdatePedidoDTO;
import com.pasteleriaBack.pasteleriaBack.model.*;
import com.pasteleriaBack.pasteleriaBack.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private EntityManager entityManager;

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
    @Autowired
    private AuditoriaRepository auditoriaRepository;

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

    //RQUERIMIENTO 18: ACTUALIZAR UN PEDIDO
    @Transactional
    public ResponseEntity<Object> updatePedido(Integer id, UpdatePedidoDTO updatedPedidoDTO, Integer autorDni) {
        try {
            // Verificar si el pedido existe
            Pedido existingPedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            // Actualizar campos del pedido
            boolean estadoCambiado = false; // Para verificar si el estado ha cambiado
            if (updatedPedidoDTO.getPed_descripcion() != null) {
                existingPedido.setPed_descripcion(updatedPedidoDTO.getPed_descripcion());
            }
            if (updatedPedidoDTO.getPed_entrega() != null) {
                existingPedido.setPed_entrega(updatedPedidoDTO.getPed_entrega());
            }
            if (updatedPedidoDTO.getPedFechaDeEntrega() != null) {
                existingPedido.setPedFechaDeEntrega(updatedPedidoDTO.getPedFechaDeEntrega());
            }
            if (updatedPedidoDTO.getPedEstado() != null) {
                estadoCambiado = !existingPedido.getPedEstado().equals(updatedPedidoDTO.getPedEstado());
                existingPedido.setPedEstado(updatedPedidoDTO.getPedEstado());
            }

            // Actualizar el cliente si es necesario
            if (updatedPedidoDTO.getCliente() != null) {
                Cliente existingCliente = existingPedido.getCliente();
                if (updatedPedidoDTO.getCliente().getCli_apellidoNombre() != null) {
                    existingCliente.setCli_apellidoNombre(updatedPedidoDTO.getCliente().getCli_apellidoNombre());
                }
                if (updatedPedidoDTO.getCliente().getCli_numCelu() != null) {
                    existingCliente.setCli_numCelu(updatedPedidoDTO.getCliente().getCli_numCelu());
                }
                if (updatedPedidoDTO.getCliente().getCli_nroTelefonoFijo() != null) {
                    existingCliente.setCli_nroTelefonoFijo(updatedPedidoDTO.getCliente().getCli_nroTelefonoFijo());
                }
                if (updatedPedidoDTO.getCliente().getCli_email() != null) {
                    existingCliente.setCli_email(updatedPedidoDTO.getCliente().getCli_email());
                }
                // Guardar el cliente actualizado
                clienteRepository.save(existingCliente);
            }

            // Actualizar PedidoDomicilio si corresponde
            if (updatedPedidoDTO.getPedidoDomicilio() != null) {
                PedidoDomicilio existingDomicilio = existingPedido.getPedidoDomicilio();
                if (updatedPedidoDTO.getPedidoDomicilio().getPed_ciudad() != null) {
                    existingDomicilio.setPed_ciudad(updatedPedidoDTO.getPedidoDomicilio().getPed_ciudad());
                }
                if (updatedPedidoDTO.getPedidoDomicilio().getPed_barrio() != null) {
                    existingDomicilio.setPed_barrio(updatedPedidoDTO.getPedidoDomicilio().getPed_barrio());
                }
                if (updatedPedidoDTO.getPedidoDomicilio().getPed_calle() != null) {
                    existingDomicilio.setPed_calle(updatedPedidoDTO.getPedidoDomicilio().getPed_calle());
                }
                if (updatedPedidoDTO.getPedidoDomicilio().getPed_numeroCasa() != null) {
                    existingDomicilio.setPed_numeroCasa(updatedPedidoDTO.getPedidoDomicilio().getPed_numeroCasa());
                }
                if (updatedPedidoDTO.getPedidoDomicilio().getPed_nroApartamento() != null) {
                    existingDomicilio.setPed_nroApartamento(updatedPedidoDTO.getPedidoDomicilio().getPed_nroApartamento());
                }
                if (updatedPedidoDTO.getPedidoDomicilio().getPed_referencia() != null) {
                    existingDomicilio.setPed_referencia(updatedPedidoDTO.getPedidoDomicilio().getPed_referencia());
                }
                // Guardar el domicilio actualizado
                pedidoDomicilioRepository.save(existingDomicilio);
            }

            // Verificar si el estado del pedido ha cambiado a "Enviado" o "Retirado"
            if (estadoCambiado && (updatedPedidoDTO.getPedEstado() == EstadoPedidoENUM.enviado || updatedPedidoDTO.getPedEstado() == EstadoPedidoENUM.retirado)) {
                // Calcular y actualizar la comisión
                double porcentajeComision = existingPedido.getEmpleado().getEmp_porcentajeComisionPedido();
                existingPedido.setPorcentajeComisionPedidoActual(porcentajeComision);
            }

            // Guardar el pedido actualizado
            Pedido savedPedido = pedidoRepository.save(existingPedido);

            // Buscar el autor por DNI
            Empleado autor = empleadoRepository.findById(autorDni)
                    .orElseThrow(() -> new RuntimeException("Autor no encontrado con DNI: " + autorDni));

            // Registrar la auditoría
            Auditoria auditoria = new Auditoria();
            auditoria.setAud_operacion("Actualización de pedido");
            auditoria.setAud_detalle("Pedido ID: " + id + " actualizado. Descripción anterior: " + existingPedido.getPed_descripcion() +
                    ", Nueva descripción: " + updatedPedidoDTO.getPed_descripcion() +
                    ", Estado anterior: " + existingPedido.getPedEstado() +
                    ", Nuevo estado: " + updatedPedidoDTO.getPedEstado());
            auditoria.setFecha(LocalDateTime.now());
            auditoria.setAutor(autor); // Usar el DNI del autor pasado como parámetro
            auditoriaRepository.save(auditoria);

            // Retornar la respuesta con el pedido actualizado
            return ResponseEntity.ok(savedPedido);
        } catch (Exception e) {
            // Manejo de excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el pedido: " + e.getMessage());
        }
    }

    //REQUERIIMIENTO 9: Reasignar pedidos a cocineros
    @Transactional
    public ResponseEntity<Pedido> reasignarPedido(Integer pedidoId, Integer cocineroDni, Integer autorDni) {
        // Verificar que autorDni no sea nulo
        if (autorDni == null) {
            throw new IllegalArgumentException("El DNI del autor no puede ser nulo");
        }

        // Buscar el pedido por ID
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));

        // Buscar el cocinero por DNI
        Empleado cocinero = empleadoRepository.findById(cocineroDni)
                .orElseThrow(() -> new RuntimeException("Cocinero no encontrado con DNI: " + cocineroDni));

        // Buscar el autor por DNI
        Empleado autor = empleadoRepository.findById(autorDni)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con DNI: " + autorDni));

        // Asignar el cocinero al pedido
        pedido.setEmpleado(cocinero);

        // Guardar el pedido actualizado
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        // Registrar la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Reasignación de pedido");
        auditoria.setAud_detalle("Pedido ID: " + pedidoId + " reasignado a cocinero: " + cocinero.getEmp_apellidoNombre() + " por administrador con DNI: " + autorDni);
        auditoria.setAutor(autor);
        auditoria.setFecha(LocalDateTime.now());
        auditoriaRepository.save(auditoria);

        return ResponseEntity.ok(pedidoActualizado);
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
        //se almacena la fecha en una variable para usarla tambien en el calculo de fecha de entrega del pedido
        Timestamp fechaDeCreacionUltimoPedido = new Timestamp(System.currentTimeMillis());
        nuevoPedido.setPedFechaDeCreacion(fechaDeCreacionUltimoPedido);
        nuevoPedido.setPedEstado(EstadoPedidoENUM.enPreparacion);
        nuevoPedido.setPed_entrega(pedidoDTO.getPed_entregaDto());
        nuevoPedido.setPed_descripcion(pedidoDTO.getPed_descripcionDto());
        nuevoPedido.setPorcentajeComisionPedidoActual(pedidoDTO.getPorcentajeComisionPedidoActualDto());

        // Asignar cocinero
        Empleado cocinero = asignarCocinero();
        nuevoPedido.setEmpleado(cocinero); // Se asigna en el campo empleado de Pedido

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

        //---------------------------------------------------------------------------------------------------
        int tiempoTotal = 0;

        for (ProductoCantidadDTO productoCantidad : pedidoDTO.getProductos()) {
            // Obtener el producto por su ID
            Producto producto = productoRepository.findById(productoCantidad.getProdId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoCantidad.getProdId()));

            // Sumar el tiempo de producción del producto multiplicado por la cantidad
            tiempoTotal += producto.getProd_tiempoDeProduccion() * productoCantidad.getCantidad();
        }

        //---------------------------------------------------------------------------------------------------

        System.out.println("El tiempo total es:::::::::::::::::::: " + tiempoTotal);

        // Obtener la fecha de entrega del último pedido del cocinero
        Timestamp fechaUltimoPedido = obtenerFechaPenultimoPedido(cocinero);
        System.out.println("Fecha del último pedido del cocinero: " + fechaUltimoPedido);
        // Calcular el tiempo total de producción del nuevo pedido

        // Asignar fecha y horario de envío/retiro
        Date fechaEntrega = calcularFechaEntrega(tiempoTotal, cocinero,fechaUltimoPedido);
        nuevoPedido.setPedFechaDeEntrega(new Timestamp(fechaEntrega.getTime()));

        // Guardar el pedido para obtener el ID generado  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++DEBE IR ABAJO DE TODO?????
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
        //List<Pedido> pedidos = pedidoRepository.findByEmpleadoAndPedFechaDeEntrega(cocinero, fechaEnvio);
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

    private Date calcularFechaEntrega(int tiempoProduccionNuevoPedido, Empleado empleado, Timestamp fechaUltimoPedido) {
        System.out.println("\n\n\nEl dni empleado es:" + empleado.getEmp_dni() + "     con nombre: " + empleado.getEmp_apellidoNombre());
        System.out.println("fecha del ultimo pedido " + fechaUltimoPedido); //PRUEBA
        System.out.println("TIEMPO PRODUCCION PEDIDO: " + tiempoProduccionNuevoPedido); //PRUEBA

        // Obtener el horario de apertura y cierre
        HorarioAperturaCierre horario = horarioAperturaCierreRepository.findFirstByOrderByHacIdAsc();
        Time aperturaManana = horario.getHac_manana_apertura();
        Time cierreManana = horario.getHac_manana_cierre();
        Time aperturaTarde = horario.getHac_tarde_apertura();
        Time cierreTarde = horario.getHac_tarde_cierre();

        // Crear un calendario a partir de la fecha del último pedido
        Calendar entregaCalendar = Calendar.getInstance();

        if (fechaUltimoPedido == null) {
            System.out.println("No se encontró ningún pedido para el cocinero. Usando la fecha actual.");
            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, aperturaManana.getHours());
            calendar.set(Calendar.MINUTE, aperturaManana.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            fechaUltimoPedido = new Timestamp(calendar.getTimeInMillis());
        }else {
            // Si no es null, establecer la fecha del último pedido
            entregaCalendar.setTimeInMillis(fechaUltimoPedido.getTime()); // obtengo la fecha del ultimo pedido en milisegundos
        }

        // Crear un formateador de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Imprimir la fecha y hora en la consola
        System.out.println("Fecha y hora de entrega ESSS: " + sdf.format(entregaCalendar.getTime()));
        System.out.println(aperturaManana);
        System.out.println(cierreManana);
        System.out.println(aperturaTarde);
        System.out.println(cierreTarde);

        // Obtener la carga de trabajo del empleado en horas y convertir a minutos
        int cargaTrabajoHoras = empleado.getEmp_jornadaLaboral();
        int cargaTrabajoMinutos = cargaTrabajoHoras * 60; //pasamos las horas a minutos

        //almacenar los minutos entre los rangos de atencion del negocio
        int rangoMinutosManana = rangoMinutosManana(aperturaManana,cierreManana);
        int rangoMinutosTarde = rangoMinutosTarde(aperturaTarde,cierreTarde);



        // Si el tiempo de producción supera la carga de trabajo
        while (tiempoProduccionNuevoPedido > 0) {
            if (tiempoProduccionNuevoPedido > cargaTrabajoMinutos) {
                // Restar la carga de trabajo del tiempo de producción
                int minutosQueQuedanHoy = minutosRestantesDelDia(entregaCalendar,aperturaManana,cierreManana,aperturaTarde,cierreTarde);
                if(minutosQueQuedanHoy > cargaTrabajoMinutos) {
                    tiempoProduccionNuevoPedido -= cargaTrabajoMinutos;
                }
                else{
                    tiempoProduccionNuevoPedido -= minutosQueQuedanHoy;
                }
                System.out.println("Aumenta un dia..\n");
                entregaCalendar.add(Calendar.DAY_OF_YEAR, 1); // Avanzar al siguiente día
                entregaCalendar.set(Calendar.HOUR_OF_DAY, aperturaManana.getHours());//como paso un dia, empezara a primera hora de apertura
                entregaCalendar.set(Calendar.MINUTE, aperturaManana.getMinutes());
            } else {
                // Si el tiempo de producción es menor o igual a la carga de trabajo
                if (tiempoProduccionNuevoPedido >= rangoMinutosManana) { //si lo que queda de tiempo es mayor a los minutos de la mañana pasar de turno
                    tiempoProduccionNuevoPedido -= rangoMinutosManana; //tomo los minutos de la mañana
                    pasarDeTurno(entregaCalendar,aperturaManana,cierreManana,aperturaTarde);
                    entregaCalendar.add(Calendar.MINUTE, tiempoProduccionNuevoPedido);//le sumo lo que faltaba cuando ya paso de turno
                    System.out.println("retorna en este else");
                    return entregaCalendar.getTime();
                } else {
                    // Restar los minutos disponibles y continuar al siguiente día
                    entregaCalendar.add(Calendar.MINUTE,tiempoProduccionNuevoPedido); // sumar minutos restantes a la fecha
                    int minutosPasadosDelCierre = minutosPasadosDelCierre(entregaCalendar,cierreManana);
                    if (minutosPasadosDelCierre > 0){ //se pregunta si se paso del horario de cierre de la mañana
                        entregaCalendar.add(Calendar.MINUTE, -minutosPasadosDelCierre);//se restan los minutos que tenia de mas
                        pasarDeTurno(entregaCalendar,aperturaManana,cierreManana,cierreTarde);
                        entregaCalendar.add(Calendar.MINUTE, minutosPasadosDelCierre); // y se los agrega una vez se paso de turno
                    } else if (minutosPasadosDelCierre(entregaCalendar,cierreTarde)>0) { //se prueba si se paso de la hora de la tarde, si es asi devuelve los minutos que se paso del horario
                        int minutosPasadosDeLaTarde = minutosPasadosDelCierre(entregaCalendar,cierreTarde);
                        entregaCalendar.add(Calendar.MINUTE, -minutosPasadosDeLaTarde);//se restan los minutos que tenia de mas en la tarde
                        pasarDeTurno(entregaCalendar,aperturaManana,cierreManana,cierreTarde);
                        entregaCalendar.add(Calendar.MINUTE, minutosPasadosDeLaTarde); // y se los agrega una vez se paso de turno
                    } else {
                        System.out.println("llega hasta el ultimo");
                        return entregaCalendar.getTime();
                    }
                }
            }
        }
        return entregaCalendar.getTime();
    }

    // Devuelve la cantidad de minutos disponibles en el día actual
    private int calcularMinutosDisponiblesHoy(Calendar entregaCalendar, Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        int minutosDisponibles = 0;

        // Si estamos en la mañana
        if (entregaCalendar.get(Calendar.HOUR_OF_DAY) < cierreManana.getHours()) {
            // Calcular minutos desde la hora actual hasta el cierre de la mañana
            int minutosHastaCierreManana = (cierreManana.getHours() * 60 + cierreManana.getMinutes()) -
                    (entregaCalendar.get(Calendar.HOUR_OF_DAY) * 60 + entregaCalendar.get(Calendar.MINUTE));
            minutosDisponibles += Math.max(minutosHastaCierreManana, 0);
        }

        // Si estamos en la tarde
        if (entregaCalendar.get(Calendar.HOUR_OF_DAY) >= aperturaTarde.getHours() && entregaCalendar.get(Calendar.HOUR_OF_DAY) < cierreTarde.getHours()) {
        // Calcular minutos desde la hora actual hasta el cierre de la tarde
            int minutosHastaCierreTarde = (cierreTarde.getHours() * 60 + cierreTarde.getMinutes()) -
                    (entregaCalendar.get(Calendar.HOUR_OF_DAY) * 60 + entregaCalendar.get(Calendar.MINUTE));
            minutosDisponibles += Math.max(minutosHastaCierreTarde, 0);
        }

        // Si estamos antes de la apertura de la tarde, contar todos los minutos de la tarde
        if (entregaCalendar.get(Calendar.HOUR_OF_DAY) < aperturaTarde.getHours()) {
            minutosDisponibles += (cierreTarde.getHours() * 60 + cierreTarde.getMinutes()) -
                    (aperturaTarde.getHours() * 60 + aperturaTarde.getMinutes());
        }

        return minutosDisponibles;
    }

    //devuelve la cantidad de minutos total de la jornada diaria del negocio
    private int calcularMinutosDisponiblesPorDia(Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Calcular los minutos disponibles en un día
        int minutosManana = (cierreManana.getHours() * 60 + cierreManana.getMinutes()) - (aperturaManana.getHours() * 60 + aperturaManana.getMinutes());
        int minutosTarde = (cierreTarde.getHours() * 60 + cierreTarde.getMinutes()) - (aperturaTarde.getHours() * 60 + aperturaTarde.getMinutes());
        return minutosManana + minutosTarde;
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


    public Timestamp obtenerFechaPenultimoPedido(Empleado empleado) {
        // Busca el penúltimo pedido del empleado, ordenado por fecha de entrega
        List<Pedido> pedidos = pedidoRepository.findByEmpleadoOrderByPedFechaDeEntregaDesc(empleado);
        if (pedidos.getFirst().getPedFechaDeEntrega() == null) {
            // Obtener el horario de apertura de la mañana
            HorarioAperturaCierre horario = horarioAperturaCierreRepository.findFirstByOrderByHacIdAsc();
            Time aperturaManana = horario.getHac_manana_apertura();

            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, aperturaManana.getHours());
            calendar.set(Calendar.MINUTE, aperturaManana.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // Retornar la fecha actual con horario de apertura de mañana
            return new Timestamp(calendar.getTimeInMillis());
        }
        return pedidos.getFirst().getPedFechaDeEntrega(); // Retorna la fecha del penúltimo pedido
    }

    private void pasarDeTurno(Calendar entregaCalendar, Time aperturaManana, Time cierreManana, Time aperturaTarde) {
        // Verificar si entregaCalendar está dentro del horario de la mañana
        if (isDentroDelHorarioManana(entregaCalendar, aperturaManana, cierreManana)) {
            // Cambiar al horario de apertura de la tarde
            entregaCalendar.set(Calendar.HOUR_OF_DAY, aperturaTarde.getHours());
            entregaCalendar.set(Calendar.MINUTE, aperturaTarde.getMinutes());
        } else {
            // Si está en el turno de la tarde, avanzar al siguiente día
            entregaCalendar.add(Calendar.DAY_OF_YEAR, 1);
            // Establecer al horario de apertura de la mañana del día siguiente
            entregaCalendar.set(Calendar.HOUR_OF_DAY, aperturaManana.getHours());
            entregaCalendar.set(Calendar.MINUTE, aperturaManana.getMinutes());
        }
    }

    private boolean isDentroDelHorarioManana(Calendar entregaCalendar, Time aperturaManana, Time cierreManana) {
        // Obtener la hora y el minuto de entregaCalendar
        int horaEntrega = entregaCalendar.get(Calendar.HOUR_OF_DAY);
        int minutoEntrega = entregaCalendar.get(Calendar.MINUTE);

        // Convertir la hora de apertura y cierre a minutos
        int minutosApertura = aperturaManana.getHours() * 60 + aperturaManana.getMinutes();
        int minutosCierre = cierreManana.getHours() * 60 + cierreManana.getMinutes();

        // Convertir la hora de entrega a minutos
        int minutosEntrega = horaEntrega * 60 + minutoEntrega;

        // Verificar si está dentro del rango
        return minutosEntrega >= minutosApertura && minutosEntrega < minutosCierre;
    }

    private int rangoMinutosManana(Time aperturaManana, Time cierreManana) {
        // Convertir la hora de apertura y cierre a minutos
        int minutosApertura = aperturaManana.getHours() * 60 + aperturaManana.getMinutes();
        int minutosCierre = cierreManana.getHours() * 60 + cierreManana.getMinutes();

        // Calcular el rango de minutos
        return minutosCierre - minutosApertura;
    }

    private int rangoMinutosTarde(Time aperturaTarde, Time cierreTarde) {
        // Convertir la hora de apertura y cierre a minutos
        int minutosApertura = aperturaTarde.getHours() * 60 + aperturaTarde.getMinutes();
        int minutosCierre = cierreTarde.getHours() * 60 + cierreTarde.getMinutes();

        // Calcular el rango de minutos
        return minutosCierre - minutosApertura;
    }

    //calcula la cantidad de minutos que se paso de un horario de cierre del local
    private int minutosPasadosDelCierre(Calendar entregaCalendar, Time cierre) {
        // Obtener la hora y el minuto de cierre
        int horaCierre = cierre.getHours();
        int minutoCierre = cierre.getMinutes();

        // Convertir la hora de cierre a minutos
        int minutosCierre = horaCierre * 60 + minutoCierre;

        // Obtener la hora y el minuto de entregaCalendar
        int horaEntrega = entregaCalendar.get(Calendar.HOUR_OF_DAY);
        int minutoEntrega = entregaCalendar.get(Calendar.MINUTE);

        // Convertir la hora de entrega a minutos
        int minutosEntrega = horaEntrega * 60 + minutoEntrega;

        // Calcular la diferencia si entregaCalendar está después del cierre
        if (minutosEntrega > minutosCierre) {
            return minutosEntrega - minutosCierre; // Devuelve la cantidad de minutos pasados del cierre
        } else {
            return 0; // No se ha pasado del cierre
        }
    }

    //calcula los minutos que sobran del dia desde la hora de entrega hasta el cierre del local en el dia.
    private int minutosRestantesDelDia(Calendar fechaEntrega, Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Inicializar los minutos restantes
        int minutosRestantes = 0;

        // Verificar si la fecha de entrega está dentro del horario de la mañana
        if (isDentroDelHorarioManana(fechaEntrega, aperturaManana, cierreManana)) {
            // Obtener la hora y el minuto de fechaEntrega
            int horaEntrega = fechaEntrega.get(Calendar.HOUR_OF_DAY);
            int minutoEntrega = fechaEntrega.get(Calendar.MINUTE);

            // Convertir la hora de cierre de la mañana a minutos
            int minutosCierreManana = cierreManana.getHours() * 60 + cierreManana.getMinutes();
            // Convertir la hora de apertura de la tarde a minutos
            int minutosAperturaTarde = aperturaTarde.getHours() * 60 + aperturaTarde.getMinutes();

            // Calcular los minutos desde la fecha de entrega hasta el cierre de la mañana
            int minutosTranscurridosManana = minutosCierreManana - (horaEntrega * 60 + minutoEntrega);

            // Calcular los minutos de atención de la tarde
            int minutosAtencionTarde = rangoMinutosTarde(aperturaTarde, cierreTarde);

            // Sumar los minutos transcurridos en la mañana y los minutos de atención de la tarde
            minutosRestantes = minutosTranscurridosManana + minutosAtencionTarde;
        }
        // Si la fecha de entrega está en la tarde
        else {
            // Obtener la hora y el minuto de fechaEntrega
            int horaEntrega = fechaEntrega.get(Calendar.HOUR_OF_DAY);
            int minutoEntrega = fechaEntrega.get(Calendar.MINUTE);

            // Convertir la hora de cierre de la tarde a minutos
            int minutosCierreTarde = cierreTarde.getHours() * 60 + cierreTarde.getMinutes();

            // Calcular los minutos restantes desde la fecha de entrega hasta el cierre de la tarde
            minutosRestantes = minutosCierreTarde - (horaEntrega * 60 + minutoEntrega);
        }

        // Asegurarse de que no se devuelvan minutos negativos
        return Math.max(minutosRestantes, 0);
    }
    private void generarComprobante(Pedido pedido) {
        // Crear un StringBuilder para construir el comprobante
        StringBuilder comprobante = new StringBuilder();

        // Información del pedido
        comprobante.append("Comprobante de Pedido\n");
        comprobante.append("Fecha de Creación: ").append(pedido.getPedFechaDeCreacion()).append("\n");
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


























//private Date porlasdudas(int tiempoProduccionNuevoPedido,Empleado empleado,Timestamp fechaUltimoPedido) {
//    System.out.println("\n\n\nEl dni empleado es:" + empleado.getEmp_dni() + "     con nombre: " + empleado.getEmp_apellidoNombre());
//    System.out.println("fecha del ultimo pedido " + fechaUltimoPedido); //PRUEBA
//    System.out.println("TIEMPO PRODUCCION PEDIDO: " + tiempoProduccionNuevoPedido); //PRUEBA
//
//    int tiempoTotal = calcularTiempoAsignado(empleado);
//    if (tiempoTotal == 0) {
//        // Manejar el caso donde no hay pedidos
//        System.out.println("No hay pedidos en preparación para el cocinero: " + empleado.getEmp_dni());
//        return null; // O alguna fecha predeterminada
//    }
//
//    // Obtener el horario de apertura y cierre
//    HorarioAperturaCierre horario = horarioAperturaCierreRepository.findFirstByOrderByHacIdAsc();
//    Time aperturaManana = horario.getHac_manana_apertura();
//    Time cierreManana = horario.getHac_manana_cierre();
//    Time aperturaTarde = horario.getHac_tarde_apertura();
//    Time cierreTarde = horario.getHac_tarde_cierre();
//
//
//    // Crear un calendario a partir de la fecha del último pedido
//    Calendar entregaCalendar = Calendar.getInstance();
//
//    // Si fechaUltimoPedido es null, usar la fecha actual
//    if (fechaUltimoPedido == null) {
//        System.out.println("No se encontró ningún pedido para el cocinero. Usando la fecha actual.");
//        avanzarAlSiguienteHorario(entregaCalendar, aperturaManana);
//        // La fecha actual ya está en entregaCalendar
//    } else {
//        // Si no es null, establecer la fecha del último pedido
//        entregaCalendar.setTimeInMillis(fechaUltimoPedido.getTime()); // obtengo la fecha del ultimo pedido en milisegundos
//    }
//
//    // Crear un formateador de fecha
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    // Imprimir la fecha y hora en la consola
//    System.out.println("Fecha y hora de entrega ESSS: " + sdf.format(entregaCalendar.getTime()));
//    System.out.println(aperturaManana);
//    System.out.println(cierreManana);
//    System.out.println(aperturaTarde);
//    System.out.println(cierreTarde);
//
//    // Verificar si el último pedido está dentro del horario de atención
////        if (!isDentroDelHorario(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde)) {
////            // Si no está dentro del horario, avanzar al siguiente día
////            avanzarAlSiguienteHorario(entregaCalendar, aperturaManana);
////        }
//
//    // Calcular el tiempo restante en el día actual
//    int minutosDisponiblesHoy = calcularMinutosDisponiblesHoy(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde);
//    System.out.println("Minutos disponibles hoy: " + minutosDisponiblesHoy);
//
//    // Si el tiempo de producción es menor o igual a los minutos disponibles hoy
//    if (tiempoProduccionNuevoPedido <= minutosDisponiblesHoy) {
//        entregaCalendar.add(Calendar.MINUTE, tiempoProduccionNuevoPedido);
//        return entregaCalendar.getTime();
//    } else {
//        // Restar los minutos disponibles y continuar al siguiente día
//        tiempoProduccionNuevoPedido -= minutosDisponiblesHoy;
//        entregaCalendar.add(Calendar.DAY_OF_YEAR, 1); // Avanzar al siguiente día
//
//        // Continuar calculando hasta que se complete el tiempo de producción
//        while (tiempoProduccionNuevoPedido > 0) {
//            // Verificar si es mañana o tarde
//            if (isDentroDelHorario(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde)) {
//                // Calcular minutos disponibles en el día actual
//                minutosDisponiblesHoy = calcularMinutosDisponiblesHoy(entregaCalendar, aperturaManana, cierreManana, aperturaTarde, cierreTarde);
//                if (tiempoProduccionNuevoPedido <= minutosDisponiblesHoy) {
//                    entregaCalendar.add(Calendar.MINUTE, tiempoProduccionNuevoPedido);
//                    return entregaCalendar.getTime();
//                } else {
//                    tiempoProduccionNuevoPedido -= minutosDisponiblesHoy;
//                    entregaCalendar.add(Calendar.DAY_OF_YEAR, 1); // Avanzar al siguiente día
//                }
//            } else {
//                // Si no está dentro del horario, avanzar al siguiente horario
//                avanzarAlSiguienteHorario(entregaCalendar, aperturaManana);
//            }
//        }
//    }
//
//    return entregaCalendar.getTime();
//}








