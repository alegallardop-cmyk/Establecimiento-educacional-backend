# Bitácora Escolar · Backend (avance)

Backend del proyecto semestral **Bitácora Escolar** (DSY1105). El frontend está en Android Studio (Kotlin + Jetpack Compose).

Este avance es el proyecto base generado con [Spring Initializr](https://start.spring.io), con la conexión a MySQL configurada.

## Integrantes

- Alen Gallardo
- Benjamín Leiva

## Configuración del Initializr

| Opción | Valor |
|---|---|
| Proyecto | Maven |
| Lenguaje | Java 21 |
| Spring Boot | 4.1.2 |
| Group | `cl.duoc` |
| Artifact | `establecimiento-educacional` |
| Package | `cl.duoc.establecimiento_educacional` |
| Packaging | Jar |

## Dependencias

| Dependencia | Para qué sirve |
|---|---|
| Spring Web | Crear la API REST que usará la app Android |
| Spring Data JPA | Trabajar con la base de datos usando entidades y repositorios |
| Validation | Validar los datos que llegan a la API (`@NotBlank`, `@Size`, `@Email`...) |
| Lombok | Generar getters, setters y constructores con anotaciones (`@Data`) |
| MySQL Driver | Conectar Spring Boot con MySQL |

## Cómo ejecutar

1. Tener MySQL encendido (por ejemplo con XAMPP o MySQL Workbench) en el puerto 3306.
2. Revisar usuario y contraseña en `src/main/resources/application.properties`.
3. Abrir la carpeta en IntelliJ IDEA o VS Code y ejecutar `EstablecimientoEducacionalApplication`, o desde la terminal:

```bash
./mvnw spring-boot:run
```

4. La base `bitacora_escolar` se crea sola. La API queda en `http://localhost:8080`.

## Próximos pasos

- Crear las entidades (`Usuario`, `Curso`, `Bitacora`, `Registro`...) igual que en Room.
- Crear los repositorios, servicios y controladores REST.
- Conectar la app Android con la API.
