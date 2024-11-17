package com.pasteleriaBack.pasteleriaBack.model;

public enum EstadoPedidoENUM {
    enPreparacion,
    pendienteDeEnvio,
    pendienteDeRetiro,
    enviado,
    retirado,
    eliminado;

    public static EstadoPedidoENUM fromString(String estado) {
        if (estado == null) {
            return null; // O lanzar una excepción si prefieres
        }
        for (EstadoPedidoENUM e : EstadoPedidoENUM.values()) {
            if (e.name().equalsIgnoreCase(estado)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + estado);
    }
}
