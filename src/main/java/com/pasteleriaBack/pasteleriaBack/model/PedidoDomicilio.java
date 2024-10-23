package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Pedidos_Domicilio")
public class PedidoDomicilio {

    @Id
    @OneToOne
    @JoinColumn(name = "ped_id") // Clave foránea que referencia a la tabla Pedidos
    private Pedido pedido; // Relación uno a uno con Pedido

    private String ped_ciudad;
    private String ped_barrio;
    private String ped_calle;
    private String ped_numeroCasa;
    private String ped_nroApartamento;
    private String ped_referencia;

    // Getters y Setters

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getPed_ciudad() {
        return ped_ciudad;
    }

    public void setPed_ciudad(String ped_ciudad) {
        this.ped_ciudad = ped_ciudad;
    }

    public String getPed_barrio() {
        return ped_barrio;
    }

    public void setPed_barrio(String ped_barrio) {
        this.ped_barrio = ped_barrio;
    }

    public String getPed_calle() {
        return ped_calle;
    }

    public void setPed_calle(String ped_calle) {
        this.ped_calle = ped_calle;
    }

    public String getPed_numeroCasa() {
        return ped_numeroCasa;
    }

    public void setPed_numeroCasa(String ped_numeroCasa) {
        this.ped_numeroCasa = ped_numeroCasa;
    }

    public String getPed_nroApartamento() {
        return ped_nroApartamento;
    }

    public void setPed_nroApartamento(String ped_nroApartamento) {
        this.ped_nroApartamento = ped_nroApartamento;
    }

    public String getPed_referencia() {
        return ped_referencia;
    }

    public void setPed_referencia(String ped_referencia) {
        this.ped_referencia = ped_referencia;
    }
}