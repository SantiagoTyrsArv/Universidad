package u.universidad.service;

import u.universidad.dto.EstudianteDTO;
import u.universidad.entity.Estudiante;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del servicio de negocio para {@link Estudiante}.
 * Principio D (Dependency Inversion): el controlador depende de esta interfaz,
 * no de la implementación concreta.
 */
public interface EstudianteService {

    /**
     * Retorna la lista completa de estudiantes registrados.
     */
    List<Estudiante> listarTodos();

    /**
     * Busca un estudiante por su ID.
     *
     * @param id identificador único
     * @return Optional con el estudiante si existe
     */
    Optional<Estudiante> buscarPorId(Long id);

    /**
     * Busca un estudiante por su código académico único.
     *
     * @param codigo código del estudiante
     * @return Optional con el estudiante si existe
     */
    Optional<Estudiante> buscarPorCodigo(String codigo);

    /**
     * Crea y persiste un nuevo estudiante a partir del DTO recibido.
     *
     * @param dto datos del estudiante a crear
     * @return el estudiante creado y persistido
     */
    Estudiante crear(EstudianteDTO dto);

    /**
     * Actualiza los datos de un estudiante existente.
     *
     * @param id  ID del estudiante a actualizar
     * @param dto nuevos datos del estudiante
     * @return el estudiante actualizado
     */
    Estudiante actualizar(Long id, EstudianteDTO dto);

    /**
     * Elimina un estudiante por su ID.
     *
     * @param id identificador del estudiante a eliminar
     */
    void eliminar(Long id);

    /**
     * Verifica las credenciales de un estudiante delegando en el método
     * {@link u.universidad.interfaces.Autenticable#login(String, String)} de la entidad.
     *
     * Expone el comportamiento de la interfaz Autenticable del diagrama UML.
     *
     * @param id       ID del estudiante que intenta autenticarse
     * @param usuario  correo electrónico institucional
     * @param password contraseña del estudiante
     * @return true si las credenciales son correctas, false en caso contrario
     */
    boolean login(Long id, String usuario, String password);
}
