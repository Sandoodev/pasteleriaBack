package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.dto.Ingreso;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoMasSolicitado;
import com.pasteleriaBack.pasteleriaBack.dto.PedidoPorCocinero;
import com.pasteleriaBack.pasteleriaBack.dto.ReporteResponse;
import com.pasteleriaBack.pasteleriaBack.repository.PedidoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.ProductoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public ReporteResponse generarReporte(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Ingreso> ingresos = generarReporteIngresos(fechaInicio, fechaFin);
        List<ProductoMasSolicitado> productosMasSolicitados = generarReporteProductosMasSolicitados(fechaInicio, fechaFin);
        List<PedidoPorCocinero> pedidosPorCocinero = generarReportePedidosPorCocinero(fechaInicio, fechaFin);

        return new ReporteResponse(ingresos, productosMasSolicitados, pedidosPorCocinero);
    }

    public List<Ingreso> generarReporteIngresos(LocalDate fechaInicio, LocalDate fechaFin) {
        // Convertir LocalDate a Timestamp
        Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fechaFin.atStartOfDay());

        return pedidoRepository.calcularIngresos(inicio, fin);
    }

    public List<ProductoMasSolicitado> generarReporteProductosMasSolicitados(LocalDate fechaInicio, LocalDate fechaFin) {
        // Convertir LocalDate a Timestamp
        Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fechaFin.atStartOfDay());

        return productoRepository.obtenerProductosMasSolicitados(inicio, fin);
    }

    public List<PedidoPorCocinero> generarReportePedidosPorCocinero(LocalDate fechaInicio, LocalDate fechaFin) {
        // Convertir LocalDate a Timestamp
        Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fechaFin.atStartOfDay());

        return empleadoRepository.obtenerPedidosPorCocinero(inicio, fin);
    }
}