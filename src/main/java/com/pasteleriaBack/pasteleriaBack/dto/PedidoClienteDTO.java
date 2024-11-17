package com.pasteleriaBack.pasteleriaBack.dto;

import java.sql.Timestamp;

//REQUERIMIENTO 17
public class PedidoClienteDTO {
    private String nombreCliente;
    private String descripcion;
    private String numeroCelular;
    private String telefonoFijo;
    private String email;
    private Timestamp fechaYHoraDeRetiroEnvio;
    // Campos de domicilio
    private String ciudad;
    private String barrio;
    private String calle;
    private String numeroCasa;
    private String numeroApartamento;
    private String referencia;

    private String estadoPedido;
    private String nombreApellidoCocinero;

    // Getters y Setters

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getFechaYHoraDeRetiroEnvio() {
        return fechaYHoraDeRetiroEnvio;
    }

    public void setFechaYHoraDeRetiroEnvio(Timestamp fechaYHoraDeRetiroEnvio) {
        this.fechaYHoraDeRetiroEnvio = fechaYHoraDeRetiroEnvio;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getNombreApellidoCocinero() {
        return nombreApellidoCocinero;
    }

    public void setNombreApellidoCocinero(String nombreApellidoCocinero) {
        this.nombreApellidoCocinero = nombreApellidoCocinero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getNumeroApartamento() {
        return numeroApartamento;
    }

    public void setNumeroApartamento(String numeroApartamento) {
        this.numeroApartamento = numeroApartamento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
