package com.pasteleriaBack.pasteleriaBack.dto;

public class Ingreso {
    private int mes;
    private double total;

    public Ingreso(int mes, double total) {
        this.mes = mes;
        this.total = total;
    }

    // Getters y Setters


    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
