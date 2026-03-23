package com.example.demo.Controller;


import com.example.demo.DTOs.Request.DisponibilidadDTO;
import com.example.demo.DTOs.Response.DisponibilidadResponseDTO;
import com.example.demo.Entities.DisponibilidadMedico;
import com.example.demo.Mapper.DisponibilidadMapper;
import com.example.demo.Service.Implementation.DisponibilidadService;
import com.example.demo.Service.Interface.IDisponibilidadService;
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
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {
    @Autowired
    private IDisponibilidadService disponibilidadService;

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadController.class);

    @PreAuthorize("hasAnyAuthority('administrador','medico','recepcionista')")
    @PostMapping("/{idMedico}/alta-disponibilidad")
    public ResponseEntity<?> altaDisponibilidad(@Valid @RequestBody List<DisponibilidadDTO> disponibilidadDTO, @PathVariable Long idMedico) {
        List<DisponibilidadMedico> disponibilidadMedico =
                disponibilidadService.altaDisponibilidad(disponibilidadDTO, idMedico);
        List<DisponibilidadResponseDTO> responseDTOS = disponibilidadMedico.stream().map(DisponibilidadMapper::toDTO).toList();
        logger.info("Disponibilidades creadas para el médico con id={}", idMedico);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTOS);
    }

    @PreAuthorize("hasAnyAuthority('administrador','medico','recepcionista')")
    @PutMapping("/{idMedico}/modificar-disponibilidad/{idDisponibilidad}")
    public ResponseEntity<?> modificarDisponibilidad(@Valid @RequestBody DisponibilidadDTO disponibilidadDTO, @PathVariable Long idMedico, @PathVariable Long idDisponibilidad) {
        DisponibilidadMedico disponibilidadMedico =
                disponibilidadService.modificarDisponibilidad(disponibilidadDTO, idMedico, idDisponibilidad);
        DisponibilidadResponseDTO responseDTOS = DisponibilidadMapper.toDTO(disponibilidadMedico);
        logger.info("Disponibilidad modificada para el médico con id={}", idMedico);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTOS);
    }

    @PreAuthorize("hasAnyAuthority('administrador','medico','recepcionista')")
    @DeleteMapping("/{idMedico}/baja-disponibilidad/{idDisponibilidad}")
    public ResponseEntity<?> bajaDisponibilidad(@PathVariable Long idDisponibilidad, @PathVariable Long idMedico) {
        disponibilidadService.bajaDisponibilidad(idDisponibilidad, idMedico);
        logger.info("Disponibilidad eliminada con id={} para el médico con id={}", idDisponibilidad, idMedico);
        return ResponseEntity.status(HttpStatus.OK).body("Disponibilidad eliminada correctamente");
    }

    @PreAuthorize("hasAnyAuthority('administrador','medico','recepcionista')")
    @GetMapping("/{idMedico}/get-disponibilidad")
    public ResponseEntity<?> getDisponibilidad(@PathVariable Long idMedico) {
        List <DisponibilidadMedico> disponibilidadMedico = disponibilidadService.getDisponibilidadesByMedicoId(idMedico);
        List<DisponibilidadResponseDTO> responseDTOS = disponibilidadMedico.stream().map(DisponibilidadMapper::toDTO).toList();
        logger.info("Disponibilidades encontradas para el médico con id={}", idMedico);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTOS);
    }





}
