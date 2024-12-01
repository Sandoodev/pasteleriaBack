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
    List<Pedido> findByEmpleadoAndPedEstado(Empleado empleado, EstadoPedidoENUM estado);
    List<Pedido> findByEmpleado(Empleado empleado);

    //REQUERIMIENTO 11 trae los pedidos realizados entre 2 fechas
    List<Pedido> findByPedFechaDeCreacionBetweenAndPedEstadoIn(Timestamp inicio, Timestamp fin, List<EstadoPedidoENUM> estados);
    //REQUERIMIENTO 11: trae los pedidos realizados por el cocinero entre 2 fechas
    @Query("SELECT p FROM Pedido p WHERE p.pedFechaDeCreacion BETWEEN :fechaInicio AND :fechaFin " +
            "AND p.pedEstado IN :estados")
    List<Pedido> findByFechaBetweenAndEstado(@Param("fechaInicio") Timestamp fechaInicio,
                                             @Param("fechaFin") Timestamp fechaFin,
                                             @Param("estados") List<EstadoPedidoENUM> estados);

    List<Pedido> findByEmpleadoOrderByPedFechaDeEntregaDesc(Empleado empleado);
    //REQUERIMIENTO 11: calcula los ingresos del negocio en un rango de fechas
    @Query("SELECT new com.pasteleriaBack.pasteleriaBack.dto.Ingreso(MONTH(p.pedFechaDeCreacion), SUM(p.porcentajeComisionPedidoActual)) " +
            "FROM Pedido p " +
            "WHERE p.pedFechaDeCreacion >= :fechaInicio AND p.pedFechaDeCreacion <= :fechaFin " +
            "AND (p.pedEstado = 'enviado' OR p.pedEstado = 'retirado') " +
            "GROUP BY MONTH(p.pedFechaDeCreacion)")
    List<Ingreso> calcularIngresos(@Param("fechaInicio") Timestamp fechaInicio, @Param("fechaFin") Timestamp fechaFin);

    //REQUERIMIENTO 13:comision por cocinero
    List<Pedido> findByEmpleadoAndPedFechaDeCreacionBetween(Empleado empleado, Timestamp inicio, Timestamp fin);

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





