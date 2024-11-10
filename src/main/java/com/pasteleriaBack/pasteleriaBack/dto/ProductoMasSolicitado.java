package com.pasteleriaBack.pasteleriaBack.dto;

public class ProductoMasSolicitado {
    private String titulo;
    private Long  cantidad;

    public ProductoMasSolicitado(String titulo, Long  cantidad) {
        this.titulo = titulo;
        this.cantidad = cantidad;
    }

    // Getters y Setters


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long  getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long  cantidad) {
        this.cantidad = cantidad;
    }
}