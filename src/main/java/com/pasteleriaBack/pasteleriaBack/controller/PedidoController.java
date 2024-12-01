package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.dto.PedidoDTO;
import com.pasteleriaBack.pasteleriaBack.dto.PedidoClienteDTO;
import com.pasteleriaBack.pasteleriaBack.dto.UpdatePedidoDTO;
import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import com.pasteleriaBack.pasteleriaBack.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind .annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @CrossOrigin
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        return pedidoService.getPedidoById(id);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody PedidoDTO pedidoDTO) {
        return pedidoService.createPedido(pedidoDTO);
    }

    //requerimiento 9: reasignar pedido a cocinero
    @CrossOrigin
    @PutMapping("/reasignar/{pedidoId}")
    public ResponseEntity<Pedido> reasignarPedido(
            @PathVariable Integer pedidoId,
            @RequestParam Integer cocineroDni,
            @RequestParam Integer autorDni) {

        return pedidoService.reasignarPedido(pedidoId, cocineroDni, autorDni);
    }

    //REQUERIMIENTO 17: listado pedidos del cliente
    @CrossOrigin
    @GetMapping("/listadoPedidosCliente")
    public ResponseEntity<List<PedidoClienteDTO>> listarPedidos(
            @RequestParam(required = false) String dniCliente,
            @RequestParam(required = false) String estadoPedido,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        // Convierte los parámetros de fecha de String a Timestamp
        Timestamp inicio = null;
        Timestamp fin = null;

        if (fechaInicio != null) {
            try {
                inicio = Timestamp.valueOf(fechaInicio); // Asegúrate de que el formato sea correcto
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar el caso de formato incorrecto
            }
        }

        if (fechaFin != null) {
            try {
                fin = Timestamp.valueOf(fechaFin); // Asegúrate de que el formato sea correcto
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar el caso de formato incorrecto
            }
        }
        // Convertir el estadoPedido de String a EstadoPedidoENUM
        EstadoPedidoENUM estado = null;
        if (estadoPedido != null) {
            try {
                estado = EstadoPedidoENUM.fromString(estadoPedido); // Usar el método de conversión
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar el caso en que el estado no es válido
            }
        }

        // Llamar al servicio para obtener la lista de pedidos
        List<PedidoClienteDTO> pedidos = pedidoService.findPedidosEncargados(inicio, fin, dniCliente, estado);

        return ResponseEntity.ok(pedidos);
    }

    //REQUERIMIENTO 18: actualizacion de pedido
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePedido(
            @PathVariable Integer id,
            @RequestBody UpdatePedidoDTO updatePedidoDTO, // Asegúrate de que este sea el tipo correcto
            @RequestParam Integer autorDni) {
        return pedidoService.updatePedido(id, updatePedidoDTO, autorDni);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(
            @PathVariable Integer id,
            @RequestParam String motivo,
            @RequestParam(required = false) String descripcion,
            @RequestParam Integer dniAutor) { // Agregar dniAutor como parámetro
        return pedidoService.eliminarPedido(id, motivo, descripcion, dniAutor);
    }

}