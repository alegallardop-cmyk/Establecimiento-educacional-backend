package cl.duoc.establecimiento_educacional.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ignoreUnknown: si la app manda campos de más (id, fechas...), se ignoran
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvidenciaRequest {

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "FOTO|ENLACE", message = "El tipo debe ser FOTO o ENLACE")
    private String tipo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "El enlace es obligatorio")
    private String enlace;
}
