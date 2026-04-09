package u.universidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) para Estudiante.
 * Usa Java Record (Java 16+) para garantizar inmutabilidad y reducir boilerplate.
 *
 * Principio S: su única responsabilidad es transportar datos de Estudiante.
 *
 * @param nombre nombre completo del estudiante
 * @param correo correo electrónico institucional (debe ser un correo válido)
 * @param codigo código único del estudiante en el sistema (ej. "2024-001")
 */
public record EstudianteDTO(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener formato válido")
        String correo,

        @NotBlank(message = "El código es obligatorio")
        String codigo
) {}
