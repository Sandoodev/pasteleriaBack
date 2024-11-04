package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.List;
//con esto(PedidoRepository) podemos acceder a todas las funcionalidades de JpaRepository(creacion,busqueda,borrado,etc)
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {//se le pasa como parametro la entidad y el tipo de dato de su id
    List<Pedido> findByEmpleadoAndPedFechaDeEntrega(Empleado empleado, Timestamp fechaEntrega);
    List<Pedido> findByEmpleadoAndPedEstado(Empleado empleado, EstadoPedidoENUM estado);
    List<Pedido> findByEmpleado(Empleado empleado);
    // Método para obtener el último pedido de un cocinero
    @Query("SELECT p FROM Pedido p WHERE p.empleado = :empleado ORDER BY p.pedFechaDeEntrega DESC")
    List<Pedido> findUltimoPedidoPorCocinero(@Param("empleado") Empleado empleado, Pageable pageable);
}





