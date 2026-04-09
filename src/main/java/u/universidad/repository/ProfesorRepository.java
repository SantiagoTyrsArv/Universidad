package u.universidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import u.universidad.entity.Profesor;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Profesor}.
 * Principio D (Dependency Inversion): los servicios dependen de esta abstracción.
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    /**
     * Busca profesores por especialidad (ignorando mayúsculas).
     *
     * @param especialidad nombre de la especialidad
     * @return lista de profesores con esa especialidad
     */
    List<Profesor> findByEspecialidadIgnoreCase(String especialidad);

    /**
     * Verifica si existe un profesor con el correo dado.
     *
     * @param correo correo a validar
     * @return true si existe un registro con ese correo
     */
    boolean existsByCorreo(String correo);
}
