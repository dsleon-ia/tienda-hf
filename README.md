# Proyecto Full-Stack de Tienda en Línea

## Descripción
Este proyecto es una aplicación full-stack que incluye un backend en Spring Boot y un frontend en React para la gestión de productos y categorías de una tienda. La persistencia de datos se maneja con PostgreSQL para los datos principales y MongoDB para la auditoría de operaciones. Todo el sistema está orquestado con Docker Compose, permitiendo levantar todos los servicios con un solo comando.

## Stack Tecnológico
- Backend: Java 17, Spring Boot 3.5.x
- Frontend: React (Vite), TypeScript, Nginx
- Bases de Datos: PostgreSQL 15, MongoDB 7
- Contenerización: Docker, Docker Compose v2

## Prerrequisitos
- Docker y Docker Compose: Asegúrate de tener instalados Docker y Docker Compose en tu sistema (Docker Desktop para Windows/macOS o Docker Engine + Compose Plugin para Linux).

## Estructura del Proyecto
- `docker-compose.yml`: Orquesta todos los servicios (backend, frontend, postgres, mongodb).
- `backend/`: Código fuente del microservicio Spring Boot.
  - `Dockerfile`: Define la imagen Docker para el backend.
- `frontend-tienda/`: Código fuente de la aplicación React.
  - `Dockerfile`: Define la imagen Docker para el frontend.
  - `nginx.conf`: Configuración de Nginx para servir el frontend.
- `documentacion/`: Collection para Postman (api.json) e imágenes de diagramas con estandar UML.

## Configuración
Crea un archivo `.env` en la raíz del proyecto con las siguientes variables para configurar las bases de datos. Puedes ajustar los valores según tus preferencias.

```env
# ==================================
# =      PROJECT VARIABLES         =
# ==================================

# --- PostgreSQL ---
POSTGRES_USER=app
POSTGRES_PASSWORD=secret
POSTGRES_DB=appdb

# --- MongoDB ---
MONGO_DB=auditdb

# --- Ports ---
POSTGRES_LOCAL_PORT=5433
MONGO_LOCAL_PORT=27017
APP_PORT=8081
```

## Ejecución del Proyecto
1.  Clona el repositorio en tu máquina local.
2.  Crea el archivo `.env` como se describe en la sección de "Configuración".
3.  Abre una terminal en la raíz del proyecto.
4.  Ejecuta el siguiente comando para construir y levantar todos los servicios en segundo plano:
    ```bash
    docker compose up --build -d
    ```

### Servicios Disponibles
Una vez que los contenedores estén en funcionamiento, podrás acceder a los siguientes servicios:

- Frontend (Aplicación de Tienda):
  - URL: `http://localhost:8083`

- Backend (API REST):
  - URL lista de productos: `http://localhost:8081/api/products`
  - Swagger UI (Documentación Interactiva): `http://localhost:8081/swagger-ui/index.html`

- API de Auditoría (Endpoints de Ejemplo):
  - Historial de un producto: `http://localhost:8081/api/audit/products/{productId}`
  - Últimos 100 registros: `http://localhost:8081/api/audit/products`

- PostgreSQL (Base de Datos Principal):
  - Host: `localhost`
  - Puerto: `${POSTGRES_LOCAL_PORT}` (definido en tu `.env`)
  - Usuario: `${POSTGRES_USER}` (definido en tu `.env`)
  - Contraseña: `${POSTGRES_PASSWORD}` (definido en tu `.env`)
  - Base de datos: `${POSTGRES_DB}` (definido en tu `.env`)

- MongoDB (Base de Datos de Auditoría):
  - Host: `localhost`
  - Puerto: `${MONGO_LOCAL_PORT}` (definido en tu `.env`)
  - Base de datos: `${MONGO_DB}` (definido en tu `.env`)

### Gestión de los Contenedores
- Ver logs de un servicio (ej. backend):
  ```bash
  docker compose logs -f backend
  ```
- Detener todos los servicios:
  ```bash
  docker compose down
  ```
- Detener y eliminar los volúmenes (borra todos los datos):
  ```bash
  docker compose down -v
  ```

## Troubleshooting
- `failed to solve: ...` durante la construcción:
  - Asegúrate de que Docker tiene suficientes recursos asignados (CPU/memoria).
  - Verifica que tienes conexión a internet para descargar las imágenes base y dependencias.
- Conflictos de puertos:
  - Si algún puerto (`8083`, `8081`, etc.) ya está en uso en tu máquina, puedes cambiarlo fácilmente modificando los valores en tu archivo `.env` (para las bases de datos) o en el archivo `docker-compose.yml` (para el frontend y backend).

## Estado de la Implementación
- La infraestructura completa (backend, frontend, bases de datos) está configurada y orquestada con Docker.
- Los endpoints de la API, la lógica de negocio y la carga de datos iniciales están listos para ser utilizados y probados a través del frontend.
