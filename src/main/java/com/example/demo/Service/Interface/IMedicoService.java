package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.Entities.Medico;
import com.example.demo.Entities.Turno;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMedicoService {
    List<Medico> getAllMedicos();
    Medico getMedicoById(Long medicoId);
    List<Turno> getTurnosByMedico(Long medicoId);

    Medico altaMedico(MedicoDTO medicoDTO);
    Medico modificarMedico(Long medicoId, UpdateMedicoDTO medicoDTO);

    boolean bajaMedico (Long medicoId) ;
    boolean habilitarMedico(Long medicoId) ;

}
