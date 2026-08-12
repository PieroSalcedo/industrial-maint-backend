package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.dto.JwtResponseDTO;
import com.maint.industrial_backend.dto.LoginRequestDTO;
import com.maint.industrial_backend.security.JwtProvider;
import com.maint.industrial_backend.security.UsuarioPrincipal;
import com.maint.industrial_backend.util.AppSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CommonsLog
@Tag(name = "01. Seguridad", description = "Endpoints para autenticación y gestión de sesiones")
@RestController
@RequestMapping("/url/auth")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Operation(summary = "Iniciar Sesión", description = "Autentica al usuario y devuelve un Token JWT junto con sus permisos y rutas de menú.")
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        log.info(">>> login >>> Intentando autenticar: " + loginDTO.login());

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtProvider.generateToken(auth);
        UsuarioPrincipal user = (UsuarioPrincipal) auth.getPrincipal();

        List<String> roles = user.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDTO(
                token, "Bearer", user.getUsername(), user.getNombreCompleto(), roles, user.getOpciones()
        ));
    }
}