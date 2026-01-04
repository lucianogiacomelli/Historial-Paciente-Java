package com.example.demo.Controller;


import com.example.demo.DTOs.Request.TurnoDTO;

import com.example.demo.Entities.Turno;
import com.example.demo.Service.Interface.ITurnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private static final Logger logger = LoggerFactory.getLogger(TurnoController.class);

    @Autowired
    private ITurnoService turnoService;

    @PreAuthorize("hasAuthority('recepcionista')")
    @PostMapping("/alta-turno")
    public ResponseEntity<?> altaTurno(TurnoDTO turnoDTO){
        try{
            Turno turno = turnoService.altaTurno(turnoDTO);
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al querer dar de alta un turno" + e.getMessage());
        }

    }



}
