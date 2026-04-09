package u.universidad.service;

import u.universidad.dto.AdministrativoDTO;
import u.universidad.entity.Administrativo;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del servicio de negocio para {@link Administrativo}.
 * Principio D (Dependency Inversion): el controlador depende de esta interfaz,
 * no de la implementación concreta.
 * Principio I (Interface Segregation): define solo los métodos propios de Administrativo.
 */
public interface AdministrativoService {

    /**
     * Retorna la lista completa de administrativos registrados.
     */
    List<Administrativo> listarTodos();

    /**
     * Busca un administrativo por su ID.
     *
     * @param id identificador único
     * @return Optional con el administrativo si existe
     */
    Optional<Administrativo> buscarPorId(Long id);

    /**
     * Crea y persiste un nuevo administrativo a partir del DTO recibido.
     *
     * @param dto datos del administrativo a crear
     * @return el administrativo creado y persistido
     */
    Administrativo crear(AdministrativoDTO dto);

    /**
     * Actualiza los datos de un administrativo existente.
     *
     * @param id  ID del administrativo a actualizar
     * @param dto nuevos datos del administrativo
     * @return el administrativo actualizado
     */
    Administrativo actualizar(Long id, AdministrativoDTO dto);

    /**
     * Elimina un administrativo por su ID.
     *
     * @param id identificador del administrativo a eliminar
     */
    void eliminar(Long id);

    /**
     * Busca administrativos por área.
     *
     * @param area nombre del área
     * @return lista de administrativos del área indicada
     */
    List<Administrativo> buscarPorArea(String area);

    /**
     * Ejecuta la lógica de aprobar una solicitud a través del administrativo indicado.
     *
     * @param id               ID del administrativo que aprueba
     * @param codigoSolicitud  código de la solicitud a aprobar
     */
    void aprobarSolicitud(Long id, String codigoSolicitud);
}
