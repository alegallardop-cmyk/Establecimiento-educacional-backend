package cl.duoc.establecimiento_educacional.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Transforma los errores en respuestas JSON claras para la app
@RestControllerAdvice
public class ManejadorErrores {

    // 400 Bad Request: falló una anotación de Validation (@NotBlank, @Size, @Min...)
    // Responde, por ejemplo: { "titulo": "El título debe tener entre 5 y 80 caracteres" }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(MethodArgumentNotValidException e) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errores);
    }

    // 409 Conflict: no se cumple una regla del negocio
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, String>> reglaNoCumplida(ReglaNegocioException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
