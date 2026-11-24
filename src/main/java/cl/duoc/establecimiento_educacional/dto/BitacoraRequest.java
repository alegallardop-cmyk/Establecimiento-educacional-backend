package cl.duoc.establecimiento_educacional.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos que envía la app para crear o editar una bitácora (POST y PUT).
// Las anotaciones de Validation revisan los datos antes de llegar al servicio.
// ignoreUnknown: si la app manda campos de más (id, fechas...), se ignoran
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraRequest {

    @NotNull(message = "Debe elegir un curso y asignatura")
    private Long asignacionId;

    @Min(value = 1, message = "Mes inválido")
    @Max(value = 12, message = "Mes inválido")
    private int mes;

    @Min(value = 2020, message = "Año inválido")
    @Max(value = 2100, message = "Año inválido")
    private int anio;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 80, message = "El título debe tener entre 5 y 80 caracteres")
    private String titulo;

    @NotBlank(message = "El propósito es obligatorio")
    private String proposito;

    @NotBlank(message = "Las instrucciones son obligatorias")
    private String instrucciones;

    @NotBlank(message = "Agregue al menos una pregunta")
    private String preguntas;

    // Al crear: BORRADOR o PUBLICADA. Al editar no se usa (el estado no cambia).
    @Pattern(regexp = "BORRADOR|PUBLICADA|CERRADA", message = "Estado inválido")
    private String estado;
}
