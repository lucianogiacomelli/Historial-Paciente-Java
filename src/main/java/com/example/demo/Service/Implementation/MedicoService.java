package com.example.demo.Service.Implementation;

import com.example.demo.Configuration.Auth0.Auth0Properties;
import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.DTOs.Request.UpdateMedicoDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Entities.Medico;
import com.example.demo.Exception.Auth0OperationException;
import com.example.demo.Exception.Especialidad.EspecialidadNotFoundException;
import com.example.demo.Exception.Medico.MedicoDuplicadoException;
import com.example.demo.Exception.Medico.MedicoNotFoundException;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Repository.MedicoRepository;
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
    private Auth0Service auth0Service;
    private Auth0Properties auth0Properties;
    private EspecialidadRepository especialidadRepository;


    public MedicoService(MedicoRepository medicoRepository,
                         Auth0Service auth0Service,
                         Auth0Properties auth0Properties,
                         EspecialidadRepository especialidadRepository) {
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

    //======================================
    // ABM MEDICO
    //======================================
    @Transactional
    @Override
    public Medico altaMedico(MedicoDTO medicoDTO) {
       Optional<Medico> medicoOptional = medicoRepository.findByMatriculaAndDni(medicoDTO.getMatricula(), medicoDTO.getDni());
        if(medicoOptional.isPresent()){
            throw new MedicoDuplicadoException("El medico ya se encuentra registrado en el sistema");
        }
        List<Especialidad> especialidades = validarEspecialidades(medicoDTO.getEspecialidadDTOList());

        String auth0_id = auth0Service.crearUsuario(medicoDTO.getEmail(), medicoDTO.getPassword());
        auth0Service.asignarRolAUsuario(auth0_id, auth0Properties.getRoleMedico());
        Medico medico = new Medico();
        medico.setNombre(medicoDTO.getNombre());
        medico.setApellido(medicoDTO.getApellido());
        medico.setDni(medicoDTO.getDni());
        medico.setMatricula(medicoDTO.getMatricula());
        medico.setEmail(medicoDTO.getEmail());
        medico.setAuth0Id(auth0_id);
        medico.setEspecialidades(especialidades);
        return medicoRepository.save(medico);
    }

    @Override
    public Medico modificarMedico(Long id, UpdateMedicoDTO medicoDTO) {
        Medico medico = getMedicoById(id);
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
        if (medicoDTO.getEspecialidadDTOList().size() > 0){
            List <Especialidad> especialidades = validarEspecialidades(medicoDTO.getEspecialidadDTOList());
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
        try {
            auth0Service.eliminarUsuario(medico.getAuth0Id());
        } catch (Exception e) {
            throw new Auth0OperationException("No se pudo eliminar al médico en Auth0: " + e.getMessage());
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
        try {
            auth0Service.habilitarUsuario(medico.getAuth0Id());
        } catch (Exception e) {
            throw new Auth0OperationException("No se pudo habilitar al médico en Auth0: " + e.getMessage());
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
