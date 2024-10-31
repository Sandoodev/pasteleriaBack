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

    // Método para crear un nuevo pedido
    public ResponseEntity<Pedido> createPedido(PedidoDTO pedidoDTO) {
        // Obtener el cliente
        Cliente cliente = clienteRepository.findById(pedidoDTO.getDni())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Crear un nuevo pedido
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setPed_fechaDeCreacion(new Timestamp(System.currentTimeMillis()));
        nuevoPedido.setPed_estado(EstadoPedidoENUM.pendienteDeEnvio); // O el estado que corresponda
        nuevoPedido.setPed_entrega(pedidoDTO.getPed_entregaDto());
        nuevoPedido.setPorcentajeComisionPedidoActual(pedidoDTO.getPorcentajeComisionPedidoActualDto());

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
            id.setPedId(pedidoGuardado.getPed_id()); // Asegúrate de que el ID del pedido esté disponible

            // Crear el objeto PedidoProducto
            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setId(id); // Establecer la clave compuesta
            pedidoProducto.setPedido(pedidoGuardado);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(productoCantidad.getCantidad());

            // Establecer los precios del producto
            pedidoProducto.setPrecioCosto(producto.getProd_precioCosto());
            pedidoProducto.setPrecioVenta(producto.getProd_precioVenta());
            // Guardar el pedidoProducto en el repositorio
            pedidoProductoRepository.save(pedidoProducto);
        }

        // Lógica adicional: Asignar un cocinero
        // asignarCocinero(pedidoGuardado);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado); // Retornar el pedido creado con estado 201
    }

        private void asignarCocinero (Pedido pedido){
            // Lógica para asignar un cocinero al pedido
            //Cocinero cocinero = obtenerCocineroDisponible(); // Implementa esta lógica según tus necesidades
            //pedido.setCocinero(cocinero);
            pedidoRepository.save(pedido); // Guardar el pedido actualizado
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
    // Otros métodos
}