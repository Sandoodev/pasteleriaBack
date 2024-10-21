package com.pasteleriaBack.pasteleriaBack.repository;

import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

//con esto(PedidoRepository) podemos acceder a todas las funcionalidades de JpaRepository(creacion,busqueda,borrado,etc)
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {//se le pasa como parametro la entidad y el tipo de dato de su id
    // Métodos personalizados si son necesarios
}


