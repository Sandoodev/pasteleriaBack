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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private final String IMAGE_DIRECTORY = "uploads" + File.separator; // Esto se ajustará automáticamente al sistema operativo

    public ResponseEntity<Producto> createProducto(MultipartFile file, String prodTitulo, String prodDescripcion,
                                                   Double prodPrecioCosto, Double prodPrecioVenta,
                                                   Integer prodTiempoDeProduccion, Double prodPorcentajeDescuento, String prodEstado, Integer dniAutor) {
        System.out.println("Hasta aqui llega");
        // Obtener la ruta absoluta del directorio de trabajo
        String absolutePath = new File(IMAGE_DIRECTORY).getAbsolutePath();
        File uploadsDir = new File(absolutePath);

        // Validar la entrada
        if (prodDescripcion == null || prodDescripcion.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (prodPrecioCosto == null || prodPrecioCosto < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (prodPrecioVenta == null || prodPrecioVenta < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (prodTiempoDeProduccion == null || prodTiempoDeProduccion < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        System.out.println("hasta aqui tmb llega");

        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs(); // Crea la carpeta si no existe
        }
        // Manejo de la imagen
        String fileName = file.getOriginalFilename();
        String filePath =  absolutePath + File.separator + fileName;
        System.out.println("hasta aqui tmb llega 2 " + filePath);

        try {
            // Guarda la imagen en el sistema de archivos
            File destinationFile = new File(filePath);
            System.out.println(destinationFile + "aqui");
            file.transferTo(destinationFile);
        } catch (IOException e) {
            e.printStackTrace(); // Imprime el stack trace para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Crear un nuevo producto
        Producto producto = new Producto();
        producto.setProd_titulo(prodTitulo);
        producto.setProd_descripcion(prodDescripcion);
        producto.setProd_precioCosto(prodPrecioCosto);
        producto.setProd_precioVenta(prodPrecioVenta);
        producto.setProd_tiempoDeProduccion(prodTiempoDeProduccion);
        producto.setProd_porcentajeDescuento(prodPorcentajeDescuento);
        producto.setProd_estado(EstadoProductoENUM.valueOf(prodEstado));
        producto.setProd_imagen(fileName); // Guarda solo el nombre del archivo o la ruta relativa

        System.out.println("h33asta aqui tmb llega");

        // Guardar el producto en la base de datos
        Producto savedProducto = productoRepository.save(producto);

        // Obtener el autor (Empleado) que realiza la acción
        Empleado autor = empleadoRepository.findById(dniAutor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        System.out.println("ha324234sta aqui tmb llega");

        // Registrar la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Registro de nuevo producto");
        auditoria.setAud_detalle("Se registró el producto: " + savedProducto.getProd_descripcion());
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(autor);
        auditoriaRepository.save(auditoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }
//    public ResponseEntity<Producto> createProducto(Producto producto, Integer dniAutor) {
//        // Validar la entrada
//        if (producto.getProd_descripcion() == null || producto.getProd_descripcion().isEmpty()) {
//            return ResponseEntity.badRequest().body(null); // O lanzar una excepción
//        }
//        if (producto.getProd_precioCosto() == null || producto.getProd_precioCosto() < 0) {
//            return ResponseEntity.badRequest().body(null);
//        }
//        if (producto.getProd_precioVenta() == null || producto.getProd_precioVenta() < 0) {
//            return ResponseEntity.badRequest().body(null);
//        }
//        if (producto.getProd_tiempoDeProduccion() == null || producto.getProd_tiempoDeProduccion() < 0) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        // Guardar el producto en la base de datos
//        Producto savedProducto = productoRepository.save(producto);
//
//        // Obtener el autor (Empleado) que realiza la acción
//        Empleado autor = empleadoRepository.findById(dniAutor)
//                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
//
//        // Registrar la auditoría
//        Auditoria auditoria = new Auditoria();
//        auditoria.setAud_operacion("Registro de nuevo producto");
//        auditoria.setAud_detalle("Se registro el producto: " + savedProducto.getProd_descripcion());
//        auditoria.setFecha(LocalDateTime.now());
//        auditoria.setAutor(autor); // Usar el objeto Empleado del autor
//        auditoriaRepository.save(auditoria);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
//    }

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