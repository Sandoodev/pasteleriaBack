package com.pasteleriaBack.pasteleriaBack.service;

import com.pasteleriaBack.pasteleriaBack.model.HorarioAperturaCierre;
import com.pasteleriaBack.pasteleriaBack.repository.HorarioAperturaCierreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioAperturaCierreService {
    @Autowired
    private HorarioAperturaCierreRepository horarioAperturaCierreRepository;

    public HorarioAperturaCierre crearHorarioAperturaCierre(HorarioAperturaCierre horarioAperturaCierre) {
        // Lógica para crear horario de apertura y cierre
        return horarioAperturaCierreRepository.save(horarioAperturaCierre);
    }

    public List<HorarioAperturaCierre> listarHorariosAperturaCierre() {
        return horarioAperturaCierreRepository.findAll();
    }

    // Otros métodos según los requerimientos
}
