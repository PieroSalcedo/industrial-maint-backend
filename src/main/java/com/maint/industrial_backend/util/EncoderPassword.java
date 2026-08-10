package com.maint.industrial_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Herramienta para generar el hash
 * de la contraseña antes de insertarlo en el script SQL.
 */
public class EncoderPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "tecnico2026";
        String encodedPassword = encoder.encode(password);

        System.out.println("Clave original: " + password);
        System.out.println("Clave cifrada para SQL: " + encodedPassword);
    }
}