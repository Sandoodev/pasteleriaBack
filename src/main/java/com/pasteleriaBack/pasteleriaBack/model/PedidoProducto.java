package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Pedidos_Productos")
public class PedidoProducto {

    @EmbeddedId
    private PedidoProductoId id;

    @ManyToOne
    @MapsId("prodId") // Mapea el campo prodId de la clave compuesta
    @JoinColumn(name = "prod_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @MapsId("pedId") // Tmb mapea el campo pedId de la clave compuesta
    @JoinColumn(name = "ped_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "prod_precioCosto", nullable = false)
    private Double precioCosto;

    @Column(name = "prod_precioVenta", nullable = false)
    private Double precioVenta;

    // Getters y Setters

    public PedidoProductoId getId() {
        return id;
    }

    public void setId(PedidoProductoId id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(Double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
}