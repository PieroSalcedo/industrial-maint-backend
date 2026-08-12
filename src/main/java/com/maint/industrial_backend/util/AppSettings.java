package com.maint.industrial_backend.util;

public class AppSettings {

    // Origen permitido para CORS (Tu Angular)
    public static final String URL_CROSS_ORIGIN = "http://localhost:4200";

    // IDs de los Catálogos (Deben coincidir con la base de datos)
    public static final int CATALOGO_TIPO_ACTIVO = 1;
    public static final int CATALOGO_PRIORIDAD = 2;
    public static final int CATALOGO_ESTADO_TICKET = 3;

    // Estados del activo (disponibilidad operativa)
    public static final int ACTIVO_OPERATIVO = 1;
    public static final int ACTIVO_FUERA_SERVICIO = 0;

    /** @deprecated usar {@link #ACTIVO_OPERATIVO} */
    public static final int ACTIVO = ACTIVO_OPERATIVO;
    /** @deprecated usar {@link #ACTIVO_FUERA_SERVICIO} */
    public static final int INACTIVO = ACTIVO_FUERA_SERVICIO;

    // Estados de ticket (data_catalogo catálogo 3)
    public static final int ESTADO_TICKET_ABIERTO = 7;
    public static final int ESTADO_TICKET_EN_REPARACION = 8;
    public static final int ESTADO_TICKET_CERRADO = 9;
}
