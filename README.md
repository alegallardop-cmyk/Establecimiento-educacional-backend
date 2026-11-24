# Bitácora Escolar · Backend (microservicio)

Microservicio REST del proyecto semestral **Bitácora Escolar** (DSY1105).
Guarda en MySQL las **bitácoras**, los **registros de clase** de los estudiantes, sus **evidencias** y la **retroalimentación** del docente.
La app Android (repositorio `Establecimiento-educacional`) lo consume con Retrofit.

## Integrantes

- Alen Gallardo
- Benjamín Leiva

## Tecnologías

| Elemento | Valor |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.1.2 (Maven) |
| Dependencias | Spring Web, Spring Data JPA, Validation, Lombok, MySQL Driver |
| Base de datos | MySQL (`bitacora_escolar`) |
| Puerto | 8080 |

## Estructura de paquetes

```
cl.duoc.establecimiento_educacional
├── model/        Entidades JPA: Bitacora, Registro, Evidencia
├── dto/          Datos que llegan en el body (con validaciones)
├── repository/   Interfaces JpaRepository
├── service/      Reglas de negocio
├── controller/   Endpoints REST
├── exception/    Manejo de errores (400 y 409)
└── config/       DataInitializer: carga datos de prueba
```

Flujo de una petición: **Controller → Service → Repository → MySQL**.

## Cómo ejecutarlo

1. Tener MySQL corriendo en `localhost:3306` (por ejemplo con XAMPP o Laragon).
2. Revisar usuario y contraseña en `src/main/resources/application.properties` (por defecto `root` sin contraseña). La base `bitacora_escolar` se crea sola.
3. Abrir el proyecto en IntelliJ y ejecutar `EstablecimientoEducacionalApplication`, o en consola:
   ```
   ./mvnw spring-boot:run        (Windows: mvnw.cmd spring-boot:run)
   ```
4. Probar en el navegador o Postman: `http://localhost:8080/bitacoras`
5. Desde el emulador Android la dirección es `http://10.0.2.2:8080/`.

La primera vez, `DataInitializer` carga 3 bitácoras y 4 registros de prueba (datos ficticios).

## Endpoints

### Bitácoras · `/bitacoras`

| Método | Ruta | Qué hace | Respuesta |
|---|---|---|---|
| GET | `/bitacoras` | Lista todas las bitácoras | 200 |
| GET | `/bitacoras/{id}` | Busca una bitácora | 200 / 404 |
| POST | `/bitacoras` | Crea una bitácora | 201 / 400 / 409 |
| PUT | `/bitacoras/{id}` | Edita una bitácora (no cerrada) | 200 / 404 / 409 |
| PUT | `/bitacoras/{id}/publicar` | BORRADOR → PUBLICADA | 200 / 404 / 409 |
| PUT | `/bitacoras/{id}/cerrar` | PUBLICADA → CERRADA | 200 / 404 / 409 |
| DELETE | `/bitacoras/{id}` | Elimina la bitácora y sus registros | 204 / 404 |

Ejemplo de body (POST y PUT):

```json
{
  "asignacionId": 1,
  "mes": 11,
  "anio": 2026,
  "titulo": "Bitácora de noviembre",
  "proposito": "Reflexionar sobre lo aprendido en la unidad de poesía",
  "instrucciones": "Completa un registro después de cada clase",
  "preguntas": "¿Qué aprendí?\n¿Qué me costó?",
  "estado": "PUBLICADA"
}
```

### Registros · `/registros`

| Método | Ruta | Qué hace | Respuesta |
|---|---|---|---|
| GET | `/registros` | Lista registros. Filtros opcionales: `?bitacoraId=2` o `?estudianteId=3` | 200 |
| GET | `/registros/{id}` | Busca un registro | 200 / 404 |
| POST | `/registros` | Crea un registro (borrador o enviado) | 201 / 400 / 409 |
| PUT | `/registros/{id}` | Edita un borrador | 200 / 404 / 409 |
| PUT | `/registros/{id}/retroalimentacion` | El docente comenta y marca revisado | 200 / 404 / 409 |
| DELETE | `/registros/{id}` | Elimina un borrador | 204 / 404 / 409 |

Ejemplo de body (POST y PUT):

```json
{
  "bitacoraId": 2,
  "estudianteId": 3,
  "fechaClase": "18/11/2026",
  "contenido": "Figuras literarias",
  "actividad": "Escribimos un poema en grupo",
  "aprendizaje": "Reconocer metáforas",
  "dudas": "",
  "estadoEnvio": "ENVIADO",
  "evidencias": [
    { "tipo": "ENLACE", "descripcion": "Presentación", "enlace": "https://ejemplo.cl/poema" }
  ]
}
```

Ejemplo de retroalimentación:

```json
{ "comentario": "Muy buen registro, sigue así", "docenteRevisorId": 1, "estadoRevision": "REVISADO" }
```

## Validaciones y reglas

- Los DTO usan `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max` y `@Pattern`. Si fallan, la API responde **400** con `{ "campo": "mensaje" }`.
- Si se rompe una regla del negocio, responde **409** con `{ "error": "mensaje" }`:
  - No puede haber dos bitácoras para la misma asignación, mes y año.
  - Una bitácora cerrada no se edita; solo se publica un borrador y solo se cierra una publicada.
  - Solo se registran clases en bitácoras publicadas.
  - Para enviar un registro hay que completar contenido, actividad y aprendizaje.
  - Un registro enviado no se modifica ni se elimina (solo los borradores).
  - Solo se retroalimentan registros enviados; el comentario, si se escribe, tiene mínimo 5 caracteres.

## Qué guarda la app y qué guarda el microservicio

| Microservicio (MySQL) | App (Room, local) |
|---|---|
| Bitácoras, registros, evidencias, retroalimentación | Usuarios y login, cursos, asignaturas, asignaciones, recordatorios |

Las bitácoras guardan `asignacionId` y los registros `estudianteId`; la app une esos ids con los nombres que tiene en Room.
