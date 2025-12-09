package com.example.demo.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MedicoController {


    @GetMapping("/api/public/ping")
    public String ping() {
        return "pong";
    }

}
