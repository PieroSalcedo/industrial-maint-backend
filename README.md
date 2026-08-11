# MaintPro Industrial OS — Backend

API REST del sistema **MaintPro Industrial OS**, plataforma para la **gestión de mantenimiento industrial**. Expone endpoints para autenticación JWT, administración de activos (maquinaria), registro de tickets de falla y catálogos auxiliares.

> **Repositorio backend:** [industrial-maint-backend](https://github.com/PieroSalcedo/industrial-maint-backend)  
> **Repositorio frontend:** [industrial-maint-frontend](https://github.com/PieroSalcedo/industrial-maint-frontend)  
> **Frontend requerido:** Angular 22 en `http://localhost:4200`  
> **Base de datos:** PostgreSQL — `industrial_db` (scripts en [`db/`](db/README.md))

**Documentación adicional:**
- [Modelo de datos](docs/MODELO-DATOS.md) — ER, catálogos, roles y opciones
- [Arquitectura](docs/ARQUITECTURA.md) — requerimientos, diseño y flujos
- [Scripts SQL](db/README.md) — instalación de la base de datos

---

## Descripción del proyecto

MaintPro resuelve un problema real de plantas industriales: **centralizar el control de activos y sus incidencias de mantenimiento** mediante una API stateless consumida por un frontend Angular.

| Módulo | Descripción |
|--------|-------------|
| **Auth** | Login con JWT. Devuelve token, roles y menú dinámico según permisos del usuario. |
| **Activos** | CRUD de equipos/maquinaria con consulta dinámica por nombre, serie, tipo y estado. |
| **Tickets** | Registro y consulta de órdenes de mantenimiento vinculadas a un activo. |
| **Util** | Catálogos maestros (tipos de activo, prioridades, estados de ticket). |

### Roles y permisos

- **Administrador (`ROLE_ADMIN`):** puede eliminar activos (`DELETE /url/activo/**`).
- **Usuarios autenticados:** acceso a consultas, registro y actualización de activos y tickets.
- **Público (sin token):** login, catálogos `/url/util/**` y documentación Swagger.

---

## Stack tecnológico

| Capa | Tecnología |
|------|------------|
| Lenguaje | **Java 21** |
| Framework | **Spring Boot 3.3.2** |
| Persistencia | **Spring Data JPA** + **PostgreSQL** |
| Seguridad | **Spring Security** + **JWT** (jjwt 0.11.5) |
| Validación | Jakarta Validation |
| API Docs | **springdoc-openapi 2.6.0** (Swagger UI) |
| Utilidades | Lombok, DevTools |
| Build | Maven |
| Frontend (externo) | **Angular 22** + Material |

---

## Arquitectura

```
┌─────────────────────────────────────────────────────┐
│              Angular Frontend (:4200)                 │
│         HttpInterceptor (Bearer JWT)                │
└────────────────────────┬────────────────────────────┘
                         │ REST API
                         ▼
┌─────────────────────────────────────────────────────┐
│         Spring Boot Backend (:8080/url)              │
│  /auth  /activo  /ticket  /util                      │
│  JWT Filter │ Spring Security │ JPA Repositories    │
└────────────────────────┬────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│           PostgreSQL — industrial_db                 │
└─────────────────────────────────────────────────────┘
```

### Modelo de datos principal

```
Catalogo (1) ──< DataCatalogo (N)
                      │
                      ├──< Activo.tipoActivo
                      ├──< Ticket.prioridad
                      └──< Ticket.estadoTicket

Activo (1) ──< TicketMantenimiento (N)

Usuario (N) ──< UsuarioHasRol >── (N) Rol
Rol (N) ──< RolHasOpcion >── (N) Opcion
```

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| `Activo` | `activo` | Equipo o maquinaria industrial |
| `TicketMantenimiento` | `ticket_mantenimiento` | Orden de mantenimiento por falla |
| `DataCatalogo` | `data_catalogo` | Valores de catálogo (tipos, prioridades, estados) |
| `Usuario` | `usuario` | Usuarios del sistema |
| `Rol` / `Opcion` | `rol`, `opcion` | Roles y opciones de menú dinámico |

**IDs de catálogo** (definidos en `AppSettings`):

| Constante | ID | Uso |
|-----------|-----|-----|
| `CATALOGO_TIPO_ACTIVO` | 1 | Tipos de maquinaria |
| `CATALOGO_PRIORIDAD` | 2 | Prioridad del ticket |
| `CATALOGO_ESTADO_TICKET` | 3 | Estado del ticket |

---

## Endpoints de la API

Base URL: `http://localhost:8080/url`

### Auth — `/url/auth` (público)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/login` | Autenticación. Retorna JWT, roles y opciones de menú |

**Request:**
```json
{ "login": "admin", "password": "admin2026" }
```

**Response:**
```json
{
  "token": "eyJ...",
  "bearer": "Bearer",
  "login": "admin",
  "nombreCompleto": "Admin Sistema",
  "roles": ["ROLE_ADMIN"],
  "opciones": [{ "idOpcion": 1, "nombre": "Activos", "ruta": "/activos", ... }]
}
```

### Activos — `/url/activo` (JWT requerido)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/listaTodos` | Lista completa de activos |
| GET | `/consultaDinamica` | Búsqueda con filtros opcionales |
| POST | `/registraActivo` | Crear activo |
| PUT | `/actualizaActivo` | Actualizar activo (requiere `idActivo`) |
| DELETE | `/eliminaActivo/{id}` | Borrado físico (**solo `ROLE_ADMIN`**) |

**Consulta dinámica — parámetros:**

| Parámetro | Tipo | Default | Descripción |
|-----------|------|---------|-------------|
| `vnombre` | string | `""` | Nombre parcial (case-insensitive) |
| `vserie` | string | `-1` | Número de serie exacto (`-1` = omitir) |
| `vtipo` | int | `-1` | ID de `DataCatalogo` tipo activo |
| `vestado` | int | `-1` | `1` activo, `0` inactivo |

### Tickets — `/url/ticket` (JWT requerido)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/registraTicket` | Crear ticket de mantenimiento |
| GET | `/consultaDinamica` | Búsqueda con filtros opcionales |

**Consulta dinámica — parámetros:**

| Parámetro | Tipo | Default | Descripción |
|-----------|------|---------|-------------|
| `vdesc` | string | `""` | Descripción parcial |
| `vactivo` | int | `-1` | ID del activo |
| `vprioridad` | int | `-1` | ID de prioridad |
| `vestado` | int | `-1` | ID de estado del ticket |

### Util — `/url/util` (público)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/listaTipoActivo` | Tipos de activo (catálogo 1) |
| GET | `/listaPrioridad` | Prioridades (catálogo 2) |
| GET | `/listaEstadoTicket` | Estados de ticket (catálogo 3) |

### Swagger UI

Documentación interactiva disponible en:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

---

## Requisitos previos

- **Java 21** (JDK)
- **Maven 3.9+** (o usar el wrapper `./mvnw`)
- **PostgreSQL** con la base de datos `industrial_db` creada y esquema cargado
- **Frontend Angular** (opcional, para uso completo del sistema)

---

## Credenciales de demo

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `admin2026` | Administrador — CRUD completo de activos |
| `tecnico` | `tecnico2026` | Técnico — acceso según permisos en BD |

---

## Cómo levantar el stack completo

```text
1. PostgreSQL activo
2. Base de datos: ejecutar db/00_create_database.sql → schema.sql → seed.sql
3. Backend:  cd industrial-backend → configurar .env → mvn spring-boot:run  (:8080)
4. Frontend: cd industrial-frontend → npm install → npm start         (:4200)
5. Abrir:    http://localhost:4200/login
```

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/PieroSalcedo/industrial-maint-backend.git
cd industrial-maint-backend
```

### 2. Crear la base de datos

Ejecutar los scripts SQL en orden (pgAdmin o psql):

```bash
psql -U postgres -f db/00_create_database.sql
psql -U postgres -d industrial_db -f db/schema.sql
psql -U postgres -d industrial_db -f db/seed.sql
```

Ver detalle en [`db/README.md`](db/README.md).

### 3. Configurar variables de entorno

Copiar el archivo de ejemplo y editar con tus credenciales de PostgreSQL:

```bash
cp .env.example .env
```

Contenido de `.env`:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=industrial_db
DB_USERNAME=postgres
DB_PASSWORD=tu_password
```

> **Importante:** El archivo `.env` no se sube al repositorio. Solo se versiona `.env.example`.

### 4. Compilar y ejecutar

Con Maven instalado:

```bash
mvn spring-boot:run
```

O usando el wrapper (Windows):

```bash
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

La API quedará disponible en **http://localhost:8080/url**.

### 5. Verificar que funciona

```bash
# Catálogos (público, sin token)
curl http://localhost:8080/url/util/listaTipoActivo

# Login
curl -X POST http://localhost:8080/url/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"login\":\"admin\",\"password\":\"admin2026\"}"
```

---

## Estructura del proyecto

```
db/
├── 00_create_database.sql              # Crear BD industrial_db
├── schema.sql                          # DDL unificado (tablas)
├── seed.sql                            # Datos demo (catálogos, usuarios, etc.)
└── README.md                           # Guía de instalación SQL

docs/
├── MODELO-DATOS.md                     # ER, catálogos, roles, opciones
└── ARQUITECTURA.md                     # Requerimientos y diseño del sistema

src/main/java/com/maint/industrial_backend/
├── IndustrialBackendApplication.java   # Punto de entrada
├── config/
│   └── OpenApiConfig.java              # Swagger / OpenAPI
├── controller/
│   ├── AuthController.java             # POST /auth/login
│   ├── ActivoController.java           # CRUD + consulta dinámica
│   ├── TicketController.java           # Registro + consulta tickets
│   └── UtilController.java             # Catálogos auxiliares
├── dto/                                # LoginRequest, JwtResponse, etc.
├── entity/                             # Entidades JPA (11 clases)
├── repository/                         # Spring Data JPA
├── service/ + service/impl/            # Lógica de negocio
├── security/
│   ├── SecurityConfig.java             # CORS, JWT, reglas de acceso
│   ├── JwtProvider.java                # Generación y validación JWT
│   ├── JwtTokenFilter.java             # Filtro Bearer token
│   └── UsuarioPrincipal.java           # UserDetails + roles + opciones
├── util/
│   └── AppSettings.java                # CORS origin, IDs de catálogo
└── exception/
    └── GlobalExceptionHandler.java     # Manejo global de errores

src/main/resources/
└── application.properties              # Configuración (usa variables .env)
```

---

## Seguridad

- **Autenticación stateless** con JWT (sin sesión en servidor).
- **Contraseñas** hasheadas con BCrypt.
- **CORS** configurado para `http://localhost:4200` (frontend Angular).
- **Eliminación de activos** restringida a usuarios con `ROLE_ADMIN`.
- Endpoints protegidos requieren header:
  ```
  Authorization: Bearer <token>
  ```
- Rutas públicas: `/url/auth/**`, `/url/util/**`, Swagger.

---

## Configuración

`application.properties` usa variables de entorno para la conexión a PostgreSQL:

| Propiedad | Valor |
|-----------|-------|
| `server.port` | `8080` |
| `spring.jpa.hibernate.ddl-auto` | `none` (esquema gestionado externamente) |
| `spring.jpa.show-sql` | `true` (útil en desarrollo) |

El origen CORS permitido está en `AppSettings.URL_CROSS_ORIGIN` (`http://localhost:4200`).

---

## Solución de problemas

| Problema | Solución |
|----------|----------|
| Error de conexión a PostgreSQL | Verificar que PostgreSQL esté activo y que `.env` tenga credenciales correctas |
| `401 Unauthorized` | Token JWT expirado o ausente. Hacer login de nuevo |
| `403 Forbidden` al eliminar activo | Solo `ROLE_ADMIN` puede usar `DELETE /url/activo/**` |
| Error al eliminar activo con tickets | El activo tiene tickets vinculados; eliminar o reasignar tickets primero |
| CORS desde el frontend | Verificar que el frontend corra en `:4200` o actualizar `SecurityConfig` y `AppSettings` |
| LazyInitializationException en JSON | Las entidades usan `@JsonIgnoreProperties` en relaciones lazy |

---

## Autor

**Piero Salcedo** — Prueba técnica / Proyecto de mantenimiento industrial.

---

## Licencia

Proyecto académico / prueba técnica. Uso educativo.
