package cl.duoc.establecimiento_educacional.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Bitácora mensual de un curso y asignatura (tabla "bitacoras").
// asignacionId identifica al docente + curso + asignatura (esos datos están en la app, en Room).
// La restricción única impide dos bitácoras de la misma asignación en el mismo mes.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "bitacoras",
        uniqueConstraints = @UniqueConstraint(columnNames = {"asignacion_id", "mes", "anio"})
)
public class Bitacora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asignacion_id", nullable = false)
    private Long asignacionId;

    @Column(nullable = false)
    private int mes;

    @Column(nullable = false)
    private int anio;

    @Column(nullable = false, length = 80)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String proposito;

    @Column(nullable = false, length = 1000)
    private String instrucciones;

    // Preguntas orientadoras, una por línea
    @Column(nullable = false, length = 1000)
    private String preguntas;

    // BORRADOR, PUBLICADA o CERRADA
    @Column(nullable = false, length = 15)
    private String estado;

    @Column(name = "fecha_publicacion", nullable = false, length = 10)
    private String fechaPublicacion;

    @Column(name = "fecha_cierre", nullable = false, length = 10)
    private String fechaCierre;
}
