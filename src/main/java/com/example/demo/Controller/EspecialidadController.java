package com.example.demo.Controller;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.DTOs.Response.EspecialidadResponseDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Mapper.EspecialidadMapper;
import com.example.demo.Service.Interface.IEspecialidadService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EspecialidadController {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

    @Autowired
    private IEspecialidadService especialidadService;

    @PreAuthorize("hasAuthority('administrador')")
    @PostMapping("/admin/alta-especialidades")
    public ResponseEntity<?> altaEspecialidad(@Valid @RequestBody EspecialidadDTO especialidadDTO) throws Exception{
            Especialidad especialidad = especialidadService.altaEspecialidad(especialidadDTO);
            EspecialidadResponseDTO response = EspecialidadMapper.toDTO(especialidad);
            logger.info("Especialidad creada: id={}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
