package cl.duoc.establecimiento_educacional.repository;

import cl.duoc.establecimiento_educacional.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    // Spring Data arma la consulta SQL a partir del nombre del método
    List<Bitacora> findAllByOrderByAnioDescMesDesc();

    // ¿Ya hay una bitácora de esa asignación en ese mes? (al crear)
    boolean existsByAsignacionIdAndMesAndAnio(Long asignacionId, int mes, int anio);

    // Lo mismo, pero sin contar la propia bitácora (al editar)
    boolean existsByAsignacionIdAndMesAndAnioAndIdNot(Long asignacionId, int mes, int anio, Long id);
}
