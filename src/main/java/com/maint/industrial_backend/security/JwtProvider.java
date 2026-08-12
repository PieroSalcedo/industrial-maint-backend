package com.maint.industrial_backend.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:36000}")
    private int expiration;

    // Método auxiliar para obtener la clave firmada correctamente en bytes
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication){
        logger.info(">>> Generando Token JWT...");
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        return generateTokenWithClaims(usuarioPrincipal);
    }

    public String generateTokenWithClaims(UsuarioPrincipal usuarioPrincipal){
        Map<String, Object> claims = new HashMap<>();
        claims.put("idUsuario", usuarioPrincipal.getIdUsuario());
        claims.put("nombreCompleto", usuarioPrincipal.getNombreCompleto());
        claims.put("login", usuarioPrincipal.getLogin());

        List<String> rolesNombres = usuarioPrincipal.getAuthorities().stream()
                .map(auth -> auth.getAuthority()).collect(Collectors.toList());
        claims.put("roles", rolesNombres);

        if (usuarioPrincipal.getOpciones() != null) {
            claims.put("opciones", usuarioPrincipal.getOpciones());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuarioPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getNombreUsuarioFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Error al validar Token: " + e.getMessage());
            return false;
        }
    }
}