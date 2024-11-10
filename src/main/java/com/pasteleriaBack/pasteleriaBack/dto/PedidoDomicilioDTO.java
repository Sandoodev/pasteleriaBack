package com.pasteleriaBack.pasteleriaBack.dto;

public class PedidoDomicilioDTO {
    private Integer ped_id; // Clave primaria que es la misma que en Pedido
    private String ped_ciudad;
    private String ped_barrio;
    private String ped_calle;
    private String ped_numeroCasa;
    private String ped_nroApartamento;
    private String ped_referencia;

    // Getters y Setters

    public Integer getPed_id() {
        return ped_id;
    }

    public void setPed_id(Integer ped_id) {
        this.ped_id = ped_id;
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