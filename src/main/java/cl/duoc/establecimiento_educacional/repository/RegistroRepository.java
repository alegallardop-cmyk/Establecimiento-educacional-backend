package cl.duoc.establecimiento_educacional.repository;

import cl.duoc.establecimiento_educacional.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

    // "Bitacora_Id" = el id de la bitácora relacionada (clave foránea bitacora_id)
    List<Registro> findByBitacora_Id(Long bitacoraId);

    List<Registro> findByEstudianteIdOrderByIdDesc(Long estudianteId);
}
