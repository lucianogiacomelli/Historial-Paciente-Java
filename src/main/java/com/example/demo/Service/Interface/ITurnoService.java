package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.ConsultaMedicaDTO;
import com.example.demo.DTOs.Request.TurnoDTO;
import com.example.demo.Entities.ConsultaMedica;
import com.example.demo.Entities.Turno;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITurnoService {
  Turno altaTurno(TurnoDTO turnoDTO);

  List<Turno> getAllTurnos();

  List<Turno> getTurnosActivos();

  ConsultaMedica altaConsulta(ConsultaMedicaDTO consultaMedicaDTO, Long turnoId);

  Turno getTurnoById(Long turnoId);

  Turno confirmarTurno(Long turnoId);

  Turno cancelarTurno(Long turnoId);

  Turno atenderTurno(Long turnoId);

}
