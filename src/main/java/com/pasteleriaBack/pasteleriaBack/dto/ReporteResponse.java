package com.pasteleriaBack.pasteleriaBack.dto;

import java.util.List;
import java.util.Map;

public class ReporteResponse {
    private Map<String, Double> ingresos;
    private List<ProductoMasSolicitado> productosMasSolicitados;
    private List<PedidoPorCocinero> pedidosPorCocinero;

    public ReporteResponse(Map<String, Double> ingresos, List<ProductoMasSolicitado> productosMasSolicitados, List<PedidoPorCocinero> pedidosPorCocinero) {
        this.ingresos = ingresos;
        this.productosMasSolicitados = productosMasSolicitados;
        this.pedidosPorCocinero = pedidosPorCocinero;
    }

    // Getters y Setters

    public Map<String, Double> getIngresos() {
        return ingresos;
    }

    public void setIngresos(Map<String, Double> ingresos) {
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
