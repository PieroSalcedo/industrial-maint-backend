package com.maint.industrial_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Cambiamos UsuarioSeguridadServiceImpl por la interfaz genérica
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuración de CORS Profesional
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))

                // 2. Desactivar CSRF para APIs Stateless
                .csrf(csrf -> csrf.disable())

                // 3. Manejo de errores de autenticación
                .exceptionHandling(exp -> exp.authenticationEntryPoint(jwtEntryPoint))

                // 4. Gestión de sesión sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. REGLAS DE AUTORIZACIÓN (orden: más específico primero)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/url/auth/**").permitAll()

                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()

                        // Catálogos: requieren JWT (sin datos sensibles)
                        .requestMatchers(
                                "/url/util/listaTipoActivo",
                                "/url/util/listaPrioridad",
                                "/url/util/listaEstadoTicket"
                        ).authenticated()

                        // Técnicos con datos personales: solo supervisor
                        .requestMatchers("/url/util/listaTecnico").hasAuthority("ROLE_ADMIN")

                        // Activos: escritura solo supervisor
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/url/activo/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/url/activo/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/url/activo/**").hasAuthority("ROLE_ADMIN")

                        // Tickets: registrar y eliminar solo supervisor
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/url/ticket/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/url/ticket/registraTicket").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                );

        // 6. Inyectar Provider y Filtro JWT
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}