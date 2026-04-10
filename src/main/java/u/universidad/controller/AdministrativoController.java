package u.universidad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import u.universidad.dto.AdministrativoDTO;
import u.universidad.entity.Administrativo;
import u.universidad.exception.NotificacionException;
import u.universidad.service.AdministrativoService;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de Administrativos.
 * Principio S: su única responsabilidad es recibir peticiones HTTP y delegar en el servicio.
 * Principio D: depende de la interfaz {@link AdministrativoService}, no de la implementación concreta.
 */
@RestController
@RequestMapping("/api/administrativos")
@RequiredArgsConstructor
@Tag(name = "Administrativos", description = "Gestión de personal administrativo universitario")
public class AdministrativoController {

    private final AdministrativoService administrativoService;

    @GetMapping
    @Operation(summary = "Listar todos los administrativos")
    public ResponseEntity<List<Administrativo>> listarTodos() {
        return ResponseEntity.ok(administrativoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar administrativo por ID")
    public ResponseEntity<Administrativo> buscarPorId(@PathVariable Long id) {
        return administrativoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/area/{area}")
    @Operation(summary = "Buscar administrativos por área")
    public ResponseEntity<List<Administrativo>> buscarPorArea(@PathVariable String area) {
        return ResponseEntity.ok(administrativoService.buscarPorArea(area));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo administrativo")
    public ResponseEntity<?> crear(@Valid @RequestBody AdministrativoDTO dto) {
        try {
            Administrativo creado = administrativoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un administrativo existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody AdministrativoDTO dto) {
        try {
            Administrativo actualizado = administrativoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un administrativo")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            administrativoService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Administrativo eliminado correctamente."));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/aprobar-solicitud")
    @Operation(summary = "Aprobar una solicitud (Aprobador + Notificable + Email real en acción)")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Long id,
                                              @RequestParam String codigoSolicitud) {
        try {
            administrativoService.aprobarSolicitud(id, codigoSolicitud);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Solicitud [" + codigoSolicitud + "] aprobada correctamente.",
                    "detalle", "Se envió un correo HTML con branding UCC al correo del administrativo."
            ));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (NotificacionException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                    "advertencia", "La solicitud fue aprobada pero el correo no pudo enviarse.",
                    "detalle", e.getMessage()
            ));
        }
    }
}
