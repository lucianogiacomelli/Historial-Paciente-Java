package com.example.demo.Controller;


import com.example.demo.DTOs.Request.ConsultaMedicaDTO;
import com.example.demo.DTOs.Request.TurnoDTO;

import com.example.demo.DTOs.Response.ConsultaMedicaResponseDTO;
import com.example.demo.DTOs.Response.TurnoResponseDTO;
import com.example.demo.Entities.ConsultaMedica;
import com.example.demo.Entities.Turno;
import com.example.demo.Mapper.ConsultaMedicaMapper;
import com.example.demo.Mapper.TurnoMapper;
import com.example.demo.Service.Interface.ITurnoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private static final Logger logger = LoggerFactory.getLogger(TurnoController.class);

    @Autowired
    private ITurnoService turnoService;

    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @PostMapping("/alta-turno")
    public ResponseEntity<?> altaTurno(@Valid @RequestBody TurnoDTO turnoDTO){
        Turno turno = turnoService.altaTurno(turnoDTO);
        TurnoResponseDTO response = TurnoMapper.toResponse(turno);
        logger.info("Se dio de alta al turno con id: {}", turno.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('medico','administrador')")
    @PostMapping("/alta-consulta/{turnoId}")
    public ResponseEntity<?> altaConsulta(@Valid @RequestBody ConsultaMedicaDTO ConsultaMedicaDTO, @PathVariable Long turnoId){
        ConsultaMedica consulta = turnoService.altaConsulta(ConsultaMedicaDTO, turnoId);
        ConsultaMedicaResponseDTO response = ConsultaMedicaMapper.toDTO(consulta);
        logger.info("Se dio de alta la consulta con id: {}", consulta.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @GetMapping("/get-all-turno")
    public ResponseEntity<?> getAllTurnos(){
        List<Turno> turno = turnoService.getAllTurnos();
        List<TurnoResponseDTO> response = turno.stream().map(TurnoMapper :: toResponse).toList();
        logger.info("Se han encontrado : {} de cantidad de turnos.", response.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @GetMapping("/get-all-turno-activos")
    public ResponseEntity<?> getAllTurnosActivos(){
        List<Turno> turno = turnoService.getTurnosActivos();
        List<TurnoResponseDTO> response = turno.stream().map(TurnoMapper :: toResponse).toList();
        logger.info("Se han encontrado : {} de cantidad de turnos activos.", response.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @GetMapping("/get-turno/{turnoId}")
    public ResponseEntity<?> getTurnoById(@PathVariable Long turnoId){
        Turno turno = turnoService.getTurnoById(turnoId);
        TurnoResponseDTO response = TurnoMapper.toResponse(turno);
        logger.info("Se ha el turno con id: {} .", turno.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @PutMapping("/confirmar-turno/{turnoId}")
    public ResponseEntity<?> confirmarTurno(@PathVariable Long turnoId){
        Turno turno = turnoService.confirmarTurno(turnoId);
        TurnoResponseDTO response = TurnoMapper.toResponse(turno);
        logger.info("Se confirmó el turno con id: {}", turno.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @PutMapping("/cancelar-turno/{turnoId}")
    public ResponseEntity<?> cancelarTurno(@PathVariable Long turnoId){
        Turno turno = turnoService.cancelarTurno(turnoId);
        TurnoResponseDTO response = TurnoMapper.toResponse(turno);
        logger.info("Se canceló el turno con id: {}", turno.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('recepcionista','administrador')")
    @PutMapping("/atender-turno/{turnoId}")
    public ResponseEntity<?> atenderTurno(@PathVariable Long turnoId){
        Turno turno = turnoService.atenderTurno(turnoId);
        TurnoResponseDTO response = TurnoMapper.toResponse(turno);
        logger.info("Se atendió el turno con id: {}", turno.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }







}
