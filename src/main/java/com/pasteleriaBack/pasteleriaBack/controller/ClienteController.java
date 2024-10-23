package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.Cliente;
import com.pasteleriaBack.pasteleriaBack.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @CrossOrigin
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteByDni(@PathVariable Integer id) {
        return clienteService.getClienteByDni(id);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        return clienteService.createCliente(cliente);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Integer id, @RequestBody Cliente updatedCliente) {
        return clienteService.updateCliente(id, updatedCliente);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Integer id) {
        return clienteService.deleteCliente(id);
    }

}