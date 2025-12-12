package com.example.demo.Service.auth0;

import com.example.demo.DTOs.Request.Auth0UserDTO;

public interface IAuth0Service {
    String crearUsuario(String email, String password);

    void asignarRolAUsuario(String auth0UserId, String roleId);

    Auth0UserDTO obtenerUsuarioPorId(String auth0UserId);

    void eliminarUsuario(String auth0UserId) throws Exception;

    void habilitarUsuario(String auth0UserId) throws Exception;
}
