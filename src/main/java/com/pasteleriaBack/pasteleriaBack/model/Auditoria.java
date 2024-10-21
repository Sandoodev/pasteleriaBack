package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Auditorias")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aud_id;

    private String aud_operacion;
    private String aud_detalle;
    private LocalDate fecha;
    private Integer autor_dni;

    // Getters y Setters

}