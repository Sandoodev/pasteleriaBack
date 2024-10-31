package com.pasteleriaBack.pasteleriaBack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ped_id;

    private String ped_descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoEntregaENUM ped_entrega; // tipo enum

    private Timestamp ped_fechaDeCreacion;
    private Timestamp ped_fechaDeEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoPedidoENUM ped_estado; // tipo enum

    @ManyToOne
    @JoinColumn(name = "emp_dni", nullable = false)
    @JsonIgnore
    //@JsonManagedReference
    private Empleado empleado; // Relación con Empleado

    @ManyToOne
    @JoinColumn(name = "cli_dni", nullable = false)
    @JsonBackReference
    private Cliente cliente; // Relación con Cliente

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference // Esta parte se serializa y es para la tabla padre
    private List<PedidoProducto> pedidoProductos; // Relación con Pedidos_Productos

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private PedidoDomicilio pedidoDomicilio; // Relación con PedidoDomicilio

    @Column(name = "porcentajeComisionPedidoActual", nullable = false)
    private Double porcentajeComisionPedidoActual;

    // Getters y Setters


    public Integer getPed_id() {
        return ped_id;
    }

    public void setPed_id(Integer ped_id) {
        this.ped_id = ped_id;
    }

    public String getPed_descripcion() {
        return ped_descripcion;
    }

    public void setPed_descripcion(String ped_descripcion) {
        this.ped_descripcion = ped_descripcion;
    }

    public EstadoEntregaENUM getPed_entrega() {
        return ped_entrega;
    }

    public void setPed_entrega(EstadoEntregaENUM ped_entrega) {
        this.ped_entrega = ped_entrega;
    }

    public Timestamp getPed_fechaDeCreacion() {
        return ped_fechaDeCreacion;
    }

    public void setPed_fechaDeCreacion(Timestamp ped_fechaDeCreacion) {
        this.ped_fechaDeCreacion = ped_fechaDeCreacion;
    }

    public Timestamp getPed_fechaDeEntrega() {
        return ped_fechaDeEntrega;
    }

    public void setPed_fechaDeEntrega(Timestamp ped_fechaDeEntrega) {
        this.ped_fechaDeEntrega = ped_fechaDeEntrega;
    }

    public EstadoPedidoENUM getPed_estado() {
        return ped_estado;
    }

    public void setPed_estado(EstadoPedidoENUM ped_estado) {
        this.ped_estado = ped_estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<PedidoProducto> getPedidoProductos() {
        return pedidoProductos;
    }

    public void setPedidoProductos(List<PedidoProducto> pedidoProductos) {
        this.pedidoProductos = pedidoProductos;
    }

    public PedidoDomicilio getPedidoDomicilio() {
        return pedidoDomicilio;
    }

    public void setPedidoDomicilio(PedidoDomicilio pedidoDomicilio) {
        this.pedidoDomicilio = pedidoDomicilio;
    }

    public Double getPorcentajeComisionPedidoActual() {
        return porcentajeComisionPedidoActual;
    }

    public void setPorcentajeComisionPedidoActual(Double porcentajeComisionPedidoActual) {
        this.porcentajeComisionPedidoActual = porcentajeComisionPedidoActual;
    }
}