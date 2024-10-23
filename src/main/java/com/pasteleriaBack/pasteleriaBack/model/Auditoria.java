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

    @ManyToOne
    @JoinColumn(name = "autor_dni", nullable = false)
    private Empleado autor; // Relación con Empleado

    // Getters y Setters

    public Integer getAud_id() {
        return aud_id;
    }

    public void setAud_id(Integer aud_id) {
        this.aud_id = aud_id;
    }

    public String getAud_operacion() {
        return aud_operacion;
    }

    public void setAud_operacion(String aud_operacion) {
        this.aud_operacion = aud_operacion;
    }

    public String getAud_detalle() {
        return aud_detalle;
    }

    public void setAud_detalle(String aud_detalle) {
        this.aud_detalle = aud_detalle;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Empleado getAutor() {
        return autor;
    }

    public void setAutor(Empleado autor) {
        this.autor = autor;
    }
}