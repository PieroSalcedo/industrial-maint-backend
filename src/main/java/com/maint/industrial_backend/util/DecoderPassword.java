package com.maint.industrial_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DecoderPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "juana2026";
        String encodedPassword = "$2a$10$rb//TcPBuUO5W83wgD9nPO.pr66aRQK5mwl7i44pN9ZZsBoR8MiNa";

        if (passwordEncoder.matches(password, encodedPassword)) {
            System.out.println("¡La clave coincide!");
        } else {
            System.out.println("La clave no coincide.");
        }
    }
}