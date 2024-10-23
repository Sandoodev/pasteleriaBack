package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import com.pasteleriaBack.pasteleriaBack.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

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
    public ResponseEntity<Pedido> createPedido(Pedido pedido) {
        Pedido savedPedido = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPedido);
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