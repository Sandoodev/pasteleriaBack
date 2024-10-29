package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MotivosEliminacionPedidos")
public class MotivoEliminacionPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mot_id;

    @ManyToOne
    @JoinColumn(name = "ped_id", nullable = false)
    private Pedido pedido; // Relación con Pedidos

    @Enumerated(EnumType.STRING) // Esto indica que se almacenará como un String en la base de datos
    private MotivoEliminacionENUM ped_motivoEliminacion;

    private String descripcion;

    // Getters y Setters
    public Integer getMot_id() {
        return mot_id;
    }

    public void setMot_id(Integer mot_id) {
        this.mot_id = mot_id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public MotivoEliminacionENUM getPed_motivoEliminacion() {
        return ped_motivoEliminacion;
    }

    public void setPed_motivoEliminacion(MotivoEliminacionENUM ped_motivoEliminacion) {
        this.ped_motivoEliminacion = ped_motivoEliminacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}