package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "HorariosAperturaCierre")
public class HorarioAperturaCierre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hac_id;

    private Time hac_horarioApertura;
    private Time hac_horarioCierre;

    // Getters y Setters

    public Integer getHac_id() {
        return hac_id;
    }

    public void setHac_id(Integer hac_id) {
        this.hac_id = hac_id;
    }

    public Time getHac_horarioApertura() {
        return hac_horarioApertura;
    }

    public void setHac_horarioApertura(Time hac_horarioApertura) {
        this.hac_horarioApertura = hac_horarioApertura;
    }

    public Time getHac_horarioCierre() {
        return hac_horarioCierre;
    }

    public void setHac_horarioCierre(Time hac_horarioCierre) {
        this.hac_horarioCierre = hac_horarioCierre;
    }
}