package com.pasteleriaBack.pasteleriaBack.dto;

import java.sql.Timestamp;
import java.util.List;

public class PedidoPorCocineroDTO {
        private Integer idPedido;
        private Timestamp fechaCreacion;
        private Timestamp fechaEntrega;
        private String metodoEntrega;
        private Integer clienteDni;
        private String clienteApellidoNombre;
        private List<ProductoCocineroDTO> productos;
        private double totalPedido;

        // Getters y Setters

        public Integer getIdPedido() {
            return idPedido;
        }

        public void setIdPedido(Integer idPedido) {
            this.idPedido = idPedido;
        }

        public Timestamp getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(Timestamp fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }

        public Timestamp getFechaEntrega() {
            return fechaEntrega;
        }

        public void setFechaEntrega(Timestamp fechaEntrega) {
            this.fechaEntrega = fechaEntrega;
        }

        public String getMetodoEntrega() {
            return metodoEntrega;
        }

        public void setMetodoEntrega(String metodoEntrega) {
            this.metodoEntrega = metodoEntrega;
        }

        public Integer getClienteDni() {
            return clienteDni;
        }

        public void setClienteDni(Integer clienteDni) {
            this.clienteDni = clienteDni;
        }

        public String getClienteApellidoNombre() {
            return clienteApellidoNombre;
        }

        public void setClienteApellidoNombre(String clienteNombre) {
            this.clienteApellidoNombre = clienteNombre;
        }

        public List<ProductoCocineroDTO> getProductos() {
            return productos;
        }

        public void setProductos(List<ProductoCocineroDTO> productos) {
            this.productos = productos;
        }

        public double getTotalPedido() {
            return totalPedido;
        }

        public void setTotalPedido(double totalPedido) {
            this.totalPedido = totalPedido;
        }
}
