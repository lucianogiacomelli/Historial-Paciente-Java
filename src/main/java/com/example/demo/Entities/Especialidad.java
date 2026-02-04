package com.example.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Especialidad")
public class Especialidad extends Base{

    private String nombre;
    private Integer duracionConsulta; // Duración en minutos

}
