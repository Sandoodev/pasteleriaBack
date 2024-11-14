package com.pasteleriaBack.pasteleriaBack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prod_id;

    private String prod_descripcion;
    private String prod_imagen;
    private Double prod_precioCosto;
    private Double prod_precioVenta;
    private Integer prod_tiempoDeProduccion;
    private Double prod_porcentajeDescuento;

    @Enumerated(EnumType.STRING)
    private EstadoProductoENUM prod_estado; // tipo enum

    private String prod_titulo;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    //@JsonManagedReference // Esta parte se serializa, pero no la uso porque me da problemas de error 415 a la hora de insertar un producto
    private List<PedidoProducto> pedidoProductos; // Relación con Pedidos_Productos

    // Getters y Setters

    public Integer getProd_id() {
        return prod_id;
    }

    public void setProd_id(Integer prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_descripcion() {
        return prod_descripcion;
    }

    public void setProd_descripcion(String prod_descripcion) {
        this.prod_descripcion = prod_descripcion;
    }

    public String getProd_imagen() {
        return prod_imagen;
    }

    public void setProd_imagen(String prod_imagen) {
        this.prod_imagen = prod_imagen;
    }

    public Double getProd_precioCosto() {
        return prod_precioCosto;
    }

    public void setProd_precioCosto(Double prod_precioCosto) {
        this.prod_precioCosto = prod_precioCosto;
    }

    public Double getProd_precioVenta() {
        return prod_precioVenta;
    }

    public void setProd_precioVenta(Double prod_precioVenta) {
        this.prod_precioVenta = prod_precioVenta;
    }

    public Integer getProd_tiempoDeProduccion() {
        return prod_tiempoDeProduccion;
    }

    public void setProd_tiempoDeProduccion(Integer prod_tiempoDeProduccion) {
        this.prod_tiempoDeProduccion = prod_tiempoDeProduccion;
    }

    public Double getProd_porcentajeDescuento() {
        return prod_porcentajeDescuento;
    }

    public void setProd_porcentajeDescuento(Double prod_porcentajeDescuento) {
        this.prod_porcentajeDescuento = prod_porcentajeDescuento;
    }

    public EstadoProductoENUM getProd_estado() {
        return prod_estado;
    }

    public void setProd_estado(EstadoProductoENUM prod_estado) {
        this.prod_estado = prod_estado;
    }

    public String getProd_titulo() {
        return prod_titulo;
    }

    public void setProd_titulo(String prod_titulo) {
        this.prod_titulo = prod_titulo;
    }

    public List<PedidoProducto> getPedidoProductos() {
        return pedidoProductos;
    }

    public void setPedidoProductos(List<PedidoProducto> pedidoProductos) {
        this.pedidoProductos = pedidoProductos;
    }
}