package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.dto.Ingreso;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoMasSolicitado;
import com.pasteleriaBack.pasteleriaBack.dto.PedidoPorCocinero;
import com.pasteleriaBack.pasteleriaBack.dto.ReporteResponse;
import com.pasteleriaBack.pasteleriaBack.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;


    //POR SI QUIERO PONER DE X FECHA A X FECHA Y NO UN RANGO DETERMINADO
    @CrossOrigin
    @GetMapping
    public ReporteResponse obtenerReportes(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {

        // Si no se proporciona fechaInicio, se establece un mes atrás
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now().minusMonths(1).withDayOfMonth(1); // Primer día del mes anterior
        }

        // Si no se proporciona fechaFin, se establece la fecha actual
        if (fechaFin == null) {
            fechaFin = LocalDate.now(); // Fecha actual
        }

        Map<String, Double> ingresos = reporteService.generarReporteIngresos(fechaInicio, fechaFin);
        List<ProductoMasSolicitado> productosMasSolicitados = reporteService.generarReporteProductosMasSolicitados(fechaInicio, fechaFin);
        List<PedidoPorCocinero> pedidosPorCocinero = reporteService.generarReportePedidosPorCocinero(fechaInicio, fechaFin);

        return new ReporteResponse(ingresos, productosMasSolicitados, pedidosPorCocinero);
    }
}
