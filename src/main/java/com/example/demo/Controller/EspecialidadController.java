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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private static final Logger logger = LoggerFactory.getLogger(EspecialidadController.class);

    @Autowired
    private IEspecialidadService especialidadService;

    @PreAuthorize("hasAuthority('administrador')")
    @PostMapping("/admin/alta-especialidades")
    public ResponseEntity<?> altaEspecialidad(@Valid @RequestBody EspecialidadDTO especialidadDTO){
            Especialidad especialidad = especialidadService.altaEspecialidad(especialidadDTO);
            EspecialidadResponseDTO response = EspecialidadMapper.toDTO(especialidad);
            logger.info("Especialidad creada: id={}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('administrador')")
    @PutMapping("/admin/alta-especialidades/{idEspecialidad}")
    public ResponseEntity<?> altaEspecialidadDadaDeBaja(@PathVariable Long idEspecialidad){
        Especialidad especialidad = especialidadService.altaEspecialidadDadaDeBaja(idEspecialidad);
        EspecialidadResponseDTO response = EspecialidadMapper.toDTO(especialidad);
        logger.info("Especialidad dada de alta nuevamente: id={}", response.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAuthority('administrador')")
    @DeleteMapping("/admin/baja-especialidades/{idEspecialidad}")
    public ResponseEntity<?> bajaEspecialidad(@PathVariable Long idEspecialidad){
        especialidadService.bajaEspecialidad(idEspecialidad);
        logger.info("Especialidad eliminada: id={}", idEspecialidad);
        return ResponseEntity.status(HttpStatus.OK).body("Especialidad eliminada correctamente");
    }

    @PreAuthorize("hasAuthority('administrador')")
    @GetMapping("/admin/especialidades-activas")
    public ResponseEntity<?> getEspecialidadesActivas(){
        List<Especialidad> especialidades = especialidadService.getEspecialidadesActivas();
        List<EspecialidadResponseDTO> especialidadResponseDTOS = especialidades.stream().map(EspecialidadMapper::toDTO).toList();
        logger.info("Se obtuvieron {} especialidades", especialidades.size());

        return ResponseEntity.status(HttpStatus.OK
        ).body(especialidadResponseDTOS);
    }

    @PreAuthorize("hasAuthority('administrador')")
    @GetMapping("/admin/especialidades")
    public ResponseEntity<?> getEspecialidades(){
        List<Especialidad> especialidades = especialidadService.getAllEspecialidades();
        List<EspecialidadResponseDTO> especialidadResponseDTOS = especialidades.stream().map(EspecialidadMapper::toDTO).toList();
        logger.info("Se obtuvieron {} especialidades", especialidades.size());

        return ResponseEntity.status(HttpStatus.OK
        ).body(especialidadResponseDTOS);
    }






}
