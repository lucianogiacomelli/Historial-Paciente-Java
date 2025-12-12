package com.example.demo.Controller;


import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Response.MedicoResponseDTO;
import com.example.demo.Entities.Medico;
import com.example.demo.Mapper.MedicoMapper;
import com.example.demo.Service.IMedicoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MedicoController {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

    @Autowired
    private IMedicoService medicoService;

    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }

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

}
