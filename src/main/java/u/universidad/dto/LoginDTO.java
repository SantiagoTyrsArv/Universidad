package u.universidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para las solicitudes de autenticación (login).
 * Se usa tanto para Estudiantes como para Profesores, ya que
 * ambos implementan la interfaz {@link u.universidad.interfaces.Autenticable}.
 *
 * Usa Java Record (Java 21) para garantizar inmutabilidad.
 * Principio S: su única responsabilidad es transportar credenciales de acceso.
 *
 * @param usuario  correo electrónico institucional del usuario
 * @param password contraseña del usuario
 */
public record LoginDTO(

        @NotBlank(message = "El usuario es obligatorio")
        @Email(message = "El usuario debe ser un correo válido")
        String usuario,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
