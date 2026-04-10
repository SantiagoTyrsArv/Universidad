package u.universidad.service;

/**
 * Contrato del servicio de envío de correos electrónicos con formato HTML.
 *
 * Principio D (Dependency Inversion): los servicios de negocio dependen de
 * esta interfaz, no de la implementación concreta con JavaMailSender.
 *
 * Principio I (Interface Segregation): interfaz delgada con un solo método,
 * separada totalmente de los servicios de negocio (Administrativo, Estudiante, Profesor).
 */
public interface EmailService {

    /**
     * Envía un correo electrónico con formato HTML institucional de la UCC.
     *
     * @param destinatario dirección de correo del receptor
     * @param asunto       asunto del correo
     * @param titulo       título principal visible en el cuerpo del correo
     * @param cuerpoHtml   mensaje detallado (puede incluir saltos de línea con &lt;br&gt;)
     * @param piePagina    mensaje adicional al pie del correo (ej. "Atentamente, el área de Registro")
     *
     * @throws u.universidad.exception.NotificacionException si el envío falla por
     *         cualquier razón (correo inválido, servidor rechaza, timeout, etc.)
     */
    void enviarCorreoHtml(String destinatario,
                          String asunto,
                          String titulo,
                          String cuerpoHtml,
                          String piePagina);
}
