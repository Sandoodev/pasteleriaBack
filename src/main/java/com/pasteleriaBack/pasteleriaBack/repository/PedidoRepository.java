package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.dto.Ingreso;
import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//con esto(PedidoRepository) podemos acceder a todas las funcionalidades de JpaRepository(creacion,busqueda,borrado,etc)
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {//se le pasa como parametro la entidad y el tipo de dato de su id
    List<Pedido> findByEmpleadoAndPedFechaDeEntrega(Empleado empleado, Timestamp fechaEntrega);
    List<Pedido> findByEmpleadoAndPedEstado(Empleado empleado, EstadoPedidoENUM estado);
    List<Pedido> findByEmpleado(Empleado empleado);
    // Método para obtener el último pedido de un cocinero
    Optional<Pedido> findTopByEmpleadoOrderByPedFechaDeEntregaDesc(Empleado empleado);
    // Método para obtener el último pedido en estado "enPreparacion"
    Pedido findTopByPedEstadoOrderByPedFechaDeCreacionDesc(EstadoPedidoENUM pedEstado);
    List<Pedido> findByEmpleadoOrderByPedFechaDeEntregaDesc(Empleado empleado);
    //REQUERIMIENTO 11: REPORTE
    @Query("SELECT new com.pasteleriaBack.pasteleriaBack.dto.Ingreso(MONTH(p.pedFechaDeCreacion), SUM(p.porcentajeComisionPedidoActual)) " +
            "FROM Pedido p " +
            "WHERE p.pedFechaDeCreacion >= :fechaInicio AND p.pedFechaDeCreacion <= :fechaFin " +
            "AND (p.pedEstado = 'enviado' OR p.pedEstado = 'retirado') " +
            "GROUP BY MONTH(p.pedFechaDeCreacion)")
    List<Ingreso> calcularIngresos(@Param("fechaInicio") Timestamp fechaInicio, @Param("fechaFin") Timestamp fechaFin);


    //REQUERIMIENTO 17: listar pedidos realizados de un cliente en particular
    @Query("SELECT p FROM Pedido p " +
            "WHERE p.pedFechaDeCreacion BETWEEN :fechaInicio AND :fechaFin " +
            "AND (:dniCliente IS NULL OR p.cliente.cli_dni = :dniCliente) " +
            "AND (:estadoPedido IS NULL OR p.pedEstado = :estadoPedido) " +
            "ORDER BY p.pedFechaDeCreacion DESC")
    List<Pedido> findPedidosEncargados(@Param("fechaInicio") Timestamp fechaInicio,
                                       @Param("fechaFin") Timestamp fechaFin,
                                       @Param("dniCliente") String dniCliente,
                                       @Param("estadoPedido") EstadoPedidoENUM estadoPedido);
    }





