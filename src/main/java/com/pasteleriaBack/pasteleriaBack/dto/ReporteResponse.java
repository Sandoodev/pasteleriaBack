package com.pasteleriaBack.pasteleriaBack.dto;

import java.util.List;

public class ReporteResponse {
    private List<Ingreso> ingresos;
    private List<ProductoMasSolicitado> productosMasSolicitados;
    private List<PedidoPorCocinero> pedidosPorCocinero;

    public ReporteResponse(List<Ingreso> ingresos, List<ProductoMasSolicitado> productosMasSolicitados, List<PedidoPorCocinero> pedidosPorCocinero) {
        this.ingresos = ingresos;
        this.productosMasSolicitados = productosMasSolicitados;
        this.pedidosPorCocinero = pedidosPorCocinero;
    }

    // Getters y Setters

    public List<Ingreso> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<Ingreso> ingresos) {
        this.ingresos = ingresos;
    }

    public List<ProductoMasSolicitado> getProductosMasSolicitados() {
        return productosMasSolicitados;
    }

    public void setProductosMasSolicitados(List<ProductoMasSolicitado> productosMasSolicitados) {
        this.productosMasSolicitados = productosMasSolicitados;
    }

    public List<PedidoPorCocinero> getPedidosPorCocinero() {
        return pedidosPorCocinero;
    }

    public void setPedidosPorCocinero(List<PedidoPorCocinero> pedidosPorCocinero) {
        this.pedidosPorCocinero = pedidosPorCocinero;
    }
}
