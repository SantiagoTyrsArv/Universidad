package u.universidad.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import u.universidad.dto.EstudianteDTO;
import u.universidad.entity.Estudiante;
import u.universidad.repository.EstudianteRepository;
import u.universidad.service.EstudianteService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementación concreta del servicio para {@link Estudiante}.
 * Principio D: implementa la abstracción {@link EstudianteService}.
 * Principio S: su única responsabilidad es la lógica de negocio de Estudiante.
 */
@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    @Override
    public List<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    @Override
    public Optional<Estudiante> buscarPorId(Long id) {
        return estudianteRepository.findById(id);
    }

    @Override
    public Optional<Estudiante> buscarPorCodigo(String codigo) {
        return estudianteRepository.findByCodigo(codigo);
    }

    @Override
    public Estudiante crear(EstudianteDTO dto) {
        if (estudianteRepository.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con el código: " + dto.codigo());
        }
        if (estudianteRepository.existsByCorreo(dto.correo())) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con el correo: " + dto.correo());
        }
        Estudiante estudiante = new Estudiante(dto.nombre(), dto.correo(), dto.codigo());
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Estudiante actualizar(Long id, EstudianteDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún estudiante con ID: " + id));
        estudiante.setNombre(dto.nombre());
        estudiante.setCorreo(dto.correo());
        estudiante.setCodigo(dto.codigo());
        return estudianteRepository.save(estudiante);
    }

    @Override
    public void eliminar(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "No se encontró ningún estudiante con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    /**
     * Verifica las credenciales del estudiante delegando en el método
     * de la entidad que implementa {@link u.universidad.interfaces.Autenticable}.
     *
     * Polimorfismo en acción: Estudiante.login() es llamado a través del contrato
     * de la interfaz Autenticable definida en el diagrama UML.
     *
     * @param id       ID del estudiante en base de datos
     * @param usuario  correo electrónico institucional
     * @param password contraseña del estudiante
     * @return true si las credenciales son válidas
     * @throws NoSuchElementException si no existe estudiante con el ID dado
     */
    @Override
    public boolean login(Long id, String usuario, String password) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún estudiante con ID: " + id));
        // Polimorfismo: Estudiante implementa Autenticable
        return estudiante.login(usuario, password);
    }
}
