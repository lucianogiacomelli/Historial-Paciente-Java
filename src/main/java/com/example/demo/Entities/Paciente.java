package com.example.demo.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Paciente")
public class Paciente extends Base{


    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private String genero;
    private String numero;
    private String email;
    private String direccion;


}
