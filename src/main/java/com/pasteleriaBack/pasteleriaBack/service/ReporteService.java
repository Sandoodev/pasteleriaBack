package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.dto.Ingreso;
import com.pasteleriaBack.pasteleriaBack.dto.ProductoMasSolicitado;
import com.pasteleriaBack.pasteleriaBack.dto.PedidoPorCocinero;
import com.pasteleriaBack.pasteleriaBack.dto.ReporteResponse;
import com.pasteleriaBack.pasteleriaBack.model.EstadoPedidoENUM;
import com.pasteleriaBack.pasteleriaBack.model.Pedido;
import com.pasteleriaBack.pasteleriaBack.model.PedidoProducto;
import com.pasteleriaBack.pasteleriaBack.repository.PedidoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.ProductoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public ReporteResponse generarReporte(LocalDate fechaInicio, LocalDate fechaFin) {
        Double ingresos = generarReporteIngresos(fechaInicio, fechaFin);
        List<ProductoMasSolicitado> productosMasSolicitados = generarReporteProductosMasSolicitados(fechaInicio, fechaFin);
        List<PedidoPorCocinero> pedidosPorCocinero = generarReportePedidosPorCocinero(fechaInicio, fechaFin);

        return new ReporteResponse(ingresos, productosMasSolicitados, pedidosPorCocinero);
    }

    //retorna la suma total de ingresos
    public Double generarReporteIngresos(LocalDate fechaInicio, LocalDate fechaFin) {
        // Convertir LocalDate a Timestamp
        Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fechaFin.atStartOfDay());

        // Definir los estados que queremos considerar
        List<EstadoPedidoENUM> estados = List.of(
                EstadoPedidoENUM.enviado,
                EstadoPedidoENUM.retirado,
                EstadoPedidoENUM.pendienteDeEnvio,
                EstadoPedidoENUM.pendienteDeRetiro
        );

        // Obtener todos los pedidos en el rango de fechas y con los estados especificados
        List<Pedido> pedidos = pedidoRepository.findByPedFechaDeCreacionBetweenAndPedEstadoIn(inicio, fin, estados);

        // Calcular ingresos
        Double totalIngresos = 0.0;
        for (Pedido pedido : pedidos) {
            for (PedidoProducto pedidoProducto : pedido.getPedidoProductos()) {
                Double ingreso = pedidoProducto.getPrecioVenta() * pedidoProducto.getCantidad();
                totalIngresos += ingreso;
            }
        }

        return totalIngresos;
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

        // Definir los estados que queremos considerar
        List<EstadoPedidoENUM> estados = List.of(
                EstadoPedidoENUM.enviado,
                EstadoPedidoENUM.retirado,
                EstadoPedidoENUM.pendienteDeEnvio,
                EstadoPedidoENUM.pendienteDeRetiro
        );

        // Obtener todos los pedidos en el rango de fechas y con los estados especificados
        List<Pedido> pedidos = pedidoRepository.findByFechaBetweenAndEstado(inicio, fin, estados);

        // Agrupar pedidos por cocinero
        Map<String, List<Pedido>> pedidosPorCocinero = pedidos.stream()
                .collect(Collectors.groupingBy(pedido -> pedido.getEmpleado().getEmp_apellidoNombre()));

        // Convertir el mapa a una lista de PedidoPorCocinero
        List<PedidoPorCocinero> reporte = new ArrayList<>();
        for (Map.Entry<String, List<Pedido>> entry : pedidosPorCocinero.entrySet()) {
            reporte.add(new PedidoPorCocinero(entry.getKey(), entry.getValue()));
        }

        return reporte;
    }
}