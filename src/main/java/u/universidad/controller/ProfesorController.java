package u.universidad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import u.universidad.dto.ProfesorDTO;
import u.universidad.entity.Profesor;
import u.universidad.service.ProfesorService;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de Profesores.
 * Principio S: su única responsabilidad es recibir peticiones HTTP y delegar en el servicio.
 * Principio D: depende de la interfaz {@link ProfesorService}, no de la implementación concreta.
 */
@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
@Tag(name = "Profesores", description = "Gestión de profesores universitarios")
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping
    @Operation(summary = "Listar todos los profesores")
    public ResponseEntity<List<Profesor>> listarTodos() {
        return ResponseEntity.ok(profesorService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profesor por ID")
    public ResponseEntity<Profesor> buscarPorId(@PathVariable Long id) {
        return profesorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(summary = "Buscar profesores por especialidad")
    public ResponseEntity<List<Profesor>> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(profesorService.buscarPorEspecialidad(especialidad));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo profesor")
    public ResponseEntity<?> crear(@Valid @RequestBody ProfesorDTO dto) {
        try {
            Profesor creado = profesorService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un profesor existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody ProfesorDTO dto) {
        try {
            Profesor actualizado = profesorService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un profesor")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            profesorService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Profesor eliminado correctamente."));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{profesorId}/evaluar/{estudianteId}")
    @Operation(summary = "Evaluar a un estudiante (Evaluador + Notificable en acción)")
    public ResponseEntity<?> evaluarEstudiante(@PathVariable Long profesorId,
                                               @PathVariable Long estudianteId,
                                               @RequestParam double nota) {
        try {
            profesorService.evaluarEstudiante(profesorId, estudianteId, nota);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Evaluación registrada correctamente.",
                    "detalle", "Revisa los logs del servidor para ver la notificación enviada al estudiante."
            ));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
