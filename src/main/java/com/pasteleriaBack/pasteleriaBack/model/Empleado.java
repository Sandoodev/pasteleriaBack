package com.pasteleriaBack.pasteleriaBack.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Empleados")
public class Empleado {
    @Id
    private Integer emp_dni;

    private String emp_email;
    private String emp_apellidoNombre;
    private String emp_nroCelular;
    private Double emp_sueldo;
    private Double emp_porcentajeComisionPedido;

    @Enumerated(EnumType.STRING)
    private EstadoEmpleadoENUM emp_estado; // Usando la enumeración EstadoEmpleado

    private String emp_contraseña;
    private Integer emp_jornadaLaboral;

    @Enumerated(EnumType.STRING)
    @Column(name = "emp_rol")
    private RolEmpleadoENUM empRol; // Usando la enumeración RolEmpleado

    @OneToMany(mappedBy = "empleado")
    //@JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    //@JsonBackReference //hijo de pedido, ORIGINALMENTE TENIA ESTE PERO AL HACER EL REQUERIMIENTO 7 ME DABA ERROR 415 POR ESO SE CAMBIO.(TAMBIEN SE CAMBIO LA RELACION CON PEDIDO EN LA CLASE PEDIDO)
    private List<Pedido> pedidos; // Relación con Pedidos

    // Getters y Setters

    public Integer getEmp_dni() {
        return emp_dni;
    }

    public void setEmp_dni(Integer emp_dni) {
        this.emp_dni = emp_dni;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_apellidoNombre() {
        return emp_apellidoNombre;
    }

    public void setEmp_apellidoNombre(String emp_apellidoNombre) {
        this.emp_apellidoNombre = emp_apellidoNombre;
    }

    public String getEmp_nroCelular() {
        return emp_nroCelular;
    }

    public void setEmp_nroCelular(String emp_nroCelular) {
        this.emp_nroCelular = emp_nroCelular;
    }

    public Double getEmp_sueldo() {
        return emp_sueldo;
    }

    public void setEmp_sueldo(Double emp_sueldo) {
        this.emp_sueldo = emp_sueldo;
    }

    public Double getEmp_porcentajeComisionPedido() {
        return emp_porcentajeComisionPedido;
    }

    public void setEmp_porcentajeComisionPedido(Double emp_porcentajeComisionPedido) {
        this.emp_porcentajeComisionPedido = emp_porcentajeComisionPedido;
    }

    public EstadoEmpleadoENUM getEmp_estado() {
        return emp_estado;
    }

    public void setEmp_estado(EstadoEmpleadoENUM emp_estado) {
        this.emp_estado = emp_estado;
    }

    public String getEmp_contraseña() {
        return emp_contraseña;
    }

    public void setEmp_contraseña(String emp_contraseña) {
        this.emp_contraseña = emp_contraseña;
    }

    public Integer getEmp_jornadaLaboral() {
        return emp_jornadaLaboral;
    }

    public void setEmp_jornadaLaboral(Integer emp_jornadaLaboral) {
        this.emp_jornadaLaboral = emp_jornadaLaboral;
    }

    public RolEmpleadoENUM getEmpRol() {
        return empRol;
    }

    public void setEmpRol(RolEmpleadoENUM empRol) {
        this.empRol = empRol;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}