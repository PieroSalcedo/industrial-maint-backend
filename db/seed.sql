-- MaintPro Industrial OS — Datos iniciales (PostgreSQL)
-- Ejecutar DESPUÉS de schema.sql, conectado a industrial_db.
--
-- Credenciales demo:
--   admin   / admin2026   → ROLE_ADMIN
--   tecnico / tecnico2026 → ROLE_TECNICO
--
-- Las contraseñas están hasheadas con BCrypt (compatible con Spring Security).
-- Para regenerar: ejecutar EncoderPassword.java en el backend.

BEGIN;

-- ============================================================
-- CATÁLOGOS
-- IDs alineados con AppSettings.java (CATALOGO_TIPO_ACTIVO = 1, etc.)
-- ============================================================

INSERT INTO catalogo (id_catalogo, descripcion) VALUES
    (1, 'Tipo de Activo'),
    (2, 'Prioridad de Ticket'),
    (3, 'Estado de Ticket');

INSERT INTO data_catalogo (id_data_catalogo, descripcion, id_catalogo) VALUES
    -- Catálogo 1: Tipos de activo
    (1,  'Compresor de Aire',       1),
    (2,  'Bomba Centrífuga',        1),
    (3,  'Motor Eléctrico',         1),
    (4,  'Transformador',           1),
    (5,  'Torno CNC',               1),
    -- Catálogo 2: Prioridades
    (6,  'Baja',                    2),
    (7,  'Media',                   2),
    (8,  'Alta',                    2),
    (9,  'Crítica',                 2),
    -- Catálogo 3: Estados de ticket
    (10, 'Pendiente',               3),
    (11, 'En Proceso',              3),
    (12, 'Resuelto',                3),
    (13, 'Cancelado',               3);

-- ============================================================
-- ROLES
-- El campo nombre debe coincidir con Spring Security (ROLE_*)
-- ============================================================

INSERT INTO rol (id_rol, nombre, descripcion, estado) VALUES
    (1, 'ROLE_ADMIN',   'Administrador General',      1),
    (2, 'ROLE_TECNICO', 'Técnico de Mantenimiento',   1);

-- ============================================================
-- OPCIONES DE MENÚ
-- tipo: 1/3 → menú GESTIÓN | 2/4 → menú REPORTES (ver menu.ts)
-- ruta: se concatena con '/' en Angular → routerLink="/activos"
-- ============================================================

INSERT INTO opcion (id_opcion, nombre, ruta, tipo, estado) VALUES
    (1, 'Inicio',              '',         1, 1),
    (2, 'Gestión de Activos',  'activos',  1, 1),
    (3, 'Tickets',             'tickets',  3, 1),
    (4, 'Dashboard KPIs',      'reportes', 2, 1);

-- ============================================================
-- USUARIOS
-- ============================================================

INSERT INTO usuario (id_usuario, nombres, apellidos, dni, login, password, correo, estado) VALUES
    (1, 'Admin',   'Sistema',  '12345678', 'admin',
     '$2b$10$N4J6ArkYWG0qFyyItKfEVu3Cq5U8lW0ndkiOubUqCsGNk50J3i2qK',
     'admin@maintpro.com', 1),
    (2, 'Carlos',  'Mendoza',  '87654321', 'tecnico',
     '$2b$10$ksrVZKmCF1i3vvSOiLYyju4xUMH4aHUf8/nraBtUhQbrn/2QB7Ohe',
     'tecnico@maintpro.com', 1);

-- ============================================================
-- ASIGNACIÓN DE ROLES Y PERMISOS DE MENÚ
-- ============================================================

INSERT INTO usuario_has_rol (id_usuario, id_rol) VALUES
    (1, 1),
    (2, 2);

INSERT INTO rol_has_opcion (id_rol, id_opcion) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4),
    (2, 1), (2, 2), (2, 3), (2, 4);

-- ============================================================
-- ACTIVOS DE EJEMPLO
-- ============================================================

INSERT INTO activo (id_activo, nombre, numero_serie, id_tipo_activo, id_usuario_registro, id_usuario_actualiza, estado) VALUES
    (1, 'Compresor Principal Planta A', 'CMP-2024-001', 1, 1, 1, 1),
    (2, 'Bomba de Agua Industrial',     'BMB-2023-045', 2, 1, 1, 1),
    (3, 'Motor de Línea 3',             'MTR-2022-112', 3, 1, 1, 1);

-- ============================================================
-- TICKETS DE EJEMPLO
-- ============================================================

INSERT INTO ticket_mantenimiento (
    id_ticket, descripcion, id_activo, id_prioridad, id_estado_ticket,
    id_usuario_tecnico, id_usuario_registro, id_usuario_actualiza
) VALUES
    (1, 'Fuga de aceite en sello mecánico del compresor', 1, 8, 10, 2, 1, 1),
    (2, 'Vibración anormal detectada en bomba',           2, 9, 11, 2, 1, 1),
    (3, 'Mantenimiento preventivo programado',            3, 6, 12, 2, 1, 1);

-- ============================================================
-- REINICIO DE SECUENCIAS (SERIAL)
-- ============================================================

SELECT setval('catalogo_id_catalogo_seq',         (SELECT MAX(id_catalogo) FROM catalogo));
SELECT setval('data_catalogo_id_data_catalogo_seq', (SELECT MAX(id_data_catalogo) FROM data_catalogo));
SELECT setval('rol_id_rol_seq',                   (SELECT MAX(id_rol) FROM rol));
SELECT setval('opcion_id_opcion_seq',             (SELECT MAX(id_opcion) FROM opcion));
SELECT setval('usuario_id_usuario_seq',           (SELECT MAX(id_usuario) FROM usuario));
SELECT setval('activo_id_activo_seq',             (SELECT MAX(id_activo) FROM activo));
SELECT setval('ticket_mantenimiento_id_ticket_seq', (SELECT MAX(id_ticket) FROM ticket_mantenimiento));

COMMIT;
