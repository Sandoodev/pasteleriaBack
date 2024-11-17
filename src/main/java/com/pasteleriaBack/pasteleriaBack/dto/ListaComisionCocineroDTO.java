package com.pasteleriaBack.pasteleriaBack.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class ListaComisionCocineroDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private Integer idPedido;
    private Timestamp fechaCreacion;
    private Timestamp fechaEntrega;
    private String producto;
    private Double comision;

    public ListaComisionCocineroDTO(String dni, String nombre, String apellido, Integer idPedido, Timestamp fechaCreacion, Timestamp fechaEntrega, String producto, Double comision) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.idPedido = idPedido;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.producto = producto;
        this.comision = comision;
    }

    // Getters y Setters


}