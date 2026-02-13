package com.example.demo.Service.Implementation;

import com.example.demo.Configuration.Auth0.Auth0Properties;
import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Entities.EstadoTurno.EstadoTurno;
import com.example.demo.Entities.Medico;
import com.example.demo.Entities.Turno;
import com.example.demo.Exception.Auth0OperationException;
import com.example.demo.Exception.Especialidad.EspecialidadNotFoundException;
import com.example.demo.Exception.Medico.MedicoDuplicadoException;
import com.example.demo.Exception.Medico.MedicoInvalidoException;
import com.example.demo.Exception.Medico.MedicoNotFoundException;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Repository.MedicoRepository;
import com.example.demo.Repository.TurnoRepository;
import com.example.demo.Service.Interface.IMedicoService;
import com.example.demo.Service.auth0.Auth0Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService implements IMedicoService {

    private MedicoRepository medicoRepository;
    private TurnoRepository turnoRepository;
    private Auth0Service auth0Service;
    private Auth0Properties auth0Properties;
    private EspecialidadRepository especialidadRepository;


    public MedicoService(MedicoRepository medicoRepository,
                         TurnoRepository turnoRepository,
                         Auth0Service auth0Service,
                         Auth0Properties auth0Properties,
                         EspecialidadRepository especialidadRepository) {
        this.turnoRepository = turnoRepository;
        this.medicoRepository = medicoRepository;
        this.auth0Service = auth0Service;
        this.especialidadRepository = especialidadRepository;
        this.auth0Properties = auth0Properties;
    }


    //======================================
    // OBTENCIONES
    //======================================
    @Override
    public List<Medico> getAllMedicos() {
        List<Medico> medicos = medicoRepository.findAll();
        return medicos;
    }

    @Override
    public Medico getMedicoById(Long id){
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if(medicoOptional.isEmpty()){
            throw new MedicoNotFoundException(id);
        }
        return medicoOptional.get();
    }

    @Override
    public List<Turno> getTurnosByMedico(Long medicoId){
        Medico medico = getMedicoById(medicoId);
        if(!medico.getEstado()){
            throw new MedicoInvalidoException("El médico con ID:{"+medico.getId()+"} se encuentra dado de baja.");
        }

        List <Turno> turnos = turnoRepository.obtenerTurnosActivos(medicoId, List.of(EstadoTurno.CONFIRMADO, EstadoTurno.PENDIENTE));
        if(turnos.isEmpty()){
            throw new ResourceNotFoundException("El médico "+medico.getNombre()+" "+medico.getApellido()+" no tiene turnos en estado Pendiente ni Confirmado");
        }
        return turnos;
    }

    //======================================
    // ABM MEDICO
    //======================================
    @Override
    public Medico altaMedico(MedicoDTO medicoDTO) {

        Optional<Medico> medicoOptional =
                medicoRepository.findByMatriculaAndDni(
                        medicoDTO.getMatricula(), medicoDTO.getDni());

        if (medicoOptional.isPresent()) {
            throw new MedicoDuplicadoException(
                    "El medico ya se encuentra registrado en el sistema");
        }

        List<Especialidad> especialidades =
                validarEspecialidades(medicoDTO.getEspecialidadDTOList());

        // 🔥 Auth0 ANTES o DESPUÉS, pero FUERA de @Transactional
        String auth0Id = auth0Service.crearUsuario(
                medicoDTO.getEmail(), medicoDTO.getPassword());

        auth0Service.asignarRolAUsuario(
                auth0Id, auth0Properties.getRoleMedico());

        return guardarMedico(medicoDTO, auth0Id, especialidades);
    }

    @Transactional
    public Medico guardarMedico(
            MedicoDTO dto,
            String auth0Id,
            List<Especialidad> especialidades) {

        Medico medico = new Medico();
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setTelefono(dto.getTelefono());
        medico.setDni(dto.getDni());
        medico.setMatricula(dto.getMatricula());
        medico.setEmail(dto.getEmail());
        medico.setAuth0Id(auth0Id);
        medico.setEspecialidades(especialidades);

        return medicoRepository.save(medico);
    }

    @Transactional
    @Override
    public Medico modificarMedico(Long id, UpdateMedicoDTO medicoDTO) {
        Medico medico = getMedicoById(id);
        if(medico.getEstado() == false) {
            throw new MedicoInvalidoException("No se puede modificar un médico dado de baja.");
        }
        if (medicoDTO.getNombre() != null) {
            medico.setNombre(medicoDTO.getNombre());
        }
        if (medicoDTO.getApellido() != null) {
            medico.setApellido(medicoDTO.getApellido());
        }
        if (medicoDTO.getTelefono() != null) {
            medico.setTelefono(medicoDTO.getTelefono());
        }
        //Se podría mejorar con un MapStruct, pero quiero dejar toda la lógica aca y no depender de librerías externas
        if (medicoDTO.getEspecialidadDTOList() != null
                && !medicoDTO.getEspecialidadDTOList().isEmpty()) {
            List<Especialidad> especialidades =
                    validarEspecialidades(medicoDTO.getEspecialidadDTOList());
            medico.setEspecialidades(especialidades);
        }

        return medicoRepository.save(medico);
    }

                //======================================
                // MANEJO DE CUENTA Y ESTADO DE MEDICO
                //======================================
    @Override
    @Transactional
    public boolean bajaMedico(Long id)  {
        Medico medico = getMedicoById(id);
        if (medico.getEstado() == false) {
            throw new MedicoInvalidoException("El médico ya se encuentra dado de baja.");
        }
        try {
            auth0Service.eliminarUsuario(medico.getAuth0Id());
        } catch (Exception e) {
            throw new Auth0OperationException("No se pudo eliminar al médico en Auth0.", e);
        }
        medico.setEstado(false);
        medico.setFechaBaja(LocalDateTime.now());
        medicoRepository.save(medico);
        return true;

    }

    @Override
    @Transactional
    public boolean habilitarMedico(Long id) {
        Medico medico = getMedicoById(id);
        if (medico.getEstado() == true) {
            throw new MedicoInvalidoException("El médico ya se encuentra dado de alta.");
        }
        try {
            auth0Service.habilitarUsuario(medico.getAuth0Id());
        } catch (Exception e) {
            throw new Auth0OperationException("No se pudo habilitar al médico en Auth0.", e);
        }
        medico.setEstado(true);
        medico.setFechaBaja(null);
        medicoRepository.save(medico);
        return true;
    }

    //======================================
    // MÉTODOS AUXILIARES
    //======================================

    private List <Especialidad> validarEspecialidades(List <Long> especialidadesIds)  {
        List <Especialidad> especialidadList = new ArrayList<>();
       for (Long id : especialidadesIds) {
           Especialidad especialidad = especialidadRepository.findById(id)
                   .orElseThrow(() -> new EspecialidadNotFoundException("La especialidad con id: " + id + " no existe"));
           especialidadList.add(especialidad);
       }
        return especialidadList;
    }




}
