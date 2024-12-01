package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.dto.*;
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

    //REQUERIMIENTO 12
    @GetMapping("/listarPedidosPorCocinero/{dniCocinero}")
    public ResponseEntity<List<PedidoPorCocineroDTO>> listarPedidosPorCocinero(
            @PathVariable Integer dniCocinero,
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {

        // Convertir las fechas de String a LocalDate
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);

        // Llamar al servicio para obtener los pedidos
        List<PedidoPorCocineroDTO> pedidos = reporteService.listarPedidosPorCocinero(dniCocinero, inicio, fin);

        // Retornar la respuesta
        return ResponseEntity.ok(pedidos);
    }

    //REQUERIMIENTO 13
    @GetMapping("/{dniCocinero}")
    public ResponseEntity<List<ComisionPorCocineroDTO>> obtenerComisionesPorCocinero(
            @PathVariable Integer dniCocinero,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {

        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);

        List<ComisionPorCocineroDTO> comisiones = reporteService.listarComisionesPorCocinero(dniCocinero, inicio, fin);
        return ResponseEntity.ok(comisiones);
    }
}
