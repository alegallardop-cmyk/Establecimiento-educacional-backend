package cl.duoc.establecimiento_educacional.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Comentario del docente sobre un registro (PUT /registros/{id}/retroalimentacion)
// ignoreUnknown: si la app manda campos de más (id, fechas...), se ignoran
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetroalimentacionRequest {

    // Puede ir vacío al marcar como revisado; si se escribe, mínimo 5 caracteres (lo revisa el servicio)
    @NotNull
    @Size(max = 500, message = "Máximo 500 caracteres")
    private String comentario;

    @NotNull(message = "Debe indicar el docente")
    private Long docenteRevisorId;

    @NotNull
    @Pattern(regexp = "PENDIENTE|REVISADO", message = "El estado debe ser PENDIENTE o REVISADO")
    private String estadoRevision;
}
