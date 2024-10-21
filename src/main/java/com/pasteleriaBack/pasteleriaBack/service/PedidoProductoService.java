package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.PedidoProducto;
import com.pasteleriaBack.pasteleriaBack.repository.PedidoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoProductoService {
    @Autowired
    private PedidoProductoRepository pedidoProductoRepository;

    public PedidoProducto crearPedidoProducto(PedidoProducto pedidoProducto) {
        // Lógica para crear pedido producto
        return pedidoProductoRepository.save(pedidoProducto);
    }

    public List<PedidoProducto> listarPedidosProductos() {
        return pedidoProductoRepository.findAll();
    }

    // Otros métodos según los requerimientos
}
