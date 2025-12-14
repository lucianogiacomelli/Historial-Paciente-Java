package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.Entities.Medico;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMedicoService {
    List<Medico> getAllMedicos();
    Medico getMedicoById(Long id) throws Exception;

    Medico altaMedico(MedicoDTO medicoDTO) throws Exception;
    Medico modificarMedico(Long id, UpdateMedicoDTO medicoDTO) throws Exception;

    boolean bajaMedico (Long id) throws Exception;
    boolean habilitarMedico(Long id) throws Exception;

}
