package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pasteleriaBack.pasteleriaBack.model.Empleado;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

//con esto(PedidoRepository) podemos acceder a todas las funcionalidades de JpaRepository(creacion,busqueda,borrado,etc)
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {//se le pasa como parametro la entidad y el tipo de dato de su id
    List<Pedido> findByEmpleadoAndPedFechaDeEntrega(Empleado empleado, Timestamp fechaEntrega);
    List<Pedido> findByEmpleadoAndPedEstado(Empleado empleado, EstadoPedidoENUM estado);
    List<Pedido> findByEmpleado(Empleado empleado);
    // Método para obtener el último pedido de un cocinero
    Optional<Pedido> findTopByEmpleadoOrderByPedFechaDeEntregaDesc(Empleado empleado);
}





