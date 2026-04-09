package u.universidad.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import u.universidad.dto.AdministrativoDTO;
import u.universidad.entity.Administrativo;
import u.universidad.repository.AdministrativoRepository;
import u.universidad.service.AdministrativoService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementación concreta del servicio para {@link Administrativo}.
 * Principio D: implementa la abstracción {@link AdministrativoService},
 * manteniendo el controlador desacoplado de esta clase concreta.
 * Principio S: su única responsabilidad es la lógica de negocio de Administrativo.
 */
@Service
@RequiredArgsConstructor
public class AdministrativoServiceImpl implements AdministrativoService {

    private final AdministrativoRepository administrativoRepository;

    @Override
    public List<Administrativo> listarTodos() {
        return administrativoRepository.findAll();
    }

    @Override
    public Optional<Administrativo> buscarPorId(Long id) {
        return administrativoRepository.findById(id);
    }

    @Override
    public Administrativo crear(AdministrativoDTO dto) {
        if (administrativoRepository.existsByCorreo(dto.correo())) {
            throw new IllegalArgumentException(
                    "Ya existe un administrativo registrado con el correo: " + dto.correo());
        }
        Administrativo administrativo = new Administrativo(dto.nombre(), dto.correo(), dto.area());
        return administrativoRepository.save(administrativo);
    }

    @Override
    public Administrativo actualizar(Long id, AdministrativoDTO dto) {
        Administrativo administrativo = administrativoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún administrativo con ID: " + id));
        administrativo.setNombre(dto.nombre());
        administrativo.setCorreo(dto.correo());
        administrativo.setArea(dto.area());
        return administrativoRepository.save(administrativo);
    }

    @Override
    public void eliminar(Long id) {
        if (!administrativoRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "No se encontró ningún administrativo con ID: " + id);
        }
        administrativoRepository.deleteById(id);
    }

    @Override
    public List<Administrativo> buscarPorArea(String area) {
        return administrativoRepository.findByAreaIgnoreCase(area);
    }

    /**
     * Ejecuta la lógica de aprobación delegando en el método del diagrama.
     * Demuestra polimorfismo: se invoca aprobarSolicitud() de la entidad,
     * que a su vez llama a enviarNotificacion() (Notificable).
     */
    @Override
    public void aprobarSolicitud(Long id, String codigoSolicitud) {
        Administrativo administrativo = administrativoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún administrativo con ID: " + id));
        administrativo.aprobarSolicitud(codigoSolicitud);
    }
}
