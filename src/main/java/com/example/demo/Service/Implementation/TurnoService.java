package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.TurnoDTO;
import com.example.demo.Entities.Medico;
import com.example.demo.Entities.Paciente;
import com.example.demo.Entities.Turno;
import com.example.demo.Repository.MedicoRepository;
import com.example.demo.Repository.PacienteRepository;
import com.example.demo.Repository.TurnoRepository;
import com.example.demo.Service.Interface.ITurnoService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TurnoService implements ITurnoService {

    private TurnoRepository turnoRepository;
    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;


    public TurnoService (TurnoRepository turnoRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository){
        this.turnoRepository = turnoRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Turno altaTurno(TurnoDTO turnoDTO) throws Exception {
        Optional<Medico> medicoOpt = medicoRepository.findById(turnoDTO.getIdMedico());
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(turnoDTO.getIdPaciente());
        if (medicoOpt.isEmpty() && pacienteOpt.isEmpty()){
            throw new Exception("No se encontro el medico o el paciente");
        }
        Medico medico = medicoOpt.get();
        Paciente paciente = pacienteOpt.get();





        return null;
    }



}
