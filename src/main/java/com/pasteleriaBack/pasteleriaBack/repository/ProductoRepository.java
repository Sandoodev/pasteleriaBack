package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.dto.ProductoMasSolicitado;
import com.pasteleriaBack.pasteleriaBack.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Mequerimiento 11:reporte
    @Query("SELECT new com.pasteleriaBack.pasteleriaBack.dto.ProductoMasSolicitado(pp.producto.prod_titulo, SUM(pp.cantidad)) " +
            "FROM Pedido p JOIN p.pedidoProductos pp " +
            "WHERE p.pedFechaDeCreacion >= :fechaInicio AND p.pedFechaDeCreacion <= :fechaFin " +
            "AND (p.pedEstado = 'enviado' OR p.pedEstado = 'retirado') " +
            "GROUP BY pp.producto.prod_titulo " +
            "ORDER BY SUM(pp.cantidad) DESC")
    List<ProductoMasSolicitado> obtenerProductosMasSolicitados(@Param("fechaInicio") Timestamp fechaInicio, @Param("fechaFin") Timestamp fechaFin);
}