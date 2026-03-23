package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Medico")
@Entity
public class Medico extends Base{
    private String nombre;
    private String apellido;
    @Column(unique = true, nullable = false)
    private String matricula;
    private String telefono;
    private String email;
    @Column(unique = true, nullable = false)
    private String dni;
    @Column(name = "auth0_user_id", unique = true, nullable = false)
    private String auth0Id;
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DisponibilidadMedico> disponibilidad = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "medico_especialidad",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    private List<Especialidad> especialidades = new ArrayList<>();

}
