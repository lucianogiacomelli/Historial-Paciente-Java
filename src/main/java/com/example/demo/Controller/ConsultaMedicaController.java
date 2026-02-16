package com.example.demo.Controller;

import com.example.demo.DTOs.Response.ConsultaMedicaResponseDTO;
import com.example.demo.Entities.ConsultaMedica;
import com.example.demo.Mapper.ConsultaMedicaMapper;
import com.example.demo.Service.Interface.IConsultaMedicaService;
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
@RequestMapping("api/consultas")
public class ConsultaMedicaController {
    @Autowired
    private IConsultaMedicaService consultaMedicaService;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaMedicaController.class);

    @PreAuthorize("hasAnyAuthority('administrador','medico')")
    @PostMapping("/{idPaciente}/get-historial")
    public ResponseEntity<?> getHistorialPaciente (@PathVariable Long idPaciente) {
        List<ConsultaMedica> consultaMedicas =
                consultaMedicaService.getHistorialPaciente(idPaciente);
        List<ConsultaMedicaResponseDTO> responseDTOS = consultaMedicas.stream().map(ConsultaMedicaMapper::toDTO).toList();
        logger.info("Se han encontrado {} cantidad de consultas para el paciente con id = {}", responseDTOS.size(), idPaciente);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTOS);
    }

    @PreAuthorize("hasAnyAuthority('administrador','medico')")
    @PostMapping("/get-consulta/{idConsulta}")
    public ResponseEntity<?> getConsulta (@PathVariable Long idConsulta) {
        ConsultaMedica consultaMedica = consultaMedicaService.getConsulta(idConsulta);
        ConsultaMedicaResponseDTO responseDTO = ConsultaMedicaMapper.toDTO(consultaMedica);
        logger.info("Se ha encontrado la consulta con id {} para el paciente con id = {}", idConsulta, consultaMedica.getTurno().getPaciente().getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }


}
