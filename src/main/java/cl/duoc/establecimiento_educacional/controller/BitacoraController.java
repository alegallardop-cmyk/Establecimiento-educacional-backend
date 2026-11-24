package cl.duoc.establecimiento_educacional.controller;

import cl.duoc.establecimiento_educacional.dto.BitacoraRequest;
import cl.duoc.establecimiento_educacional.model.Bitacora;
import cl.duoc.establecimiento_educacional.service.BitacoraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Microservicio de BITÁCORAS: http://localhost:8080/bitacoras
@RestController
@RequestMapping("/bitacoras")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;

    // 1. GET: Listar todas
    @GetMapping
    public ResponseEntity<List<Bitacora>> listar() {
        return ResponseEntity.ok(bitacoraService.listar());
    }

    // 2. GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Bitacora> buscarPorId(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.buscarPorId(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bitacora);
    }

    // 3. POST: Crear (@Valid revisa las anotaciones de BitacoraRequest)
    @PostMapping
    public ResponseEntity<Bitacora> crear(@Valid @RequestBody BitacoraRequest datos) {
        Bitacora nueva = bitacoraService.crear(datos);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // 4. PUT: Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Bitacora> actualizar(@PathVariable Long id, @Valid @RequestBody BitacoraRequest datos) {
        Bitacora actualizada = bitacoraService.actualizar(id, datos);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    // 5. PUT: Publicar (BORRADOR -> PUBLICADA)
    @PutMapping("/{id}/publicar")
    public ResponseEntity<Bitacora> publicar(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.publicar(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bitacora);
    }

    // 6. PUT: Cerrar (PUBLICADA -> CERRADA)
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<Bitacora> cerrar(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.cerrar(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bitacora);
    }

    // 7. DELETE: Borrar (también borra sus registros)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (bitacoraService.borrar(id)) {
            return ResponseEntity.noContent().build();   // 204 No Content
        }
        return ResponseEntity.notFound().build();        // 404 Not Found
    }
}
