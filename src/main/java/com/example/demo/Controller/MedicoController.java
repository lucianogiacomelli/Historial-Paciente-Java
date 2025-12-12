package com.example.demo.Controller;


import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.DTOs.Response.MedicoResponseDTO;
import com.example.demo.Entities.Medico;
import com.example.demo.Mapper.MedicoMapper;
import com.example.demo.Service.IMedicoService;
import jakarta.validation.Valid;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MedicoController {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

    @Autowired
    private IMedicoService medicoService;

    //======================================
    // GETTERS
    //======================================

    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }


    @PreAuthorize("hasAuthority('administrador')")
    @GetMapping("/admin/get-all-medicos")
    public ResponseEntity<?> getAllMedicos(){
        try{
            List<Medico> medicos = medicoService.getAllMedicos();
            List<MedicoResponseDTO> response = medicos.stream()
                    .map(MedicoMapper::toDTO)
                    .toList();
            logger.info("Se obtuvieron {} medicos", medicos.size());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado en obtención de todos los médicos: "+ e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('administrador')")
    @GetMapping("/admin/get-medico/{id}")
    public ResponseEntity<?> getMedicoById(@PathVariable Long id) {
        try {
            Medico medico = medicoService.getMedicoById(id);
            MedicoResponseDTO response = MedicoMapper.toDTO(medico);
            logger.info("Medico econtrado con id ={" + id + "}");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al querer encontrar el médico con id = {" + id + "} " + e.getMessage());
        }
    }


    //======================================
    // POST
    //======================================

    @PreAuthorize("hasAuthority('administrador')")
    @PostMapping("/admin/alta-medico")
    public ResponseEntity<?> altaMedico(@Valid @RequestBody MedicoDTO medicoDTO) throws Exception{
        try{
            Medico medico = medicoService.altaMedico(medicoDTO);
            MedicoResponseDTO response = MedicoMapper.toDTO(medico);
            logger.info("Medico creado: id={}", medico.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado en creación de medico: "+ e.getMessage());
        }
    }


    //======================================
    // PUT
    //======================================
    @PreAuthorize("hasAuthority('administrador')")
    @PutMapping("/admin/modificar-medico/{id}")
    public ResponseEntity<?> modificarMedico(@Valid @RequestBody UpdateMedicoDTO medicoDTO, @PathVariable Long id) {
        try {
            Medico medicoModificado = medicoService.modificarMedico(id, medicoDTO);
            MedicoResponseDTO response = MedicoMapper.toDTO(medicoModificado);
            logger.info("Medico modificado con id ={" + id + "}");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al querer modificar el médico con id = {" + id + "} " + e.getMessage());
        }
    }

    //======================================
    // DELETE
    //======================================
    @PreAuthorize("hasAuthority('administrador')")
    @DeleteMapping("/admin/baja-medico/{id}")
    public ResponseEntity<?> bajaMedico (@PathVariable Long id) {
        try{
            medicoService.bajaMedico(id);
            logger.info("Medico eliminado con id ={" + id + "}");
            MedicoResponseDTO response = MedicoMapper.toDTO((medicoService.getMedicoById(id)));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al querer eliminar el médico con id = {" + id + "} " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('administrador')")
    @PutMapping("/admin/habilitar-medico/{id}")
    public ResponseEntity<?> habilitarMedico (@PathVariable Long id) {
        try {
            medicoService.habilitarMedico(id);
            logger.info("Medico habilitado con id ={" + id + "}");
            MedicoResponseDTO response = MedicoMapper.toDTO(medicoService.getMedicoById(id));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al querer habilitar al médico con id = {" + id + "} " + e.getMessage());
        }
    }










}
