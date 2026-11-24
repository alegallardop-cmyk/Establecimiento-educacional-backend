package cl.duoc.establecimiento_educacional.config;

import cl.duoc.establecimiento_educacional.model.Bitacora;
import cl.duoc.establecimiento_educacional.model.Evidencia;
import cl.duoc.establecimiento_educacional.model.Registro;
import cl.duoc.establecimiento_educacional.repository.BitacoraRepository;
import cl.duoc.establecimiento_educacional.repository.RegistroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Datos FICTICIOS para la demostración. Se cargan solo si la base está vacía.
// Los ids de asignaciones y usuarios son los mismos que crea la app en Room:
//   asignación 1 = Ana Fuentes, Lenguaje 7° Básico A
//   asignación 4 = Pedro Rojas, Matemática 7° Básico A
//   usuario 1 = Ana Fuentes (docente), 3 = Martina Soto, 4 = Benjamín Lagos (estudiantes)
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDemo(BitacoraRepository bitacoraRepository, RegistroRepository registroRepository) {
        return args -> {
            if (bitacoraRepository.count() > 0) return;

            Bitacora septiembre = bitacoraRepository.save(new Bitacora(null, 1L, 9, 2026,
                    "Bitácora de Lenguaje - Septiembre",
                    "Registrar lo aprendido en la unidad de textos narrativos.",
                    "Después de cada clase escribe qué trabajamos, qué actividad hicimos y qué aprendiste.",
                    "¿Qué texto leímos hoy?\n¿Qué fue lo más interesante?",
                    "CERRADA", "01/09/2026", "30/09/2026"));

            Bitacora octubre = bitacoraRepository.save(new Bitacora(null, 1L, 10, 2026,
                    "Bitácora de Lenguaje - Octubre",
                    "Reflexionar sobre la lectura de poemas y la escritura creativa.",
                    "Completa un registro después de cada clase. Puedes agregar el enlace a una foto de tu cuaderno.",
                    "¿Qué poema trabajamos?\n¿Qué recurso literario aprendiste?\n¿Qué duda te quedó?",
                    "PUBLICADA", "01/10/2026", ""));

            bitacoraRepository.save(new Bitacora(null, 4L, 10, 2026,
                    "Bitácora de Matemática - Octubre",
                    "Seguir el aprendizaje de números enteros.",
                    "Registra los ejercicios que hiciste y explica el procedimiento con tus palabras.",
                    "¿Qué operación practicamos?\n¿Cómo resolviste el ejercicio más difícil?",
                    "PUBLICADA", "01/10/2026", ""));

            // Martina (3): registro revisado de septiembre
            Registro r1 = nuevoRegistro(septiembre, 3L, "10/09/2026", "Estructura del cuento.",
                    "Leímos un cuento en grupo y marcamos inicio, desarrollo y desenlace.",
                    "Aprendí a reconocer el conflicto de una historia.", "", "ENVIADO");
            r1.setEstadoRevision("REVISADO");
            r1.setComentarioDocente("¡Muy bien identificado el conflicto!");
            r1.setFechaRetroalimentacion("12/09/2026");
            r1.setDocenteRevisorId(1L);
            registroRepository.save(r1);

            // Martina (3): registro revisado de octubre, con un enlace como evidencia
            Registro r2 = nuevoRegistro(octubre, 3L, "02/10/2026", "La rima en la poesía.",
                    "Leímos dos poemas en voz alta y buscamos rimas.",
                    "Aprendí la diferencia entre rima asonante y consonante.",
                    "No entendí bien qué es una estrofa libre.", "ENVIADO");
            r2.setEstadoRevision("REVISADO");
            r2.setComentarioDocente("Excelente. La estrofa libre la veremos la próxima clase.");
            r2.setFechaRetroalimentacion("03/10/2026");
            r2.setDocenteRevisorId(1L);
            r2.getEvidencias().add(new Evidencia(null, "ENLACE", "Página sobre la rima", "https://es.wikipedia.org/wiki/Rima"));
            registroRepository.save(r2);

            // Benjamín (4): enviado, pendiente de revisión
            registroRepository.save(nuevoRegistro(octubre, 4L, "02/10/2026", "Comparación y metáfora.",
                    "Escribimos tres comparaciones sobre la naturaleza.",
                    "Una metáfora no usa la palabra 'como'.", "", "ENVIADO"));

            // Martina (3): borrador (el docente NO lo ve)
            registroRepository.save(nuevoRegistro(octubre, 3L, "05/10/2026", "Escritura de un caligrama.",
                    "", "", "", "BORRADOR"));
        };
    }

    private Registro nuevoRegistro(Bitacora bitacora, Long estudianteId, String fecha, String contenido,
                                   String actividad, String aprendizaje, String dudas, String estadoEnvio) {
        Registro registro = new Registro();
        registro.setBitacora(bitacora);
        registro.setEstudianteId(estudianteId);
        registro.setFechaClase(fecha);
        registro.setContenido(contenido);
        registro.setActividad(actividad);
        registro.setAprendizaje(aprendizaje);
        registro.setDudas(dudas);
        registro.setEstadoEnvio(estadoEnvio);
        registro.setEstadoRevision("PENDIENTE");
        registro.setFechaCreacion(fecha);
        registro.setFechaEnvio("ENVIADO".equals(estadoEnvio) ? fecha : "");
        registro.setComentarioDocente("");
        registro.setFechaRetroalimentacion("");
        return registro;
    }
}
