package com.example.demo.Exception.Handler;

import com.example.demo.Exception.Disponibilidad.*;
import com.example.demo.Exception.Especialidad.*;
import com.example.demo.Exception.ResourceInvalidException;
import com.example.demo.Exception.Medico.*;
import com.example.demo.Exception.Auth0OperationException;
import com.example.demo.Exception.ResourceAlreadyExistsException;
import com.example.demo.Exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /* ======================================================
       DISPONIBILIDAD
       ====================================================== */

    @ExceptionHandler(DisponibilidadDuplicadaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadDuplicada(
            DisponibilidadDuplicadaException ex, WebRequest request) {
        log.warn("Disponibilidad duplicada: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "Conflicto de disponibilidad",
                ex.getMessage(), request);
    }

    @ExceptionHandler(DisponibilidadSuperpuestaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadSuperpuesta(
            DisponibilidadSuperpuestaException ex, WebRequest request) {
        log.warn("Disponibilidad superpuesta: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Horarios superpuestos",
                ex.getMessage(), request);
    }

    @ExceptionHandler(DisponibilidadInvalidaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadInvalida(
            DisponibilidadInvalidaException ex, WebRequest request) {
        log.warn("Disponibilidad inválida: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Disponibilidad inválida",
                ex.getMessage(), request);
    }

    @ExceptionHandler(DisponibilidadVaciaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadVacia(
            DisponibilidadVaciaException ex, WebRequest request) {
        log.warn("Disponibilidad vacía: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Disponibilidad vacía",
                ex.getMessage(), request);
    }

    /* ======================================================
       ESPECIALIDAD
       ====================================================== */

    @ExceptionHandler(EspecialidadDuplicadaException.class)
    public ResponseEntity<ApiError> handleEspecialidadDuplicada(
            EspecialidadDuplicadaException ex, WebRequest request) {
        log.warn("Especialidad duplicada: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "Especialidad duplicada",
                ex.getMessage(), request);
    }

    @ExceptionHandler(EspecialidadInvalidaException.class)
    public ResponseEntity<ApiError> handleEspecialidadInvalida(
            EspecialidadInvalidaException ex, WebRequest request) {
        log.warn("Especialidad inválida: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Especialidad inválida",
                ex.getMessage(), request);
    }

    @ExceptionHandler(EspecialidadNotFoundException.class)
    public ResponseEntity<ApiError> handleEspecialidadNotFound(
            EspecialidadNotFoundException ex, WebRequest request) {
        log.warn("Especialidad no encontrada: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Especialidad no encontrada",
                ex.getMessage(), request);
    }


    /* ======================================================
   MÉDICO
   ====================================================== */

    @ExceptionHandler(MedicoNotFoundException.class)
    public ResponseEntity<ApiError> handleMedicoNotFound(
            MedicoNotFoundException ex, WebRequest request) {
        log.warn("Médico no encontrado: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Médico no encontrado",
                ex.getMessage(), request);
    }

    @ExceptionHandler(MedicoDuplicadoException.class)
    public ResponseEntity<ApiError> handleMedicoDuplicado(
            MedicoDuplicadoException ex, WebRequest request) {
        log.warn("Médico duplicado: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "Médico duplicado",
                ex.getMessage(), request);
    }

    @ExceptionHandler(MedicoInvalidoException.class)
    public ResponseEntity<ApiError> handleMedicoInvalido(
            MedicoInvalidoException ex, WebRequest request) {
        log.warn("Médico inválido: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Médico inválido",
                ex.getMessage(), request);
    }


    /* ======================================================
       RECURSOS / AUTH0
       ====================================================== */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Recurso no encontrado",
                ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceInvalidException.class)
    public ResponseEntity<ApiError> handleInvalidResource(
            ResourceInvalidException ex, WebRequest request) {
        log.warn("Recurso inválido: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Recurso inválido",
                ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex, WebRequest request) {
        log.warn("Recurso ya existe: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "Recurso duplicado",
                ex.getMessage(), request);
    }

    @ExceptionHandler(Auth0OperationException.class)
    public ResponseEntity<ApiError> handleAuth0Error(
            Auth0OperationException ex, WebRequest request) {
        log.error("Error en operación Auth0: {}", ex.getMessage(), ex);
        return buildError(HttpStatus.BAD_GATEWAY, "Error de autenticación",
                "No se pudo completar la operación de autenticación", request);
    }

    /* ======================================================
       VALIDACIONES DTO (@Valid)
       ====================================================== */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Errores de validación: {}", ex.getBindingResult().getFieldErrors().size());

        String errores = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Errores de validación",
                "Los datos enviados no cumplen con las validaciones requeridas",
                LocalDateTime.now(),
                getPath(request),
                errores
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /* ======================================================
       FALLBACK GENERAL
       ====================================================== */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex, WebRequest request) {
        // Log completo del error para debugging
        log.error("Error inesperado: ", ex);

        // Respuesta genérica para el cliente (sin detalles sensibles)
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ha ocurrido un error inesperado. Por favor contacte al administrador.",
                request
        );
    }

    /* ======================================================
       MÉTODOS AUXILIARES
       ====================================================== */

    private ResponseEntity<ApiError> buildError(
            HttpStatus status, String error, String message, WebRequest request) {
        ApiError apiError = new ApiError(
                status.value(),
                error,
                message,
                LocalDateTime.now(),
                getPath(request),
                null
        );
        return new ResponseEntity<>(apiError, status);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
