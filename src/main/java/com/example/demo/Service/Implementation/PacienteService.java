package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.PacienteDTO;
import com.example.demo.DTOs.Request.UpdatePacienteDTO;
import com.example.demo.Entities.Paciente;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Exception.ResourceAlreadyExistsException;
import com.example.demo.Repository.PacienteRepository;
import com.example.demo.Service.Interface.IPacienteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {


    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository){
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente altaPaciente(PacienteDTO pacienteDTO) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findByDni(pacienteDTO.getDni());
        if(pacienteOptional.isPresent()){
            throw new ResourceAlreadyExistsException("El paciente con dni: " + pacienteDTO.getDni() + " ya existe");
        }
        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setDni(pacienteDTO.getDni());
        paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        paciente.setGenero(pacienteDTO.getGenero());
        paciente.setNumero(pacienteDTO.getNumero());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setDireccion(pacienteDTO.getDireccion());
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente modificarPaciente(UpdatePacienteDTO pacienteDTO, Long id) {
        Optional <Paciente> pacienteOptional = pacienteRepository.findById(id);
        if(pacienteOptional.isEmpty()) {
            throw new ResourceNotFoundException("El paciente con id: " + id + " no existe");
        }

        Paciente paciente = pacienteOptional.get();
        if (pacienteDTO.getNombre() != null) {
            paciente.setNombre(pacienteDTO.getNombre());
        }
        if (pacienteDTO.getApellido() != null) {
            paciente.setApellido(pacienteDTO.getApellido());
        }
        if (pacienteDTO.getGenero() != null) {
            paciente.setGenero(pacienteDTO.getGenero());
        }
        if (pacienteDTO.getNumero() != null) {
            paciente.setNumero(pacienteDTO.getNumero());
        }
        if (pacienteDTO.getEmail() != null) {
            paciente.setEmail(pacienteDTO.getEmail());
        }
        if (pacienteDTO.getDireccion() != null) {
            paciente.setDireccion(pacienteDTO.getDireccion());
        }
        return pacienteRepository.save(paciente);

    }

    @Override
    public void bajaPaciente(Long id) {
        Optional <Paciente> pacienteOptional = pacienteRepository.findById(id);
        if(pacienteOptional.isEmpty()){
            throw new ResourceNotFoundException("El paciente con id: {"+id+"} no existe");
        }
        Paciente paciente = pacienteOptional.get();
        paciente.setEstado(false);
        paciente.setFechaBaja(LocalDateTime.now());
        pacienteRepository.save(paciente);
    }

    @Override
    public Paciente habilitarPaciente(Long id)  {
        Optional <Paciente> pacienteOptional = pacienteRepository.findById(id);
        if(pacienteOptional.isEmpty()){
            throw new ResourceNotFoundException("El paciente con id: {"+id+"} no existe");
        }
        Paciente paciente = pacienteOptional.get();
        paciente.setEstado(true);
        paciente.setFechaBaja(null);
        return pacienteRepository.save(paciente);
    }
}
