package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "HorariosAperturaCierre")
public class HorarioAperturaCierre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hac_id;

    private Time hac_manana_apertura;
    private Time hac_manana_cierre;
    private Time hac_tarde_apertura;
    private Time hac_tarde_cierre;

    // Getters y Setters
    public Integer getHac_id() {
        return hac_id;
    }

    public void setHac_id(Integer hac_id) {
        this.hac_id = hac_id;
    }

    public Time getHac_manana_apertura() {
        return hac_manana_apertura;
    }

    public void setHac_manana_apertura(Time hac_manana_apertura) {
        this.hac_manana_apertura = hac_manana_apertura;
    }

    public Time getHac_manana_cierre() {
        return hac_manana_cierre;
    }

    public void setHac_manana_cierre(Time hac_manana_cierre) {
        this.hac_manana_cierre = hac_manana_cierre;
    }

    public Time getHac_tarde_apertura() {
        return hac_tarde_apertura;
    }

    public void setHac_tarde_apertura(Time hac_tarde_apertura) {
        this.hac_tarde_apertura = hac_tarde_apertura;
    }

    public Time getHac_tarde_cierre() {
        return hac_tarde_cierre;
    }

    public void setHac_tarde_cierre(Time hac_tarde_cierre) {
        this.hac_tarde_cierre = hac_tarde_cierre;
    }
}