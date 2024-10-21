package com.pasteleriaBack.pasteleriaBack.model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;

@Embeddable
public class PedidoProductoId implements Serializable {
    private Integer prodId;
    private Integer pedId;

    // Constructor vacío
    public PedidoProductoId() {}

    // Getters y Setters
    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getPedId() {
        return pedId;
    }

    public void setPedId(Integer pedId) {
        this.pedId = pedId;
    }

    // hashCode y equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedidoProductoId)) return false;
        PedidoProductoId that = (PedidoProductoId) o;
        return prodId.equals(that.prodId) && pedId.equals(that.pedId);
    }

    @Override
    public int hashCode() {
        return 31 * prodId.hashCode() + pedId.hashCode();
    }
}