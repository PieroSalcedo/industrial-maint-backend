-- Patch: roles, trazabilidad ticket-activo y datos demo
-- Ejecutar sobre industrial_db si ya tenías seed anterior (no es necesario en instalación nueva).

BEGIN;

UPDATE rol SET descripcion = 'Supervisor de Mantenimiento' WHERE id_rol = 1;

UPDATE usuario SET nombres = 'Supervisor' WHERE login = 'admin';

UPDATE ticket_mantenimiento
SET id_usuario_tecnico = 2,
    id_usuario_registro = 1
WHERE id_ticket = 1 AND id_usuario_tecnico IS NULL;

-- Sincronizar activo del ticket demo (carro id=3 tiene ticket abierto → fuera de servicio)
UPDATE activo SET estado = 0 WHERE id_activo = 3
  AND EXISTS (
    SELECT 1 FROM ticket_mantenimiento t
    WHERE t.id_activo = 3 AND t.id_estado_ticket IN (7, 8)
  );

COMMIT;
