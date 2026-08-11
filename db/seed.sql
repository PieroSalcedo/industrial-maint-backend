-- MaintPro Industrial OS — Datos iniciales (PostgreSQL)
-- Ejecutar DESPUÉS de schema.sql, conectado a industrial_db.
--
-- Credenciales demo:
--   admin   / admin2026   → ROLE_ADMIN
--   tecnico / tecnico2026 → ROLE_TECH
--
-- Contraseñas hasheadas con BCrypt (Spring Security).
-- Para regenerar: ejecutar EncoderPassword.java en el backend.

BEGIN;

-- ============================================================
-- CATÁLOGOS
-- ============================================================

INSERT INTO catalogo (id_catalogo, descripcion) VALUES
    (1, 'Tipo de Activo'),
    (2, 'Prioridad'),
    (3, 'Estado de Ticket');

INSERT INTO data_catalogo (id_data_catalogo, descripcion, id_catalogo) VALUES
    (1, 'Maquinaria Pesada',     1),
    (2, 'Equipos Electrónicos',  1),
    (3, 'Vehículos',             1),
    (4, 'Baja',                  2),
    (5, 'Media',                 2),
    (6, 'Urgente',               2),
    (7, 'Abierto',               3),
    (8, 'En Reparación',         3),
    (9, 'Cerrado/Reparado',      3);

-- ============================================================
-- ROLES
-- ============================================================

INSERT INTO rol (id_rol, nombre, descripcion, estado) VALUES
    (1, 'ROLE_ADMIN', 'Administrador General',    1),
    (2, 'ROLE_TECH',  'Técnico de Mantenimiento', 1);

-- ============================================================
-- OPCIONES DE MENÚ
-- tipo: 1/3 → menú GESTIÓN | 2/4 → menú REPORTES (ver menu.ts)
-- ============================================================

INSERT INTO opcion (id_opcion, nombre, ruta, tipo, estado) VALUES
    (1, 'Gestión de Activos',       'activos',   3, 1),
    (2, 'Tickets de Mantenimiento', 'tickets',   3, 1),
    (3, 'Panel de Usuarios',        'usuarios',  3, 1),
    (4, 'Reportes y Dashboard',     'reportes',  4, 1);

-- ============================================================
-- USUARIOS
-- ============================================================

INSERT INTO usuario (id_usuario, nombres, apellidos, dni, login, password, correo, estado) VALUES
    (1, 'Admin',   'MaintPro', '11111111', 'admin',
     '$2a$10$IUWTjcD7NQnQBcQn0oSZYunBJkGryX24qLB1jFFThRcdGna1P4s.y',
     'admin@maintpro.com', 1),
    (2, 'Tecnico', 'Juan',     '22222222', 'tecnico',
     '$2a$10$3Gjq.H1B3QFQPkJ.I7YxGuDnXEykz7ZhI6ls94IvLBWX5GBYIdKmy',
     'juan@maintpro.com', 1);

-- ============================================================
-- ASIGNACIÓN DE ROLES Y PERMISOS DE MENÚ
-- ============================================================

INSERT INTO usuario_has_rol (id_usuario, id_rol) VALUES
    (1, 1),
    (2, 2);

INSERT INTO rol_has_opcion (id_rol, id_opcion) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4),
    (2, 1), (2, 2);

-- ============================================================
-- ACTIVOS
-- ============================================================

INSERT INTO activo (
    id_activo, nombre, numero_serie, id_tipo_activo,
    fecha_registro, fecha_actualizacion,
    id_usuario_registro, id_usuario_actualiza, estado
) VALUES
    (3, 'carro', '123456',  3, '2026-08-10 20:37:40',       '2026-08-10 20:57:19.127188', 1, NULL, 0),
    (4, 'moto',  'hjc-ads', 3, '2026-08-10 21:48:50.505779', '2026-08-10 21:48:50.505779', 1, NULL, 1);

-- ============================================================
-- TICKETS
-- ============================================================

INSERT INTO ticket_mantenimiento (
    id_ticket, descripcion, id_activo, id_prioridad, id_estado_ticket
) VALUES
    (1, 'se quemo el carro eqd', 3, 6, 7);

-- ============================================================
-- REINICIO DE SECUENCIAS (SERIAL)
-- ============================================================

SELECT setval('catalogo_id_catalogo_seq',           (SELECT MAX(id_catalogo) FROM catalogo));
SELECT setval('data_catalogo_id_data_catalogo_seq',   (SELECT MAX(id_data_catalogo) FROM data_catalogo));
SELECT setval('rol_id_rol_seq',                     (SELECT MAX(id_rol) FROM rol));
SELECT setval('opcion_id_opcion_seq',               (SELECT MAX(id_opcion) FROM opcion));
SELECT setval('usuario_id_usuario_seq',             (SELECT MAX(id_usuario) FROM usuario));
SELECT setval('activo_id_activo_seq',               (SELECT MAX(id_activo) FROM activo));
SELECT setval('ticket_mantenimiento_id_ticket_seq', (SELECT MAX(id_ticket) FROM ticket_mantenimiento));

COMMIT;
