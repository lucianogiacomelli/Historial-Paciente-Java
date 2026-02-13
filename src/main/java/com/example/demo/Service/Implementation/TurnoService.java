package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.TurnoDTO;
import com.example.demo.Entities.*;
import com.example.demo.Entities.EstadoTurno.EstadoTurno;
import com.example.demo.Entities.EstadoTurno.TurnoEstado;
import com.example.demo.Exception.Especialidad.EspecialidadInvalidaException;
import com.example.demo.Exception.Medico.MedicoInvalidoException;
import com.example.demo.Exception.ResourceInvalidException;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repository.*;
import com.example.demo.Service.Interface.ITurnoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
public class TurnoService implements ITurnoService {

    private TurnoRepository turnoRepository;
    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;
    private EspecialidadRepository especialidadRepository;
    private DisponibilidadRepository disponibilidadRepository;


    public TurnoService (TurnoRepository turnoRepository, MedicoRepository medicoRepository
            ,PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository
            , DisponibilidadRepository disponibilidadRepository){
        this.turnoRepository = turnoRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    @Override
    @Transactional
    public Turno altaTurno(TurnoDTO turnoDTO) {
        Optional<Medico> medicoOpt = medicoRepository.findById(turnoDTO.getMedicoId());
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(turnoDTO.getPacienteId());
        Optional<Especialidad> especialidadOpt = especialidadRepository.findById(turnoDTO.getEspecialidadId());
        if (medicoOpt.isEmpty() && pacienteOpt.isEmpty()){
            throw new ResourceNotFoundException("No se encontro el medico o el paciente,");
        }
        if(especialidadOpt.isEmpty()){
            throw new EspecialidadInvalidaException("No se encontró la especialidad enviada al sistema.");
        }

        Medico medico = medicoOpt.get();
        Paciente paciente = pacienteOpt.get();
        Especialidad especialidad = especialidadOpt.get();

        if(medico.getEstado() == false){
            throw new MedicoInvalidoException("El medico con id: " + medico.getId() + " se encuentra inhabilitado o dado de baja.");
        }

        if(paciente.getEstado() == false){
            throw new ResourceInvalidException("El paciente con id: " + paciente.getId() + " se encuentra inhabilitado o dado de baja.");
        }


        if(!medico.getEspecialidades().contains(especialidad)){
            throw new ResourceInvalidException("La especialidad elegida para el médico: "+medico.getNombre()+medico.getApellido()+" no la tiene como especialidad cargada a su entidad.");
        }

        Integer duracion = especialidad.getDuracionConsulta();
        LocalTime horarioInicio = turnoDTO.getHoraInicio();
        LocalTime horarioFin = horarioInicio.plusMinutes(duracion);


        Dias diaSemana = Dias.from(turnoDTO.getFecha().getDayOfWeek());

        DisponibilidadMedico disponibilidad = disponibilidadRepository
                .findByMedicoAndDiaSemana(medico, diaSemana)
                .orElseThrow(() -> new ResourceInvalidException("El médico no atiende ese día"));

        if (horarioInicio.isBefore(disponibilidad.getHoraInicio()) ||
                horarioFin.isAfter(disponibilidad.getHoraFin())) {
            throw new ResourceInvalidException("Horario fuera de la disponibilidad del médico");
        }

        boolean existeSuperposicion =  turnoRepository
                .existeTurnoSolapado(
                        medico.getId(),
                        turnoDTO.getFecha(),
                        horarioInicio,
                        horarioFin
                );

        if (existeSuperposicion) {
            throw new ResourceInvalidException("El horario ya está ocupado");
        }

        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setMedico(medico);
        turno.setFecha(turnoDTO.getFecha());
        turno.setHorarioInicio(horarioInicio);
        turno.setHorarioFin(horarioFin);
        turno.setEspecialidad(especialidad);
        pendienteEstadoTurno(turno);
        return turnoRepository.save(turno);
    }

    @Override
    public List<Turno> getAllTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos;
    }

    @Override
    public List<Turno> getTurnosActivos(){
        List <Turno> turnos = turnoRepository.getAllTurnosByEstadoTrue();
        return turnos;
    }

