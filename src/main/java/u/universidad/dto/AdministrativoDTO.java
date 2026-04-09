package u.universidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) para Administrativo.
 * Usa Java Record (Java 16+) para garantizar inmutabilidad y reducir boilerplate.
 * Solo transporta los datos necesarios desde/hacia la API, sin exponer la entidad JPA.
 *
 * Principio S: su única responsabilidad es transportar datos de Administrativo.
 *
 * @param nombre nombre completo del administrativo
 * @param correo correo electrónico institucional (debe ser un correo válido)
 * @param area   área institucional a la que pertenece
 */
public record AdministrativoDTO(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener formato válido")
        String correo,

        @NotBlank(message = "El área es obligatoria")
        String area
) {}
