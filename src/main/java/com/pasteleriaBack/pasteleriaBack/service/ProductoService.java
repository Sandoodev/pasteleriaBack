package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Producto;
import com.pasteleriaBack.pasteleriaBack.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo producto
    public ResponseEntity<Producto> createProducto(Producto producto) {
        // agregar lógica si es necesario
        Producto savedProducto = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    // Método para obtener todos los productos
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    // Obtener un producto por ID
    public ResponseEntity<Producto> getProductoById(Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un producto existente
    public ResponseEntity<Producto> updateProducto(Integer id, Producto updatedProducto) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedProducto.setProd_id(id); // Asegurarse de que el ID se mantenga
        Producto savedProducto = productoRepository.save(updatedProducto);
        return ResponseEntity.ok(savedProducto);
    }

    // Eliminar un producto
    public ResponseEntity<Void> deleteProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Otros métodos según los requerimientos
}