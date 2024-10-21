package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.model.MotivoEliminacionPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoEliminacionPedidoRepository extends JpaRepository<MotivoEliminacionPedido, Integer> {
    // Métodos personalizados si son necesarios
}