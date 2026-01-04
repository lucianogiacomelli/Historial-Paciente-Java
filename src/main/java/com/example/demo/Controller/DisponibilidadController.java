package com.example.demo.Controller;


import com.example.demo.DTOs.Request.DisponibilidadDTO;
import com.example.demo.Entities.DisponibilidadMedico;
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
    @PostMapping("/alta-disponibilidad/{idMedico}")
    public ResponseEntity<?> altaDisponibilidad(@Valid @RequestBody List<DisponibilidadDTO> disponibilidadDTO, @PathVariable Long idMedico) {
        List<DisponibilidadMedico> disponibilidadMedico =
                disponibilidadService.altaDisponibilidad(disponibilidadDTO, idMedico);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(disponibilidadMedico);
    }




}
