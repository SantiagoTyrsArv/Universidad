package u.universidad.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import u.universidad.dto.ProfesorDTO;
import u.universidad.entity.Estudiante;
import u.universidad.entity.Profesor;
import u.universidad.repository.EstudianteRepository;
import u.universidad.repository.ProfesorRepository;
import u.universidad.service.ProfesorService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementación concreta del servicio para {@link Profesor}.
 * Principio D: implementa la abstracción {@link ProfesorService}.
 * Principio S: su única responsabilidad es la lógica de negocio de Profesor.
 */
@Service
@RequiredArgsConstructor
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final EstudianteRepository estudianteRepository;

    @Override
    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    @Override
    public Optional<Profesor> buscarPorId(Long id) {
        return profesorRepository.findById(id);
    }

    @Override
    public List<Profesor> buscarPorEspecialidad(String especialidad) {
        return profesorRepository.findByEspecialidadIgnoreCase(especialidad);
    }

    @Override
    public Profesor crear(ProfesorDTO dto) {
        if (profesorRepository.existsByCorreo(dto.correo())) {
            throw new IllegalArgumentException(
                    "Ya existe un profesor registrado con el correo: " + dto.correo());
        }
        Profesor profesor = new Profesor(dto.nombre(), dto.correo(), dto.especialidad());
        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor actualizar(Long id, ProfesorDTO dto) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún profesor con ID: " + id));
        profesor.setNombre(dto.nombre());
        profesor.setCorreo(dto.correo());
        profesor.setEspecialidad(dto.especialidad());
        return profesorRepository.save(profesor);
    }

    @Override
    public void eliminar(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "No se encontró ningún profesor con ID: " + id);
        }
        profesorRepository.deleteById(id);
    }

    /**
     * Ejecuta la evaluación delegando en el método evaluar() de la entidad Profesor.
     * Demuestra polimorfismo: Profesor implementa Evaluador y dentro de evaluar()
     * llama a enviarNotificacion() del Estudiante (Notificable).
     *
     * @param profesorId   ID del profesor que evalúa
     * @param estudianteId ID del estudiante a evaluar
     * @param nota         nota asignada (0.0 - 5.0)
     */
    @Override
    public void evaluarEstudiante(Long profesorId, Long estudianteId, double nota) {
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún profesor con ID: " + profesorId));
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún estudiante con ID: " + estudianteId));
        // Polimorfismo en acción: Profesor implementa Evaluador
        profesor.evaluar(estudiante, nota);
    }
}
