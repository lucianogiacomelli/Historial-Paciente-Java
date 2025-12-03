package com.example.demo.Entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass //Para JPA ponga los atributos en la bd para hijos
public abstract class Base implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default true")
    private Boolean estado = true;

    private LocalDateTime fechaAlta;
    private LocalDateTime fechaModificacion;
    private LocalDateTime fechaBaja;

    @PrePersist
    public void prePersist() {
        fechaAlta = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }



}
