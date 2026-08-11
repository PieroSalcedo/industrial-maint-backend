-- MaintPro Industrial OS — Esquema de base de datos (PostgreSQL)
-- Ejecutar conectado a la base de datos: industrial_db
--
-- Orden de ejecución recomendado:
--   1. 00_create_database.sql  (opcional, solo la primera vez)
--   2. schema.sql              (este archivo)
--   3. seed.sql                (datos iniciales)

BEGIN;

-- ============================================================
-- SEGURIDAD Y ACCESO
-- ============================================================

CREATE TABLE usuario (
    id_usuario          SERIAL PRIMARY KEY,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    dni                 CHAR(8) UNIQUE NOT NULL,
    login               VARCHAR(50) UNIQUE NOT NULL,
    password            VARCHAR(255) NOT NULL,
    correo              VARCHAR(100) UNIQUE NOT NULL,
    estado              INT NOT NULL DEFAULT 1
);

CREATE TABLE rol (
    id_rol              SERIAL PRIMARY KEY,
    nombre              VARCHAR(50) UNIQUE NOT NULL,
    descripcion         VARCHAR(100),
    estado              INT NOT NULL DEFAULT 1
);

CREATE TABLE opcion (
    id_opcion           SERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    ruta                VARCHAR(100) NOT NULL,
    tipo                INT NOT NULL,
    estado              INT NOT NULL DEFAULT 1
);

CREATE TABLE usuario_has_rol (
    id_usuario          INT NOT NULL REFERENCES usuario(id_usuario),
    id_rol              INT NOT NULL REFERENCES rol(id_rol),
    PRIMARY KEY (id_usuario, id_rol)
);

CREATE TABLE rol_has_opcion (
    id_rol              INT NOT NULL REFERENCES rol(id_rol),
    id_opcion           INT NOT NULL REFERENCES opcion(id_opcion),
    PRIMARY KEY (id_rol, id_opcion)
);

-- ============================================================
-- CATÁLOGOS MAESTROS
-- ============================================================

CREATE TABLE catalogo (
    id_catalogo         SERIAL PRIMARY KEY,
    descripcion         VARCHAR(100) NOT NULL
);

CREATE TABLE data_catalogo (
    id_data_catalogo    SERIAL PRIMARY KEY,
    descripcion         VARCHAR(100) NOT NULL,
    id_catalogo         INT NOT NULL REFERENCES catalogo(id_catalogo)
);

-- ============================================================
-- NEGOCIO — ACTIVOS Y TICKETS
-- ============================================================

CREATE TABLE activo (
    id_activo               SERIAL PRIMARY KEY,
    nombre                  VARCHAR(100) NOT NULL,
    numero_serie            VARCHAR(50) UNIQUE NOT NULL,
    id_tipo_activo          INT NOT NULL REFERENCES data_catalogo(id_data_catalogo),
    fecha_registro          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario_registro     INT REFERENCES usuario(id_usuario),
    id_usuario_actualiza    INT REFERENCES usuario(id_usuario),
    estado                  INT NOT NULL DEFAULT 1
);

CREATE TABLE ticket_mantenimiento (
    id_ticket               SERIAL PRIMARY KEY,
    descripcion             TEXT NOT NULL,
    id_activo               INT NOT NULL REFERENCES activo(id_activo),
    id_prioridad            INT NOT NULL REFERENCES data_catalogo(id_data_catalogo),
    id_estado_ticket        INT NOT NULL REFERENCES data_catalogo(id_data_catalogo),
    id_usuario_tecnico      INT REFERENCES usuario(id_usuario),
    fecha_registro          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario_registro     INT REFERENCES usuario(id_usuario),
    id_usuario_actualiza    INT REFERENCES usuario(id_usuario)
);

COMMIT;
