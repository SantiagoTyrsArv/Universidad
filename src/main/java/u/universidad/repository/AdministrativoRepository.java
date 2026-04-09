package u.universidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import u.universidad.entity.Administrativo;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Administrativo}.
 * Principio D (Dependency Inversion): los servicios dependen de esta abstracción,
 * no de la implementación concreta de acceso a datos.
 */
@Repository
public interface AdministrativoRepository extends JpaRepository<Administrativo, Long> {

    /**
     * Busca administrativos cuyo campo area coincida (ignorando mayúsculas).
     *
     * @param area nombre del área a buscar
     * @return lista de administrativos que pertenecen a esa área
     */
    List<Administrativo> findByAreaIgnoreCase(String area);

    /**
     * Verifica si existe un administrativo con el correo dado.
     *
     * @param correo correo a validar
     * @return true si existe un registro con ese correo
     */
    boolean existsByCorreo(String correo);
}
