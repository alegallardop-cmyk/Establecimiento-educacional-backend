package cl.duoc.establecimiento_educacional.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Evidencia de un registro: una foto (su URI en el teléfono) o un enlace.
// La columna registro_id (clave foránea) la crea la relación @OneToMany de Registro.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evidencias")
public class Evidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FOTO o ENLACE
    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false, length = 500)
    private String enlace;
}
