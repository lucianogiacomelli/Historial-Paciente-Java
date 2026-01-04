package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.Entities.Medico;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMedicoService {
    List<Medico> getAllMedicos();
    Medico getMedicoById(Long id);

    Medico altaMedico(MedicoDTO medicoDTO);
    Medico modificarMedico(Long id, UpdateMedicoDTO medicoDTO);

    boolean bajaMedico (Long id) ;
    boolean habilitarMedico(Long id) ;

}
