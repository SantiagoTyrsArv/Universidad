package u.universidad.exception;

/**
 * Excepción personalizada que se lanza cuando el envío de una
 * notificación por correo electrónico falla.
 *
 * Puede ocurrir por:
 * - Dirección de correo inválida o rechazada por el servidor SMTP
 * - Fallo de conexión con el servidor de correo
 * - Timeout en el envío
 *
 * Principio S: su única responsabilidad es representar un error de notificación.
 */
public class NotificacionException extends RuntimeException {

    /**
     * Constructor con mensaje descriptivo del error.
     *
     * @param mensaje descripción de por qué falló la notificación
     */
    public NotificacionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa original (para no perder el stack trace).
     *
     * @param mensaje descripción del error
     * @param causa   excepción original que provocó el fallo
     */
    public NotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
