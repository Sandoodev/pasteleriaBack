package com.pasteleriaBack.pasteleriaBack.controller;

import com.pasteleriaBack.pasteleriaBack.dto.Ingreso;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoMasSolicitado;
import com.pasteleriaBack.pasteleriaBack.dto.PedidoPorCocinero;
import com.pasteleriaBack.pasteleriaBack.dto.ReporteResponse;
import com.pasteleriaBack.pasteleriaBack.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    //REQUERIMIENTO 11: AL PARECER FUNCIONA
    @CrossOrigin
    @GetMapping
    public ReporteResponse obtenerReportes(@RequestParam(required = false) String periodo) {
        LocalDate fechaInicio = LocalDate.now().minusMonths(1); // Por defecto, un mes atrás
        LocalDate fechaFin = LocalDate.now();

        // Aquí puedes agregar lógica para manejar el parámetro "periodo" y ajustar las fechas

        List<Ingreso> ingresos = reporteService.generarReporteIngresos(fechaInicio, fechaFin);
        List<ProductoMasSolicitado> productosMasSolicitados = reporteService.generarReporteProductosMasSolicitados(fechaInicio, fechaFin);
        List<PedidoPorCocinero> pedidosPorCocinero = reporteService.generarReportePedidosPorCocinero(fechaInicio, fechaFin);

        return new ReporteResponse(ingresos, productosMasSolicitados, pedidosPorCocinero);
    }

    //POR SI QUIERO PONER DE X FECHA A X FECHA Y NO UN RANGO DETERMINADO
//    @CrossOrigin
//    @GetMapping
//    public ReporteResponse obtenerReportes(
//            @RequestParam(required = false) LocalDate fechaInicio,
//            @RequestParam(required = false) LocalDate fechaFin) {
//
//        // Si no se proporciona fechaInicio, se establece un mes atrás
//        if (fechaInicio == null) {
//            fechaInicio = LocalDate.now().minusMonths(1).withDayOfMonth(1); // Primer día del mes anterior
//        }
//
//        // Si no se proporciona fechaFin, se establece la fecha actual
//        if (fechaFin == null) {
//            fechaFin = LocalDate.now(); // Fecha actual
//        }
//
//        List<Ingreso> ingresos = reporteService.generarReporteIngresos(fechaInicio, fechaFin);
//        List<ProductoMasSolicitado> productosMasSolicitados = reporteService.generarReporteProductosMasSolicitados(fechaInicio, fechaFin);
//        List<PedidoPorCocinero> pedidosPorCocinero = reporteService.generarReportePedidosPorCocinero(fechaInicio, fechaFin);
//
//        return new ReporteResponse(ingresos, productosMasSolicitados, pedidosPorCocinero);
//    }
}
