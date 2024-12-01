package com.pasteleriaBack.pasteleriaBack.dto;

import com.pasteleriaBack.pasteleriaBack.model.EstadoEntregaENUM;
import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public class UpdatePedidoDTO {

    private String ped_descripcion;

    private EstadoEntregaENUM ped_entrega; // Cambiado a tipo enum según tu modelo

    @NotNull(message = "La fecha de entrega no puede ser nula")
    private Timestamp pedFechaDeEntrega; // Cambiado a Timestamp para coincidir con tu modelo

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoPedidoENUM pedEstado;

    private ClienteDTO cliente; // DTO para el cliente

    private PedidoDomicilioDTO pedidoDomicilio; // DTO para el domicilio

    // Getters y Setters
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

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public PedidoDomicilioDTO getPedidoDomicilio() {
        return pedidoDomicilio;
    }

    public void setPedidoDomicilio(PedidoDomicilioDTO pedidoDomicilio) {
        this.pedidoDomicilio = pedidoDomicilio;
    }
}