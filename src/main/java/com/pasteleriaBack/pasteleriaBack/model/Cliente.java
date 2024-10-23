package com.pasteleriaBack.pasteleriaBack.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    private Integer cli_dni;

    private String cli_apellidoNombre;
    private String cli_numCelu;
    private String cli_nroTelefonoFijo;
    private String cli_email;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos; // Relación con Pedidos

    // Getters y Setters

    public Integer getCli_dni() {
        return cli_dni;
    }

    public void setCli_dni(Integer cli_dni) {
        this.cli_dni = cli_dni;
    }

    public String getCli_apellidoNombre() {
        return cli_apellidoNombre;
    }

    public void setCli_apellidoNombre(String cli_apellidoNombre) {
        this.cli_apellidoNombre = cli_apellidoNombre;
    }

    public String getCli_numCelu() {
        return cli_numCelu;
    }

    public void setCli_numCelu(String cli_numCelu) {
        this.cli_numCelu = cli_numCelu;
    }

    public String getCli_nroTelefonoFijo() {
        return cli_nroTelefonoFijo;
    }

    public void setCli_nroTelefonoFijo(String cli_nroTelefonoFijo) {
        this.cli_nroTelefonoFijo = cli_nroTelefonoFijo;
    }

    public String getCli_email() {
        return cli_email;
    }

    public void setCli_email(String cli_email) {
        this.cli_email = cli_email;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}