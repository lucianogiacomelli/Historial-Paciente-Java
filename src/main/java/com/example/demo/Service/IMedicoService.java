package com.example.demo.Service;

import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.Entities.Medico;
import org.springframework.stereotype.Service;

@Service
public interface IMedicoService {
    Medico altaMedico(MedicoDTO medicoDTO) throws Exception;
    Medico modificarMedico(MedicoDTO medicoDTO) throws Exception;
    boolean bajaMedico (Long id) throws Exception;

}
