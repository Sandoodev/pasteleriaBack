package com.pasteleriaBack.pasteleriaBack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Column(name = "ped_fechaDeCreacion")
    private Timestamp pedFechaDeCreacion;

    @Column(name = "ped_fechaDeEntrega")
    private Timestamp pedFechaDeEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "ped_estado")
    private EstadoPedidoENUM pedEstado; // tipo enum

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
    private List<PedidoProducto> pedidoProductos = new ArrayList<>(); // Relación con Pedidos_Productos, inicialmente no estaba inicializada, fue cambiada para implementar la logica de asignar pedido a cocineros.

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private PedidoDomicilio pedidoDomicilio; // Relación con PedidoDomicilio

    @Column(name = "porcentajeComisionPedidoActual", nullable = false)
    private Double porcentajeComisionPedidoActual;

    // Getters y Setters

    @JsonProperty("cli_dni")
    public Integer getCliDni() {
        return cliente != null ? cliente.getCli_dni() : null;
    }

    @JsonProperty("emp_dni")
    public Integer getEmpDni() {
        return empleado != null ? empleado.getEmp_dni() : null;
    }

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

    public Timestamp getPedFechaDeCreacion() {
        return pedFechaDeCreacion;
    }

    public void setPedFechaDeCreacion(Timestamp pedFechaDeCreacion) {
        this.pedFechaDeCreacion = pedFechaDeCreacion;
    }

    public Timestamp getPedFechaDeEntrega() {
        return pedFechaDeEntrega;
    }

    public void setPedFechaDeEntrega(Timestamp pedFechaDeEntrega) {
        this.pedFechaDeEntrega = pedFechaDeEntrega;
    }

    public EstadoPedidoENUM getPedEstado() {
        return pedEstado;
    }

    public void setPedEstado(EstadoPedidoENUM pedEstado) {
        this.pedEstado = pedEstado;
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

    @Override
    public String toString() {
        return "\nPedido{" +
                "ped_id=" + ped_id +
                ", ped_descripcion='" + ped_descripcion + '\'' +
                ", ped_entrega=" + ped_entrega +
                ", pedFechaDeCreacion=" + pedFechaDeCreacion +
                ", pedFechaDeEntrega=" + pedFechaDeEntrega +
                ", pedEstado=" + pedEstado +
                ", empleado=" + empleado +
                ", cliente=" + cliente +
                ", pedidoProductos=" + pedidoProductos +
                ", pedidoDomicilio=" + pedidoDomicilio +
                ", porcentajeComisionPedidoActual=" + porcentajeComisionPedidoActual +
                '}'+"\n";
    }
}