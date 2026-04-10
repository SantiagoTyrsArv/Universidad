package u.universidad.service;

import u.universidad.dto.ProfesorDTO;
import u.universidad.entity.Profesor;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del servicio de negocio para {@link Profesor}.
 * Principio D (Dependency Inversion): el controlador depende de esta interfaz,
 * no de la implementación concreta.
 */
public interface ProfesorService {

    /**
     * Retorna la lista completa de profesores registrados.
     */
    List<Profesor> listarTodos();

    /**
     * Busca un profesor por su ID.
     *
     * @param id identificador único
     * @return Optional con el profesor si existe
     */
    Optional<Profesor> buscarPorId(Long id);

    /**
     * Busca profesores por especialidad.
     *
     * @param especialidad nombre de la especialidad
     * @return lista de profesores con esa especialidad
     */
    List<Profesor> buscarPorEspecialidad(String especialidad);

    /**
     * Crea y persiste un nuevo profesor a partir del DTO recibido.
     *
     * @param dto datos del profesor a crear
     * @return el profesor creado y persistido
     */
    Profesor crear(ProfesorDTO dto);

    /**
     * Actualiza los datos de un profesor existente.
     *
     * @param id  ID del profesor a actualizar
     * @param dto nuevos datos del profesor
     * @return el profesor actualizado
     */
    Profesor actualizar(Long id, ProfesorDTO dto);

    /**
     * Elimina un profesor por su ID.
     *
     * @param id identificador del profesor a eliminar
     */
    void eliminar(Long id);

    /**
     * Ejecuta la lógica de evaluación de un estudiante por parte de un profesor.
     *
     * @param profesorId  ID del profesor que evalúa
     * @param estudianteId ID del estudiante a evaluar
     * @param nota        nota asignada (0.0 - 5.0)
     */
    void evaluarEstudiante(Long profesorId, Long estudianteId, double nota);

    /**
     * Verifica las credenciales de un profesor delegando en el método
     * {@link u.universidad.interfaces.Autenticable#login(String, String)} de la entidad.
     *
     * Expone el comportamiento de la interfaz Autenticable del diagrama UML.
     *
     * @param id       ID del profesor que intenta autenticarse
     * @param usuario  correo electrónico institucional
     * @param password contraseña del profesor
     * @return true si las credenciales son correctas, false en caso contrario
     */
    boolean login(Long id, String usuario, String password);
}
