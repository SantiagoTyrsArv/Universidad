package u.universidad.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import u.universidad.dto.ProfesorDTO;
import u.universidad.entity.Estudiante;
import u.universidad.entity.Profesor;
import u.universidad.repository.EstudianteRepository;
import u.universidad.repository.ProfesorRepository;
import u.universidad.service.EmailService;
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
    private final EmailService emailService;

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
     * Ejecuta la evaluación delegando en el método evaluar() de la entidad Profesor,
     * y luego envía un correo HTML real al estudiante evaluado.
     *
     * Flujo:
     * 1. Entidad: profesor.evaluar(estudiante, nota) → log (Evaluador)
     * 2. Entidad: estudiante.enviarNotificacion()    → log (Notificable)
     * 3. EmailService: enviarCorreoHtml()            → SMTP real (Gmail)
     *
     * @param profesorId   ID del profesor que evalúa
     * @param estudianteId ID del estudiante a evaluar
     * @param nota         nota asignada (0.0 - 5.0)
     * @throws u.universidad.exception.NotificacionException si el envío de correo falla
     */
    @Override
    public void evaluarEstudiante(Long profesorId, Long estudianteId, double nota) {
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún profesor con ID: " + profesorId));
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún estudiante con ID: " + estudianteId));

        // Paso 1 y 2: polimorfismo del diagrama (log en consola)
        profesor.evaluar(estudiante, nota);

        // Paso 3: envío real de correo HTML con branding UCC al estudiante
        String resultado    = nota >= 3.0 ? "APROBÓ" : "REPROBÓ";
        String colorResultado = nota >= 3.0 ? "#2e7d32" : "#c62828";

        String cuerpo = """
                <p>Estimado/a estudiante <strong>%s</strong>,</p>
                <p>Se ha registrado una evaluación en el sistema universitario UCC
                para tu historial académico:</p>
                <table style='width:100%%; border-collapse:collapse; margin:16px 0; font-size:14px;'>
                    <tr style='background-color:#f4f7f9;'>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; font-weight:600; color:#4a4a4a;'>Profesor</td>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; color:#4a4a4a;'>%s</td>
                    </tr>
                    <tr>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; font-weight:600; color:#4a4a4a;'>Especialidad</td>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; color:#4a4a4a;'>%s</td>
                    </tr>
                    <tr style='background-color:#f4f7f9;'>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; font-weight:600; color:#4a4a4a;'>Código</td>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; color:#4a4a4a;'>%s</td>
                    </tr>
                    <tr>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; font-weight:600; color:#4a4a4a;'>Nota</td>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0;'>
                            <strong style='font-size:18px; color:%s;'>%.1f / 5.0</strong>
                        </td>
                    </tr>
                    <tr style='background-color:#f4f7f9;'>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0; font-weight:600; color:#4a4a4a;'>Resultado</td>
                        <td style='padding:10px 14px; border:1px solid #e0e0e0;'>
                            <strong style='color:%s;'>%s</strong>
                        </td>
                    </tr>
                </table>
                <p style='color:#4a4a4a; font-size:13px;'>
                    Si tienes inquietudes sobre esta evaluación, comunícate con tu profesor
                    o con el área de Registro Académico.
                </p>
                """.formatted(
                        estudiante.getNombre(),
                        profesor.getNombre(),
                        profesor.getEspecialidad(),
                        estudiante.getCodigo(),
                        colorResultado, nota,
                        colorResultado, resultado);

        emailService.enviarCorreoHtml(
                estudiante.getCorreo(),
                "Resultado de Evaluación — Nota: " + nota,
                "📋 Resultado de tu Evaluación Académica",
                cuerpo,
                "Evaluación registrada por el Prof. " + profesor.getNombre()
                        + " · " + profesor.getEspecialidad()
        );
    }
}
