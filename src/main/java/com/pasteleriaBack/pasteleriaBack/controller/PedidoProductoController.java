package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.PedidoProducto;
import com.pasteleriaBack.pasteleriaBack.service.PedidoProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos-productos")
public class PedidoProductoController {
    @Autowired
    private PedidoProductoService pedidoProductoService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<PedidoProducto> crearPedidoProducto(@RequestBody PedidoProducto pedidoProducto) {
        return ResponseEntity.ok(pedidoProductoService.crearPedidoProducto(pedidoProducto));
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<PedidoProducto>> listarPedidosProductos() {
        return ResponseEntity.ok(pedidoProductoService.listarPedidosProductos());
    }
}