    @Override
    public Turno getTurnoById(Long turnoId){
        Turno turno = turnoRepository.getById(turnoId);
        return turno;
    }



    @Override
    @Transactional
    public Turno confirmarTurno(Long turnoId){
        Optional<Turno> turnoOpt = turnoRepository.findById(turnoId);
        if(turnoOpt.isEmpty()){
            throw new ResourceNotFoundException("El turno con id:"+turnoId+" no existe");
        }
        Turno turno = turnoOpt.get();
        if(turno.getEstado() != true){
            throw new ResourceInvalidException("El turno con id: "+turno.getId()+" se encuentra dado de baja o estado cancelado.");
        }
        confirmarEstadoTurno(turno);
        return turnoRepository.save(turno);
    }

    @Override
    @Transactional
    public Turno cancelarTurno(Long turnoId){
        Optional<Turno> turnoOpt = turnoRepository.findById(turnoId);
        if(turnoOpt.isEmpty()){
            throw new ResourceNotFoundException("El turno con id:"+turnoId+" no existe");
        }
        Turno turno = turnoOpt.get();
        if(turno.getEstado() != true){
            throw new ResourceInvalidException("El turno con id: "+turno.getId()+" se encuentra dado de baja o estado cancelado.");
        }
        canceladoEstadoTurno(turno);
        turno.setEstado(false);
        turno.setFechaBaja(LocalDateTime.now());
        return turnoRepository.save(turno);
    }

    @Override
    @Transactional
    public Turno atenderTurno(Long turnoId){
        Optional<Turno> turnoOpt = turnoRepository.findById(turnoId);
        if(turnoOpt.isEmpty()){
            throw new ResourceNotFoundException("El turno con id:"+turnoId+" no existe");
        }
        Turno turno = turnoOpt.get();
        if(turno.getEstado() != true){
            throw new ResourceInvalidException("El turno con id: "+turno.getId()+" se encuentra dado de baja o estado cancelado.");
        }
        atendidoEstadoTurno(turno);
        return turnoRepository.save(turno);
    }

    // ----------------
    // Auxiliares
    // ----------------

    public TurnoEstado altaEstadoTurno(Turno turno, EstadoTurno estadoTurno) {
        TurnoEstado turnoEstado = new TurnoEstado();
        turnoEstado.setEstadoTurno(estadoTurno);
        turnoEstado.setFecha(LocalDateTime.now());
        turnoEstado.setTurno(turno);
        turno.getEstadoList().add(turnoEstado);
        turno.setEstadoTurno(estadoTurno);
        return turnoEstado;
    }

    public void confirmarEstadoTurno(Turno turno){

        if(turno.getEstadoActual() != EstadoTurno.PENDIENTE){
            throw new ResourceInvalidException("El estado del turno para confirmarse debe ser Pendiente. Estado actual:"+ turno.getEstadoActual());
        }
        TurnoEstado turnoConfirmado = altaEstadoTurno(turno, EstadoTurno.CONFIRMADO);
    }

    public void pendienteEstadoTurno(Turno turno){
        int tamaño = turno.getEstadoList().size();
        if(tamaño != 0){
            throw new ResourceInvalidException("El estado del turno para estar en estado Pendiente no se le debe haber asignado otro estado");
        }
        TurnoEstado turnoPendiente = altaEstadoTurno(turno, EstadoTurno.PENDIENTE);
    }

    public void canceladoEstadoTurno(Turno turno){
        if(turno.getEstadoActual() != EstadoTurno.PENDIENTE){
            throw new ResourceInvalidException("El estado del turno para cancelarse debe ser Pendiente. Estado actual:"+ turno.getEstadoActual());
        }
        TurnoEstado turnoCancelado = altaEstadoTurno(turno, EstadoTurno.CANCELADO);
    }

    public void atendidoEstadoTurno(Turno turno){
        if(turno.getEstadoActual() != EstadoTurno.CONFIRMADO){
            throw new ResourceInvalidException("El estado del turno para quedar en estado Atendido debe ser Confirmado. Estado actual:"+ turno.getEstadoActual());
        }
        TurnoEstado turnoConfirmado = altaEstadoTurno(turno, EstadoTurno.ATENDIDO);
    }





}
