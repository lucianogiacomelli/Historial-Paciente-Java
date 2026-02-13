package com.example.demo.Controller;


import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.DTOs.Response.MedicoResponseDTO;
import com.example.demo.DTOs.Response.TurnoResponseDTO;
import com.example.demo.Entities.Medico;
import com.example.demo.Entities.Turno;
import com.example.demo.Mapper.MedicoMapper;
import com.example.demo.Mapper.TurnoMapper;
import com.example.demo.Service.Interface.IMedicoService;
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
@RequestMapping("/api/medicos")
public class MedicoController {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

    @Autowired
    private IMedicoService medicoService;

    //======================================
    // GETTERS
    //======================================



    @PreAuthorize("hasAuthority('administrador')")
    @GetMapping("/admin/get-all-medicos")
    public ResponseEntity<?> getAllMedicos(){
            List<Medico> medicos = medicoService.getAllMedicos();
            List<MedicoResponseDTO> response = medicos.stream()
                    .map(MedicoMapper::toDTO)
                    .toList();
            logger.info("Se obtuvieron {} medicos", medicos.size());
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAuthority('administrador')")
    @GetMapping("/admin/get-medico/{medicoId}")
    public ResponseEntity<?> getMedicoById(@PathVariable Long medicoId) {
            Medico medico = medicoService.getMedicoById(medicoId);
            MedicoResponseDTO response = MedicoMapper.toDTO(medico);
            logger.info("Medico encontrado con id ={" + medicoId + "}");
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('administrador','medico')")
    @GetMapping("/admin/get-turnos-by-medico/{medicoId}")
    public ResponseEntity<?> getTurnosActivosByMedico(@PathVariable Long medicoId) {
         List<Turno> turnos = medicoService.getTurnosByMedico(medicoId);
        List<TurnoResponseDTO> response = turnos.stream()
                .map(TurnoMapper::toResponse).toList();
        logger.info("Se han encontrado " + response.size() + " turnos para el médico con id: "+medicoId );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    //======================================
    // POST
    //======================================

    @PreAuthorize("hasAuthority('administrador')")
    @PostMapping("/admin/alta-medico")
    public ResponseEntity<?> altaMedico(@Valid @RequestBody MedicoDTO medicoDTO) throws Exception{
            Medico medico = medicoService.altaMedico(medicoDTO);
            MedicoResponseDTO response = MedicoMapper.toDTO(medico);
            logger.info("Medico creado: id={}", medico.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);


    }


    //======================================
    // PUT
    //======================================
    @PreAuthorize("hasAuthority('administrador')")
    @PutMapping("/admin/modificar-medico/{id}")
    public ResponseEntity<?> modificarMedico(@Valid @RequestBody UpdateMedicoDTO medicoDTO, @PathVariable Long id) {
            Medico medicoModificado = medicoService.modificarMedico(id, medicoDTO);
            MedicoResponseDTO response = MedicoMapper.toDTO(medicoModificado);
            logger.info("Medico modificado con id ={" + id + "}");
            return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    //======================================
    // DELETE
    //======================================
    @PreAuthorize("hasAuthority('administrador')")
    @DeleteMapping("/admin/baja-medico/{id}")
    public ResponseEntity<?> bajaMedico (@PathVariable Long id) {
            medicoService.bajaMedico(id);
            logger.info("Medico eliminado con id ={" + id + "}");
            MedicoResponseDTO response = MedicoMapper.toDTO((medicoService.getMedicoById(id)));
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAuthority('administrador')")
    @PutMapping("/admin/habilitar-medico/{id}")
    public ResponseEntity<?> habilitarMedico (@PathVariable Long id) {
            medicoService.habilitarMedico(id);
            logger.info("Medico habilitado con id ={" + id + "}");
            MedicoResponseDTO response = MedicoMapper.toDTO(medicoService.getMedicoById(id));
            return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
