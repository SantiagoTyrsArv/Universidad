package u.universidad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import u.universidad.dto.EstudianteDTO;
import u.universidad.dto.LoginDTO;
import u.universidad.entity.Estudiante;
import u.universidad.service.EstudianteService;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de Estudiantes.
 * Principio S: su única responsabilidad es recibir peticiones HTTP y delegar en el servicio.
 * Principio D: depende de la interfaz {@link EstudianteService}, no de la implementación concreta.
 */
@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "Gestión de estudiantes universitarios")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    @Operation(summary = "Listar todos los estudiantes")
    public ResponseEntity<List<Estudiante>> listarTodos() {
        return ResponseEntity.ok(estudianteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estudiante por ID")
    public ResponseEntity<Estudiante> buscarPorId(@PathVariable Long id) {
        return estudianteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar estudiante por código académico")
    public ResponseEntity<Estudiante> buscarPorCodigo(@PathVariable String codigo) {
        return estudianteService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo estudiante")
    public ResponseEntity<?> crear(@Valid @RequestBody EstudianteDTO dto) {
        try {
            Estudiante creado = estudianteService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estudiante existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody EstudianteDTO dto) {
        try {
            Estudiante actualizado = estudianteService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estudiante")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            estudianteService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Estudiante eliminado correctamente."));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
