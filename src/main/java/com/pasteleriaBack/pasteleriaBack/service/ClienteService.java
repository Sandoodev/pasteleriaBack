package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Cliente;
import com.pasteleriaBack.pasteleriaBack.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    // Método para crear un nuevo cliente
    public ResponseEntity<Cliente> createCliente(Cliente cliente) {
        // fijarse si falta agregar logica
        Cliente savedCliente = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
    }

    // Método para obtener todos los clientes
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Método para obtener un cliente por DNI
    public ResponseEntity<Cliente> getClienteByDni(Integer dni) {
        Optional<Cliente> cliente = clienteRepository.findById(dni);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Método para actualizar un cliente existente
    public ResponseEntity<Cliente> updateCliente(Integer dni, Cliente updatedCliente) {
        if (!clienteRepository.existsById(dni)) {
            return ResponseEntity.notFound().build();
        }
        updatedCliente.setCli_dni(dni); // Asegurarse de que el DNI se mantenga
        Cliente savedCliente = clienteRepository.save(updatedCliente);
        return ResponseEntity.ok(savedCliente);
    }

    // Método para eliminar un cliente
    public ResponseEntity<Void> deleteCliente(Integer dni) {
        if (!clienteRepository.existsById(dni)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(dni);
        return ResponseEntity.noContent().build();
    }

    // Otros métodos
}