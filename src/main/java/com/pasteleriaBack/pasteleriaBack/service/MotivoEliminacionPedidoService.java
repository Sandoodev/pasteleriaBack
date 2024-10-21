package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.MotivoEliminacionPedido;
import com.pasteleriaBack.pasteleriaBack.repository.MotivoEliminacionPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotivoEliminacionPedidoService {
    @Autowired
    private MotivoEliminacionPedidoRepository motivoEliminacionPedidoRepository;

    public MotivoEliminacionPedido crearMotivoEliminacionPedido(MotivoEliminacionPedido motivoEliminacionPedido) {
        // Lógica para crear motivo de eliminación de pedido
        return motivoEliminacionPedidoRepository.save(motivoEliminacionPedido);
    }

    public List<MotivoEliminacionPedido> listarMotivosEliminacionPedidos() {
        return motivoEliminacionPedidoRepository.findAll();
    }

    // Otros métodos según los requerimientos
}
