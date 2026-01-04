package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.TurnoDTO;
import com.example.demo.Entities.Turno;
import org.springframework.stereotype.Service;

@Service
public interface ITurnoService {
    Turno altaTurno(TurnoDTO turnoDTO) throws Exception;

}
