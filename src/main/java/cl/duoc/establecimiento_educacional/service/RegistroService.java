package cl.duoc.establecimiento_educacional.service;

import cl.duoc.establecimiento_educacional.dto.EvidenciaRequest;
import cl.duoc.establecimiento_educacional.dto.RegistroRequest;
import cl.duoc.establecimiento_educacional.dto.RetroalimentacionRequest;
import cl.duoc.establecimiento_educacional.exception.ReglaNegocioException;
import cl.duoc.establecimiento_educacional.model.Bitacora;
import cl.duoc.establecimiento_educacional.model.Evidencia;
import cl.duoc.establecimiento_educacional.model.Registro;
import cl.duoc.establecimiento_educacional.repository.BitacoraRepository;
import cl.duoc.establecimiento_educacional.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Lógica del microservicio de registros: borradores, envío y retroalimentación
@Service
public class RegistroService {
    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private BitacoraRepository bitacoraRepository;

    // 1. Listar: de una bitácora, de un estudiante o todos
    public List<Registro> listar(Long bitacoraId, Long estudianteId) {
        if (bitacoraId != null) return registroRepository.findByBitacora_Id(bitacoraId);
        if (estudianteId != null) return registroRepository.findByEstudianteIdOrderByIdDesc(estudianteId);
        return registroRepository.findAll();
    }

    // 2. Buscar por ID (null si no existe)
    public Registro buscarPorId(Long id) {
        return registroRepository.findById(id).orElse(null);
    }

    // 3. Crear (como borrador o enviado)
    public Registro crear(RegistroRequest datos) {
        Bitacora bitacora = bitacoraRepository.findById(datos.getBitacoraId()).orElse(null);
        if (bitacora == null) {
            throw new ReglaNegocioException("La bitácora no existe");
        }
        if (!"PUBLICADA".equals(bitacora.getEstado())) {
            throw new ReglaNegocioException("Solo se pueden agregar registros a una bitácora publicada");
        }
        revisarContenido(datos);

        Registro registro = new Registro();
        registro.setBitacora(bitacora);
        registro.setEstudianteId(datos.getEstudianteId());
        registro.setEstadoRevision("PENDIENTE");
        registro.setFechaCreacion(hoy());
        registro.setComentarioDocente("");
        registro.setFechaRetroalimentacion("");
        copiarContenido(datos, registro);
        return registroRepository.save(registro);
    }

    // 4. Actualizar un borrador (o enviarlo)
    public Registro actualizar(Long id, RegistroRequest datos) {
        Registro registro = buscarPorId(id);
        if (registro == null) return null;
        if ("ENVIADO".equals(registro.getEstadoEnvio())) {
            throw new ReglaNegocioException("Un registro enviado ya no se puede modificar");
        }
        if (!"PUBLICADA".equals(registro.getBitacora().getEstado())) {
            throw new ReglaNegocioException("La bitácora ya no está publicada");
        }
        revisarContenido(datos);
        copiarContenido(datos, registro);
        return registroRepository.save(registro);
    }

    // 5. Retroalimentación del docente
    public Registro guardarRetroalimentacion(Long id, RetroalimentacionRequest datos) {
        Registro registro = buscarPorId(id);
        if (registro == null) return null;
        if (!"ENVIADO".equals(registro.getEstadoEnvio())) {
            throw new ReglaNegocioException("Solo se pueden revisar registros enviados");
        }
        String comentario = datos.getComentario().trim();
        if (!comentario.isEmpty() && comentario.length() < 5) {
            throw new ReglaNegocioException("El comentario debe tener al menos 5 caracteres");
        }
        registro.setComentarioDocente(comentario);
        registro.setFechaRetroalimentacion(hoy());
        registro.setDocenteRevisorId(datos.getDocenteRevisorId());
        registro.setEstadoRevision(datos.getEstadoRevision());
        return registroRepository.save(registro);
    }

    // 6. Borrar: solo borradores (sus evidencias se borran solas)
    public boolean borrar(Long id) {
        Registro registro = buscarPorId(id);
        if (registro == null) return false;
        if ("ENVIADO".equals(registro.getEstadoEnvio())) {
            throw new ReglaNegocioException("Solo se pueden eliminar borradores");
        }
        registroRepository.delete(registro);
        return true;
    }

    // Para ENVIAR, los tres campos principales son obligatorios
    private void revisarContenido(RegistroRequest datos) {
        if ("ENVIADO".equals(datos.getEstadoEnvio())) {
            if (datos.getContenido().isBlank() || datos.getActividad().isBlank() || datos.getAprendizaje().isBlank()) {
                throw new ReglaNegocioException("Para enviar, completa contenido, actividad y aprendizaje");
            }
        }
    }

    private void copiarContenido(RegistroRequest datos, Registro registro) {
        registro.setFechaClase(datos.getFechaClase().trim());
        registro.setContenido(datos.getContenido().trim());
        registro.setActividad(datos.getActividad().trim());
        registro.setAprendizaje(datos.getAprendizaje().trim());
        registro.setDudas(datos.getDudas().trim());
        registro.setEstadoEnvio(datos.getEstadoEnvio());
        registro.setFechaEnvio("ENVIADO".equals(datos.getEstadoEnvio()) ? hoy() : "");

        // Se reemplazan las evidencias (clear + add, para que JPA borre las que se quitaron)
        registro.getEvidencias().clear();
        for (EvidenciaRequest e : datos.getEvidencias()) {
            registro.getEvidencias().add(new Evidencia(null, e.getTipo(), e.getDescripcion().trim(), e.getEnlace().trim()));
        }
    }

    private String hoy() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
