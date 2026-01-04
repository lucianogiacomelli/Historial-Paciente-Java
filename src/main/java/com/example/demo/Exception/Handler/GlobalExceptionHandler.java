package com.example.demo.Exception.Handler;

import com.example.demo.Exception.Disponibilidad.*;
import com.example.demo.Exception.Especialidad.*;
import com.example.demo.Exception.Medico.*;
import com.example.demo.Exception.Auth0OperationException;
import com.example.demo.Exception.ResourceAlreadyExistsException;
import com.example.demo.Exception.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* ======================================================
       DISPONIBILIDAD
       ====================================================== */

    @ExceptionHandler(DisponibilidadDuplicadaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadDuplicada(DisponibilidadDuplicadaException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DisponibilidadSuperpuestaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadSuperpuesta(DisponibilidadSuperpuestaException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DisponibilidadInvalidaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadInvalida(DisponibilidadInvalidaException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DisponibilidadVaciaException.class)
    public ResponseEntity<ApiError> handleDisponibilidadVacia(DisponibilidadVaciaException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /* ======================================================
       ESPECIALIDAD
       ====================================================== */

    @ExceptionHandler(EspecialidadDuplicadaException.class)
    public ResponseEntity<ApiError> handleEspecialidadDuplicada(EspecialidadDuplicadaException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EspecialidadInvalidaException.class)
    public ResponseEntity<ApiError> handleEspecialidadInvalida(EspecialidadInvalidaException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EspecialidadNotFoundException.class)
    public ResponseEntity<ApiError> handleEspecialidadNotFound(EspecialidadNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /* ======================================================
       MÉDICO / RECURSOS / AUTH0
       ====================================================== */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(Auth0OperationException.class)
    public ResponseEntity<ApiError> handleAuth0Error(Auth0OperationException ex) {
        return buildError(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    /* ======================================================
       VALIDACIONES DTO (@Valid)
       ====================================================== */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errores.put(error.getField(), error.getDefaultMessage())
                );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", errores);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /* ======================================================
       FALLBACK GENERAL
       ====================================================== */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor"
        );
    }

    /* ======================================================
       MÉTODO COMÚN
       ====================================================== */

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message) {
        ApiError error = new ApiError(
                status.value(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }
}
