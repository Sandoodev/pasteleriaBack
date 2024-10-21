package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MotivosEliminacionPedidos")
public class MotivoEliminacionPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mot_id;

    private Integer ped_id;
    private String ped_motivoEliminacion;
    private String descripcion;

    // Getters y Setters

    public Integer getMot_id() {
        return mot_id;
    }

    public void setMot_id(Integer mot_id) {
        this.mot_id = mot_id;
    }

    public Integer getPed_id() {
        return ped_id;
    }

    public void setPed_id(Integer ped_id) {
        this.ped_id = ped_id;
    }

    public String getPed_motivoEliminacion() {
        return ped_motivoEliminacion;
    }

    public void setPed_motivoEliminacion(String ped_motivoEliminacion) {
        this.ped_motivoEliminacion = ped_motivoEliminacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}