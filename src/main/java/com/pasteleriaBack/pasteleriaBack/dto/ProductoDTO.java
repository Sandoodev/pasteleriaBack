package com.pasteleriaBack.pasteleriaBack.dto;

public class ProductoDTO {
    private String prod_titulo;
    private Integer cantidad;
    private Double prod_precioCosto;
    private Double precioVenta;
    private Double prod_porcentajeDescuento;

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

    public Double getProd_precioCosto() {
        return prod_precioCosto;
    }

    public void setProd_precioCosto(Double prod_precioCosto) {
        this.prod_precioCosto = prod_precioCosto;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getProd_porcentajeDescuento() {
        return prod_porcentajeDescuento;
    }

    public void setProd_porcentajeDescuento(Double prod_porcentajeDescuento) {
        this.prod_porcentajeDescuento = prod_porcentajeDescuento;
    }
}