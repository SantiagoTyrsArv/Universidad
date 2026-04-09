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
}
