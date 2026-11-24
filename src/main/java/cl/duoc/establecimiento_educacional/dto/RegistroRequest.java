package cl.duoc.establecimiento_educacional.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Datos que envía la app para crear o editar un registro (POST y PUT).
// ignoreUnknown: si la app manda campos de más (id, fechas...), se ignoran
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {

    @NotNull(message = "Debe indicar la bitácora")
    private Long bitacoraId;

    @NotNull(message = "Debe indicar el estudiante")
    private Long estudianteId;

    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Use el formato dd/mm/aaaa")
    private String fechaClase;

    // Pueden ir vacíos en un borrador; para enviar se revisan en el servicio
    @NotNull
    private String contenido;

    @NotNull
    private String actividad;

    @NotNull
    private String aprendizaje;

    @NotNull
    private String dudas;

    @NotNull(message = "El estado es obligatorio")
    @Pattern(regexp = "BORRADOR|ENVIADO", message = "El estado debe ser BORRADOR o ENVIADO")
    private String estadoEnvio;

    @Valid
    @Size(max = 5, message = "Máximo 5 evidencias por registro")
    private List<EvidenciaRequest> evidencias = new ArrayList<>();
}
