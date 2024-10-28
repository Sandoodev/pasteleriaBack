package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.Auditoria;
import com.pasteleriaBack.pasteleriaBack.model.EstadoProductoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Producto;
import com.pasteleriaBack.pasteleriaBack.repository.AuditoriaRepository;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private AuditoriaRepository auditoriaRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    // REQUERIMIENTO 4: Crear un nuevo producto por parte del admin
    public ResponseEntity<Producto> createProducto(Producto producto, Integer dniAutor) {
        // Validar la entrada
        if (producto.getProd_descripcion() == null || producto.getProd_descripcion().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // O lanzar una excepción
        }
        if (producto.getProd_precioCosto() == null || producto.getProd_precioCosto() < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (producto.getProd_precioVenta() == null || producto.getProd_precioVenta() < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (producto.getProd_tiempoDeProduccion() == null || producto.getProd_tiempoDeProduccion() < 0) {
            return ResponseEntity.badRequest().body(null);
        }

        // Guardar el producto en la base de datos
        Producto savedProducto = productoRepository.save(producto);

        // Obtener el autor (Empleado) que realiza la acción
        Empleado autor = empleadoRepository.findById(dniAutor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        // Registrar la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Registro de nuevo producto");
        auditoria.setAud_detalle("Se registro el producto: " + savedProducto.getProd_descripcion());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(autor); // Usar el objeto Empleado del autor
        auditoriaRepository.save(auditoria);

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

    // REQUERIMIENTO 5: Actualizar un producto existente por el administrador
    public ResponseEntity<Producto> updateProducto(Integer id, Producto updatedProducto, Integer dniAutor) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Obtener el producto existente
        Producto existingProducto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizar solo los campos permitidos
        if (updatedProducto.getProd_descripcion() != null) {
            existingProducto.setProd_descripcion(updatedProducto.getProd_descripcion());
        }
        if (updatedProducto.getProd_imagen() != null) {
            existingProducto.setProd_imagen(updatedProducto.getProd_imagen());
        }
        if (updatedProducto.getProd_precioCosto() != null) {
            existingProducto.setProd_precioCosto(updatedProducto.getProd_precioCosto());
        }
        if (updatedProducto.getProd_precioVenta() != null) {
            existingProducto.setProd_precioVenta(updatedProducto.getProd_precioVenta());
        }
        if (updatedProducto.getProd_tiempoDeProduccion() != null) {
            existingProducto.setProd_tiempoDeProduccion(updatedProducto.getProd_tiempoDeProduccion());
        }
        if (updatedProducto.getProd_porcentajeDescuento() != null) {
            existingProducto.setProd_porcentajeDescuento(updatedProducto.getProd_porcentajeDescuento());
        }

        // Guardar el producto actualizado
        Producto savedProducto = productoRepository.save(existingProducto);

        // Registrar la auditoría
        Empleado autor = empleadoRepository.findById(dniAutor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Actualización de producto");
        auditoria.setAud_detalle("Se actualizó el producto: " + savedProducto.getProd_descripcion());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(autor);
        auditoriaRepository.save(auditoria);

        return ResponseEntity.ok(savedProducto);
    }

    //REQUERIMIENTO 6: baja logica del producto por el administrador
    public ResponseEntity<Void> bajaLogicaProducto(Integer id, Integer dniAutor) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (!optionalProducto.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = optionalProducto.get();
        // Cambiar el estado del producto a "Eliminado"
        producto.setProd_estado(EstadoProductoENUM.inactivo);
        productoRepository.save(producto); // Guardar el cambio en la base de datos

        // Registrar la auditoría
        Empleado autor = empleadoRepository.findById(dniAutor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Baja lógica de producto");
        auditoria.setAud_detalle("Se eliminó lógicamente el producto: " + producto.getProd_descripcion());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(autor);
        auditoriaRepository.save(auditoria);

        return ResponseEntity.noContent().build(); // Devuelve un 204 No Content
    }

    // Eliminar un producto
    public ResponseEntity<Void> deleteProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}