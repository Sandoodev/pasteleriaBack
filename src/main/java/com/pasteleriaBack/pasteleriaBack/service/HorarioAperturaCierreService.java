package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.Auditoria;
import com.pasteleriaBack.pasteleriaBack.model.Empleado;
import com.pasteleriaBack.pasteleriaBack.model.HorarioAperturaCierre;
import com.pasteleriaBack.pasteleriaBack.repository.AuditoriaRepository;
import com.pasteleriaBack.pasteleriaBack.repository.EmpleadoRepository;
import com.pasteleriaBack.pasteleriaBack.repository.HorarioAperturaCierreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HorarioAperturaCierreService {
    @Autowired
    private HorarioAperturaCierreRepository horarioAperturaCierreRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public HorarioAperturaCierre crearHorarioAperturaCierre(HorarioAperturaCierre horarioAperturaCierre) {
        // Lógica para crear horario de apertura y cierre
        return horarioAperturaCierreRepository.save(horarioAperturaCierre);
    }

    public List<HorarioAperturaCierre> listarHorariosAperturaCierre() {
        return horarioAperturaCierreRepository.findAll();
    }

    //REQUERIMIENTO 14: editar horario de apertura y cierre
    public HorarioAperturaCierre editarHorarioAperturaCierre(Integer autorDni, Time aperturaManana, Time cierreManana, Time aperturaTarde, Time cierreTarde) {
        // Buscar el horario existente   NOTA: se asume que solo hay un horario de apertura y cierre
        int id = 1;
        HorarioAperturaCierre horarioExistente = horarioAperturaCierreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));

        // Buscar el empleado autor por DNI
        Empleado autor = empleadoRepository.findById(autorDni)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con DNI: " + autorDni));

        // Registrar los cambios en la auditoría
        Auditoria auditoria = new Auditoria();
        auditoria.setAud_operacion("Actualización de horarios");
        auditoria.setAud_detalle("Apertura mañana: " + aperturaManana + ", Cierre mañana: " + cierreManana +
                ", Apertura tarde: " + aperturaTarde + ", Cierre tarde: " + cierreTarde);
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setAutor(autor); // Asigna el autor de la operación
        auditoriaRepository.save(auditoria);

        // Actualizar los horarios
        horarioExistente.setHac_manana_apertura(aperturaManana);
        horarioExistente.setHac_manana_cierre(cierreManana);
        horarioExistente.setHac_tarde_apertura(aperturaTarde);
        horarioExistente.setHac_tarde_cierre(cierreTarde);

        // Guardar los cambios en la base de datos
        return horarioAperturaCierreRepository.save(horarioExistente);
    }
    // Otros métodos según los requerimientos
}
