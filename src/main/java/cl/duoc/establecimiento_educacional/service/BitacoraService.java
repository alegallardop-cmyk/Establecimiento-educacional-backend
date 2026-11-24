package cl.duoc.establecimiento_educacional.service;

import cl.duoc.establecimiento_educacional.dto.BitacoraRequest;
import cl.duoc.establecimiento_educacional.exception.ReglaNegocioException;
import cl.duoc.establecimiento_educacional.model.Bitacora;
import cl.duoc.establecimiento_educacional.repository.BitacoraRepository;
import cl.duoc.establecimiento_educacional.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Lógica del microservicio de bitácoras: estados BORRADOR -> PUBLICADA -> CERRADA
@Service
public class BitacoraService {
    @Autowired
    private BitacoraRepository bitacoraRepository;

    @Autowired
    private RegistroRepository registroRepository;

    // 1. Listar todas (las más nuevas primero)
    public List<Bitacora> listar() {
        return bitacoraRepository.findAllByOrderByAnioDescMesDesc();
    }

    // 2. Buscar por ID (null si no existe)
    public Bitacora buscarPorId(Long id) {
        return bitacoraRepository.findById(id).orElse(null);
    }

    // 3. Crear
    public Bitacora crear(BitacoraRequest datos) {
        if (bitacoraRepository.existsByAsignacionIdAndMesAndAnio(datos.getAsignacionId(), datos.getMes(), datos.getAnio())) {
            throw new ReglaNegocioException("Ya existe una bitácora para ese curso, asignatura y mes");
        }
        boolean publicar = "PUBLICADA".equals(datos.getEstado());

        Bitacora bitacora = new Bitacora();
        bitacora.setAsignacionId(datos.getAsignacionId());
        copiarContenido(datos, bitacora);
        bitacora.setEstado(publicar ? "PUBLICADA" : "BORRADOR");
        bitacora.setFechaPublicacion(publicar ? hoy() : "");
        bitacora.setFechaCierre("");
        return bitacoraRepository.save(bitacora);
    }

    // 4. Actualizar (no cambia la asignación ni el estado)
    public Bitacora actualizar(Long id, BitacoraRequest datos) {
        Bitacora bitacora = buscarPorId(id);
        if (bitacora == null) return null;
        if ("CERRADA".equals(bitacora.getEstado())) {
            throw new ReglaNegocioException("Una bitácora cerrada no se puede editar");
        }
        if (bitacoraRepository.existsByAsignacionIdAndMesAndAnioAndIdNot(bitacora.getAsignacionId(), datos.getMes(), datos.getAnio(), id)) {
            throw new ReglaNegocioException("Ya existe una bitácora para ese curso, asignatura y mes");
        }
        copiarContenido(datos, bitacora);
        return bitacoraRepository.save(bitacora);
    }

    // 5. Publicar: solo desde BORRADOR
    public Bitacora publicar(Long id) {
        Bitacora bitacora = buscarPorId(id);
        if (bitacora == null) return null;
        if (!"BORRADOR".equals(bitacora.getEstado())) {
            throw new ReglaNegocioException("Solo se puede publicar una bitácora en borrador");
        }
        bitacora.setEstado("PUBLICADA");
        bitacora.setFechaPublicacion(hoy());
        return bitacoraRepository.save(bitacora);
    }

    // 6. Cerrar: solo desde PUBLICADA
    public Bitacora cerrar(Long id) {
        Bitacora bitacora = buscarPorId(id);
        if (bitacora == null) return null;
        if (!"PUBLICADA".equals(bitacora.getEstado())) {
            throw new ReglaNegocioException("Solo se puede cerrar una bitácora publicada");
        }
        bitacora.setEstado("CERRADA");
        bitacora.setFechaCierre(hoy());
        return bitacoraRepository.save(bitacora);
    }

    // 7. Borrar: primero sus registros (y sus evidencias), después la bitácora
    @Transactional
    public boolean borrar(Long id) {
        if (!bitacoraRepository.existsById(id)) return false;
        registroRepository.deleteAll(registroRepository.findByBitacora_Id(id));
        bitacoraRepository.deleteById(id);
        return true;
    }

    private void copiarContenido(BitacoraRequest datos, Bitacora bitacora) {
        bitacora.setMes(datos.getMes());
        bitacora.setAnio(datos.getAnio());
        bitacora.setTitulo(datos.getTitulo().trim());
        bitacora.setProposito(datos.getProposito().trim());
        bitacora.setInstrucciones(datos.getInstrucciones().trim());
        bitacora.setPreguntas(datos.getPreguntas().trim());
    }

    // Fecha de hoy con el mismo formato que usa la app: dd/mm/aaaa
    private String hoy() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
