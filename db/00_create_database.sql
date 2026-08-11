-- MaintPro Industrial OS — Creación de base de datos
-- Ejecutar conectado a PostgreSQL como superusuario (postgres).

CREATE DATABASE industrial_db
    WITH ENCODING 'UTF8'
         LC_COLLATE = 'Spanish_Peru.1252'
         LC_CTYPE = 'Spanish_Peru.1252'
         TEMPLATE template0;

-- Si tu instalación no soporta el locale anterior, usa esta variante:
-- CREATE DATABASE industrial_db WITH ENCODING 'UTF8';

COMMENT ON DATABASE industrial_db IS 'Base de datos del sistema MaintPro Industrial OS';
