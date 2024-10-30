package com.pasteleriaBack.pasteleriaBack.dto;
//Este DTO se utiliza para representar cada producto en el pedido, incluyendo su ID, cantidad y descripción
public class ProductoCantidadDTO {
    private Integer prodId; // ID del producto
    private int cantidad; // Cantidad del producto
    private String descripcion; // Descripción del producto

    // Getters y Setters

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}