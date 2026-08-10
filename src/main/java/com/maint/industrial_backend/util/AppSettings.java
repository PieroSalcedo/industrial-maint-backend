package com.maint.industrial_backend.util;

public class AppSettings {

    // Origen permitido para CORS (Tu Angular)
    public static final String URL_CROSS_ORIGIN = "http://localhost:4200";

    // IDs de los Catálogos (Deben coincidir con la base de datos)
    public static final int CATALOGO_TIPO_ACTIVO = 1;
    public static final int CATALOGO_PRIORIDAD = 2;
    public static final int CATALOGO_ESTADO_TICKET = 3;

    // Estados lógicos
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
}
