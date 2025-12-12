package com.example.demo.DTOs.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth0UserDTO {

    private String user_id;
    private String email;
    private Boolean email_verified;
    private String name;

}