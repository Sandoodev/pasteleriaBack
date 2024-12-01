package com.pasteleriaBack.pasteleriaBack.dto;

public class ProductoCocineroDTO {
    private String prod_titulo;
    private Integer cantidad;
    private Double precioVenta;

    // Getters y Setters
    public String getProd_titulo() {
        return prod_titulo;
    }

    public void setProd_titulo(String prod_titulo) {
        this.prod_titulo = prod_titulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
}
