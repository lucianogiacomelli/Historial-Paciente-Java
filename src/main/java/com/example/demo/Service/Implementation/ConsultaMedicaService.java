package com.example.demo.Service.Implementation;

import com.example.demo.Entities.ConsultaMedica;
import com.example.demo.Entities.EstadoTurno.EstadoTurno;
import com.example.demo.Entities.Paciente;
import com.example.demo.Entities.Tratamiento;
import com.example.demo.Entities.Turno;
import com.example.demo.Exception.ResourceInvalidException;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repository.ConsultaMedicaRepository;
import com.example.demo.Repository.TurnoRepository;
import com.example.demo.Service.Interface.IConsultaMedicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaMedicaService implements IConsultaMedicaService {
    private ConsultaMedicaRepository consultaMedicaRepository;
    private PacienteService pacienteService;
    private TurnoRepository turnoRepository;

    public ConsultaMedicaService (ConsultaMedicaRepository consultaMedicaRepository, PacienteService pacienteService,
                                  TurnoRepository turnoRepository){
        this.consultaMedicaRepository = consultaMedicaRepository;
        this.pacienteService = pacienteService;
        this.turnoRepository = turnoRepository;
    }

    @Override
    public List<ConsultaMedica> getHistorialPaciente (Long pacienteId){
        Paciente paciente = pacienteService.getPacienteById(pacienteId);

        if(!paciente.getEstado()){
            throw new ResourceInvalidException("El paciente con id:{"+paciente.getId()+"} " +
                    "y DNI:{"+paciente.getDni()+"} se encuentra dado de baja en el sistema.");
        }
        List <Turno> turnos = turnoRepository.obtenerHistorialTurnos(paciente.getId(), EstadoTurno.ATENDIDO);

        List<ConsultaMedica> consultas = turnos.stream().map(Turno::getConsulta).filter(Objects::nonNull).toList();

        return consultas;

    }

    @Override
    public ConsultaMedica getConsulta(Long idConsulta){
        Optional<ConsultaMedica> consultaMedicaOpt = consultaMedicaRepository.findById(idConsulta);

        if(consultaMedicaOpt.isEmpty()){
            throw new ResourceNotFoundException("La consulta con id:"+idConsulta+" no existe.");
        }
        return consultaMedicaOpt.get();
    }
}
