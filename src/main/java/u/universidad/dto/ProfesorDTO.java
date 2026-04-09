package u.universidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) para Profesor.
 * Usa Java Record (Java 16+) para garantizar inmutabilidad y reducir boilerplate.
 *
 * Principio S: su única responsabilidad es transportar datos de Profesor.
 *
 * @param nombre       nombre completo del profesor
 * @param correo       correo electrónico institucional (debe ser un correo válido)
 * @param especialidad área académica de especialización del profesor
 */
public record ProfesorDTO(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener formato válido")
        String correo,

        @NotBlank(message = "La especialidad es obligatoria")
        String especialidad
) {}
