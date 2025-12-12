package com.example.demo.Service;

import com.example.demo.Configuration.Auth0Properties;
import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Entities.Medico;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Repository.MedicoRepository;
import com.example.demo.Service.auth0.Auth0Service;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService implements IMedicoService{

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


    @Override
    public Medico altaMedico(MedicoDTO medicoDTO) throws Exception {
       Optional<Medico> medicoOptional = medicoRepository.findByMatriculaAndDni(medicoDTO.getMatricula(), medicoDTO.getDni());
        if(medicoOptional.isPresent()){
            throw new Exception("El medico ya se encuentra registrado en el sistema");
        }
        List<Especialidad> especialidades =
                especialidadRepository.findAllById(medicoDTO.getEspecialidadDTOList());

        if (especialidades.size() != medicoDTO.getEspecialidadDTOList().size()) {
            throw new Exception("Alguna de las especialidades enviadas no existe");
        }
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
    public Medico modificarMedico(MedicoDTO medicoDTO) throws Exception {
        return null;
    }

    @Override
    public boolean bajaMedico(Long id) throws Exception {
        return false;
    }
}
