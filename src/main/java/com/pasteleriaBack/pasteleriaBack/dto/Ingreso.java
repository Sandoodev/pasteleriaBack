package com.pasteleriaBack.pasteleriaBack.dto;

public class Ingreso {
    private Integer pedidoId;
    private Double monto;

    public Ingreso(Integer pedidoId, Double monto) {
        this.pedidoId = pedidoId;
        this.monto = monto;
    }

    // Getters y Setters
    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}