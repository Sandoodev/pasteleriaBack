package com.pasteleriaBack.pasteleriaBack.dto;


public class PedidoPorCocinero {
    private String nombreCocinero;
    private long cantidadPedidos;

    public PedidoPorCocinero(String nombreCocinero, long cantidadPedidos) {
        this.nombreCocinero = nombreCocinero;
        this.cantidadPedidos = cantidadPedidos;
    }

    // Getters y Setters


    public String getNombreCocinero() {
        return nombreCocinero;
    }

    public void setNombreCocinero(String nombreCocinero) {
        this.nombreCocinero = nombreCocinero;
    }

    public long getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(long cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }
}