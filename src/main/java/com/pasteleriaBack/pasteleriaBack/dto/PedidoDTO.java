package com.pasteleriaBack.pasteleriaBack.dto;

import com.pasteleriaBack.pasteleriaBack.model.EstadoEntregaENUM;

import java.util.List;

public class PedidoDTO {
    private Integer dni;
    private String cli_apellidoNombreDto;
    private String emailDto;
    private String cli_numCeluDto;
    private String cli_nroTelefonoFijoDto;
    private EstadoEntregaENUM ped_entregaDto; // "envio-domicilio" o "retiro-local"
    private String ped_barrioDto; // Solo si se selecciona "envio-domicilio"
    private String ped_calleDto; // Solo si se selecciona "envio-domicilio"
    private String ped_numeroCasaDto; // Solo si se selecciona "envio-domicilio"
    private String ped_ciudadDto; // Solo si se selecciona "envio-domicilio"
    private String ped_referenciaDto; // Opcional
    private String ped_descripcionDto; // Opcional
    private Double porcentajeComisionPedidoActualDto;
    private List<ProductoCantidadDTO> productos; // Lista de productos y cantidades

    // Getters y Setters

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getCli_apellidoNombreDto() {
        return cli_apellidoNombreDto;
    }

    public void setCli_apellidoNombreDto(String cli_apellidoNombreDto) {
        this.cli_apellidoNombreDto = cli_apellidoNombreDto;
    }

    public String getEmailDto() {
        return emailDto;
    }

    public void setEmailDto(String emailDto) {
        this.emailDto = emailDto;
    }

    public String getCli_numCeluDto() {
        return cli_numCeluDto;
    }

    public void setCli_numCeluDto(String cli_numCeluDto) {
        this.cli_numCeluDto = cli_numCeluDto;
    }

    public String getCli_nroTelefonoFijoDto() {
        return cli_nroTelefonoFijoDto;
    }

    public void setCli_nroTelefonoFijoDto(String cli_nroTelefonoFijoDto) {
        this.cli_nroTelefonoFijoDto = cli_nroTelefonoFijoDto;
    }

    public EstadoEntregaENUM getPed_entregaDto() {
        return ped_entregaDto;
    }

    public void setPed_entregaDto(EstadoEntregaENUM ped_entregaDto) {
        this.ped_entregaDto = ped_entregaDto;
    }

    public String getPed_barrioDto() {
        return ped_barrioDto;
    }

    public void setPed_barrioDto(String ped_barrioDto) {
        this.ped_barrioDto = ped_barrioDto;
    }

    public String getPed_calleDto() {
        return ped_calleDto;
    }

    public void setPed_calleDto(String ped_calleDto) {
        this.ped_calleDto = ped_calleDto;
    }

    public String getPed_numeroCasaDto() {
        return ped_numeroCasaDto;
    }

    public void setPed_numeroCasaDto(String ped_numeroCasaDto) {
        this.ped_numeroCasaDto = ped_numeroCasaDto;
    }

    public String getPed_ciudadDto() {
        return ped_ciudadDto;
    }

    public void setPed_ciudadDto(String ped_ciudadDto) {
        this.ped_ciudadDto = ped_ciudadDto;
    }

    public String getPed_referenciaDto() {
        return ped_referenciaDto;
    }

    public void setPed_referenciaDto(String ped_referenciaDto) {
        this.ped_referenciaDto = ped_referenciaDto;
    }

    public String getPed_descripcionDto() {
        return ped_descripcionDto;
    }

    public void setPed_descripcionDto(String ped_descripcionDto) {
        this.ped_descripcionDto = ped_descripcionDto;
    }

    public Double getPorcentajeComisionPedidoActualDto() {
        return porcentajeComisionPedidoActualDto;
    }

    public void setPorcentajeComisionPedidoActualDto(Double porcentajeComisionPedidoActual) {
        this.porcentajeComisionPedidoActualDto = porcentajeComisionPedidoActual;
    }

    public List<ProductoCantidadDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCantidadDTO> productos) {
        this.productos = productos;
    }
}