package cl.duoc.establecimiento_educacional.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Registro (aporte) de un estudiante después de una clase, con la retroalimentación del docente.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registros")
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CLAVE FORÁNEA: cada registro pertenece a una bitácora (columna bitacora_id)
    @ManyToOne(optional = false)
    @JoinColumn(name = "bitacora_id", nullable = false)
    @JsonIgnore
    private Bitacora bitacora;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    // dd/mm/aaaa
    @Column(name = "fecha_clase", nullable = false, length = 10)
    private String fechaClase;

    @Column(nullable = false, length = 500)
    private String contenido;

    @Column(nullable = false, length = 500)
    private String actividad;

    @Column(nullable = false, length = 500)
    private String aprendizaje;

    @Column(nullable = false, length = 500)
    private String dudas;

    // BORRADOR o ENVIADO
    @Column(name = "estado_envio", nullable = false, length = 10)
    private String estadoEnvio;

    // PENDIENTE o REVISADO
    @Column(name = "estado_revision", nullable = false, length = 10)
    private String estadoRevision;

    @Column(name = "fecha_creacion", nullable = false, length = 10)
    private String fechaCreacion;

    @Column(name = "fecha_envio", nullable = false, length = 10)
    private String fechaEnvio;

    // Retroalimentación del docente
    @Column(name = "comentario_docente", nullable = false, length = 500)
    private String comentarioDocente;

    @Column(name = "fecha_retroalimentacion", nullable = false, length = 10)
    private String fechaRetroalimentacion;

    // null = todavía nadie lo revisa
    @Column(name = "docente_revisor_id")
    private Long docenteRevisorId;

    // Evidencias del registro: se guardan y se borran junto con él
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "registro_id")
    private List<Evidencia> evidencias = new ArrayList<>();

    // En el JSON se envía solo el id de la bitácora, no la bitácora completa
    @JsonProperty("bitacoraId")
    public Long getBitacoraId() {
        return bitacora == null ? null : bitacora.getId();
    }
}
