package cl.duoc.establecimiento_educacional.controller;

import cl.duoc.establecimiento_educacional.dto.RegistroRequest;
import cl.duoc.establecimiento_educacional.dto.RetroalimentacionRequest;
import cl.duoc.establecimiento_educacional.model.Registro;
import cl.duoc.establecimiento_educacional.service.RegistroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Microservicio de REGISTROS: http://localhost:8080/registros
@RestController
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    // 1. GET: Listar. Filtros opcionales: /registros?bitacoraId=2 o /registros?estudianteId=3
    @GetMapping
    public ResponseEntity<List<Registro>> listar(
            @RequestParam(required = false) Long bitacoraId,
            @RequestParam(required = false) Long estudianteId) {
        return ResponseEntity.ok(registroService.listar(bitacoraId, estudianteId));
    }

    // 2. GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Registro> buscarPorId(@PathVariable Long id) {
        Registro registro = registroService.buscarPorId(id);
        if (registro == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(registro);
    }

    // 3. POST: Crear
    @PostMapping
    public ResponseEntity<Registro> crear(@Valid @RequestBody RegistroRequest datos) {
        Registro nuevo = registroService.crear(datos);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // 4. PUT: Actualizar un borrador (o enviarlo)
    @PutMapping("/{id}")
    public ResponseEntity<Registro> actualizar(@PathVariable Long id, @Valid @RequestBody RegistroRequest datos) {
        Registro actualizado = registroService.actualizar(id, datos);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    // 5. PUT: Retroalimentación del docente
    @PutMapping("/{id}/retroalimentacion")
    public ResponseEntity<Registro> retroalimentar(@PathVariable Long id, @Valid @RequestBody RetroalimentacionRequest datos) {
        Registro registro = registroService.guardarRetroalimentacion(id, datos);
        if (registro == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(registro);
    }

    // 6. DELETE: Borrar un borrador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (registroService.borrar(id)) {
            return ResponseEntity.noContent().build();   // 204 No Content
        }
        return ResponseEntity.notFound().build();        // 404 Not Found
    }
}
