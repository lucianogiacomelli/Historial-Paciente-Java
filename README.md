# Historial Médico - Proyecto Java Spring Boot

## Descripción
Este proyecto es un **MVP de gestión de historial médico**, desarrollado en **Java con Spring Boot** y PostgreSQL. Permite manejar pacientes, médicos, especialidades, consultas, tratamientos y turnos con baja lógica y control de estados.

---

## Tecnologías utilizadas
- Java 17
- Spring Boot 4
- Spring Data JPA
- PostgreSQL 17
- Lombok
- Gradle
- Docker
- PgAdmin (para gestión de la base de datos)

---

## Estructura del proyecto

### Entidades principales

#### Base
Clase abstracta que heredan todas las entidades. Contiene:
- `id` (Long, PK)
- `estado` (Boolean, default `true`)
- `fechaAlta` (LocalDateTime, asignada automáticamente al crear)
- `fechaModificacion` (LocalDateTime, asignada automáticamente al actualizar)
- `fechaBaja` (LocalDateTime, asignada al dar de baja un registro)

Se manejan callbacks de JPA `@PrePersist` y `@PreUpdate` para controlar fechas automáticamente.

---

#### Paciente
- `nombre`
- `apellido`
- `dni`
- `fechaNacimiento`
- `genero`
- `numero`
- `email`
- `direccion`

#### Medico
- `nombre`
- `apellido`
- `matricula`
- `telefono`
- `email`
- `dni`
- Relación **ManyToMany** con `Especialidad`

#### Especialidad
- `nombre`

#### ConsultaMedica
- `fechaConsulta`
- `motivo`
- `sintomas`
- `diagnostico`
- `observaciones`
- Relación **ManyToOne** con `Paciente` y `Medico`
- Relación **OneToMany** con `Tratamiento`

#### Tratamiento
- `tratamiento`
- `dosis`
- `frecuencia`
- `duracionDias`
- `instrucciones`
- Relación **ManyToOne** con `ConsultaMedica`

#### Turno
- `horario`
- `motivo`
- Relación **ManyToOne** con `Paciente` y `Medico`
- Relación **OneToOne** con `ConsultaMedica`
- Relación **OneToMany** con `TurnoEstado` (histórico de estados del turno)

#### TurnoEstado
- `fecha` (LocalDateTime)
- `estadoTurno` (Enum: `PENDIENTE`, `CONFIRMADO`, `CANCELADO`, `ATENDIDO`)
- Relación **ManyToOne** con `Turno`

---

## Flujo de baja lógica
- Se utiliza el campo `estado` y `fechaBaja` para implementar baja lógica (soft delete)
- En la capa Service se actualiza:
```java
entidad.setEstado(false);
entidad.setFechaBaja(LocalDateTime.now());
repository.save(entidad);
