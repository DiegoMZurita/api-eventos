# 📅 API Gestión de Eventos

API REST desarrollada con **Spring Boot** orientada a la gestión de eventos. La aplicación permite administrar eventos de forma segura mediante autenticación basada en **JWT**, exponiendo endpoints protegidos y documentados, con persistencia en base de datos relacional y preparada para despliegue en contenedores Docker.

El proyecto sigue buenas prácticas de arquitectura backend, separación de responsabilidades y configuración por entornos.

---

## 🚀 Funcionalidades

- Gestión de eventos mediante API REST.
- Autenticación y autorización con **JWT**.
- Endpoints protegidos con **Spring Security**.
- Persistencia de datos con **Spring Data JPA**.
- Mapeo de entidades y DTOs con **MapStruct**.
- Validación de datos de entrada.
- Documentación automática con **Swagger / OpenAPI**.
- Configuración por perfiles (`dev` / `prod`).
- Preparado para ejecución con **Docker** y **Docker Compose**.

---

## 🛠️ Tecnologías utilizadas

### Backend
- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation

### Seguridad
- JWT (jjwt)

### Base de datos
- PostgreSQL (Neon)
- H2 (entorno de desarrollo / pruebas)

### Documentación
- SpringDoc OpenAPI (Swagger UI)

### DevOps
- Docker
- Docker Compose

### Otras herramientas
- Maven
- Lombok
- MapStruct
- Logback

---

## 🧱 Estructura del proyecto

### 📂 src/main/java

```text
controller/   → Controladores REST de la API
data/         → Carga de datos iniciales
domain/       → Entidades del dominio
dto/          → Objetos de Transferencia de Datos
exception/    → Manejo de excepciones personalizadas
mapper/       → Mapeo entre entidades y DTOs (MapStruct)
repository/   → Acceso a datos con Spring Data JPA
security/     → Configuración de seguridad y JWT
service/      → Lógica de negocio
```
📂 src/main/resources
```text
application.properties        → Configuración base
application-dev.properties    → Configuración entorno desarrollo
application-prod.properties   → Configuración entorno producción
logback-spring.xml             → Configuración de logging
```
