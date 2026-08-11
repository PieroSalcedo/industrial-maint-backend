package com.maint.industrial_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Herramienta para generar el hash
 * de la contraseña antes de insertarlo en el script SQL.
 */
public class EncoderPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String[] passwords = {"admin2026", "tecnico2026"};

        for (String password : passwords) {
            System.out.println("Clave original: " + password);
            System.out.println("Clave cifrada para SQL: " + encoder.encode(password));
            System.out.println("---");
        }
    }
}