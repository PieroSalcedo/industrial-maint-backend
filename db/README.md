# Base de datos — MaintPro Industrial OS

Scripts SQL para crear y poblar la base de datos **PostgreSQL** del sistema MaintPro.

## Archivos

| Archivo | Descripción |
|---------|-------------|
| `00_create_database.sql` | Crea la base de datos `industrial_db` |
| `schema.sql` | Estructura de tablas (DDL unificado) |
| `seed.sql` | Datos iniciales: catálogos, usuarios, roles, menú, ejemplos |

## Instalación rápida

### Opción A — pgAdmin

1. Conectar a PostgreSQL como `postgres`
2. Ejecutar `00_create_database.sql` (solo la primera vez)
3. Conectar a la base `industrial_db`
4. Ejecutar `schema.sql`
5. Ejecutar `seed.sql`

### Opción B — línea de comandos (psql)

```bash
psql -U postgres -f db/00_create_database.sql
psql -U postgres -d industrial_db -f db/schema.sql
psql -U postgres -d industrial_db -f db/seed.sql
```

### Opción C — base ya existente (reinstalar desde cero)

```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
-- Luego ejecutar schema.sql y seed.sql
```

## Credenciales demo

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `admin2026` | `ROLE_ADMIN` |
| `tecnico` | `tecnico2026` | `ROLE_TECH` |

## Regenerar contraseñas BCrypt

Si necesitas cambiar las contraseñas demo, edita `EncoderPassword.java` y ejecuta:

```bash
mvn compile org.codehaus.mojo:exec-maven-plugin:3.1.0:java \
  -Dexec.mainClass=com.maint.industrial_backend.util.EncoderPassword \
  -Dexec.classpathScope=compile
```

Copia el hash generado al campo `password` en `seed.sql`.

## Documentación relacionada

- [Modelo de datos](../docs/MODELO-DATOS.md) — ER, catálogos, roles y opciones
- [Arquitectura](../docs/ARQUITECTURA.md) — requerimientos y diseño del sistema

## Notas

- El backend usa `spring.jpa.hibernate.ddl-auto=none`: **no** genera tablas automáticamente.
- Los IDs de catálogo (`1`, `2`, `3`) deben coincidir con `AppSettings.java` en el backend.
- Los nombres de rol deben usar el prefijo `ROLE_` (ej. `ROLE_ADMIN`) para Spring Security.
