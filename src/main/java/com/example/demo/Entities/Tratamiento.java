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

    private String descripcion;       // Qué es
    private String dosis;             // Cantidad
    private String frecuencia;        // Cada cuánto
    private Integer duracionDias;     // Cuántos días
    private String indicaciones;      // Observaciones adicionales

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private ConsultaMedica consulta;
}
