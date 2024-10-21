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
}