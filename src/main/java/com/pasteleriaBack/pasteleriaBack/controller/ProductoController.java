package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.model.Producto;
import com.pasteleriaBack.pasteleriaBack.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    private final String IMAGE_DIRECTORY = "uploads" + File.separator;

    @CrossOrigin
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(IMAGE_DIRECTORY).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        return productoService.getProductoById(id);
    }

    @CrossOrigin
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Producto> createProducto(
            @RequestParam MultipartFile file,
            @RequestParam String prodTitulo,
            @RequestParam String prodDescripcion,
            @RequestParam Double prodPrecioCosto,
            @RequestParam Double prodPrecioVenta,
            @RequestParam Integer prodTiempoDeProduccion,
            @RequestParam Double prodPorcentajeDescuento,
            @RequestParam String prodEstado,
            @RequestParam Integer dniAutor) {
        return productoService.createProducto(file, prodTitulo, prodDescripcion, prodPrecioCosto, prodPrecioVenta, prodTiempoDeProduccion, prodPorcentajeDescuento, prodEstado, dniAutor);
    }

//    @CrossOrigin
//    @PostMapping(consumes = "application/json")
//    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto, @RequestParam Integer dniAutor) {
//        return productoService.createProducto(producto, dniAutor);
//    }

    //REQUERIMIENTO 5: actualizacion de producto
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody Producto updatedProducto, @RequestParam Integer dniAutor) {
        return productoService.updateProducto(id, updatedProducto, dniAutor);
    }

    //REQUERIMIENTO 6: baja logica
    @CrossOrigin
    @PutMapping("/{id}/baja")
    public ResponseEntity<Void> bajaLogicaProducto(@PathVariable Integer id, @RequestParam Integer dniAutor) {
        return productoService.bajaLogicaProducto(id, dniAutor);
    }
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        return productoService.deleteProducto(id);
    }


}