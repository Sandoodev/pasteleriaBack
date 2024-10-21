package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.MotivoEliminacionPedido;
import com.pasteleriaBack.pasteleriaBack.service.MotivoEliminacionPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation .PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/motivos-eliminacion-pedidos")
public class MotivoEliminacionPedidoController {
    @Autowired
    private MotivoEliminacionPedidoService motivoEliminacionPedidoService;

    @PostMapping
    public ResponseEntity<MotivoEliminacionPedido> crearMotivoEliminacionPedido(@RequestBody MotivoEliminacionPedido motivoEliminacionPedido) {
        return ResponseEntity.ok(motivoEliminacionPedidoService.crearMotivoEliminacionPedido(motivoEliminacionPedido));
    }

    // Otros métodos
}
