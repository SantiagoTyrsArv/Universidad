package u.universidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import u.universidad.entity.Estudiante;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Estudiante}.
 * Principio D (Dependency Inversion): los servicios dependen de esta abstracción.
 */
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    /**
     * Busca un estudiante por su código único académico.
     *
     * @param codigo código del estudiante (ej. "2024-001")
     * @return Optional con el estudiante si se encontró
     */
    Optional<Estudiante> findByCodigo(String codigo);

    /**
     * Verifica si existe un estudiante con el código dado.
     *
     * @param codigo código a validar
     * @return true si existe un estudiante con ese código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica si existe un estudiante con el correo dado.
     *
     * @param correo correo a validar
     * @return true si existe un registro con ese correo
     */
    boolean existsByCorreo(String correo);
}
