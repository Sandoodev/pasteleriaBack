package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    private Integer emp_dni;

    @Column(name = "porcentajeComisionPedidoActual", nullable = false)
    private Double porcentajeComisionPedidoActual;
    private Integer cli_dni;

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

    public Integer getEmp_dni() {
        return emp_dni;
    }

    public void setEmp_dni(Integer emp_dni) {
        this.emp_dni = emp_dni;
    }

    public Double getPorcentajeComisionPedidoActual() {
        return porcentajeComisionPedidoActual;
    }

    public void setPorcentajeComisionPedidoActual(Double porcentajeComisionPedidoActual) {
        this.porcentajeComisionPedidoActual = porcentajeComisionPedidoActual;
    }

    public Integer getCli_dni() {
        return cli_dni;
    }

    public void setCli_dni(Integer cli_dni) {
        this.cli_dni = cli_dni;
    }
}