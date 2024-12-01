package com.pasteleriaBack.pasteleriaBack.dto;

import com.pasteleriaBack.pasteleriaBack.model.Pedido;

import java.util.List;

public class PedidoPorCocinero {
    private String cocineroNombre;
    private List<Pedido> pedidos; // Lista de pedidos realizados por el cocinero

    public PedidoPorCocinero(String cocineroNombre, List<Pedido> pedidos) {
        this.cocineroNombre = cocineroNombre;
        this.pedidos = pedidos;
    }

    // Getters y Setters

    public String getCocineroNombre() {
        return cocineroNombre;
    }

    public void setCocineroNombre(String cocineroNombre) {
        this.cocineroNombre = cocineroNombre;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}