package com.example.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tratamiento")
public class Tratamiento extends Base {

    private String tratamiento;
    private String dosis;
    private String frecuencia;
    private Integer duracionDias;
    private String instrucciones;

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    private ConsultaMedica consulta;
}
